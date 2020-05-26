package org.dew.test;

import org.dew.wprof.util.TimerTaskRecorder;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestWMon extends TestCase {
  
  public TestWMon(String testName) {
    super(testName);
  }
  
  public static Test suite() {
    return new TestSuite(TestWMon.class);
  }
  
  public void testApp() throws Exception {
    
    TimerTaskRecorder.init();
    
    TimerTaskRecorder.executeJob();
    
  }
}
