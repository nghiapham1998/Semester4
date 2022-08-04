package fpt.aptech.projectcard.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import fpt.aptech.projectcard.MainActivity;
import fpt.aptech.projectcard.Payload.request.LoginRequest;
import fpt.aptech.projectcard.R;
import fpt.aptech.projectcard.callApiService.ApiConstant;
import fpt.aptech.projectcard.callApiService.ApiService;
import fpt.aptech.projectcard.retrofit.RetrofitService;
import fpt.aptech.projectcard.session.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    //login
    private TextView txtError,txtAboutUs,txtForgotPass,txtBuyProduct;
    private EditText editUsername,editPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtAboutUs = findViewById(R.id.aboutUs);
        txtForgotPass = findViewById(R.id.forgotPass);
        txtBuyProduct = findViewById(R.id.txt_Buy_Product);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                if (validateLogin()) {
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setUsername(username);
                    loginRequest.setPassword(password);

                    ApiService apiService = RetrofitService.getInstance().create(ApiService.class);
                    apiService.signin(loginRequest).enqueue(new Callback<LoginRequest>() {
                        @Override
                        public void onResponse(Call<LoginRequest> call, Response<LoginRequest> response) {
                            if (response.body() != null) {

                                SessionManager.setSaveUserID(response.body().getUserid());
                                SessionManager.setSaveUsername(response.body().getUsername());
                                SessionManager.setSaveEmail(response.body().getEmail());
                                SessionManager.setSaveToken(response.body().getAccessToken());
                                SessionManager.setSaveLinkImage(response.body().getLinkImage());

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                                editUsername.setText("");
                                editPassword.setText("");
                                editUsername.setError(null);
                                editPassword.setError(null);
                            }
                            if(response.code() == 401){
                                editUsername.setError("Invalid");
                                editPassword.setError("Invalid");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginRequest> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Failed connect to IP", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        txtAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiConstant.BASE_URL_CLIENT + "/Details-1"));
                startActivity(intent);
            }
        });

        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiConstant.BASE_URL_CLIENT + "/Login"));
                startActivity(intent);
            }
        });

        txtBuyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiConstant.BASE_URL_CLIENT + "/Shopping"));
                startActivity(intent);
            }
        });
    }

    //login
    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
//        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }
    //validate login
    private boolean validateLogin() {
        if (TextUtils.isEmpty(editUsername.getText().toString())) {
            editUsername.setError("username cannot be empty");
            editUsername.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(editPassword.getText().toString())) {
            editPassword.setError("password cannot be empty");
            editPassword.requestFocus();
            return false;
        }
        return true;
    }
}