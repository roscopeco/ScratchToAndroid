package com.roscopeco.scratch.runtime.android;

import android.app.Application;
import android.util.Log;

import com.roscopeco.scratch.runtime.MediaManager;
import com.roscopeco.scratch.runtime.ScriptController;

public class ScratchApplication extends Application {
  private static ScratchApplication instance;
    
  public static ScratchApplication instance() {
    if (instance == null) {
      throw new IllegalStateException("Application not initialized");
    }
    return instance;
  }
  
  public ScratchApplication() {
    if (ScratchApplication.instance != null) {
      throw new IllegalStateException("Application already initialized");
    }
    ScratchApplication.instance = this;
  }

  @Override
  public void onCreate() {    
    super.onCreate();
    
    Log.i("ScratchApplication", "Registering script controller");    
    ScriptController.registerInstance(new AndroidScriptController());
    
    Log.i("ScratchApplication", "Registering media manager");    
    MediaManager.registerInstance(new AndroidMediaManager(getApplicationContext()));    
  }

  @Override
  public void onTerminate() {
    Log.i("ScratchApplication", "Application is going away...");    
    super.onTerminate();
  }
  
  
}
