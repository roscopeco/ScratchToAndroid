package com.roscopeco.scratch.runtime.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.Surface.OutOfResourcesException;
import android.view.SurfaceView;

import com.roscopeco.scratch.runtime.AbstractSprite;
import com.roscopeco.scratch.runtime.AbstractStage;

public class MainLoopSurfaceView extends SurfaceView implements Runnable {
  AbstractStage stage;
  AbstractSprite[] sprites;

  public MainLoopSurfaceView(Context context) {
    super(context);
  }

  public MainLoopSurfaceView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MainLoopSurfaceView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }
  
  public void initialize(AbstractStage stage, AbstractSprite[] sprites) {
    this.stage = stage;
    this.sprites = sprites;
  }
  
  @Override
  public void run() {
    if (sprites[0] == null || stage == null) {
      throw new IllegalStateException("MainLoop sprites/stage not initialized");      
    }
    
    Canvas c;
    Surface surface = getHolder().getSurface();

    while (!Thread.interrupted()) {
      c = null;
      try {
        c = surface.lockCanvas(null);
        synchronized (surface) {
          onDraw(c);
        }
      } catch (OutOfResourcesException e) {
        Log.e("MainLoopSurfaceView", "Out of resources", e);
        break;
      } finally {
        if (c != null) {
          surface.unlockCanvasAndPost(c);
        }
      }
    }
    
    Log.d("MAINLOOP", "Main loop is exiting");
  }
  
  Paint p = new Paint();  
  
  private static final float SCRATCHWIDTH = 480.f;
  private static final float SCRATCHHEIGHT = 360.f;
  
  @Override
  public void onDraw(Canvas c) {
    // draw background 
    c.drawColor(Color.BLACK);
    // TODO
    
    // draw sprites
    for (int i = 0; i < sprites.length; i++) {
      AbstractSprite sprite = sprites[i];
      
      if (sprite == null) {
        Log.e("<MAINLOOP:onDraw>", "Null sprite at index " + i);
      }
      
      c.drawBitmap(((AndroidCostume)sprite.costume()).bitmap(), 
          sprite.x() + SCRATCHWIDTH / 2, 
          360 - (sprite.y() + SCRATCHHEIGHT / 2), p);
    }
  }
}
