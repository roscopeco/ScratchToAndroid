package com.roscopeco.scratch.template;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;

import com.roscopeco.scratch.runtime.ScriptController;
import com.roscopeco.scratch.runtime.android.AndroidScriptController;
import com.roscopeco.scratch.runtime.android.MainLoopSurfaceView;

public class Main extends Activity implements SurfaceHolder.Callback {
  MainLoopSurfaceView sv;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(sv = new MainLoopSurfaceView(this));
    sv.getHolder().addCallback(this);
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width,
      int height) {
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    new Thread(new Runnable() {
      public void run() {
        Objects.initialize();        
        sv.initialize(Objects.stage, Objects.sprites);
        
        Thread mainLoop = new Thread(sv);
        mainLoop.setName("<MAINLOOP>");
        mainLoop.start();
        
        ((AndroidScriptController)ScriptController.getInstance()).registerMainLoop(mainLoop);
        ScriptController.getInstance().start();
      }
    }).start();
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    Log.d("Main", "Surface destroyed");
    
    // TODO cleanup etc...
  }
}
