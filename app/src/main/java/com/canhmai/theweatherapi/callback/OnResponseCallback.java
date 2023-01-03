package com.canhmai.theweatherapi.callback;

public interface OnResponseCallback {
    void onApiSuccess(String key, Object data,String msg);
    void onApiFailure(String key, Object data, String msg );
}
