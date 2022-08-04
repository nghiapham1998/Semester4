package fpt.aptech.projectcard.Payload.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginRequest implements Serializable {
    //filed for login
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    // fields for get data json after login success return json data
    @SerializedName("id") //this is key name of key:value when return data json
    @Expose
    private Long userid;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("linkImage")
    @Expose
    private String linkImage;

    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String toString() {
        return "UserInfo {"
                + getUsername() + "\n"
                + getEmail() + "\n"
                + getAccessToken()
                + "}";
    }
}

