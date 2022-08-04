package fpt.aptech.projectcard.Payload.request;

import okhttp3.MultipartBody;

public class ChangeImageUserRequest {

    private String username;

    private MultipartBody.Part image;
}
