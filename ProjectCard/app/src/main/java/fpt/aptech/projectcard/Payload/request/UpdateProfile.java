package fpt.aptech.projectcard.Payload.request;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class UpdateProfile implements Serializable {

    //model to update user info from data json
    @SerializedName("fullname")
    @Expose
    private String fullname;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("dateOfbirth")
    @Expose
    private String dateOfbirth;

    @SerializedName("gender")
    @Expose
    private Boolean gender;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("lastname") // key name of key:value when retrieve data from json type
    @Expose
    private String lastname;

    public UpdateProfile() {
    }

    public UpdateProfile(String fullname, String email, String phone, String address, String dateOfbirth, Boolean gender, String description) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dateOfbirth = dateOfbirth;
        this.gender = gender;
        this.description = description;
    }

    public UpdateProfile(String fullname, String email, String phone, String address, String dateOfbirth, Boolean gender, String description, String lastname) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dateOfbirth = dateOfbirth;
        this.gender = gender;
        this.description = description;
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfbirth() {
        return dateOfbirth;
    }

    public void setDateOfbirth(String dateOfbirth) {
        this.dateOfbirth = dateOfbirth;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    // post raw json manual
//    @Override
//    public String toString() {
//        return '{' +
//                "\"lastname\" : \"" + lastname + '\"' +
//                ", \"description\" : \"" + description + '\"' +
//                ", \"fullname\" : \"" + fullname + '\"' +
//                ", \"phone\" : \"" + phone + '\"' +
//                ", \"address\" : \"" + address + '\"' +
//                ", \"email\" : \"" + email + '\"' +
//                ", \"dateOfbirth\" : \"" + dateOfbirth + '\"' +
//                ", \"gender\" :" + gender +
//                ", \"province\" : \"" + province + '\"' +
//                '}';
//    }
}
