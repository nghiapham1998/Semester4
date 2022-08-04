package fpt.aptech.projectcard.ui.social;

import android.graphics.Color;
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
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
 * Use the {@link UpdateUrlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateUrlFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ApiService apiService;
    UrlRequest urlRequest;
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

    public UpdateUrlFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateUrlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateUrlFragment newInstance(String param1, String param2) {
        UpdateUrlFragment fragment = new UpdateUrlFragment();
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
        View view = inflater.inflate(R.layout.fragment_update_url, container, false);
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
        spUrlType.setSelection(SessionManager.getSaveUrlProduct().getLinkType().getId().intValue()-1);//spinner index start at 0
        edUrlName.setText(SessionManager.getSaveUrlProduct().getName());
        edUrlLink.setText(SessionManager.getSaveUrlProduct().getUrl());

        spUrlType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinkType type_id = (LinkType) spUrlType.getSelectedItem();
                if (type_id.getId() == 6){
                    edUrlLink.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                if (type_id.getId() == 7){
                    edUrlLink.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
                if (type_id.getId() != 6 && type_id.getId() != 7){
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
                Long url_id = SessionManager.getSaveUrlProduct().getId();
                LinkType type_id = (LinkType) spUrlType.getSelectedItem();
                String urlName = edUrlName.getText().toString().trim();
                String urlLink = edUrlLink.getText().toString().trim();
                Long product_id = product.getId();
                Long user_id = SessionManager.getSaveUser().getId();
                if (validateUrl(urlName,urlLink)){
                    urlRequest = new UrlRequest(urlName,urlLink,type_id.getId(),product_id,user_id);
                    apiService.updateUrl(url_id, urlRequest).enqueue(new Callback<UrlRequest>() {
                        @Override
                        public void onResponse(Call<UrlRequest> call, Response<UrlRequest> response) {
                            if (response.isSuccessful()) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        //can't save session value in onResponse, because it will null after finish run this function
                                        Toast.makeText(getActivity().getApplicationContext(), "Updated success", Toast.LENGTH_SHORT).show();
                                        spUrlType.setSelection(0);
                                        edUrlName.setText("");
                                        edUrlLink.setText("");
                                        fragmentTransaction = getChildFragmentManager().beginTransaction();
                                        HomeFragment homeFragment = new HomeFragment();
                                        fragmentTransaction.replace(R.id.frmUpdateUrl, homeFragment);
                                        fragmentTransaction.addToBackStack(null).commit();
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
                            Toast.makeText(getActivity().getApplicationContext(), "Update Url failed", Toast.LENGTH_SHORT).show();
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
                fragmentTransaction.replace(R.id.frmUpdateUrl, homeFragment);
                fragmentTransaction.addToBackStack(null).commit();
                edUrlName.setError(null);
                edUrlLink.setError(null);
            }
        });
    }

    //validate
    private boolean validateUrl(String urlName,String urlLink) {

        if (TextUtils.isEmpty(urlName)) {
            edUrlName.setError("Url Name cannot be empty");
            edUrlName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(urlLink)) {
            edUrlLink.setError("Url link cannot be empty");
            edUrlLink.requestFocus();
            return false;
        }
        return true;
    }
}