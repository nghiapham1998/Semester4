package fpt.aptech.projectcard.Payload.request;

import okhttp3.MultipartBody;

public class AddCategoryRequest {
    private String name;
    private String price;
    private MultipartBody.Part front;
    private MultipartBody.Part back;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public MultipartBody.Part getFront() {
        return front;
    }

    public void setFront(MultipartBody.Part front) {
        this.front = front;
    }

    public MultipartBody.Part getBack() {
        return back;
    }

    public void setBack(MultipartBody.Part back) {
        this.back = back;
    }
}
