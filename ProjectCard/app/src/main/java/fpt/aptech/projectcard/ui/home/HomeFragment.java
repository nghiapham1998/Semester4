package fpt.aptech.projectcard.ui.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fpt.aptech.projectcard.MainActivity;
import fpt.aptech.projectcard.R;
import fpt.aptech.projectcard.callApiService.ApiConstant;
import fpt.aptech.projectcard.callApiService.ApiService;
import fpt.aptech.projectcard.domain.Orders;
import fpt.aptech.projectcard.domain.Product;
import fpt.aptech.projectcard.domain.UrlProduct;
import fpt.aptech.projectcard.retrofit.RetrofitService;
import fpt.aptech.projectcard.session.SessionManager;
import fpt.aptech.projectcard.ui.login.LoginActivity;
import fpt.aptech.projectcard.ui.profile.ProfileFragment;
import fpt.aptech.projectcard.ui.social.SocialFragment;

public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;

    Product getProduct;
    List<Orders> ordersListUser;

    TextView txtFrontHeaderCard,txtBehindHeaderCard;
    //front card
    TextView txtName,txtEmail,txtAddress,txtBirthday,txtProvince, txtGender;
    //behind card
    GridView gridViewUrl;
    List<UrlProduct> urlProductList;
    ImageView imgQRInfo, imgQRUrl;

    ConstraintLayout layoutCard_front, layoutCard_behind;
    LinearLayout layout_updateProfile;
    //Button
    Button btnPause;
    private String username,password;
    private FragmentTransaction fragmentTransaction;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)//api > 24
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        txtFrontHeaderCard = view.findViewById(R.id.txt_front_header_card);
        txtBehindHeaderCard = view.findViewById(R.id.txt_behind_header_card);
        txtName = view.findViewById(R.id.txt_Name);
        txtEmail = view.findViewById(R.id.txt_Email);
        txtBirthday = view.findViewById(R.id.txt_Birthday);
        txtAddress = view.findViewById(R.id.txt_Address);
        txtGender = view.findViewById(R.id.txt_Gender);
        //behind card
        gridViewUrl = view.findViewById(R.id.gvUrl);
        imgQRInfo = view.findViewById(R.id.imgQRInfo);
        imgQRUrl = view.findViewById(R.id.imgQRUrl);

        btnPause = view.findViewById(R.id.btnControlFlip);
        layoutCard_front = view.findViewById(R.id.layoutCard_front);
        layoutCard_behind = view.findViewById(R.id.layoutCard_behind);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }
        //hide layout_UpdateProfile and btnUpdateProfile
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
                ((MainActivity) getActivity()).setActionBarTitle("Home");
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)//api > 24 for set TooltipText imageView
    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)getActivity()).isLogined(SessionManager.getSaveToken());

        ApiService apiService = RetrofitService.proceedToken().create(ApiService.class);


        try {
            ordersListUser = apiService.getOrdersByUsername(SessionManager.getSaveUsername()).execute().body();
            getProduct = apiService.getProduct(SessionManager.getSaveUsername(), SessionManager.getSaveToken()).execute().body();
            SessionManager.setSaveProduct(getProduct);

            SessionManager.setSaveUser(apiService.getProfile(SessionManager.getSaveUsername()).execute().body());
            urlProductList = apiService.getUrlProduct(SessionManager.getSaveUsername()).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ///////////////////////////////////////////////
        if (getProduct == null){
            Toast.makeText(getActivity().getApplicationContext(), "The your product is not purchased yet", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
        else {
            if (getProduct != null && getProduct.getStatus() == 0) {
                Toast.makeText(getActivity().getApplicationContext(), "The your product has not still active yet, please wait admin confirm your payment!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
            showCardInfo();
            showUrlProduct();
        }

        //================================================================
        String animated = btnPause.getText().toString();
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animated.equals("Flip Card: On")){
                    btnPause.setText(R.string.btn_flip_off);
                } else {
                    btnPause.setText(R.string.btn_flip_on);
                }
                onStart();
            }
        });
        //front
        layoutCard_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPause.getText();

                txtBehindHeaderCard.setText(getProduct.getName().toUpperCase() + " (" + getProduct.getCount() + " VISITS)");
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(layoutCard_front, "scaleX", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(layoutCard_front, "scaleX", 0f, 1f);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.setDuration(2000);
                oa2.setDuration(2000);
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layoutCard_front.setVisibility(View.GONE);
                        layoutCard_behind.setVisibility(View.VISIBLE);
                        oa2.start();
                    }
                });
                if (btnPause.getText().toString().equals("Flip Card: On")){
                    Toast.makeText(getActivity().getApplicationContext(), "to Behind", Toast.LENGTH_SHORT).show();
                    oa1.resume();
                    oa1.start();
                } else {
                    oa1.pause();
                }
            }
        });
        //behind
        layoutCard_behind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPause.getText();

                txtFrontHeaderCard.setText(R.string.front_header_card);

                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(layoutCard_behind, "scaleX", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(layoutCard_behind, "scaleX", 0f, 1f);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.setDuration(2000);
                oa2.setDuration(2000);
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layoutCard_behind.setVisibility(View.GONE);
                        layoutCard_front.setVisibility(View.VISIBLE);
                        oa2.start();
                    }
                });
                if (btnPause.getText().toString().equals("Flip Card: On")){
                    Toast.makeText(getActivity().getApplicationContext(), "to Front", Toast.LENGTH_SHORT).show();
                    oa1.resume();
                    oa1.start();
                } else {
                    oa1.pause();
                }
            }
        });
        //================================================================

        // to update layout
        layoutCard_front.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Update profile layout", Toast.LENGTH_SHORT).show();

                fragmentTransaction = getChildFragmentManager().beginTransaction();
                ProfileFragment profileFragment = new ProfileFragment();
                fragmentTransaction.replace(R.id.fl_content_home, profileFragment);
                fragmentTransaction.addToBackStack(null).commit();
                return false;
            }
        });
        //to add url layout
        layoutCard_behind.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Add URL layout", Toast.LENGTH_SHORT).show();
                fragmentTransaction = getChildFragmentManager().beginTransaction();
                SocialFragment socialFragment = new SocialFragment();
                fragmentTransaction.replace(R.id.fl_content_home, socialFragment);
                fragmentTransaction.addToBackStack(null).commit();
                return false;
            }
        });
        txtFrontHeaderCard.setTooltipText("Touch and hold your touch on card to go to update profile layout");
        txtBehindHeaderCard.setTooltipText("Touch and hold your touch at corner card to go to add url layout");
    }

    //check order status for login

    //function to display smart card info and qr cde
    @RequiresApi(api = Build.VERSION_CODES.O) //api > 24
    public void showCardInfo(){

        txtBehindHeaderCard.setText(getProduct.getName().toUpperCase() + " (" + getProduct.getCount() + " VISITS)");
        //user info
        String fullname = SessionManager.getSaveUser().getFullname();
        String email = SessionManager.getSaveUser().getEmail();
        String phone = SessionManager.getSaveUser().getPhone();
        String address = SessionManager.getSaveUser().getAddress();
        String description = SessionManager.getSaveUser().getDescription();
        String province = SessionManager.getSaveUser().getProvince();

        String[] birthday = SessionManager.getSaveUser().getDateOfbirth().split("T");
        SessionManager.getSaveUser().setDateOfbirth(birthday[0]);
        String gender = (SessionManager.getSaveUser().getGender()==true?"Female":"Male");

         txtName.setText("Firstname: " + fullname);
        txtEmail.setText("Main email: " + email);
        txtBirthday.setText("Birthday: " + birthday[0]);
        txtAddress.setText("Address: " + address);
        txtGender.setText("Gender: " + gender);

        String dynamicQRInfo = "BEGIN:VCARD"
                      +"\nFN:"+ fullname
                      + "\nTEL:" + phone
                      + "\nEMAIL;TYPE=INTERNET:" + email
                      + "\nBDAY:" + birthday[0]
                      + "\nADR:" + address
                      + "\nNOTE:" + description
                      + "\nURL:" + ApiConstant.BASE_URL_CLIENT + "/Display/" + SessionManager.getSaveUsername()
                      + "\nEND:VCARD\n";

        String dynamicQRUrl= ApiConstant.BASE_URL_CLIENT + "/Display/" + SessionManager.getSaveUsername();

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(dynamicQRInfo, BarcodeFormat.QR_CODE,450,450);
            BitMatrix matrix2 = writer.encode(dynamicQRUrl, BarcodeFormat.QR_CODE,450,450);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            Bitmap bitmap2 = encoder.createBitmap(matrix2);
            imgQRInfo.setImageBitmap(bitmap);
            imgQRUrl.setImageBitmap(bitmap2);
            imgQRInfo.setTooltipText("Scan this to add contact");
            imgQRUrl.setTooltipText(ApiConstant.BASE_URL_CLIENT + "/Display/" + SessionManager.getSaveUsername());
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void showUrlProduct(){

        urlProductList.sort(new Comparator<UrlProduct>() {
            public int compare(UrlProduct u1, UrlProduct u2) {
                return u1.getLinkType().getName().compareTo(u2.getLinkType().getName());
            }
        });

        ArrayList<UrlProduct> urlProductArrayList = new ArrayList<>(urlProductList);
        GridViewURLAdapter adapter = new GridViewURLAdapter(getActivity(),urlProductArrayList,getChildFragmentManager());
        adapter.notifyDataSetChanged();
        gridViewUrl.setAdapter(adapter);
    }
}