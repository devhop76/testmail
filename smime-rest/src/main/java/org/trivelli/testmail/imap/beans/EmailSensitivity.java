package org.trivelli.testmail.imap.beans;

public class EmailSensitivity {
	public static final int NORMAL = 1;
	public static final int PERSONAL = 2;
	public static final int PRIVATE = 3;
	public static final int CONFIDENTIAL = 4;
	
	public static String toStringValue(int val){
		switch(val){
			case PERSONAL: return "Personal";
			case PRIVATE: return "Private";
			case CONFIDENTIAL: return "Company-Confidential";
			default: return "Normal";
		}
	}
	
	public static int valueOf(String val){
		String value = val.toLowerCase(); 
		if(value.equals("personal")) return PERSONAL;
		else if(value.equals("private")) return PRIVATE;
		else if(value.indexOf("confidential") > -1) return CONFIDENTIAL;
		else return NORMAL;
	}
}
