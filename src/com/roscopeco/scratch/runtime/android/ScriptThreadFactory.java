package com.roscopeco.scratch.runtime.android;

import java.util.concurrent.ThreadFactory;

public class ScriptThreadFactory implements ThreadFactory {
  final ThreadGroup group;
  
  public ScriptThreadFactory() {
    group = new ThreadGroup("scripts");    
  }

  public ThreadGroup getGroup() {
    return group;    
  }
  
  @Override
  public Thread newThread(Runnable r) {    
    return new Thread(group, r);
  }
}
