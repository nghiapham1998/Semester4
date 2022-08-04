package fpt.aptech.projectcard.Payload.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Set;

import okhttp3.MultipartBody;

public class SignupRequest {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("roles")
    @Expose
    private Set<String> roles;


    private MultipartBody.Part image;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("fullname")
    @Expose
    private String fullname;

    @SerializedName("dateOfbirth")
    @Expose
    private String dateOfbirth;

    @SerializedName("gender")
    @Expose
    private Boolean gender;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("lastname")
    @Expose
    private String lastname;

    @SerializedName("province")
    @Expose
    private String province;

    public SignupRequest(String username, String email, String password, Set<String> roles, String phone, String address, String fullname,
                         String lastname, String description, String dateOfbirth, Boolean gender,String province) {
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.image = image;
        this.address = address;
        this.phone = phone;
        this.fullname = fullname;
        this.lastname = lastname;
        this.description = description;
        this.dateOfbirth = dateOfbirth;
        this.gender = gender;
        this.province = province;
    }
}
