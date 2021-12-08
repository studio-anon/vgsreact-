package com.vgsreact.vgs;


import com.vgsreact.OnCreateViewInstanceListener;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.verygoodsecurity.vgscollect.view.InputFieldView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VGSCollectPackage implements ReactPackage, OnCreateViewInstanceListener {

  private VGSCollectModule module;

  public OnCreateViewInstanceListener getListener() {
    return this;
  }

  public VGSCollectModule getVGSCollectModule() {
    return module;
  }

  @Override
  public void onCreateViewInstance(InputFieldView inputFieldView) {
    module.bindView(inputFieldView);
  }
  @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
      return Collections.emptyList();
  }

  @Override
  public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
    module = new VGSCollectModule(reactContext);

    List<NativeModule> modules = new ArrayList<>();

    modules.add(module);

    return modules;
  }


}