package fpt.aptech.projectcard.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable {
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("linkImage")
    @Expose
    private String linkImage;

    @SerializedName("dateOfbirth")
    @Expose
    private String dateOfbirth;//de string, de date hoac datetime se bi loi onFailure vi sai kieu du lieu voi model web

    @SerializedName("province")
    @Expose
    private String province;

    @SerializedName("gender")
    @Expose
    private Boolean gender;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("nameImage")
    @Expose
    private String nameImage;

    @SerializedName("enable")
    @Expose
    private Boolean enable;

    @SerializedName("fullname")
    @Expose
    private String fullname;

    @SerializedName("lastname")
    @Expose
    private String lastname;

    @SerializedName("description")
    @Expose
    private String description;

    private Boolean locked;

    private String token;

    private Set<Role> roles = new HashSet<>();

    public User(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public User() {
    }

    public User(String lastname, String description, String fullname, String phone, String address, String email, String dateOfbirth, Boolean gender, String province) {
        this.lastname = lastname;
        this.description = description;
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.dateOfbirth = dateOfbirth;
        this.gender = gender;
        this.province = province;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password, Set<Role> roles, String nameImage, String linkImage,String phone,String address,String fullname,String lastname
            ,String description,String dateOfbirth,Boolean gender,String province ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.linkImage = linkImage;
        this.nameImage = nameImage;
        this.address = address;
        this.phone = phone;
        this.fullname = fullname;
        this.lastname = lastname;
        this.description = description;
        this.dateOfbirth = dateOfbirth;
        this.gender = gender;
        this.province = province;
    }

    public User(String username, String email, String password, Boolean enable, Boolean locked) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enable = enable;
        this.locked = locked;

    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getNameImage() {
        return nameImage;
    }

    public void setNameImage(String nameImage) {
        this.nameImage = nameImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
