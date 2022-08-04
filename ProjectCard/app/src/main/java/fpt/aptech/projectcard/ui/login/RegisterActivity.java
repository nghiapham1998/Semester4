package fpt.aptech.projectcard.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fpt.aptech.projectcard.Payload.request.SignupRequest;
import fpt.aptech.projectcard.R;
import fpt.aptech.projectcard.callApiService.ApiService;
import fpt.aptech.projectcard.retrofit.RetrofitService;
import fpt.aptech.projectcard.retrofit.catchError.APIError;
import fpt.aptech.projectcard.retrofit.catchError.ErrorUtils;
import fpt.aptech.projectcard.session.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText edUsername,edPassword,edConfirmPassword,edEmail,edPhone,edFullname,edLastname,edGender,edAddress,edProvince,edDescription;
    private TextView edbirth;
    RadioGroup rgGender;
    RadioButton getGenderSelected;

    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;
    private View view;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();

        edUsername = findViewById(R.id.editUsername);
        edPassword = findViewById(R.id.editPassword);
        edConfirmPassword = findViewById(R.id.editConfirmPassword);
        edEmail = findViewById(R.id.editEmail);
        edFullname = findViewById(R.id.editFullname);
        edLastname = findViewById(R.id.editLastname);
        edbirth = findViewById(R.id.txtEdBirth);
        edPhone = findViewById(R.id.editPhone);
        //gender
        rgGender = (RadioGroup)findViewById(R.id.rgGender);
        edAddress = findViewById(R.id.editAddress);
        edDescription = findViewById(R.id.editDescription);
        edProvince = findViewById(R.id.editProvince);
        btnRegister = findViewById(R.id.btnRegister);

        //==========================DATE PICKER=========================================
        // Get Current Date
        final Calendar c = Calendar.getInstance();
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
                        if (monthOfYear+1 <10) {
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
                datePickerDialog = new DatePickerDialog(RegisterActivity.this, dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
                // Show
                datePickerDialog.show();
            }
        });
        //=====================end date picker================================

        //register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edUsername.getText().toString().trim();
                String email = edEmail.getText().toString().trim();
                String fullname = edFullname.getText().toString().trim();
                String lastname = edLastname.getText().toString().trim();
                String phone = edPhone.getText().toString().trim();
                String address = edAddress.getText().toString().trim();
                String description = edDescription.getText().toString().trim();
                String province = edProvince.getText().toString().trim();
                String birthday = edbirth.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                String confirmPass = edConfirmPassword.getText().toString().trim();
                boolean gender;
                //gender
                getGenderSelected = (RadioButton) rgGender.findViewById(rgGender.getCheckedRadioButtonId());
                if (getGenderSelected.getText().equals("Female")) {
                    gender = true;
                } else {
                    gender = false;
                }

                if (validateRegister(username,email,fullname,lastname,phone,address,description,
                        province,birthday,password,confirmPass)) {

                    Set<String> role = new HashSet<String>();

                    SignupRequest signupRequest = new SignupRequest
                            (
                                    username, email, password, role,phone,address,fullname,lastname,description,birthday,gender,province
                            );

                    ApiService apiService = RetrofitService.getInstance().create(ApiService.class);
                    apiService.signup(signupRequest).enqueue(new Callback<SignupRequest>() {
                        @Override
                        public void onResponse(@NonNull Call<SignupRequest> call, @NonNull Response<SignupRequest> response) {//connect spring boot success
                            if (response.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Sign up successful, please check mail to active", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                APIError error = ErrorUtils.parseError(response);
                                Toast.makeText(RegisterActivity.this, error.message(), Toast.LENGTH_SHORT).show();
                                //check validate field input
                                if (error.message() != null){
                                    if (error.message().contains("Username")){
                                        edUsername.setError(error.message());
                                        edUsername.requestFocus();
                                    }
                                    if (error.message().contains("Email")){
                                        edEmail.setError(error.message());
                                        edEmail.requestFocus();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<SignupRequest> call, @NonNull Throwable t) {//when can't connect to spring boot
                            Toast.makeText(RegisterActivity.this, "Successful, please check mail to active", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //register
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(RegisterActivity.this,R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        finish();

    }
    //validate
    private boolean validateRegister(String username,String email,String fullname,String lastname,String phone,
                                     String address,String description,String province,String birthday, String password,String confirmPass) {

        //Date type
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start =sdf.parse(birthday);
            Date today = new Date();//new Date to get current date
            Calendar t = Calendar.getInstance();
            Calendar c = Calendar.getInstance();
            t.setTime(today);
            c.setTime(start);
            c.add(Calendar.DATE,1);
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

        if (TextUtils.isEmpty(username)) {
            edUsername.setError("Username cannot be empty");
            edUsername.requestFocus();
            return false;
        }
        if (username.trim().length() < 3 || username.trim().length() > 40){
            edUsername.setError("Username must be at least 3 characters and max is 20 characters");
            edUsername.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            edPassword.setError("Password cannot be empty");
            edPassword.requestFocus();
            return false;
        }
        if (password.length() < 6 || password.length() > 40){
            edPassword.setError("Password must be at least 6 characters and max is 40 characters");
            edPassword.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(confirmPass)
                || !TextUtils.equals(confirmPass,password)) {
            edConfirmPassword.setError("Confirm password don't match Password");
            edConfirmPassword.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            edEmail.setError("Email cannot be empty");
            edEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(fullname)) {
            edFullname.setError("Firstname cannot be empty");
            edFullname.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(lastname)) {
            edLastname.setError("Lastname cannot be empty");
            edLastname.requestFocus();
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
        if (TextUtils.isEmpty(province)) {
            edProvince.setError("Province cannot be empty");
            edProvince.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            edDescription.setError("Description cannot be empty");
            edDescription.requestFocus();
            return false;
        }
        return true;
    }

}