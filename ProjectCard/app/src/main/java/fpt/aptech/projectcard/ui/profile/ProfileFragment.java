package fpt.aptech.projectcard.ui.profile;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fpt.aptech.projectcard.MainActivity;
import fpt.aptech.projectcard.Payload.request.ProductRequest;
import fpt.aptech.projectcard.Payload.request.UpdateProfile;
import fpt.aptech.projectcard.R;
import fpt.aptech.projectcard.callApiService.ApiService;
import fpt.aptech.projectcard.domain.User;
import fpt.aptech.projectcard.retrofit.RetrofitService;
import fpt.aptech.projectcard.retrofit.catchError.APIError;
import fpt.aptech.projectcard.retrofit.catchError.ErrorUtils;
import fpt.aptech.projectcard.session.SessionManager;
import fpt.aptech.projectcard.ui.home.HomeFragment;
import fpt.aptech.projectcard.ui.login.RegisterActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private EditText edEmail,edPhone,edFullname,edAddress,edDescription;
    private TextView edbirth;
    ImageView imgAvatarProfile, imgBackHomeClick;
    RadioGroup rgGender;
    RadioButton getGenderSelected;

    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;
    private View view;
    private Button btnUpdate;

    private FragmentTransaction fragmentTransaction;


    public ProfileFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).setActionBarTitle("Update User's Profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        edFullname = view.findViewById(R.id.editFullnameProfile);
        edEmail = view.findViewById(R.id.editEmailProfile);
        edPhone = view.findViewById(R.id.editPhoneProfile);
        edAddress = view.findViewById(R.id.editAddressProfile);
        edDescription = view.findViewById(R.id.editDescriptionProfile);
        edbirth = view.findViewById(R.id.txtEdBirthProfile);
        rgGender = view.findViewById(R.id.rgGenderProfile);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        //image
        imgBackHomeClick = view.findViewById(R.id.backHomeClick);
        imgAvatarProfile = view.findViewById(R.id.imgAvatarProfile);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }

        ((MainActivity)getActivity()).isLogined(SessionManager.getSaveToken());

        //==========================DATE PICKER=========================================
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        c.set(2000,3,30);
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);


        ApiService apiServiceGetProfile = RetrofitService.proceedToken().create(ApiService.class);
        apiServiceGetProfile.getProfile(SessionManager.getSaveUsername()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    try {
                        //avatar
                        Bitmap bitmap1 = BitmapFactory.decodeStream((InputStream)new URL(response.body().getLinkImage()).getContent());
                        imgAvatarProfile.setImageBitmap(bitmap1);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    edFullname.setText(response.body().getFullname());
                    edEmail.setText(response.body().getEmail());
                    edAddress.setText(response.body().getAddress());
                    edPhone.setText(response.body().getPhone());
                    edDescription.setText(response.body().getDescription());

                    String[]birth = response.body().getDateOfbirth().split("T");
                    edbirth.setText(birth[0]);
                    dateBirthPicker();
                    if (response.body().getGender() == true) {
                        RadioButton rdMale = view.findViewById(R.id.rdFemaleProfile);
                        rdMale.setChecked(true);
                    } else {
                        RadioButton rdFemale = view.findViewById(R.id.rdMaleProfile);
                        rdFemale.setChecked(true);
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Can't get data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Retrieve data failed", Toast.LENGTH_SHORT).show();
            }
        });

        //update profile
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = SessionManager.getSaveUser().getEmail();
                String fullname = edFullname.getText().toString().trim();
                String phone = edPhone.getText().toString().trim();
                String address = edAddress.getText().toString().trim();
                String description = edDescription.getText().toString().trim();
                String birthday = edbirth.getText().toString().trim();
                boolean gender;
                //gender
                getGenderSelected = (RadioButton) rgGender.findViewById(rgGender.getCheckedRadioButtonId());
                if (getGenderSelected.getText().equals("Female")) {
                    gender = true;
                } else {
                    gender = false;
                }
                if (validateUpdateProfile(fullname,phone,address,description,birthday)){

                    UpdateProfile updateProfile = new UpdateProfile(fullname,email,phone,address,birthday,gender,description, SessionManager.getSaveUser().getLastname());

                    ApiService apiService = RetrofitService.proceedToken().create(ApiService.class);
                    apiService.updateProfile(SessionManager.getSaveUserID(), updateProfile).enqueue(new Callback<UpdateProfile>() {
                        @Override
                        public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getActivity().getApplicationContext(), "Updated success", Toast.LENGTH_SHORT).show();
                                edFullname.setError(null);
                                edPhone.setError(null);
                                edAddress.setError(null);
                                edDescription.setError(null);
                                edbirth.setError(null);
                            }
                            if (response.code() == 400){
                                Toast.makeText(getActivity().getApplicationContext(), "Error field input", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateProfile> call, Throwable t) {
                            Toast.makeText(getActivity().getApplicationContext(),"Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });


        imgBackHomeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentTransaction = getChildFragmentManager().beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.frmProfile, homeFragment);
                fragmentTransaction.addToBackStack(null).commit();
                ((MainActivity) getActivity()).setActionBarTitle("Home");
                edFullname.setError(null);
                edPhone.setError(null);
                edAddress.setError(null);
                edbirth.setError(null);
                edDescription.setError(null);
            }
        });

        return view;
    }

    //date birth picker dialog
    public void dateBirthPicker(){
        //==========================DATE PICKER=========================================
        String[]birthdate = edbirth.getText().toString().trim().split("-");
        // Get Birth Date from db
        final Calendar c = Calendar.getInstance();
        c.set(Integer.valueOf(birthdate[0]),Integer.valueOf(birthdate[1])-1,Integer.valueOf(birthdate[2]));//yyyy/MM/dd
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        //date picker
        edbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear + 1 <10) {
                            if (dayOfMonth <10) {
                                //thêm số 0 vào đúng định dạng mới dùng order by date trong db
                                //month + 1 vì month trong calendar có values từ 0-11
                                edbirth.setText(year+ "-" + "0"+(monthOfYear + 1) + "-" + "0"+dayOfMonth);
                            }
                            else{
                                edbirth.setText(year+ "-" + "0"+(monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }
                        else {
                            if (dayOfMonth <10) {
                                edbirth.setText(year+ "-" + (monthOfYear + 1) + "-" + "0"+dayOfMonth);
                            }
                            else {
                                edbirth.setText(year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }
                        lastSelectedYear = year;
                        lastSelectedMonth = monthOfYear;
                        lastSelectedDayOfMonth = dayOfMonth;
                    }
                };
                DatePickerDialog datePickerDialog = null;
                // Calendar Mode (Default):
                datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
                // Show
                datePickerDialog.show();
            }
        });
        //=====================end date picker================================
    }

    //validate
    private boolean validateUpdateProfile(String fullname,String phone,
                                     String address,String description,String birthday) {
        //Date type
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start =sdf.parse(birthday);
            Date today = new Date();//new Date to get current date
            Calendar t = Calendar.getInstance();
            Calendar c = Calendar.getInstance();
            t.setTime(today);
            c.setTime(start);
            c.add(Calendar.DATE,1);// cộng 1 ngày so với ngày hiện tại đề dùng hàm after add đc ngày hiên tại
            start = c.getTime();
            int age = t.get(Calendar.YEAR) - c.get(Calendar.YEAR);
            if (age < 6) { //check age
                edbirth.setError("Age must greater than or equal 6");
                edbirth.setText("Age must greater than or equal 6");
                edbirth.setTextColor(Color.RED);
                edbirth.requestFocus();
                return false;
            } else {
                edbirth.setTextColor(Color.GREEN);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(fullname)) {
            edFullname.setError("Firstname cannot be empty");
            edFullname.requestFocus();
            return false;
        }
        if (fullname.length()<2 || fullname.length()>100) {
            edFullname.setError("Firstname is min 2 character and max 100 character");
            edFullname.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            edPhone.setError("Phone cannot be empty");
            edPhone.requestFocus();
            return false;
        }
        if (phone.length() != 10){
            edPhone.setError("Phone number must be 10 digits");
            edPhone.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(birthday)) {
            edbirth.setError("Birthday cannot be empty");
            edbirth.setText("Birthday cannot be empty");
            edbirth.setTextColor(Color.RED);
            edbirth.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            edAddress.setError("Address cannot be empty");
            edAddress.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            edDescription.setError("Description cannot be empty");
            edDescription.requestFocus();
            return false;
        }
        if (description.length()<3 || description.length()>200) {
            edDescription.setError("Description is min 3 character and max 200 character");
            edDescription.requestFocus();
            return false;
        }
        return true;
    }
}