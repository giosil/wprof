package org.dew.wprof.util;

import java.io.Writer;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public 
class WebUtil 
{
  @SuppressWarnings("unchecked")
  public static
  Map<String, Object> getMapAttribute(HttpServletRequest request, String name, boolean notNull)
  {
    Object result = request.getAttribute(name);
    
    if(result == null) {
      if(notNull) {
        return new HashMap<String, Object>();
      }
      return null;
    }
    
    if(!(result instanceof Map)) {
      if(notNull) {
        return new HashMap<String, Object>();
      }
      return null;
    }
    
    return (Map<String, Object>) result;
  }
  
  public static
  String getStringAttribute(HttpServletRequest request, String name, String defaultValue)
  {
    Object result = request.getAttribute(name);
    
    if(result == null) {
      return defaultValue;
    }
    
    if(result instanceof String) {
      return (String) result;
    }
    
    return result.toString();
  }
  
  public static
  Integer getIntegerAttribute(HttpServletRequest request, String name, Integer defaultValue)
  {
    Object result = request.getAttribute(name);
    
    if(result == null) {
      return defaultValue;
    }
    
    if(result instanceof Integer) {
      return (Integer) result;
    }
    else if(result instanceof Number) {
      return new Integer(((Number) result).intValue());
    }
    
    return defaultValue;
  }
  
  public static
  Double getDoubleAttribute(HttpServletRequest request, String name, Double defaultValue)
  {
    Object result = request.getAttribute(name);
    
    if(result == null) {
      return defaultValue;
    }
    
    if(result instanceof Double) {
      return (Double) result;
    }
    else if(result instanceof Number) {
      return new Double(((Number) result).doubleValue());
    }
    
    return defaultValue;
  }
  
  public static
  Map<String, Object> getParameters(HttpServletRequest request)
  {
    Map<String, Object> map = new HashMap<String, Object>();
    
    Enumeration<String> enumeration = request.getParameterNames();
    while(enumeration.hasMoreElements()) {
      String sParameterName  = enumeration.nextElement();
      String sParameterValue = request.getParameter(sParameterName);
      
      if(Character.isDigit(sParameterName.charAt(0)) && Character.isDigit(sParameterName.charAt(sParameterName.length()-1))) {
        if(sParameterValue == null || sParameterValue.length() == 0) {
          continue;
        }
      }
      
      map.put(sParameterName, sParameterValue);
    }
    
    return map;
  }
  
  public static
  void printTableRows(final Writer out, final Map<String, Object> mapData)
  {
    if(mapData == null || mapData.isEmpty()) {
      return;
    }
    
    mapData.entrySet().forEach(e -> { 
      try{ 
        out.write("<tr><td>" + e.getKey() + "</td><td>" + e.getValue() + "</td></tr>");
      } 
      catch(Exception ex){
      }
    });
  }
  
  public static
  void printSpan(final Writer out, final String text, final String tagId, final String tagClass, String tagStyle)
  {
    if(text == null) return;
    
    String i = tagId    != null && tagId.length()    > 0 ? " id=\""    + tagId    + "\"" : "";
    String c = tagClass != null && tagClass.length() > 0 ? " class=\"" + tagClass + "\"" : "";
    String s = tagStyle != null && tagStyle.length() > 0 ? " style=\"" + tagStyle + "\"" : "";
    
    try {
      out.write("<span" + i + c + s + ">" + html(text) + "</span>");
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public static
  void printDiv(final Writer out, final String inner, final String tagId, final String tagClass, String tagStyle)
  {
    String i = tagId    != null && tagId.length()    > 0 ? " id=\""    + tagId    + "\"" : "";
    String c = tagClass != null && tagClass.length() > 0 ? " class=\"" + tagClass + "\"" : "";
    String s = tagStyle != null && tagStyle.length() > 0 ? " style=\"" + tagStyle + "\"" : "";
    
    try {
      if(inner == null) {
        out.write("<div" + i + c + s + "></div>");
      }
      else {
        out.write("<div" + i + c + s + ">" + inner + "</div>");
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public static
  String html(String text)
  {
    if(text == null || text.length() == 0) return "";
    int length = text.length();
    StringBuffer sb = new StringBuffer(length);
    for(int i = 0; i < length; i++) {
      char c = text.charAt(i);
      switch (c) {
        case    '<':   sb.append("&lt;");     break;
        case    '>':   sb.append("&gt;");     break;
        case    '&':   sb.append("&amp;");    break;
        case '\300':   sb.append("&Agrave;"); break;
        case '\310':   sb.append("&Egrave;"); break;
        case '\314':   sb.append("&Igrave;"); break;
        case '\322':   sb.append("&Ograve;"); break;
        case '\331':   sb.append("&Ugrave;"); break;
        case '\301':   sb.append("&Aacute;"); break;
        case '\311':   sb.append("&Eacute;"); break;
        case '\315':   sb.append("&Iacute;"); break;
        case '\323':   sb.append("&Oacute;"); break;
        case '\332':   sb.append("&Uacute;"); break;
        case '\340':   sb.append("&agrave;"); break;
        case '\350':   sb.append("&egrave;"); break;
        case '\354':   sb.append("&igrave;"); break;
        case '\362':   sb.append("&ograve;"); break;
        case '\371':   sb.append("&ugrave;"); break;
        case '\341':   sb.append("&aacute;"); break;
        case '\351':   sb.append("&eacute;"); break;
        case '\355':   sb.append("&iacute;"); break;
        case '\363':   sb.append("&oacute;"); break;
        case '\372':   sb.append("&uacute;"); break;
        case '\252':   sb.append("&ordf;");   break;
        case '\260':   sb.append("&deg;");    break;
        case '\u20ac': sb.append("&euro;");   break;
        default: {
          if(c < 128) {
            sb.append(c);
          }
          else {
            int code = (int) c;
            sb.append("&#" + code + ";");
          }
        }
      }
    }
    return sb.toString();
  }
  
  public static
  String jsVar(String name, Object value)
  {
    return "var " + name + "=" + JSONUtils.stringify(value) + ";";
  }
  
  public static
  String getTimestamp() 
  {
    Calendar cal = Calendar.getInstance();
    
    int iYear  = cal.get(Calendar.YEAR);
    int iMonth = cal.get(Calendar.MONTH) + 1;
    int iDay   = cal.get(Calendar.DATE);
    int iHour  = cal.get(Calendar.HOUR_OF_DAY);
    int iMin   = cal.get(Calendar.MINUTE);
    int iSec   = cal.get(Calendar.SECOND);
    String sYear  = String.valueOf(iYear);
    String sMonth = iMonth < 10 ? "0" + iMonth : String.valueOf(iMonth);
    String sDay   = iDay   < 10 ? "0" + iDay   : String.valueOf(iDay);
    String sHour  = iHour  < 10 ? "0" + iHour  : String.valueOf(iHour);
    String sMin   = iMin   < 10 ? "0" + iMin   : String.valueOf(iMin);
    String sSec   = iSec   < 10 ? "0" + iSec   : String.valueOf(iSec);
    
    String sDate  = sYear + "-" + sMonth + "-" + sDay;
    String sTime  = sHour + ":" + sMin   + ":" + sSec;
    
    return sDate + " " + sTime;
  }
}
