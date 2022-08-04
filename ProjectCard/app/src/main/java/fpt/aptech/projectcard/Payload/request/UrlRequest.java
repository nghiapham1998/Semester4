package fpt.aptech.projectcard.Payload.request;

//using for add and update url product
public class UrlRequest {
//    json for postman test
//    "name": "Twitter",
//    "url": "twitter.com/nero3103",
//    "type_id" : 1,
//    "product_id" : 7,
//    "user_id" : 9
    //url name
    private String name;

    private String url;

    private Long type_id;

    private Long product_id;

    private Long user_id;

    public UrlRequest() {
    }

    public UrlRequest(String name, String url, Long type_id, Long product_id, Long user_id) {
        this.name = name;
        this.url = url;
        this.type_id = type_id;
        this.product_id = product_id;
        this.user_id = user_id;
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

    public Long getType_id() {
        return type_id;
    }

    public void setType_id(Long type_id) {
        this.type_id = type_id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
