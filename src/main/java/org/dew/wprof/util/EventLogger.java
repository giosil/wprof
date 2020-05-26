package org.dew.wprof.util;

import static org.dew.wprof.util.WebUtil.getTimestamp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.dew.wprof.api.EventTypes;

public 
class EventLogger 
{
  public static final String sFILE_PATH = System.getProperty("user.home") + File.separator + "wprof_evn.csv";
  
  public static final String APP_NAME = "wprof";
  
  public static void application(Class<?> cls, String methodName) {
    String className = cls != null ? cls.getCanonicalName() : "";
    write(EventTypes.APPLICATION, className, methodName, 0, 0, "");
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
  
  public static void write(EventTypes type, String className, String event, long elapsed, int size, String exception) {
    
    if(className == null) className = "";
    if(event     == null) event     = "";
    if(exception == null) exception = "";
    
    PrintWriter pw = null;
    try {
      String row = getTimestamp() + ";";
      
      row += type      + ";";
      row += APP_NAME  + ";";
      row += className + ";";
      row += event     + ";";
      row += elapsed   + ";";
      row += size      + ";";
      row += exception + ";";
      
      // Write data
      
      pw = new PrintWriter(new BufferedWriter(new FileWriter(sFILE_PATH, true)));
      pw.println(row);
      pw.close();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    finally {
      if(pw != null) try { pw.close(); } catch(Exception ex) {}
    }
    
  }
  
}
