package org.trivelli.testmail.imap;

import org.trivelli.testmail.imap.beans.AuthProfile;
import org.trivelli.testmail.imap.beans.ConnectionMetaHandler;
import org.trivelli.testmail.imap.beans.ConnectionProfile;


public class ProtocolFactory {
	private ConnectionProfile profile;
	private AuthProfile auth;
	private ConnectionMetaHandler handler;

	/**
	 * 
	 * @param profile
	 * @param auth
	 * @param handler
	 */
	public ProtocolFactory(ConnectionProfile profile, AuthProfile auth, ConnectionMetaHandler handler) {
		this.profile = profile;
		this.auth = auth;
		this.handler = handler;
	}

	/**
	 * 
	 * @param folder
	 * @return
	 */
	public ImapProtocol getImap(String folder) {
		return new ImapProtocolImpl(profile, auth, handler, folder);
	}
	
	/**
	 * 
	 * @param folder
	 * @return
	 */
	public ImapProtocol getProtocol(String folder) {
		return getImap(folder);
	}
}
