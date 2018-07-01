package org.trivelli.testmail.persistence.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MailAccount {

    @Id
    private Long boxId;
    private String boxDescription;
    private String emailAddress;
    private String password;
    private String server;
    private Integer port;
    
    protected MailAccount() {}

    public Long getBoxId() {
		return boxId;
	}

	public String getBoxDescription() {
		return boxDescription;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public String getServer() {
		return server;
	}

	public Integer getPort() {
		return port;
	}

	public void setBoxId(Long boxId) {
		this.boxId = boxId;
	}

	public void setBoxDescription(String boxDescription) {
		this.boxDescription = boxDescription;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	@Override
    public String toString() {
        return String.format(
                "Mail Account [BoxId=%d, boxDescription='%s', emailAddress='%s']",
                boxId, boxDescription, emailAddress);
    }

}