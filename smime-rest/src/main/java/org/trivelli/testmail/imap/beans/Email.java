package org.trivelli.testmail.imap.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.Message;
import javax.mail.Part;

import org.trivelli.testmail.Utility;

/**
 * @author Andrea Trivelli
 *
 */
public class Email implements Serializable {
	private static final long serialVersionUID = 318395800499501554L;
	private Long msgId;
	private transient HashMap<Integer, EmailPart> parts = new HashMap<Integer, EmailPart>();
	private EmailHeader baseHeader;
	private List headers = new ArrayList();
	private List<Part> attachedParts;
	private String bodyText;
    private Message originalMsg;
    private Message sentMsg;
    private boolean isPECMsg;
    private int messageType;

	public Message getOriginalMsg() {
		return originalMsg;
	}

	public void setOriginalMsg(Message originalMsg) {
		this.originalMsg = originalMsg;
	}

	/**
	 * @return the sentMsg
	 */
	public Message getSentMsg() {
		return sentMsg;
	}

	/**
	 * @param sentMsg the sentMsg to set
	 */
	public void setSentMsg(Message sentMsg) {
		this.sentMsg = sentMsg;
	}

	public HashMap<Integer, EmailPart> getParts() {
		return parts;
	}

	public void setParts(HashMap<Integer, EmailPart> parts) {
		this.parts = parts;
	}

	public boolean addHeader(String name, Object value) {
		headers.add(name + (char)6 + value);
		return true;
	}
	
	/**
	 * @return
	 */
	public List getHeaders() {
		return headers;
	}

	/**
	 * @param list
	 */
	public void setHeaders(List list) {
		headers = list;
	}

	class HeaderPair {
		String name;
		Object value; 
	}

	/**
	 * @return the attachedParts
	 */
	public List<Part> getAttachedParts() {
		return attachedParts;
	}

	/**
	 * @param attachedParts the attachedParts to set
	 */
	public void setAttachedParts(List<Part> attachedParts) {
		this.attachedParts = attachedParts;
	}

	/**
	 * @return
	 */
	public EmailHeader getBaseHeader() {
		return baseHeader;
	}

	/**
	 * @param header
	 */
	public void setBaseHeader(EmailHeader header) {
		baseHeader = header;
	}
	/**
	 * @return
	 */
	public boolean isCcExists() {
		return (getBaseHeader().getCc() != null);
	}

	/**
	 * @return
	 */
	public boolean isDateExists() {
		return (getBaseHeader().getDate() != null);
	}

	public String getTo() {
		return Utility.addressArrToString(getBaseHeader().getTo());
	}

	public String getFrom() {
		String from = Utility.addressArrToString(getBaseHeader().getFrom());
		if (from.equals("")) {
			from = "-";
		}
		return from;
	}

	public String getCc() {
		return Utility.addressArrToString(getBaseHeader().getCc());
	}

	public Date getDate() {
		return getBaseHeader().getDate();
	}

	public String getSubject() {
		String subject = Utility.doCharsetCorrections(getBaseHeader().getSubject());
		if (subject == null || subject.length() == 0) {
			subject = "No Subject";
		}
		return subject;
	}

	/**
	 * @return
	 */
	public String getBodyText() {
		return bodyText;
	}

	/**
	 * @param string
	 */
	public void setBodyText(String string) {
		bodyText = string;
	}

	/**
	 * @return
	 */
	public Long getMsgId() {
		return msgId;
	}

	/**
	 * @param emailId
	 */
	public void setMsgId(Long messageId) {
		msgId = messageId;
	}

	public boolean isPECMsg() {
		return isPECMsg;
	}

	public void setPECMsg(boolean isPECMsg) {
		this.isPECMsg = isPECMsg;
	}
    
	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

}
