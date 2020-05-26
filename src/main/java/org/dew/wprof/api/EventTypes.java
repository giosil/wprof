package org.dew.wprof.api;

public 
enum EventTypes 
{
  APPLICATION("a", "Application"),
  ENTERING("e", "Entering"),
  EXITING("x", "Exiting"),
  INVOKE("i", "Invoke external service"),
  RETURN("r", "Return external service");
  
  private String code;
  private String description;
  
  private EventTypes(String code, String description)
  {
    this.code        = code;
    this.description = description;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }
  
  public String toString() {
    return code;
  }
}
