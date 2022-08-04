package fpt.aptech.projectcard.ui.social;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import fpt.aptech.projectcard.MainActivity;
import fpt.aptech.projectcard.Payload.request.UrlRequest;
import fpt.aptech.projectcard.R;
import fpt.aptech.projectcard.callApiService.ApiService;
import fpt.aptech.projectcard.domain.LinkType;
import fpt.aptech.projectcard.domain.Product;
import fpt.aptech.projectcard.retrofit.RetrofitService;
import fpt.aptech.projectcard.session.SessionManager;
import fpt.aptech.projectcard.ui.home.HomeFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ApiService apiService;
    UrlRequest urlRequest;
    TextView txtTitleName, txtTitleLink;
    EditText edUrlName, edUrlLink;

    Spinner spUrlType;
    Product product;
    List<LinkType> linkTypeList;
    Button btnAdd_UpdateURL;
    private Long type_id;
    ImageView imgBackHome;
    private FragmentTransaction fragmentTransaction;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SocialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SocialFragment newInstance(String param1, String param2) {
        SocialFragment fragment = new SocialFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_social, container, false);
        txtTitleName = view.findViewById(R.id.txtUrlName);
        txtTitleLink = view.findViewById(R.id.txtUrlLink);
        edUrlName = view.findViewById(R.id.editUrlName);
        edUrlLink = view.findViewById(R.id.editUrlLink);
        imgBackHome = view.findViewById(R.id.backHomeClick);
        spUrlType = view.findViewById(R.id.spUrlType);
        btnAdd_UpdateURL = view.findViewById(R.id.btnSave_UpdateURL);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)getActivity()).isLogined(SessionManager.getSaveToken());

        apiService = RetrofitService.proceedToken().create(ApiService.class);

        try {
            linkTypeList = apiService.getAllLinkType().execute().body();
            product = apiService.getProduct(SessionManager.getSaveUsername(),SessionManager.getSaveToken()).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        spUrlType.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, linkTypeList));

        spUrlType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinkType type_id = (LinkType) spUrlType.getSelectedItem();
                if (type_id.getId() == 6){//phone
                    txtTitleName.setText("CALL TO");
                    txtTitleLink.setText("PHONE NUMBER");
                    edUrlLink.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                if (type_id.getId() == 7){//email
                    txtTitleName.setText("MAIL TO");
                    txtTitleLink.setText("MAIL ADDRESS");
                    edUrlLink.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
                if (type_id.getId() != 6 && type_id.getId() != 7){//url
                    txtTitleName.setText(R.string.txt_url_name);
                    txtTitleLink.setText(R.string.txt_url_link);
                    edUrlLink.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAdd_UpdateURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkType type_id = (LinkType) spUrlType.getSelectedItem();
                String urlName = edUrlName.getText().toString().trim();
                String urlLink = edUrlLink.getText().toString().trim();
                Long product_id = product.getId();
                Long user_id = SessionManager.getSaveUser().getId();
                urlRequest = new UrlRequest(urlName,urlLink,type_id.getId(),product_id,user_id);
                if (validateUrl(urlName,urlLink)){
                    apiService.addNewUrl(urlRequest).enqueue(new Callback<UrlRequest>() {
                        @Override
                        public void onResponse(Call<UrlRequest> call, Response<UrlRequest> response) {
                            if (response.isSuccessful()) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        Toast.makeText(getActivity().getApplicationContext(), "Added success", Toast.LENGTH_SHORT).show();
                                        spUrlType.setSelection(0);
                                        edUrlName.setText("");
                                        edUrlLink.setText("");
                                    }
                                    if (response.body() == null){
                                        Toast.makeText(getActivity().getApplicationContext(), "Null", Toast.LENGTH_SHORT).show();
                                    }
                                    if (response.code() == 401){
                                        Toast.makeText(getActivity().getApplicationContext(), "Error Auth", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UrlRequest> call, Throwable t) {
                            Toast.makeText(getActivity().getApplicationContext(), "Add Url failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        //back home click
        imgBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getChildFragmentManager().beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.frmSocialNWeb, homeFragment);
                fragmentTransaction.addToBackStack(null).commit();
                ((MainActivity) getActivity()).setActionBarTitle("Home");
                edUrlName.setError(null);
                edUrlLink.setError(null);
            }
        });
    }

    public boolean validateUrl(String urlTitle, String urlLink){
        if (TextUtils.isEmpty(urlTitle)){
            edUrlName.setError("Field can not be empty");
            edUrlName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(urlLink)){
            edUrlLink.setError("Field can not be empty");
            edUrlLink.requestFocus();
            return false;
        }
        return true;
    }
}