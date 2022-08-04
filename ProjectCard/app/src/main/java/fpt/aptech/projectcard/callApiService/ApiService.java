package fpt.aptech.projectcard.callApiService;

import java.util.List;

import fpt.aptech.projectcard.Payload.request.LoginRequest;
import fpt.aptech.projectcard.Payload.request.SignupRequest;
import fpt.aptech.projectcard.Payload.request.UpdateProfile;
import fpt.aptech.projectcard.Payload.request.UrlRequest;
import fpt.aptech.projectcard.domain.LinkType;
import fpt.aptech.projectcard.domain.Orders;
import fpt.aptech.projectcard.domain.Product;
import fpt.aptech.projectcard.domain.UrlProduct;
import fpt.aptech.projectcard.domain.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST(ApiConstant.URL_LOGIN)
    Call<LoginRequest> signin(@Body LoginRequest loginRequest);

    @POST(ApiConstant.URL_SIGNUP)
    Call<SignupRequest> signup(@Body SignupRequest signupRequest);

    @GET(ApiConstant.URL_GET_ORDER_BY_USERNAME)
    Call<List<Orders>> getOrdersByUsername(@Path("username") String username);

    @GET(ApiConstant.URL_GET_PRODUCT_INFO_BY_USERID)
    Call<Product> getProduct(@Path("username") String username, @Query("Authorization") String token);

    @GET(ApiConstant.URL_PROFILE)
    Call<User> getProfile(@Path("username") String username);

    @POST(ApiConstant.URL_UPDATE_PROFILE)
    Call<UpdateProfile> updateProfile(@Path("user_id") Long user_id, @Body UpdateProfile updateProfile);

    @GET(ApiConstant.URL_GET_SOCIAL_URL_BY_USERNAME)
    Call<List<UrlProduct>> getUrlProduct(@Path("username") String username);

    @GET(ApiConstant.URL_GET_LIST_LINK_TYPE_URL)
    Call<List<LinkType>> getAllLinkType();

    @POST(ApiConstant.URL_ADD_NEW_URL)
    Call<UrlRequest> addNewUrl(@Body UrlRequest urlRequest);

    @POST(ApiConstant.URL_UPDATE_URL)
    Call<UrlRequest> updateUrl(@Path("url_id") Long url_id,@Body UrlRequest urlRequest);

    @GET(ApiConstant.URL_DELETE_URL)
    Call<String> deleteUrlById(@Path("url_id") Long url_id);
}
