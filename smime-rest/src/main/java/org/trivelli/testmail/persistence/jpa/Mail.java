package org.trivelli.testmail.persistence.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
//@Table(name = "MAIL", schema = "TESTMAIL")
@Table(name = "MAIL")
public class Mail {

	@Id
	@Column(name="MESSAGEID")
	private Long messageId;
    @ManyToOne
    @JoinColumn(name="BOXID")
    private MailAccount mailbox;
    @Column(name="DATE")
    private Date date;
    @Column(name="SENDER")
    private String sender;
    @Column(name="SUBJECT")
    private String subject;
    @Column(name="MESSAGETYPE")
    private MessageType messageType;
    @Column(name="H_MESSAGEID")
    private String h_MessageID;
    @Column(name="H_XRIFERIMENTOMESSAGEID")
    private String h_XRiferimentoMessageID;
    @Column(name="HASH")
    private String hash;
    @Column(name="FSPATH")
    private String fsPath;   
    
    protected Mail() {}

    public Mail(Long messageId, MailAccount mailbox, Date date, String sender, String subject, MessageType messageType,
			String h_MessageID, String h_XRiferimentoMessageID) {
		super();
		this.messageId = messageId;
		this.mailbox = mailbox;
		this.date = date;
		this.sender = sender;
		this.subject = subject;
		this.messageType = messageType;
		this.h_MessageID = h_MessageID;
		this.h_XRiferimentoMessageID = h_XRiferimentoMessageID;
	}

	public MailAccount getMailbox() {
		return mailbox;
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
		result = prime * result + ((mailbox == null) ? 0 : mailbox.hashCode());
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Mail [messageId=").append(messageId).append(", mailbox=").append(mailbox).append(", date=")
				.append(date).append(", sender=").append(sender).append(", subject=").append(subject)
				.append(", messageType=").append(messageType).append(", h_MessageID=").append(h_MessageID)
				.append(", h_XRiferimentoMessageID=").append(h_XRiferimentoMessageID).append(", hash=").append(hash)
				.append(", fsPath=").append(fsPath).append("]");
		return builder.toString();
	}

}
