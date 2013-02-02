package com.roscopeco.scratch.runtime.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.roscopeco.scratch.runtime.AbstractScript;
import com.roscopeco.scratch.runtime.ScriptController;

public class AndroidScriptController extends ScriptController {
  private ArrayList<AbstractScript> startScripts = new ArrayList<AbstractScript>();
  //private LinkedList<RunningScriptWrapper> runningScripts = new LinkedList<RunningScriptWrapper>();
  private HashMap<String, ArrayList<AbstractScript>> broadcastReceivers = new HashMap<String, ArrayList<AbstractScript>>();
  private HashMap<String, Object> vars = new HashMap<String, Object>();
  private ScriptThreadFactory factory = new ScriptThreadFactory();
  private ExecutorService exec = Executors.newCachedThreadPool(factory);
  
  private volatile boolean isRunning = false;
  private Thread mainLoop;
  
  /*
  class RunningScriptWrapper implements Runnable {
    Runnable delegate;
    Thread thread;
    
    RunningScriptWrapper(AbstractScript delegate) {
      this.delegate = delegate;
    }

    @Override
    public void run() {
      addRunningScript(this);
      
      try {
        delegate.run();
      } finally {
        removeRunningScript(this);
      }
    }
  }
  
  synchronized void addRunningScript(RunningScriptWrapper script) {
    Log.d("<AndroidScriptController>", "Adding " + script.delegate + " to running list");
    runningScripts.add(script);
  }
  
  synchronized void removeRunningScript(RunningScriptWrapper script) {
    Log.d("<AndroidScriptController>", "Removing " + script.delegate + " from running list");
    runningScripts.remove(script);
  }
  */
  
  //Thread startScript(AbstractScript script) {        
  void startScript(AbstractScript script) {        
    exec.execute(script);
    
    /*
    RunningScriptWrapper w = new RunningScriptWrapper(script);
    Thread t = new Thread(w);
    w.thread = t;
    t.start();
    return t;
    */    
  }
  
  public void registerMainLoop(Thread mainLoop) {
    this.mainLoop = mainLoop;
  }
  
  @Override
  public void registerStartScript(AbstractScript script) {
    if (isRunning) {
      throw new IllegalStateException("ScriptController already started");
    }
    startScripts.add(script);
  }

  @Override
  public void registerKeyEventReceiver(String keyName, AbstractScript script) {
    // TODO Auto-generated method stub

  }

  @Override
  public void registerBroadcastReceiver(String broadcast, AbstractScript script) {
    if (isRunning) {
      throw new IllegalStateException("ScriptController already started");
    }
    
    ArrayList<AbstractScript> scripts = broadcastReceivers.get(broadcast);
    
    if (scripts == null) {
      broadcastReceivers.put(broadcast, scripts = new ArrayList<AbstractScript>());      
    }
    scripts.add(script);
  }

  @Override
  public void registerMouseClickEventReceiver(AbstractScript script) {
    // TODO Auto-generated method stub

  }

  @Override
  public void broadcast(String name) {
    if (!isRunning) {
      throw new IllegalStateException("ScriptController not started");
    }
    
    ArrayList<AbstractScript> scripts = broadcastReceivers.get(name);
    if (scripts != null) {
      for (AbstractScript script : scripts) {
        startScript(script);
      }
    }
  }

  @Override
  public void stopAllSounds() {
    // TODO Auto-generated method stub

  }

  @Override
  public void setVar(String name, Object value) {
    vars.put(name, value);

  }

  @Override
  public Object getVar(String name) {
    Object var; 
    var = vars.get(name);
    
    if (var == null) {
      // TODO just a workaround, to be removed.
      return Long.valueOf(0);
    } else {
      return var;
    }
  }

  @Override
  public boolean checkKeyPress(String keyname) {
    // TODO Auto-generated method stub
    return false;
  }

  Object startStopMonitor = new Object();
  
  @Override
  public void start() {
    if (mainLoop == null) {
      throw new IllegalStateException("Main loop not registered with ScriptController");
    }
    
    synchronized (startStopMonitor) {
      if (!isRunning) {
        isRunning = true;
        for (AbstractScript script : startScripts) {
          startScript(script);
        }
      } else {
        throw new IllegalStateException("ScriptController already started");
      }
    }
  }

  @Override
  public void stopAll() {
    synchronized (startStopMonitor) {
      if (isRunning) {
        /*
        for (RunningScriptWrapper w : runningScripts) {
          w.thread.interrupt();
        }
        */
        mainLoop.interrupt();
        exec.shutdownNow();
        isRunning = false;
      }
    }    
  }

  @Override
  public boolean allStopped() {
    return !isRunning;
  }
}
