package fpt.aptech.projectcard.Payload.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

import fpt.aptech.projectcard.domain.User;
import okhttp3.MultipartBody;

public class ProductRequest {
    //model to get user info from return data json in spring boot web api
    @SerializedName("user") // key name of key:value when retrieve data from json type
    @Expose
    private User userInfo;

    public User getUserInfo() {
        return userInfo;
    }

    //model for ProductRequest
    @SerializedName("id")
    @Expose
    private Long id ;

    @SerializedName("create_at")
    @Expose
    private String create_at;//de string, de localdatetime se bi loi onFailure vi sai kieu du lieu voi model web

    @SerializedName("delete_at")
    @Expose
    private String delete_at;

    @SerializedName("update_at")
    @Expose
    private String update_at;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("imageUrlcode")
    private String qrcode;//de string, de multipart.part se bi loi onFailure vi sai kieu du lieu voi model web

    @SerializedName("url")
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getDelete_at() {
        return delete_at;
    }

    public void setDelete_at(String delete_at) {
        this.delete_at = delete_at;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
