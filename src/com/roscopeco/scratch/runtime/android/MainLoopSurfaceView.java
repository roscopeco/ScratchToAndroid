package com.roscopeco.scratch.runtime.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
    if (sprites.length == 0 || sprites[0] == null || stage == null) {
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
  public void onDraw(Canvas canvas) {
    final float scaleFactor = Math.min(getWidth() / 480.f, getHeight() / 360.f);
    final float finalWidth = 480.f * scaleFactor;
    final float finalHeight = 360.f * scaleFactor;
    final float leftPadding = (getWidth() - finalWidth) / 2;
    final float topPadding = (getHeight() - finalHeight) / 2;

    final int savedState = canvas.save();
    try {
      canvas.clipRect(leftPadding, topPadding, leftPadding + finalWidth,
          topPadding + finalHeight);

      canvas.translate(leftPadding, topPadding);
      canvas.scale(scaleFactor, scaleFactor);

      doDraw(canvas);
    } finally {
      canvas.restoreToCount(savedState);
    }
  }
  
  private final Rect tempRect = new Rect();

  public void doDraw(Canvas c) {
    // draw background 
    c.drawColor(Color.BLACK);
    // TODO
    
    // draw sprites
    for (int i = 0; i < sprites.length; i++) {
      AbstractSprite sprite = sprites[i];
      
      if (sprite == null) {
        Log.e("<MAINLOOP:onDraw>", "Null sprite at index " + i);
      }
      
      int save = c.save();      
      c.rotate((float)sprite.heading());
      
      /*
      c.drawBitmap(((AndroidCostume)sprite.costume()).bitmap(), 
          sprite.x() + SCRATCHWIDTH / 2, 
          360 - (sprite.y() + SCRATCHHEIGHT / 2), p);
      */
      
      int x = (int)(sprite.x() + SCRATCHWIDTH / 2);
      int y = (int)(360 - (sprite.y() + SCRATCHHEIGHT / 2));
      tempRect.left = x;
      tempRect.top = y;
      tempRect.right = x + sprite.w();
      tempRect.bottom = y + sprite.h();      
      
      AndroidCostume costume = (AndroidCostume)sprite.costume();
      Bitmap b = costume.bitmap();
      c.drawBitmap(b, costume.boundsRect(), tempRect, p);
      c.restoreToCount(save);
    }
  }
}
