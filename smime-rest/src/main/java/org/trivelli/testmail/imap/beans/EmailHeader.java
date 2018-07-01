package org.trivelli.testmail.imap.beans;

import java.io.Serializable;
import java.util.Date;

import javax.mail.Address;

/**
 * @author Umut Gokbayrak
 */
public class EmailHeader implements Serializable {
	private static final long serialVersionUID = 846372960987843509L;
	// rimozione bcc
//	private Address[] bcc;
	private Address[] from;
	private Address[] to;
	private Address[] cc;
	private Address[] replyTo;
	private String subject;
	private Date date;
	private long size;
	private int messageId;
	private Boolean unread;
	private Boolean requestReceiptNotification;
	private String receiptNotificationEmail;
	private int priority;
	private int sensitivity;
	private String receiptFormatRequested;	
	private EmailREMHeader remHeader;
	// human readable portions. These are shown on jsps.
	private String sizeShown;
	private String subjectShown;	
	private String fromShown;
	private String toShown;
	private String toShort;
	private String ccShown;
	private String dateShown;
    private Boolean hasAttach;
	
	/**
	 * Default constructor
	 */
    public EmailHeader() {
        super();
    }
    // rimozione bcc
//    /**
//     * @return Returns the bcc.
//     */
//    public Address[] getBcc() {
//        return bcc;
//    }
//    /**
//     * @param bcc The bcc to set.
//     */
//    public void setBcc(Address[] bcc) {
//        this.bcc = bcc;
//    }
    /**
     * @return Returns the cc.
     */
    public Address[] getCc() {
        return cc;
    }
    /**
     * @param cc The cc to set.
     */
    public void setCc(Address[] cc) {
        this.cc = cc;
    }
    /**
     * @return Returns the date.
     */
    public Date getDate() {
        return date;
    }
    /**
     * @param date The date to set.
     */
    public void setDate(Date date) {
        this.date = date;
    }
    /**
     * @return Returns the from.
     */
    public Address[] getFrom() {
        return from;
    }
    /**
     * @param from The from to set.
     */
    public void setFrom(Address[] from) {
        this.from = from;
    }
    /**
     * @return Returns the messageId.
     */
    public int getMessageId() {
        return messageId;
    }
    /**
     * @param messageId The messageId to set.
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
    /**
     * @return Returns the replyTo.
     */
    public Address[] getReplyTo() {
        return replyTo;
    }
    /**
     * @param replyTo The replyTo to set.
     */
    public void setReplyTo(Address[] replyTo) {
        this.replyTo = replyTo;
    }
    /**
     * @return Returns the size.
     */
    public long getSize() {
        return size;
    }
    /**
     * @param size The size to set.
     */
    public void setSize(long size) {
        this.size = size;
    }
    /**
     * @return Returns the subject.
     */
    public String getSubject() {
        return subject;
    }
    /**
     * @param subject The subject to set.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    /**
     * @return Returns the to.
     */
    public Address[] getTo() {
        return to;
    }
    /**
     * @param to The to to set.
     */
    public void setTo(Address[] to) {
        this.to = to;
    }
	/**
	 * @return
	 */
	public String getCcShown() {
		return ccShown;
	}

	/**
	 * @return
	 */
	public String getDateShown() {
		return dateShown;
	}

	/**
	 * @return
	 */
	public String getFromShown() {
		return fromShown;
	}

	/**
	 * @return
	 */
	public String getSizeShown() {
		return sizeShown;
	}
	
	/**
	 * @return
	 */
	public String getSubjectShown() {
		return subjectShown;
	}
	
	/**
	 * @return
	 */
	public String getToShort() {
		return toShort;
	}

	/**
	 * @return
	 */
	public String getToShown() {
		return toShown;
	}

	/**
	 * @param string
	 */
	public void setCcShown(String string) {
		ccShown = string;
	}

	/**
	 * @param string
	 */
	public void setDateShown(String string) {
		dateShown = string;
	}

	/**
	 * @param string
	 */
	public void setFromShown(String string) {
		fromShown = string;
	}

	/**
	 * @param string
	 */
	public void setSizeShown(String string) {
		sizeShown = string;
	}

	/**
	 * @param string
	 */
	public void setSubjectShown(String string) {
		subjectShown = showShortened(string);	
	}	
	
	/**
	 * @param string
	 */
	public void setToShort(String string) {
		toShort = showShortened(string);	
	}
	
	/**
	 * @param string
	 */
	public void setToShown(String string) {
		toShown = string;	
	}
		
	/**
	 * @return
	 */
	public Boolean getUnread() {
		return unread;
	}

	/**
	 * @param boolean1
	 */
	public void setUnread(Boolean unread) {
		this.unread = unread;
	}

	/**
	 * @return
	 */
	public Boolean getRequestReceiptNotification() {
		return requestReceiptNotification;
	}

	/**
	 * @param boolean1
	 */
	public void setRequestReceiptNotification(Boolean requestReceiptNotification) {
		this.requestReceiptNotification = requestReceiptNotification;
	}

	/**
	 * @return
	 */
	public String getReceiptNotificationEmail() {
		return receiptNotificationEmail;
	}

	/**
	 * @param boolean1
	 */
	public void setReceiptNotificationEmail(String receiptNotificationEmail) {
		this.receiptNotificationEmail = receiptNotificationEmail;
	}
	
	/**
	 * @return
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param boolean1
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 * @return
	 */
	public int getSensitivity() {
		return sensitivity;
	}

	/**
	 * @param boolean1
	 */
	public void setSensitivity(int sensitivity) {
		this.sensitivity = sensitivity;
	}
	
	public EmailREMHeader getRemHeader() {
		return remHeader;
	}
	
	public void setRemHeader(EmailREMHeader remHeader) {
		this.remHeader = remHeader;
	}
	/**
	 * @return the receiptType
	 */
	public String getReceiptFormatRequested() {
		return receiptFormatRequested;
	}
	/**
	 * @param receiptType the receiptType to set
	 */
	public void setReceiptFormatRequested(String receiptFormatRequested) {
		this.receiptFormatRequested = receiptFormatRequested;
	}
	/**
	 * @return the hasAttach
	 */
	public Boolean getHasAttach() {
		return hasAttach;
	}
	/**
	 * @param hasAttach the hasAttach to set
	 */
	public void setHasAttach(Boolean hasAttach) {
		this.hasAttach = hasAttach;
	}
    
	private String showShortened(String in){
		String out = null;
		if (in != null && in.length() > 50) {
			out = in.substring(0,47) + "...";
		} else {
			out = in;
		}
		return out;
	}
}
