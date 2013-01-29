package com.roscopeco.scratch.runtime.android;

import android.graphics.Bitmap;

import com.roscopeco.scratch.runtime.Costume;

public class AndroidCostume implements Costume {
  private final String name;
  private final int index;
  private final Bitmap bitmap;
  
  AndroidCostume(String name, int index, Bitmap bitmap) {    
    this.name = name;
    this.index = index;    
    this.bitmap = bitmap;
    
    if (bitmap == null) {
      throw new IllegalArgumentException("Null bitmap for media `" + name + "'");
    }
  }

  @Override
  public int index() {
    return index;
  }

  @Override
  public String name() {
    return name;
  }
  
  public Bitmap bitmap() {
    return bitmap;    
  }
}
