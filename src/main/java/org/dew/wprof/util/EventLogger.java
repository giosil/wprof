package org.dew.wprof.util;

import static org.dew.wprof.util.WebUtil.getTimestamp;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.Calendar;

import org.dew.wprof.api.EventTypes;

public 
class EventLogger 
{
  public static final Path PATH_FILE  = Paths.get(TimerTaskRecorder.EVN_FILE_PATH);
  
  public static final String APP_NAME = "wprof";
  
  public static long lRetentionThreshold = System.currentTimeMillis();
  
  static {
    
    init();
    
  }
  
  public static void application(Class<?> cls, String methodName) {
    String className = cls != null ? cls.getCanonicalName() : "";
    write(EventTypes.APPLICATION, className, methodName, 0, 0, "");
  }
  
  public static void application(Class<?> cls, String methodName, int size) {
    String className = cls != null ? cls.getCanonicalName() : "";
    write(EventTypes.APPLICATION, className, methodName, 0, size, "");
  }
  
  public static void application(Class<?> cls, String methodName, long startTime, long endTime, int size) {
    String className = cls != null ? cls.getCanonicalName() : "";
    long elapsed = endTime > 0 ? endTime - startTime : 0;
    write(EventTypes.APPLICATION, className, methodName, elapsed, size, "");
  }
  
  public static void application(String className, String methodName, long startTime, long endTime, Exception ex) {
    long elapsed = endTime > 0 ? endTime - startTime : 0;
    String exception = ex != null ? ex.toString() : "";
    write(EventTypes.APPLICATION, className, methodName, elapsed, 0, exception);
  }
  
  public static void invoke(Class<?> cls, String methodName, int size) {
    String className = cls != null ? cls.getCanonicalName() : "";
    write(EventTypes.INVOKE, className, methodName, 0, size, "");
  }
  
  public static void returnInvoke(Class<?> cls, String methodName, long startTime, long endTime, int size) {
    String className = cls != null ? cls.getCanonicalName() : "";
    long elapsed = endTime > 0 ? endTime - startTime : 0;
    write(EventTypes.RETURN, className, methodName, elapsed, size, "");
  }
  
  public static void entering(Class<?> cls, String methodName) {
    String className = cls != null ? cls.getCanonicalName() : "";
    write(EventTypes.ENTERING, className, methodName, 0, 0, "");
  }
  
  public static void entering(String className, String methodName) {
    write(EventTypes.ENTERING, className, methodName, 0, 0, "");
  }
  
  public static void entering(Class<?> cls, String methodName, int size) {
    String className = cls != null ? cls.getCanonicalName() : "";
    write(EventTypes.ENTERING, className, methodName, 0, size, "");
  }
  
  public static void exiting(Class<?> cls, String methodName, long startTime, long endTime, int size) {
    String className = cls != null ? cls.getCanonicalName() : "";
    long elapsed = endTime > 0 ? endTime - startTime : 0;
    write(EventTypes.EXITING, className, methodName, elapsed, size, "");
  }
  
  public static void exiting(String className, String methodName, long startTime, long endTime, int size) {
    long elapsed = endTime > 0 ? endTime - startTime : 0;
    write(EventTypes.EXITING, className, methodName, elapsed, size, "");
  }
  
  public static void exiting(Class<?> cls, String methodName, long elapsed, int size) {
    String className = cls != null ? cls.getCanonicalName() : "";
    write(EventTypes.EXITING, className, methodName, elapsed, size, "");
  }
  
  public static void exiting(String className, String methodName, long startTime, long endTime, Exception ex) {
    long elapsed = endTime > 0 ? endTime - startTime : 0;
    String exception = ex != null ? ex.toString() : "";
    write(EventTypes.EXITING, className, methodName, elapsed, 0, exception);
  }
  
  public static 
  void write(EventTypes type, String className, String event, long elapsed, int size, String exception)
  {
    long currentTimeMillis = System.currentTimeMillis();
    if(currentTimeMillis > lRetentionThreshold) {
      
      init();
      
    }
    
    if(className == null) className = "";
    if(event     == null) event     = "";
    if(exception == null) exception = "";
    try {
      String row = getTimestamp() + ";";
      row += type      + ";";
      row += APP_NAME  + ";";
      row += className + ";";
      row += event     + ";";
      row += elapsed   + ";";
      row += size      + ";";
      row += exception + ";\n";
      
      // Write data
      Files.write(PATH_FILE, row.getBytes(), StandardOpenOption.APPEND);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public static
  void init()
  {
    Calendar calToday = Calendar.getInstance();
    calToday.set(Calendar.HOUR_OF_DAY, 0);
    calToday.set(Calendar.MINUTE,      0);
    calToday.set(Calendar.SECOND,      0);
    calToday.set(Calendar.MILLISECOND, 0);
    
    long lToday = calToday.getTimeInMillis();
    
    calToday.add(Calendar.DATE, 1);
    
    lRetentionThreshold = calToday.getTimeInMillis() - 1;
    
    try {
      File fileEvn = new File(TimerTaskRecorder.EVN_FILE_PATH);
      if(fileEvn.exists()) {
        if(fileEvn.lastModified() < lToday) {
          if(fileEvn.delete()) {
            fileEvn.createNewFile();
          }
        }
      }
      else {
        fileEvn.createNewFile();
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}
