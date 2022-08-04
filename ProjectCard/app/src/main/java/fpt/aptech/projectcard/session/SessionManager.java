package fpt.aptech.projectcard.session;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fpt.aptech.projectcard.callApiService.ApiService;
import fpt.aptech.projectcard.domain.LinkType;
import fpt.aptech.projectcard.domain.Product;
import fpt.aptech.projectcard.domain.UrlProduct;
import fpt.aptech.projectcard.domain.User;
import fpt.aptech.projectcard.retrofit.RetrofitService;
import fpt.aptech.projectcard.ui.login.LoginActivity;

public class SessionManager {
    private static Long saveUserID;
    private static String saveUsername;
    private static String saveEmail;
    private static String saveFullname;
    private static String saveToken;
    private static String saveLinkImage;
    private static User saveUser;
    private static Product saveProduct;
    private static UrlProduct saveUrlProduct;
    private static List<LinkType> saveLinkTypeList;
    private static List<UrlProduct> saveUrlProductList;

    //calculator date 1 - date 2 = years from milliseconds
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public SessionManager() {
    }

    public static Long getSaveUserID() {
        return saveUserID;
    }

    public static void setSaveUserID(Long saveUserID) {
        SessionManager.saveUserID = saveUserID;
    }

    public static String getSaveUsername() {
        return saveUsername;
    }

    public static void setSaveUsername(String saveUsername) {
        SessionManager.saveUsername = saveUsername;
    }

    public static String getSaveEmail() {
        return saveEmail;
    }

    public static void setSaveEmail(String saveEmail) {
        SessionManager.saveEmail = saveEmail;
    }

    public static String getSaveFullname() {
        return saveFullname;
    }

    public static void setSaveFullname(String saveFullname) {
        SessionManager.saveFullname = saveFullname;
    }

    public static String getSaveToken() {
        return saveToken;
    }

    public static void setSaveToken(String saveToken) {
        SessionManager.saveToken = saveToken;
    }

    public static String getSaveLinkImage() {
        return saveLinkImage;
    }

    public static void setSaveLinkImage(String saveLinkImage) {
        SessionManager.saveLinkImage = saveLinkImage;
    }

    public static User getSaveUser() {
        return saveUser;
    }

    public static void setSaveUser(User saveUser) {
        SessionManager.saveUser = saveUser;
    }

    public static Product getSaveProduct() {
        return saveProduct;
    }

    public static void setSaveProduct(Product saveProduct) {
        SessionManager.saveProduct = saveProduct;
    }

    public static UrlProduct getSaveUrlProduct() {
        return saveUrlProduct;
    }

    public static void setSaveUrlProduct(UrlProduct saveUrlProduct) {
        SessionManager.saveUrlProduct = saveUrlProduct;
    }

    public static List<LinkType> getSaveLinkTypeList() {
        return saveLinkTypeList;
    }

    public static void setSaveLinkTypeList(List<LinkType> saveLinkTypeList) {
        SessionManager.saveLinkTypeList = saveLinkTypeList;
    }

    public static List<UrlProduct> getSaveUrlProductList() {
        return saveUrlProductList;
    }

    public static void setSaveUrlProductList(List<UrlProduct> saveUrlProductList) {
        SessionManager.saveUrlProductList = saveUrlProductList;
    }
}
