package org.dew.test;

import org.dew.wprof.util.TimerTaskRecorder;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestWProf extends TestCase {
  
  public TestWProf(String testName) {
    super(testName);
  }
  
  public static Test suite() {
    return new TestSuite(TestWProf.class);
  }
  
  public void testApp() throws Exception {
    
    TimerTaskRecorder.init();
    
    TimerTaskRecorder.executeJob();
    
  }
}
