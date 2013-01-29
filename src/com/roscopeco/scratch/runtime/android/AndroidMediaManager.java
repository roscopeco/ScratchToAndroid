package com.roscopeco.scratch.runtime.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.roscopeco.scratch.runtime.Costume;
import com.roscopeco.scratch.runtime.MediaManager;
import com.roscopeco.scratch.runtime.Sound;

public class AndroidMediaManager extends MediaManager {
  private final String packageName;
  private final Context context;
  
  public AndroidMediaManager(Context context) {
    this.context = context;
    packageName = context.getApplicationContext().getApplicationInfo().packageName;
  }

  @Override
  public Costume loadImage(int index, String name, String id) {    
    // remove file extension from id (Android doesn't want or need it)
    id = id.substring(0, id.lastIndexOf('.'));    
    Log.v("<MEDIAMANAGER>", "Loading bitmap; id : " + id + "('" + name + "') (packageName is '" + packageName + "')");
    int rid = context.getResources().getIdentifier(id, "drawable", packageName);
    
    // TODO check if rid is null, indicating resource not found... Throw exception.
    
    Bitmap b = BitmapFactory.decodeResource(context.getResources(), rid);
    return new AndroidCostume(name, index, b);    
  }

  @Override
  public Sound loadSound(String id) {
    // TODO Auto-generated method stub
    return null;
  }

}
