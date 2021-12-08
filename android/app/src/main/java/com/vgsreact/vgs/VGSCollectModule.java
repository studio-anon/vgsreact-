package com.vgsreact.vgs;

import android.app.Activity;
import android.content.Intent;

import com.vgsreact.R;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.verygoodsecurity.vgscollect.core.Environment;
import com.verygoodsecurity.vgscollect.core.VGSCollect;
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener;
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest;
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse;
import com.verygoodsecurity.vgscollect.core.model.state.FieldState;
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener;
import com.verygoodsecurity.vgscollect.view.InputFieldView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class VGSCollectModule extends ReactContextBaseJavaModule {
    private static final String RESPONSE_EVENT_NAME = "onVGSResponse";
    private static ReactApplicationContext reactContext;
    private Activity activity;

    private VGSCollect collect;
    private HashMap<String, String> cardData = new HashMap<>();
    private final String CARD_BRAND_PROPERTY_NAME = "cardBrand";
    private final String CARD_LAST_4_PROPERTY_NAME = "last4";
    private final String CARD_BIN_PROPERTY_NAME = "bin";

    private String token = "eyJraWQiOiJRZlJHTVBRV3QzSkN5M1ltZU1kaHYwa0NBQXVtTmpVSU1uc1hpUlRRXC9jZz0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI0MzcxYjMzYS02MWNkLTQzY2YtYWQyMi03YmI2NjljMDdiZDYiLCJldmVudF9pZCI6ImU2MGM4Mjk0LWI2YzYtNGJhZi1iMDliLTk2MWVkNGYxNDQ5MiIsInRva2VuX3VzZSI6ImFjY2VzcyIsInNjb3BlIjoiYXdzLmNvZ25pdG8uc2lnbmluLnVzZXIuYWRtaW4iLCJhdXRoX3RpbWUiOjE2MzgxNTkyMzgsImlzcyI6Imh0dHBzOlwvXC9jb2duaXRvLWlkcC5hcC1zb3V0aGVhc3QtMi5hbWF6b25hd3MuY29tXC9hcC1zb3V0aGVhc3QtMl92cTdzTjExQ3IiLCJleHAiOjE2Mzg5NDEwMjEsImlhdCI6MTYzODkzNzQyMSwianRpIjoiZWM5ODMwNTItYjRjNy00YzkwLWFiMGMtZmFkMDUzODAzMTBlIiwiY2xpZW50X2lkIjoiMXNicWtkN2NtMTE2MDB2cmxkN2VlMm1mYXUiLCJ1c2VybmFtZSI6IjNjNDdhZDFmLWY2YzItNDBiYS05MWY2LTgxYWVlZjhiNWIxNCJ9.WFJXTRsfp8jhbwwZOwFMMlni3GCVh5O0uLKum3c1b3oYxdPiBJlXUpkj_uXUqNeSLJvdIMudbmPGSf3BfVtOfEXS98XgXlnXHtaGl4LejCUu4RDrPe8v60eaIxeHDm1MuDFEXSi63bL0teOuWnMapQ24-EDWw0d2skmvfdF58fkhMbwrkk_Zi50R00x3oSR46XAo9N2gN-2l0GCpE9S4lxyOuU-2FAgLmjhx4Y-KflGmCyNsI9vPBC6ZjOdg7583ZFH8YezxKBP11mEUCeI5_Fer6IYTUceARPzds7jDP7wCyxW5C5Fwg0oQQp7JrbkDfR_ARV22fIpXCskAIqjhnA";
    private String correlationId = "";
    private String path = "";
    private String masterCardPath = "";
    private InputFieldView inputFieldViewInstance;

    VGSCollectModule(ReactApplicationContext c) {
        super(c);
        reactContext = c;
    }

    @Override
    public void initialize() {
        super.initialize();
        activity = reactContext.getCurrentActivity();

        init();
    }

    @ReactMethod
    public void init() {
        try {
            boolean isVGSSandBoxEnv = Boolean.parseBoolean(activity.getString(R.string.isVGSSandBoxEnv));
            if (isVGSSandBoxEnv) {
                collect = new VGSCollect.Builder(activity, "tntgwratk6j")
                        .setEnvironment(Environment.SANDBOX)
                        .create();
                path = "/live/api/v2/enroll";
                masterCardPath = "/live/api/v2/enrollees/mastercard";
            } else {
                collect = new VGSCollect.Builder(activity, "tntnxygs5ut")
                        .setEnvironment(Environment.LIVE)
                        .create();
                path = "/prod/api/v2/enroll";
                masterCardPath = "/prod/api/v2/enrollees/mastercard";

            }
            initListeners();
        } catch (Exception ex){
            //Swallowed
        }

    }

    private void initListeners() {
        collect.addOnResponseListeners(new VgsCollectResponseListener() {
            @Override
            public void onResponse( VGSResponse response) {
                sendResponse(response);
            }
        });
        collect.addOnFieldStateChangeListener(new OnFieldStateChangeListener() {
            @Override
            public void onStateChange(FieldState state) {
                updateUserStates();
            }
        });
    }

    @ReactMethod
    public void addHeaders(String token) {
        HashMap data = new HashMap<String, String>();
        data.put("Authorization", "Bearer " + token);
        this.token = token;
        collect.setCustomHeaders(data);
    }

    @ReactMethod
    public void doAddVisaANZCard(boolean isOptin, Callback callback) {
        this.correlationId = UUID.randomUUID().toString();
        HashMap header = new HashMap<String, String>();
        header.put("Authorization", "Bearer " + this.token);
        header.put("x-correlation-id", this.correlationId);
        collect.setCustomHeaders(header);
        HashMap data = new HashMap<String, String>();
        data.put("opt_in", isOptin);
        data.put("card_scheme", cardData.get(CARD_BRAND_PROPERTY_NAME));
        VGSRequest params = new VGSRequest.VGSRequestBuilder().setPath(path)
                .setCustomData(data).build();
       collect.asyncSubmit(params);
        callback.invoke("{}");
    }

    @ReactMethod
    public void doAddMasterCard(Callback callback) {
        HashMap data = new HashMap<String, String>();
        data.put("card_scheme", cardData.get(CARD_BRAND_PROPERTY_NAME));
        VGSRequest params = new VGSRequest.VGSRequestBuilder().setPath(masterCardPath).setCustomData(data).build();
        collect.asyncSubmit(params);
        callback.invoke("{}");
    }

    @Override
    public String getName() {
        return "VGSManager";
    }

    @ReactMethod
    public void submitDataFromJS(String carNumber, Callback callback) {
        HashMap data = new HashMap<String, String>();
        data.put("card_number", carNumber);
        VGSRequest params = new VGSRequest.VGSRequestBuilder().setPath(masterCardPath)
                .setCustomData(data).build();
        collect.asyncSubmit(params);
        callback.invoke("{}");
    }

    @ReactMethod
    public void getCardState(Callback callback) {
        final JSONObject json = new JSONObject(cardData);
        callback.invoke(json.toString());
    }

    @ReactMethod
    public void sendDataRegister(Callback callback){
        if(inputFieldViewInstance != null) {
            inputFieldViewInstance.hideKeyboard();
        }
    }


    public void bindView(InputFieldView inputFieldView) {
        collect.bindView(inputFieldView);
        inputFieldViewInstance = inputFieldView;
    }

    private void sendResponse(VGSResponse response) {
        String responseStr;

        if (response instanceof VGSResponse.SuccessResponse) {
            responseStr = response.getBody();
            if(inputFieldViewInstance != null){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inputFieldViewInstance.setText("");
                    }
                });

            }
        } else {
            JSONObject json = new JSONObject();
            try {
                json.put("correlationId", this.correlationId);
                json.put("errorData", response.getBody() == null ? "Oops, Something Went Wrong": response.getBody());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            responseStr = json.toString();
        }
        if(inputFieldViewInstance != null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    inputFieldViewInstance.setText("");
                }
            });

        }
        this.getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(RESPONSE_EVENT_NAME, responseStr);
    }

    private void updateUserStates() {
        String eventName = "onVGSStateChange";
        List<FieldState> states = collect.getAllStates();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < states.size(); i++) {
            FieldState it = states.get(i);
            builder.append(it.getFieldName()).append("\n")
                    .append("   hasFocus: ").append(it.getHasFocus()).append("\n")
                    .append("   isValid: ").append(it.isValid()).append("\n")
                    .append("   isEmpty: ").append(it.isEmpty()).append("\n")
                    .append("   isRequired: ").append(it.isRequired()).append("\n");
            if (it instanceof FieldState.CardNumberState && it.isValid()) {
                builder.append("    type: ").append(((FieldState.CardNumberState) it).getCardBrand()).append("\n")
                        .append("       end: ").append(((FieldState.CardNumberState) it).getLast()).append("\n")
                        .append("       bin: ").append(((FieldState.CardNumberState) it).getBin()).append("\n")
                        .append(((FieldState.CardNumberState) it).getNumber()).append("\n");
                cardData.put(CARD_BRAND_PROPERTY_NAME, ((FieldState.CardNumberState) it).getCardBrand().toUpperCase());
                cardData.put(CARD_LAST_4_PROPERTY_NAME, ((FieldState.CardNumberState) it).getLast());
                cardData.put(CARD_BIN_PROPERTY_NAME, ((FieldState.CardNumberState) it).getBin());
            } else if(it instanceof FieldState.CardNumberState && it.isValid()) {
                cardData.put(CARD_BRAND_PROPERTY_NAME,"");
                cardData.put(CARD_LAST_4_PROPERTY_NAME, "");
                cardData.put(CARD_BIN_PROPERTY_NAME, "");
            }

            builder.append("\n");
        }
        System.out.println(builder.toString());
        this.getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, builder.toString());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}