package org.trivelli.testmail.imap.beans;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Store;

/**
 * @author Andrea Trivelli, �mit Daival�
 *
 */
public class ConnectionMetaHandler {
	private Folder mbox;
	private Store store;
	private Properties sessionProps;
	private int totalMessagesCount;
	
	public ConnectionMetaHandler() {
	    super();
	}

    /**
     * @return Returns the mbox.
     */
    public Folder getMbox() {
        return mbox;
    }
    /**
     * @param mbox The mbox to set.
     */
    public void setMbox(Folder mbox) {
        this.mbox = mbox;
    }
    /**
     * @return Returns the store.
     */
    public Store getStore() {
        return store;
    }
    /**
     * @param store The store to set.
     */
    public void setStore(Store store) {
        this.store = store;
    }
    /**
     * @return Returns the totalMessagesCount.
     */
    public int getTotalMessagesCount() {
        return totalMessagesCount;
    }
    /**
     * @param totalMessagesCount The totalMessagesCount to set.
     */
    public void setTotalMessagesCount(int totalMessagesCount) {
        this.totalMessagesCount = totalMessagesCount;
    }

	/**
	 * @return the sessionProps
	 */
	public Properties getSessionProps() {
		return sessionProps;
	}

	/**
	 * @param sessionProps the sessionProps to set
	 */
	public void setSessionProps(Properties sessionProps) {
		this.sessionProps = sessionProps;
	}
}
