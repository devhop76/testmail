package org.trivelli.testmail.persistence.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
//@Table(name = "MAIL_ACCOUNT", schema = "TESTMAIL")
@Table(name = "MAIL_ACCOUNT")
public class MailAccount {

    @Id
    @Column(name="BOXID")
    private Long boxId;
    @Column(name="DESCRIPTION")
    private String description;
    @Column(name="EMAILADDRESS")
    private String emailAddress;
    @Column(name="PASSWORD")
    private String password;
    @Column(name="SERVER")
    private String server;
    @Column(name="PORT")
    private Integer port;
    
    protected MailAccount() {}

    public Long getBoxId() {
		return boxId;
	}

	public String getDescription() {
		return description;
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

	public void setDescription(String description) {
		this.description = description;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boxId == null) ? 0 : boxId.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		result = prime * result + ((server == null) ? 0 : server.hashCode());
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
		MailAccount other = (MailAccount) obj;
		if (boxId == null) {
			if (other.boxId != null)
				return false;
		} else if (!boxId.equals(other.boxId))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		if (server == null) {
			if (other.server != null)
				return false;
		} else if (!server.equals(other.server))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MailAccount [boxId=").append(boxId).append(", description=").append(description)
				.append(", emailAddress=").append(emailAddress).append(", password=").append(password)
				.append(", server=").append(server).append(", port=").append(port).append("]");
		return builder.toString();
	}

}