package fpt.aptech.projectcard.domain;

import java.util.Date;
import java.util.List;

public class Category {

    private Long id;

    private int price;

    private String name;

    private String url;

    private String secretSeri;

    private String frontImage;

    private String backImage;

    private Date create_at;

    private Date update_at;

    private Date delete_at;

//    @OneToMany(mappedBy = "category")
//    private List<Product> products;
//
//    @OneToMany(mappedBy = "category")
//    private List<Orders> orders;

    public Category(int price, String name, Date create_at, Date update_at, Date delete_at) {

        this.price = price;
        this.name = name;
        this.create_at = create_at;
        this.update_at = update_at;
        this.delete_at = delete_at;
    }

    public Category(int price, String name, String frontImage, String backImage, Date create_at, Date update_at, Date delete_at) {
        this.price = price;
        this.name = name;
        this.frontImage = frontImage;
        this.backImage = backImage;
        this.create_at = create_at;
        this.update_at = update_at;
        this.delete_at = delete_at;
    }

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecretSeri() {
        return secretSeri;
    }

    public void setSecretSeri(String secretSeri) {
        this.secretSeri = secretSeri;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public Date getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }

    public Date getDelete_at() {
        return delete_at;
    }

    public void setDelete_at(Date delete_at) {
        this.delete_at = delete_at;
    }
}
