package org.trivelli.testmail.imap;

import java.util.List;

import javax.mail.Message;

import org.trivelli.testmail.imap.beans.ConnectionMetaHandler;
import org.trivelli.testmail.imap.beans.EmailHeader;
import org.trivelli.testmail.imap.exception.ConnectionException;
import org.trivelli.testmail.imap.exception.MailboxActionException;
import org.trivelli.testmail.imap.exception.ProtocolNotAvailableException;
import org.trivelli.testmail.imap.exception.ServerDownException;
import org.trivelli.testmail.imap.exception.SystemException;


public interface ImapProtocol {
	public ConnectionMetaHandler connect() throws SystemException, ConnectionException, ServerDownException;
	public List<Message> fetchAllHeadersAsMessages() throws SystemException, ConnectionException;
	public List<EmailHeader> getHeadersSortedList(String sortCriteriaRaw, String sortDirectionRaw) throws ProtocolNotAvailableException;
	public int getTotalMessageCount() throws Exception;
	public Message getMessage(int messageId) throws MailboxActionException, SystemException, ConnectionException, Exception;

}
