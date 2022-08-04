package fpt.aptech.projectcard.domain;


import java.util.Date;

public class Orders {

    private Long id;

    private int price;

    private Category category;

    private User user;

    private String address;

    private String phone;

    private String fullname;

    private Order_Process order_process;

    private Product product;

    private String createdAt;

    private String confirmedAt;

    private String finishedAt;

    private String canceledAt;

    public Orders() {
    }

    public Orders(Long id, int price, Category category, User user, String address, String phone, String fullname, Order_Process order_process, Product product, String createdAt, String confirmedAt, String finishedAt, String canceledAt) {
        this.id = id;
        this.price = price;
        this.category = category;
        this.user = user;
        this.address = address;
        this.phone = phone;
        this.fullname = fullname;
        this.order_process = order_process;
        this.product = product;
        this.createdAt = createdAt;
        this.confirmedAt = confirmedAt;
        this.finishedAt = finishedAt;
        this.canceledAt = canceledAt;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Order_Process getOrder_process() {
        return order_process;
    }

    public void setOrder_process(Order_Process order_process) {
        this.order_process = order_process;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(String confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(String finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(String canceledAt) {
        this.canceledAt = canceledAt;
    }
}
