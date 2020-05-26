package org.dew.wprof.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.ThreadMXBean;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

import static org.dew.wprof.util.WebUtil.getTimestamp;

public 
class TimerTaskRecorder extends TimerTask 
{
  public static final String DATA_FOLDER   = System.getProperty("user.home");
  
  public static final String JVM_FILE_PATH = DATA_FOLDER + File.separator + "wprof_jvm.csv";
  public static final String SYS_FILE_PATH = DATA_FOLDER + File.separator + "wprof_sys.csv";
  public static final String WIN_BATCH     = DATA_FOLDER + File.separator + "wprof.cmd";
  public static final String LINUX_BATCH   = DATA_FOLDER + File.separator + "wprof.sh";
  
  protected transient boolean running = false;
  
  protected boolean firstTime = true;
  
  public void run() {
    
    if(running) return;
    running = true;
    
    if(firstTime) {
      
      init();
      
    }
    
    try {
      String row = getTimestamp() + ";";
      
      // This space is used to store the class definitions loaded by class loaders.
      String metaspace    = "0;0";
      // When we create an object, the memory will be allocated from the Eden Space.
      String heapEden     = "0;0";
      // This contains the objects that have survived from the Young garbage collection or Minor garbage collection.
      String heapSurvivor = "0;0";
      // The objects which reach to max tenured threshold during the minor GC or young GC, will be moved to "Tenured Space" or "Old Generation Space".
      String heapTenured  = "0;0";
      // The frequently accessed code blocks will be compiled to native code by the JIT and stored it in code cache.
      String codeCache    = "0;0";
      
      List<MemoryPoolMXBean> listOfMemoryPoolMXBean = ManagementFactory.getMemoryPoolMXBeans();
      for(MemoryPoolMXBean mbean : listOfMemoryPoolMXBean) {
        String name = mbean.getName().toLowerCase();
        if(name.indexOf("metaspace") >= 0) {
          metaspace    = mbean.getUsage().getUsed() + ";" + mbean.getUsage().getMax();
        }
        else if(name.indexOf("eden") >= 0) {
          heapEden     = mbean.getUsage().getUsed() + ";" + mbean.getUsage().getMax();
        }
        else if(name.indexOf("surv") >= 0) {
          heapSurvivor = mbean.getUsage().getUsed() + ";" + mbean.getUsage().getMax();
        }
        else if(name.indexOf("old") >= 0 || name.indexOf("tenured") >= 0) {
          heapTenured  = mbean.getUsage().getUsed() + ";" + mbean.getUsage().getMax();
        }
        else if(name.indexOf("code") >= 0) {
          codeCache    = mbean.getUsage().getUsed() + ";" + mbean.getUsage().getMax();
        }
      }
      row += metaspace    + ";";
      row += heapEden     + ";";
      row += heapSurvivor + ";";
      row += heapTenured  + ";";
      row += codeCache    + ";";
      
      // Class loading
      ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
      row += classLoadingMXBean.getLoadedClassCount()      + ";";
      row += classLoadingMXBean.getTotalLoadedClassCount() + ";";
      row += classLoadingMXBean.getUnloadedClassCount()    + ";";
      
      // Threads
      ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
      row += threadMXBean.getThreadCount()             + ";";
      row += threadMXBean.getPeakThreadCount()         + ";";
      row += threadMXBean.getTotalStartedThreadCount() + ";\n";
      
      // Write data (the file JVM_FILE_PATH must exist)
      
      Files.write(Paths.get(JVM_FILE_PATH), row.getBytes(), StandardOpenOption.APPEND);
      
      // Execute job to write system data (CPU usage, Memory usage, Disk usage)
      
      executeJob();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    finally {
      running   = false;
      firstTime = false;
    }
    
  }
  
  public static
  int executeJob()
    throws Exception
  {
    if(File.pathSeparatorChar != ':') {
      
      return executeWindowsJob();
      
    }
    
    return executeLinuxJob();
  }
  
  public static
  int executeLinuxJob()
    throws Exception
  {
    File file = new File(LINUX_BATCH);
    if(!file.exists()) {
      System.err.println(LINUX_BATCH + " don't exists.");
      return -1;
    }
    
    ProcessBuilder builder = new ProcessBuilder();
    builder.command(file.getAbsolutePath());
    builder.directory(new File(DATA_FOLDER));
    
    String row = null;
    Process process = builder.start();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if(line.length() < 10) continue;
        row = line;
        break;
      }
    }
    
    int result = process.waitFor();
    
    if(row == null || row.length() == 0) return result;
    row += "\n";
    try {
      // Write data (the file SYS_FILE_PATH must exist)
      Files.write(Paths.get(SYS_FILE_PATH), row.getBytes(), StandardOpenOption.APPEND);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }
  
  public static
  int executeWindowsJob()
    throws Exception
  {
    File file = new File(WIN_BATCH);
    if(!file.exists()) {
      System.err.println(WIN_BATCH + " don't exists.");
      return -1;
    }
    
    ProcessBuilder builder = new ProcessBuilder();
    builder.command(file.getAbsolutePath());
    builder.directory(new File(DATA_FOLDER));
    
    Process process = builder.start();
    
    int iRow = 0;
    int  iCpuPercentage = 0;
    long lFreeMemory    = 0;
    long lTotalMemory   = 0;
    int  iPercMemory    = 0;
    long lFreeSpace     = 0;
    long lTotalSpace    = 0;
    int  iPercSpace     = 0;
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if(line.length() == 0) continue;
        char c0 = line.charAt(0);
        if(!Character.isDigit(c0)) continue;
        iRow++;
        switch (iRow) {
        case 1:
          try { iCpuPercentage = Integer.parseInt(line.trim()); } catch(Exception ex) {}
          break;
        case 2:
          try { lFreeMemory    = Long.parseLong(line.trim()); } catch(Exception ex) {}
          break;
        case 3:
          try { lTotalMemory   = Long.parseLong(line.trim()); } catch(Exception ex) {}
          break;
        case 4:
          try { lFreeSpace     = Long.parseLong(line.trim()); } catch(Exception ex) {}
          break;
        case 5:
          try { lTotalSpace    = Long.parseLong(line.trim()); } catch(Exception ex) {}
          break;
        }
      }
      if(lTotalMemory > 0) {
        iPercMemory = (int) ((lFreeMemory * 100) / lTotalMemory);
        if(iPercMemory == 0) {
          iPercMemory = (int) ((lFreeMemory * 1000 * 100) / lTotalMemory);
        }
      }
      if(lTotalSpace  > 0) iPercSpace  = (int) ((lFreeSpace  * 100) / lTotalSpace);
    }
    
    int result = process.waitFor();
    if(iCpuPercentage == 0 && iPercMemory == 0 && iPercSpace == 0) return 0;
    try {
      // Write data (the file SYS_FILE_PATH must exist)
      String row = getTimestamp() + ";" + iCpuPercentage + ";" + iPercMemory + ";" + iPercSpace + ";\n";
      Files.write(Paths.get(SYS_FILE_PATH), row.getBytes(), StandardOpenOption.APPEND);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    return result;
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
    
    try {
      File fileSys = new File(SYS_FILE_PATH);
      if(fileSys.exists()) {
        if(fileSys.lastModified() < lToday) {
          if(fileSys.delete()) {
            fileSys.createNewFile();
          }
        }
      }
      else {
        fileSys.createNewFile();
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    
    try {
      File fileJvm = new File(JVM_FILE_PATH);
      if(fileJvm.exists()) {
        if(fileJvm.lastModified() < lToday) {
          if(fileJvm.delete()) {
            fileJvm.createNewFile();
          }
        }
      }
      else {
        fileJvm.createNewFile();
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}
