package org.trivelli.testmail.imap.beans;


public class ConnectionProfile {
	private String shortName;
	private String serverDomain;
	private String fetchServer;
	private String fetchPort;
	private String fetchAuthPlain;
	private String fetchAuthLdap;
	private String fetchAuthLdapAttribute;	
	private String serviceAddress;
	private String protocol;
	private String smtpServer;
	private String smtpPort;
	private String smtpPassword;
	private String smtpAuthenticated;
	
	private String smtpFreeRelay;
	private String folderNameSpace;
	private String fetchSSL;
	private String smtpSSL;
	private String crlCheckEnabled;
	private String extensionsOid;
	private boolean supportSort;

	/**
	 * Default constructor
	 */
    public ConnectionProfile() {
        super();
    }

    /**
     * @return Returns the fetchPort.
     */
    public int getIFetchPort() {
        return Integer.parseInt(fetchPort);
    }

    /**
     * @return Returns the fetchServer.
     */
    public String getFetchServer() {
        return fetchServer;
    }
    /**
     * @param fetchServer The fetchServer to set.
     */
    public void setFetchServer(String fetchServer) {
        this.fetchServer = fetchServer;
    }
    /**
	 * @return the serverDomain
	 */
	public String getServerDomain() {
		return serverDomain;
	}

	/**
	 * @param serverDomain the serverDomain to set
	 */
	public void setServerDomain(String serverDomain) {
		this.serverDomain = serverDomain;
	}

	/**
     * @return Returns the smtpPort.
     */
    public int getISmtpPort() {
        return Integer.parseInt(smtpPort);
    }

    /**
     * @return Returns the smtpServer.
     */
    public String getSmtpServer() {
        return smtpServer;
    }
    /**
     * @param smtpServer The smtpServer to set.
     */
    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }
    /**
     * @return Returns the protocol.
     */
    public String getProtocol() {
        return protocol;
    }
    /**
     * @param protocol The protocol to set.
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
	/**
	 * @return
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param string
	 */
	public void setShortName(String string) {
		shortName = string;
	}

	/**
	 * @return
	 */
	public String getFetchPort() {
		return fetchPort;
	}

	/**
	 * @return
	 */
	public String getSmtpPort() {
		return smtpPort;
	}

	/**
	 * @param string
	 */
	public void setFetchPort(String string) {
		fetchPort = string;
	}

	/**
	 * @param string
	 */
	public void setSmtpPort(String string) {
		smtpPort = string;
	}
	
	public String getFetchAuthPlain() {
		return fetchAuthPlain;
	}

	public void setFetchAuthPlain(String fetchAuthPlain) {
		this.fetchAuthPlain = fetchAuthPlain;
	}	
	
	
	public String getFetchAuthLdap() {
		return fetchAuthLdap;
	}

	public void setFetchAuthLdap(String fetchAuthLdap) {
		this.fetchAuthLdap = fetchAuthLdap;
	}

	public String getFetchAuthLdapAttribute() {
		return fetchAuthLdapAttribute;
	}

	public void setFetchAuthLdapAttribute(String fetchAuthLdapAttribute) {
		this.fetchAuthLdapAttribute = fetchAuthLdapAttribute;
	}
	
	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public String getServiceAddress() {
		return this.serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}
	
	/**
	 * @return
	 */
	public String getSmtpAuthenticated() {
		return smtpAuthenticated;
	}

	/**
	 * @param string
	 */
	public void setSmtpAuthenticated(String string) {
		smtpAuthenticated = string;
	}

	public String getFolderNameSpace() {
		return folderNameSpace;
	}

	public void setFolderNameSpace(String folderNameSpace) {
		this.folderNameSpace = folderNameSpace;
	}

	public String getFetchSSL() {
		return fetchSSL;
	}

	public void setFetchSSL(String fetchSSL) {
		this.fetchSSL = fetchSSL;
	}

	public String getSmtpSSL() {
		return smtpSSL;
	}

	public void setSmtpSSL(String smtpSSL) {
		this.smtpSSL = smtpSSL;
	}

	public boolean isSupportSort() {
		return supportSort;
	}

	public void setSupportSort(boolean supportSort) {
		this.supportSort = supportSort;
	}

	/**
	 * @return the smtpFreeRelay
	 */
	public int getSmtpFreeRelay() {
		return Integer.parseInt(smtpFreeRelay);
	}

	/**
	 * @param smtpFreeRelay the smtpFreeRelay to set
	 */
	public void setSmtpFreeRelay(String smtpFreeRelay) {
		this.smtpFreeRelay = smtpFreeRelay;
	}

	/**
	 * @return the cspEnabled
	 */
	public String getCrlCheckEnabled() {
		return crlCheckEnabled;
	}

	/**
	 * @param cspEnabled the cspEnabled to set
	 */
	public void setCrlCheckEnabled(String crlCheckEnabled) {
		this.crlCheckEnabled = crlCheckEnabled;
	}

	/**
	 * @return the extensionsOid
	 */
	public String getExtensionsOid() {
		return extensionsOid;
	}

	/**
	 * @param extensionsOid the extensionsOid to set
	 */
	public void setExtensionsOid(String extensionsOid) {
		this.extensionsOid = extensionsOid;
	}

}
