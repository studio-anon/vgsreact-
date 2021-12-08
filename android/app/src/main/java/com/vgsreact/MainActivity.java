package com.vgsreact;

import com.facebook.react.ReactActivity;
import android.app.Activity;
import android.content.Intent;

import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import android.os.Bundle;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import android.util.Log;
import java.util.List;
import java.util.ArrayList;
import com.facebook.react.ReactPackage;
import java.util.Arrays;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactApplicationContext;
import com.vgsreact.vgs.VGSCollectModule;
import com.vgsreact.vgs.VGSCollectPackage;

public class MainActivity extends ReactActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "vgsreact";
  }

 @Override
 public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
   super.onActivityResult(requestCode, resultCode, data);
     ReactInstanceManager m = getReactInstanceManager();
     List<ReactPackage> l = m.getPackages();
     for(int i = 0;i<l.size();i++) {
         ReactPackage rp = l.get(i);
         if(rp instanceof VGSCollectPackage) {
             VGSCollectModule module = ((VGSCollectPackage)rp).getVGSCollectModule();
             module.onActivityResult(requestCode, resultCode, data);
         }
     }
 }
}
