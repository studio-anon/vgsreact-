package com.vgsreact.number;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;

public class CardNumberModule extends ReactContextBaseJavaModule {

    private String fieldName;

    public CardNumberModule(ReactApplicationContext reactContext, CardNumberManager calManager) {
        super(reactContext);
        if (calManager != null) {
            fieldName = calManager.getFieldName();
        }
    }

    @Override
    public String getName() {
        return "NumberVGSEditText";
    }

    @ReactMethod
    public void getFieldName(Callback successCallback) {
        try {
            successCallback.invoke(fieldName);
        } catch (IllegalViewOperationException e) { }
    }
}