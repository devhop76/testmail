package org.trivelli.testmail.imap.beans;

public class EmailPriority {
	public static final int HIGHEST = 1;
	public static final int HIGH = 2;
	public static final int NORMAL = 3;
	public static final int LOW = 4;
	public static final int LOWEST = 5;
	
	public static String toStringValue(int val){
		switch(val){
			case HIGHEST: case HIGH: return "High";
			case LOW: case LOWEST: return "Low";
			default: return "Normal";
		}
	}
	
	public static int valueOf(String val){
		String value = val.toLowerCase(); 
		if(value.equals("high")) return HIGH;
		else if(value.equals("low")) return LOW;
		else return NORMAL;
	}
	
}