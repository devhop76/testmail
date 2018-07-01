package org.trivelli.testmail;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDetail {

    private Date id;
    private String sender;
    private String subject;
    private String messageType;
    private String h_MessageID;   
    private String h_XRiferimentoMessageID;
    private String messageHash;
    private String b64Message;
    
    public MessageDetail() {
    }

	public Date getId() {
		return id;
	}

	public String getSender() {
		return sender;
	}

	public String getSubject() {
		return subject;
	}

	public String getMessageType() {
		return messageType;
	}

	public String getMessageHash() {
		return messageHash;
	}

	public String getB64Message() {
		return b64Message;
	}

	public void setId(Date id) {
		this.id = id;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public void setMessageHash(String messageHash) {
		this.messageHash = messageHash;
	}

	public void setB64Message(String b64Message) {
		this.b64Message = b64Message;
	}

	public String getH_MessageID() {
		return h_MessageID;
	}

	public String getH_XRiferimentoMessageID() {
		return h_XRiferimentoMessageID;
	}

	public void setH_MessageID(String h_MessageID) {
		this.h_MessageID = h_MessageID;
	}

	public void setH_XRiferimentoMessageID(String h_XRiferimentoMessageID) {
		this.h_XRiferimentoMessageID = h_XRiferimentoMessageID;
	}


}
