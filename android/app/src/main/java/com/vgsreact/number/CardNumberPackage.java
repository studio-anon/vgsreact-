package com.vgsreact.number;

import com.vgsreact.OnCreateViewInstanceListener;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.List;

public class CardNumberPackage implements ReactPackage {

  private OnCreateViewInstanceListener listener;
  private CardNumberManager calManager;

  public CardNumberPackage(OnCreateViewInstanceListener listener) {
    this.listener = listener;
  }

  @Override
  public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
    if (calManager == null) {
      calManager = new CardNumberManager(listener);
    }
    return Arrays.<NativeModule>asList(
            new CardNumberModule(reactContext, calManager)
    );
  }

  @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
    if (calManager == null) {
      calManager = new CardNumberManager(listener);
    }
    return Arrays.<ViewManager>asList(
            calManager
    );
  }
}