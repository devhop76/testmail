package org.trivelli.testmail.imap.beans;

import java.io.Serializable;
import java.util.Date;

import javax.mail.Address;

public class EmailREMSign implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean signValidityOk;
	private boolean signVerificationOk;
    private boolean addressMatchOk;
    private boolean validCertPath;
    private Address smimeAddress;
    private String issuer;
    private Date validFromDate;
    private Date validToDate;
    private String serialNumber;
    private String subjectDN;

	public EmailREMSign() {
		super();
	}

	public boolean isSignValidityOk() {
		return signValidityOk;
	}

	public void setSignValidityOk(boolean signValidityOk) {
		this.signValidityOk = signValidityOk;
	}

	public boolean isSignVerificationOk() {
		return signVerificationOk;
	}

	public void setSignVerificationOk(boolean signVerificationOk) {
		this.signVerificationOk = signVerificationOk;
	}

	public boolean isAddressMatchOk() {
		return addressMatchOk;
	}

	public void setAddressMatchOk(boolean addressMatchOk) {
		this.addressMatchOk = addressMatchOk;
	}

	public boolean isValidCertPath() {
		return validCertPath;
	}

	public void setValidCertPath(boolean validCertPath) {
		this.validCertPath = validCertPath;
	}

	public Address getSmimeAddress() {
		return smimeAddress;
	}

	public void setSmimeAddress(Address smimeAddress) {
		this.smimeAddress = smimeAddress;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public Date getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(Date validFromDate) {
		this.validFromDate = validFromDate;
	}

	public Date getValidToDate() {
		return validToDate;
	}

	public void setValidToDate(Date validToDate) {
		this.validToDate = validToDate;
	}

	public String getSerialNumber() {
		return serialNumber;
	}
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the subjectDN
	 */
	public String getSubjectDN() {
		return subjectDN;
	}

	/**
	 * @param subjectDN the subjectDN to set
	 */
	public void setSubjectDN(String subjectDN) {
		this.subjectDN = subjectDN;
	}
}
