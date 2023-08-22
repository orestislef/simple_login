package com.orestislef.simplelogin.api.interfaces;

import com.orestislef.simplelogin.api.models.ApiResponse;
import com.orestislef.simplelogin.api.models.ChangePasswordRequest;
import com.orestislef.simplelogin.api.models.LoginRequest;
import com.orestislef.simplelogin.api.models.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("user_api/login.php")
    Call<ApiResponse> login(@Body LoginRequest loginRequest);

    @POST("user_api/register.php")
    Call<ApiResponse> register(@Body RegisterRequest registerRequest);

    @POST("user_api/changepassword.php")
    Call<ApiResponse> changePassword(@Body ChangePasswordRequest changePasswordRequest);
}
