package org.trivelli.testmail;

import javax.mail.Folder;

import org.trivelli.testmail.imap.beans.ConnectionProfile;

/**
 * @author Umut Gokbayrak
 *
 */
public class Constants {

	public static final String PEC = "PEC";
	
	public static final String IMAP = "imap";
	
	public static final int CONNECTION_READ_ONLY = Folder.READ_ONLY;
	public static final String[] PREFIX_INBOX = new String[]{"POSTA CERTIFICATA:", "PRESA IN CARICO"};
	public static final String[] PREFIX_DELIVERY_MSG = new String[]{"CONSEGNA:", "MANCATA CONSEGNA"};
	public static final String[] PREFIX_ACCEPTANCE_MSG = new String[]{"ACCETTAZIONE:", "NON ACCETTAZIONE"};
	
	private static String STR_FOLDER_UNREAD = "Unread";
    private static String STR_FOLDER_INBOX = "INBOX";
	private static String STR_FOLDER_DELIVERY_MSG = "Delivery Receipts";
	private static String STR_FOLDER_ACCEPTANCE_MSG = "Acceptance Receipts";
	private static String STR_FOLDER_READ_MSG = "Read Receipts";
	private static String STR_FOLDER_DRAFT = "Draft";
    private static String STR_FOLDER_OUTBOX = "Outbox";
	private static String STR_FOLDER_SENT = "Sent";
	private static String STR_FOLDER_JUNK = "Junk";
	private static String STR_FOLDER_TRASH = "Trash";
	
	public static String FOLDER_UNREAD(ConnectionProfile profile) {
		if (profile.getProtocol().equals(IMAP)) {
			return profile.getFolderNameSpace() + STR_FOLDER_UNREAD;
		}
		return STR_FOLDER_UNREAD;
	}
	/**
	 * 
	 * @param profile
	 * @return
	 */
	public static String FOLDER_INBOX(ConnectionProfile profile) {
		return STR_FOLDER_INBOX;
	}
	
	public static String FOLDER_DELIVERY_MSG(ConnectionProfile profile) {
		if (profile.getProtocol().equals(IMAP)) {
			return profile.getFolderNameSpace() + STR_FOLDER_DELIVERY_MSG;
		}
		return STR_FOLDER_DELIVERY_MSG;
	}
	
	public static String FOLDER_ACCEPTANCE_MSG(ConnectionProfile profile) {
		if (profile.getProtocol().equals(IMAP)) {
			return profile.getFolderNameSpace() + STR_FOLDER_ACCEPTANCE_MSG;
		}
		return STR_FOLDER_ACCEPTANCE_MSG;
	}
	
	
	public static String FOLDER_READ_MSG(ConnectionProfile profile) {
		if (profile.getProtocol().equals(IMAP)) {
			return profile.getFolderNameSpace() + STR_FOLDER_READ_MSG;
		}
		return STR_FOLDER_READ_MSG;
	}

	/**
	 * 
	 * @param profile
	 * @return
	 */
	public static String FOLDER_DRAFT(ConnectionProfile profile) {
		if (profile.getProtocol().equals(IMAP)) {
			return profile.getFolderNameSpace() + STR_FOLDER_DRAFT;
		}
		return STR_FOLDER_DRAFT;
	}
	
	/**
	 * 
	 * @param profile
	 * @return
	 */
	public static String FOLDER_OUTBOX(ConnectionProfile profile) {
		if (profile.getProtocol().equals(IMAP)) {
			return profile.getFolderNameSpace() + STR_FOLDER_OUTBOX;
		}
		return STR_FOLDER_OUTBOX;
	}
	
	/**
	 * 
	 * @param profile
	 * @return
	 */
	public static String FOLDER_SENT(ConnectionProfile profile) {
		if (profile.getProtocol().equals(IMAP)) {
			return profile.getFolderNameSpace() + STR_FOLDER_SENT;
		}
		return STR_FOLDER_SENT;
	}
	
	/**
	 * 
	 * @param profile
	 * @return
	 */
	public static String FOLDER_JUNK(ConnectionProfile profile) {
		if (profile.getProtocol().equals(IMAP)) {
			return profile.getFolderNameSpace() + STR_FOLDER_JUNK;
		}
		return STR_FOLDER_JUNK;
	}

	/**
	 * 
	 * @param profile
	 * @return
	 */
	public static String FOLDER_TRASH(ConnectionProfile profile) {
		if (profile.getProtocol().equals(IMAP)) {
			return profile.getFolderNameSpace() + STR_FOLDER_TRASH;
		}
		return STR_FOLDER_TRASH;
	}
	
}
