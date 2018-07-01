package org.trivelli.testmail.persistence.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mail", schema = "testmail")
public class Mail {

	@Id
	@Column(name="messageId")
	private Long messageId;
    @ManyToOne
    @Column(name="boxId")
    private Long boxId;
    @Column(name="date")
    private Date date;
    @Column(name="sender")
    private String sender;
    @Column(name="subject")
    private String subject;
    @Column(name="messageType")
    private MessageType messageType;
    @Column(name="h_MessageID")
    private String h_MessageID;
    @Column(name="h_XRiferimentoMessageID")
    private String h_XRiferimentoMessageID;
    @Column(name="hash")
    private String hash;
    @Column(name="fsPath")
    private String fsPath;   
    
    protected Mail() {}

    public Mail(Long messageId, Long boxId, Date date, String sender, String subject, MessageType messageType,
			String h_MessageID, String h_XRiferimentoMessageID) {
		super();
		this.messageId = messageId;
		this.boxId = boxId;
		this.date = date;
		this.sender = sender;
		this.subject = subject;
		this.messageType = messageType;
		this.h_MessageID = h_MessageID;
		this.h_XRiferimentoMessageID = h_XRiferimentoMessageID;
	}

	public Long getBoxId() {
		return boxId;
	}

	public void setBoxId(Long boxId) {
		this.boxId = boxId;
	}

	public Long getMessageId() {
		return messageId;
	}

	public Date getDate() {
		return date;
	}

	public String getSender() {
		return sender;
	}

	public String getSubject() {
		return subject;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public String getH_MessageID() {
		return h_MessageID;
	}

	public String getH_XRiferimentoMessageID() {
		return h_XRiferimentoMessageID;
	}

	public String getHash() {
		return hash;
	}

	public String getFsPath() {
		return fsPath;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public void setH_MessageID(String h_MessageID) {
		this.h_MessageID = h_MessageID;
	}

	public void setH_XRiferimentoMessageID(String h_XRiferimentoMessageID) {
		this.h_XRiferimentoMessageID = h_XRiferimentoMessageID;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public void setFsPath(String fsPath) {
		this.fsPath = fsPath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boxId == null) ? 0 : boxId.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((fsPath == null) ? 0 : fsPath.hashCode());
		result = prime * result + ((h_MessageID == null) ? 0 : h_MessageID.hashCode());
		result = prime * result + ((h_XRiferimentoMessageID == null) ? 0 : h_XRiferimentoMessageID.hashCode());
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		result = prime * result + ((messageId == null) ? 0 : messageId.hashCode());
		result = prime * result + ((messageType == null) ? 0 : messageType.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mail other = (Mail) obj;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		return true;
	}

	
}
