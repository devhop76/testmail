package org.trivelli.testmail.imap.beans;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Umut Gokbayrak
 */
public class ConnectionProfileList {
    final static Logger log = LoggerFactory.getLogger(ConnectionProfileList.class);
	
    public static Map<String, ConnectionProfile> conList = new HashMap<String, ConnectionProfile>();

	/**
	 * 
	 */
	public ConnectionProfileList() {
		super();
	}
	
	public void addConnectionProfile(ConnectionProfile con) {
		if (con == null) return;
		conList.put(con.getShortName(), con);
	}

	public static Map<String, ConnectionProfile> getConList() {
		return conList;
	}
	
	public static ConnectionProfile getProfileByShortName(String shortName) {
		ConnectionProfile con = conList.get(shortName);
		if (con == null) {
			log.warn("The Shortname searched at the ConnectionProfileList does not correspond to a ConnectionProfile");
			return null;
		}
		return con;
	}
}
