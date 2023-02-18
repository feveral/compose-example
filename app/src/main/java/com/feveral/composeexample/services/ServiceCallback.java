package com.feveral.composeexample.services;

public interface ServiceCallback<T> {
    void onFinish(T t);

    default void onFail() {

    }
    default void onAuthFail() {

    }
    default void onInternetFailure() {

    }
}
