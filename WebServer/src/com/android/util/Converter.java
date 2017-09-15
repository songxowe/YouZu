package com.android.util;

public class Converter {
	  public static Integer parseInt(String s) {
	    Integer i = 0;
	    if (s.trim().length() > 0) {
	      try {
	        i = Integer.parseInt(s);
	      } catch (NumberFormatException e) {
	        i = 0;
	      }
	    }
	    return i;
	  }
	}