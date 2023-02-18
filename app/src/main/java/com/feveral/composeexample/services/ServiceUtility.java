package com.feveral.composeexample.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Response;

public class ServiceUtility {

    public static boolean isResponseOk(Response response) {
        return response.code() >= 200 && response.code() < 300;
    }

    public static boolean isAuthFailed(Response response) {
        return response.code() == 401 || response.code() == 403;
    }

    public static String extractEndpoint(Call call) {
        String endpoint = call.request().url().url().toString();
        endpoint = endpoint.replace("http://", "");
        endpoint = endpoint.replace("https://", "");
        endpoint = endpoint.replace(call.request().url().host(), "");
        return endpoint;
    }

    public static Object handleResponse(Call call, Response response, ServiceCallback callback) {
        String endpoint = extractEndpoint(call);
        Log.d("[API]", String.format("%s %s status = %s", call.request().method(), endpoint, response.code()));
        if (isResponseOk(response)) {
            callback.onFinish(response.body());
            return response.body();
        } else if (isAuthFailed(response)) {
            callback.onAuthFail();
            handleErrorResponse(String.format("%s %s", call.request().method(), endpoint), response);
            return null;
        } else {
            callback.onFail();
            handleErrorResponse(String.format("%s %s", call.request().method(), endpoint), response);
            return null;
        }
    }

    public static void handleErrorResponse(String apiName, Response response) {
        try {
            Log.e("[API Error]", String.format("%s: %s", apiName, response.errorBody().string()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onFailureMessage(Throwable t) {
        if (t != null) {
            Log.e("[API Failed]", t.getMessage());
        }
    }
}