package fpt.aptech.projectcard.callApiService;

public class ApiConstant {

    public static final String STATUS_CODE_SUCCESS = "200";
    //for api backend
    public static final String BASE_URL = "http://172.16.0.173:8080";//for same ip wifi, to test api intellij phone replace localhost
    //for client show web front end use on mobile and pc together
    public static final String BASE_URL_CLIENT = "http://172.16.0.173:8081";//test only on Ngoc's pc

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String URL_LOGIN = "/api/auth/signin";
    public static final String URL_SIGNUP = "/api/auth/signup";
    public static final String URL_PROFILE = "/api/auth/profile/{username}";
    public static final String URL_UPDATE_PROFILE = "/api/android/updateProfile/{user_id}";
    public static final String URL_GET_ORDER_BY_USERNAME = "/api/android/orderListByUsername/{username}";
    public static final String URL_GET_PRODUCT_INFO_BY_USERID = "/api/android/getProduct/{username}";
    public static final String URL_GET_SOCIAL_URL_BY_USERNAME = "/api/urlProduct/list/{username}";
    public static final String URL_GET_LIST_LINK_TYPE_URL = "/api/android/listLinkType";
    public static final String URL_ADD_NEW_URL = "/api/android/addUrl";
    public static final String URL_UPDATE_URL = "/api/android/updateUrl/{url_id}";
    public static final String URL_DELETE_URL = "/api/android/deleteUrl/{url_id}";
}
