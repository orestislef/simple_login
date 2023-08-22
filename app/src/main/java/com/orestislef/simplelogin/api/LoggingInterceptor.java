package com.orestislef.simplelogin.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        // Log the response data
        Response mRes = logResponse(response);

        return mRes != null ? mRes : response;
    }

    private Response logResponse(Response response) {
        try {
            // Log the request URL
            System.out.println("Request URL: " + response.request().url());

            // Log the response code
            System.out.println("Response Code: " + response.code());

            // Log the response body if it exists
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String responseBodyString = responseBody.string();
                System.out.println("Response Body: " + responseBodyString);

                // Reconstruct the response body because OkHttp's response.body() can only be read once
                responseBody = ResponseBody.create(responseBody.contentType(), responseBodyString);
            }

            // Rebuild the response with the original body, so it can be consumed by Retrofit
            response = response.newBuilder().body(responseBody).build();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

