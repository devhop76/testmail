package org.trivelli.testmail.imap.beans;

import java.io.Serializable;

/**
 * @author trivelli
 *
 */
public class EmailPECHeader implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -224056156966724918L;
	
	public static final String XPEC_MSG_DISPATCH = "Dispatch";
	public static final String XPEC_MSG_MESSAGE = "Message";
	public static final String XPEC_EVD_MESSAGE_DELIVERY = "DeliveryNonDeliveryToRecipient";
	public static final String XPEC_EVD_MESSAGE_ACCEPTANCE  = "SubmissionAcceptanceRejection";
	public static final String XPEC_EVD_MESSAGE_READ = "ReadConfirmation";
	public static final String XPEC_EVENT_ACCEPTANCE = "Submission acceptance";
	public static final String XPEC_EVENT_NON_ACCEPTANCE = "Submission non acceptance";
	public static final String XPEC_EVENT_DELIVERY = "Delivery";
	public static final String XPEC_EVENT_NON_DELIVERY = "Non delivery within a given retention period";
	public static final int PEC_MSG_STANDARD = 0;
	public static final int PEC_MSG_DISPATCH = 1;
	public static final int PEC_MSG_MESSAGE = 2;
	public static final int PEC_EVD_MESSAGE_DELIVERY = 20;
	public static final int PEC_EVD_MESSAGE_ACCEPTANCE  = 21;
	public static final int PEC_EVD_MESSAGE_READ = 22;
	public static final int PEC_REC_STANDARD = 0;
	public static final int PEC_REC_DISPATCH = 1;
	public static final int PEC_REC_MESSAGE = 2;	
	private int messageId;
	private String X_PEC_Msg_Type;
	private String X_PEC_Evidence_Type;
	private String X_PEC_Event;
	private String X_PEC_Section_Type;
	private String X_PEC_Extension_Code;
	private String X_PEC_Evidence_Identifier;
	private String X_PEC_Message_Identifier; 
    private String X_PEC_DigestMethod;
    private String X_PEC_DigestValue;
    private int X_Priority;
    //Virus section
    private String X_Virus_Scanned;
    private String X_Virus_Status;
    //To be checked
    private String X_Trasporto;
    private String X_Riferimento_Message_ID;
    private String X_Tipo_Ricevuta; 
    private String X_Has_Attach;
    //[AT] added headers for Read Receipt handling
    private String X_Read_Confirmation;
    private String X_Sent_Confirmation;   

	/**
	 * 
	 */
	public EmailPECHeader(int messageId) {
		this.messageId = messageId;
	}
	
	public int getMessageId() {
		return messageId;
	}
	
	public String getX_PEC_Msg_Type() {
		return X_PEC_Msg_Type;
	}

	public void setX_PEC_Msg_Type(String msg_Type) {
		this.X_PEC_Msg_Type = msg_Type;
	}
	
	public String getX_PEC_Evidence_Type() {
		return X_PEC_Evidence_Type;
	}

	public void setX_PEC_Evidence_Type(String evidence_Type) {
		this.X_PEC_Evidence_Type = evidence_Type;
	}
	
	public String getX_PEC_Event() {
		return X_PEC_Event;
	}

	public void setX_PEC_Event(String remEvent) {
		this.X_PEC_Event = remEvent;
	}

    public String getX_PEC_Section_Type() {
		return X_PEC_Section_Type;
	}

	public void setX_PEC_Section_Type(String xPECSectionType) {
		X_PEC_Section_Type = xPECSectionType;
	}

	public String getX_PEC_Extension_Code() {
		return X_PEC_Extension_Code;
	}

	public void setX_PEC_Extension_Code(String xPECExtensionCode) {
		X_PEC_Extension_Code = xPECExtensionCode;
	}

    public String getX_PEC_Evidence_Identifier() {
		return X_PEC_Evidence_Identifier;
	}

	public void setX_PEC_Evidence_Identifier(String evidence_Identifier) {
		X_PEC_Evidence_Identifier = evidence_Identifier;
	}
	
	public String getX_PEC_Message_Identifier() {
		return X_PEC_Message_Identifier;
	}

	public void setX_PEC_Message_Identifier(String xPECMessageIdentifier) {
		X_PEC_Message_Identifier = xPECMessageIdentifier;
	}
	
	public String getX_Trasporto() {
		return X_Trasporto;
	}

	public void setX_Trasporto(String trasporto) {
		X_Trasporto = trasporto;
	}

	public String getX_Virus_Scanned() {
		return X_Virus_Scanned;
	}

	public void setX_Virus_Scanned(String virus_Scanned) {
		X_Virus_Scanned = virus_Scanned;
	}

	public String getX_Virus_Status() {
		return X_Virus_Status;
	}

	public void setX_Virus_Status(String virus_Status) {
		this.X_Virus_Status = virus_Status;
	}

	public String getX_PEC_DigestMethod() {
		return X_PEC_DigestMethod;
	}

	public void setX_PEC_DigestMethod(String digestMethod) {
		X_PEC_DigestMethod = digestMethod;
	}

	public String getX_PEC_DigestValue() {
		return X_PEC_DigestValue;
	}

	public void setX_PEC_DigestValue(String digestValue) {
		X_PEC_DigestValue = digestValue;
	}
    
	public String getX_Riferimento_Message_ID() {
		return X_Riferimento_Message_ID;
	}

	public void setX_Riferimento_Message_ID(String xRiferimentoMessageID) {
		X_Riferimento_Message_ID = xRiferimentoMessageID;
	}

	/**
	 * @return the x_Has_Attach
	 */
	public String getX_Has_Attach() {
		return X_Has_Attach;
	}

	/**
	 * @param x_Has_Attach the x_Has_Attach to set
	 */
	public void setX_Has_Attach(String x_Has_Attach) {
		X_Has_Attach = x_Has_Attach;
	}

	public String getX_Read_Confirmation() {
		return X_Read_Confirmation;
	}

	public void setX_Read_Confirmation(String xReadConfirmation) {
		this.X_Read_Confirmation = xReadConfirmation;
	}
	
	public String getX_Sent_Confirmation() {
		return X_Sent_Confirmation;
	}

	public void setX_Sent_Confirmation(String xSentConfirmation) {
		this.X_Sent_Confirmation = xSentConfirmation;
	}

	/**
	 * @return the x_Tipo_Ricevuta
	 */
	public String getX_Tipo_Ricevuta() {
		return X_Tipo_Ricevuta;
	}

	/**
	 * @param x_Tipo_Ricevuta the x_Tipo_Ricevuta to set
	 */
	public void setX_Tipo_Ricevuta(String x_Tipo_Ricevuta) {
		X_Tipo_Ricevuta = x_Tipo_Ricevuta;
	}

	/**
	 * @return the x_Priority
	 */
	public int getX_Priority() {
		return X_Priority;
	}

	/**
	 * @param x_Priority the x_Priority to set
	 */
	public void setX_Priority(int x_Priority) {
		X_Priority = x_Priority;
	}

}
