package org.trivelli.testmail;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.validator.EmailValidator;


@SuppressWarnings("deprecation")
public class Utility {
	
	private final static char trChars[] = {'\u0131', '\u0130', '\u015F', '\u015E', '\u011F', '\u011E', 
											'\u00fd', '\u00dd', '\u00fe', '\u00de', '\u00f0', '\u00d0', 
											'\u00E7', '\u00C7', '\u00FC', '\u00DC', '\u00F6', '\u00D6',
											'\u00EC', '\u00ED'};
	private final static char enChars[] = {'i', 'I', 's', 'S', 'g', 'G', 'i', 'I', 's', 'S', 
											'g', 'G', 'c', 'C', 'u', 'U', 'o', 'O'};
	private final static char enLowerCaseChars[] = {'i', 'i', 's', 's', 'g', 'g', 'i', 'i', 's', 's', 
											'g', 'g', 'c', 'c', 'u', 'u', 'o', 'o'};
	private final static String trUnicodes[] = {"&#305;", "&#304;", "&#351;", "&#350;", "&#287;", "&#286;", 
											"&#305;", "&#304;", "&#351;", "&#350;", "&#287;", "&#286;", 
											"&#231;", "&#199;", "&#252;", "&#220;", "&#246;", "&#214;",
											"&#236;", "&#237;"};
	
	private final static char trDirtyChars[] = { '\u00fd', '\u00dd', '\u00fe', '\u00de', '\u00f0', '\u00d0' };
	private final static char trCleanChars[] = { '\u0131', '\u0130', '\u015F', '\u015E', '\u011F', '\u011E' };

	/**
	 * 
	 * @param str
	 * @param from
	 * @param to
	 * @return
	 */
	public static String replaceAllOccurances(String str, String from, String to) {
		if (str == null || str.length() == 0) {
			return str;
		} else if (str.length() == 1 && str.equals(from)) {
			return to;
		} else if (str.length() == 1 && !str.equals(from)) {
			return str;
		}
		int j = -1;
		while ((j = str.indexOf(from)) >= 0) {
			str = str.substring(0, j) + (char)5 + str.substring(j + from.length());
		}

		int i = -1;
		while ((i = str.indexOf((char)5)) >= 0) {
			str = str.substring(0, i) + to + str.substring(i + 1);
		}

		return str;
	}

	/**
	 * 
	 * @param str
	 * @param trimStr
	 * @return
	 */
	public static String extendedTrim(String str, String trimStr) {
		if (str == null || str.length() == 0)
			return str;
		for (str = str.trim(); str.startsWith(trimStr); str = str.substring(trimStr.length()).trim());
		for (; str.endsWith(trimStr); str = str.substring(0, str.length() - trimStr.length()).trim());
		return str;
	}

	/**
	 * 
	 * @param number
	 * @return
	 */
	public static Object checkDecimalFormat(Object number) {
		String str = "-";
		try {
			str = number.toString();
			int posDot = str.indexOf(".");
			int posComma = str.indexOf(",");

			if (posComma > posDot) {
				str = Utility.replaceAllOccurances(str, ".", "");
				str = Utility.replaceAllOccurances(str, ",", ".");
			} else if (posComma == -1 && posDot > 0) {
				int lastPosDot = str.lastIndexOf(".");
				if (posDot != lastPosDot) {
					str = Utility.replaceAllOccurances(str, ".", "");
				}
			}
		} catch (Exception e) {
			str = "-";
		}
		return str;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String doCharsetCorrections(String str) {
		if (str == null) return "";
		
		String extraChars[] = {"\u00FD","\u00DD","\u00FE","\u00DE","\u00F0","\u00D0"};
		String unicode[] = {"\u0131", "\u0130", "\u015F", "\u015E", "\u011F", "\u011E"};
		for (int i=0;i<extraChars.length;i++) {
			while (str.indexOf(extraChars[i]) != -1) {
				String tmp = str;
				str = tmp.substring(0, tmp.indexOf(extraChars[i])) 
					+ unicode[i] + tmp.substring (tmp.indexOf(extraChars[i])+1, tmp.length());
			}
		}
		return str;
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String htmlSpecialChars(String input) {
		StringBuffer filtered;
		try {
			filtered = new StringBuffer(input.length());
			char c;
			for (int i = 0; i < input.length(); i++) {
				c = input.charAt(i);
				if (c == '<') {
					filtered.append("&lt;");
				} else if (c == '>') {
					filtered.append("&gt;");
				} else if (c == '"') {
					filtered.append("&quot;");
				} else if (c == '&') {
					filtered.append("&amp;");
				} else {
					filtered.append(c);
				}
			}
		} catch (Exception e) {
			return input;
		}
		return (filtered.toString());
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static String convertTRCharsToHtmlSafe(String str) {
		if ((str == null) || (str.length() == 0))
			return "";

		int pos = -1;
		for (int i = 0; i < trChars.length; i++) {
			while ((pos = str.indexOf(trChars[i])) != -1) {
				str = str.substring(0, pos)
				+ trUnicodes[i] 
				+ str.substring(pos+1, str.length());
			}
		}
		return str;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static String updateTRChars(String str) {
		if ((str == null) || (str.length() == 0))
			return "";
		String ret = "";
		try{
			ret = javax.mail.internet.MimeUtility.decodeText(str);
		}
		catch(Exception e){
		}
		String strLowerCase = ret.toLowerCase(new Locale("en", "US"));
		if(strLowerCase.startsWith("=?iso-8859-9?q?")) {
			ret = ret.substring(15);
			if(strLowerCase.endsWith("?=")) {
				ret = ret.substring(0, ret.length()-2);
			}
			else
			{
				int pos = -1;
				while ((pos = ret.indexOf("?=")) != -1) {
					ret = ret.substring(0, pos) 
						+ ret.substring(pos+2, ret.length());
				}
			}
			try {
				ret = ret.replace('=', '%');
				ret = URLDecoder.decode(ret, "iso-8859-9");
			} catch(Exception ex) { }
		}
		for (int i = 0; i < trDirtyChars.length; i++) {
			int pos = -1;
			while ((pos = ret.indexOf(trDirtyChars[i])) != -1) {
				ret = ret.substring(0, pos)
					+ trCleanChars[i] 
					+ ret.substring(pos+1, ret.length());
			}
		}
		return ret;
	}
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public static String convertTRCharsToENChars(String str) {
		if ((str == null) || (str.length() == 0))
			return "";

		int pos = -1;
		for (int i = 0; i < trChars.length; i++) {
			while ((pos = str.indexOf(trChars[i])) != -1) {
				str = str.substring(0, pos)
				+ enChars[i] 
				+ str.substring(pos+1, str.length());
			}
		}
		return str;
	}
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public static String convertTRCharsToENLowerCaseChars(String str) {
		if ((str == null) || (str.length() == 0))
			return "";

		int pos = -1;
		for (int i = 0; i < trChars.length; i++) {
			while ((pos = str.indexOf(trChars[i])) != -1) {
				str = str.substring(0, pos)
				+ enLowerCaseChars[i] 
				+ str.substring(pos+1, str.length());
			}
		}
		return str;
	}

	public static int[] toIntArray(List<Integer> list){
		if(list.size() < 1) return null;
		int[] ret = new int[list.size()];
		for(int i = 0;i < ret.length;i++)
			ret[i] = list.get(i);
		return ret;
	}
	/**
	 * 
	 * @param addr
	 * @return
	 */
	public static String[] addressArrToStringArr(Address[] addr) {
		if (addr != null && addr.length > 0) {
			String[] str = new String[addr.length];
			for (int j = 0; j < addr.length; j++) {
				InternetAddress add = (InternetAddress) addr[j];
				String personal = Utility.doCharsetCorrections(add.getPersonal());
				String address = Utility.doCharsetCorrections(add.getAddress());

				if (personal != null && personal.length() > 0) {
					if (address != null && address.length() > 0) {
						str[j] = personal + " <" + address + ">";
					} else {
						str[j] = personal;
					}
				} else {
					if (address != null && address.length() > 0) {
						str[j] = address;
					} else {
						str[j] = "";
					}
				}
			}
			return str;
		}
		return null;
	}

	/**
	 * 
	 * @param addr
	 * @return
	 */
	public static String addressArrToString(Address[] addr) {
		String addrStr = "";
		String str[] = addressArrToStringArr(addr);
		if (str != null && str.length > 0) {
			addrStr = "";
			for (int i = 0; i < str.length; i++) {
				addrStr += str[i] + ", ";
			}
		}
		String msg = Utility.extendedTrim(addrStr, ",");
		return Utility.doCharsetCorrections(msg);
	}

	/**
	 * 
	 * @param addr
	 * @return
	 */
	public static String[] addressArrToStringArrShort(Address[] addr) {
		if (addr != null && addr.length > 0) {
			String[] str = new String[addr.length];
			for (int j = 0; j < addr.length; j++) {
				InternetAddress add = (InternetAddress) addr[j];
				String personal = Utility.doCharsetCorrections(add.getPersonal());
				String address = Utility.doCharsetCorrections(add.getAddress());

				if (personal != null && personal.length() > 0) {
					str[j] = personal;
				} else if (address != null && address.length() > 0) { 
					str[j] = address;
				} else {
					str[j] = "Unknown";
				}
			}
			return str;
		}
		return null;
	}
	
	/**
	 * 
	 * @param addr
	 * @return
	 */
	public static String addressArrToStringShort(Address[] addr) {
		String addrStr = "";
		String str[] = addressArrToStringArrShort(addr);
		if (str != null && str.length > 0) {
			addrStr = "";
			for (int i = 0; i < str.length; i++) {
				addrStr += str[i] + ", ";
			}
		}
		String msg = Utility.extendedTrim(addrStr, ",");
		msg =  Utility.doCharsetCorrections(msg);
		return msg;
	}

	/**
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static Address[] stringToAddressArray(String str, boolean validateEmails) throws AddressException {
		if (str == null)
			return null;
		
		str = Utility.extendedTrim(str, ",");
		StringTokenizer token = new StringTokenizer(str, ",");
		if (token.countTokens() != 0) {
			Address[] outAddr = new Address[token.countTokens()];
			int counter = 0;
			while (token.hasMoreTokens()) {
				String addr = token.nextToken().trim();
				addr = Utility.replaceAllOccurances(addr, "&lt;", "<");
				addr = Utility.replaceAllOccurances(addr, "&gt;", ">");
				String fullname = "";
				String emailAddress = "";
				int j;
				try {
					if ((j = addr.indexOf("<")) > 0) {
						fullname = Utility.extendedTrim(addr.substring(0, j).trim(), "\"");
						emailAddress = Utility.extendedTrim(Utility.extendedTrim(addr.substring(j + 1), ">"), "\"").trim();
						if(validateEmails){
							if(! EmailValidator.getInstance().isValid(emailAddress))
								throw new AddressException("Indirizzo \'"+emailAddress+"\' non valido. Prego verificare.");
						}
						String charset = "utf-8";
						outAddr[counter] = new InternetAddress(emailAddress, fullname, charset);
					} else {
						if(validateEmails){
							if(! EmailValidator.getInstance().isValid(addr))
								throw new AddressException("Indirizzo \'"+addr+"\' non valido. Prego verificare.");
						}
						outAddr[counter] = new InternetAddress(addr);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				counter++;
			}
			return outAddr;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param size
	 * @return
	 */
	public static String sizeToHumanReadable(long size) {
		String out = Math.round(size / 1024) + "K";
		if (out.equals("0K")) {
			out = size + "B";
		}
		return out;
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public static String stripHTMLTags(String message) {
		StringBuffer returnMessage = new StringBuffer(message);
		try {
			int startPosition = message.indexOf("<"); // encountered the first opening brace
			int endPosition = message.indexOf(">"); // encountered the first closing braces
			while (startPosition != -1) {
				returnMessage.delete(startPosition, endPosition + 1); // remove the tag
				returnMessage.insert(startPosition, " ");
				startPosition = (returnMessage.toString()).indexOf("<"); // look for the next opening brace
				endPosition = (returnMessage.toString()).indexOf(">"); // look for the next closing brace
			}
		} catch (Throwable e) {
			// do nothing sier
		}
		return returnMessage.toString();
	}
	
	/**
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Object date, String pattern) {
		if (date != null && date instanceof Timestamp) {
			date = new Date(((Timestamp)date).getTime());
		}
		
		if (date != null && date instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		}
		return null;
	}
	
	public static void main(String[] argz){
		Address[] ess = null;
		try {
			ess = Utility.stringToAddressArray(argz[0], true);
		} catch (AddressException e) {
			e.printStackTrace();
		}
		for(Address addr : ess){
			System.out.println(addr);
		}
	}	
}
