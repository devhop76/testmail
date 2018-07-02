package org.trivelli.testmail.imap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.util.SharedByteArrayInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trivelli.testmail.Constants;
import org.trivelli.testmail.Utility;
import org.trivelli.testmail.imap.beans.AuthProfile;
import org.trivelli.testmail.imap.beans.ConnectionMetaHandler;
import org.trivelli.testmail.imap.beans.ConnectionProfile;
import org.trivelli.testmail.imap.beans.EmailHeader;
import org.trivelli.testmail.imap.beans.EmailPECHeader;
import org.trivelli.testmail.imap.exception.ConnectionException;
import org.trivelli.testmail.imap.exception.MailboxActionException;
import org.trivelli.testmail.imap.exception.ProtocolNotAvailableException;
import org.trivelli.testmail.imap.exception.ServerDownException;
import org.trivelli.testmail.imap.exception.SystemException;

import com.sun.mail.iap.ProtocolException;
import com.sun.mail.imap.IMAPFolder;

import org.trivelli.testmail.imap.MessageParser;


public class ImapProtocolImpl implements ImapProtocol {
    
	final static Logger log = LoggerFactory.getLogger(ImapProtocolImpl.class);
	
	private ConnectionMetaHandler handler;
	private ConnectionProfile profile;
	private AuthProfile auth;
    private String folder;

	private static Map<String, HashMap<String, Folder>> imapFolders = Collections.synchronizedMap(new HashMap<String, HashMap<String, Folder>>());
	
	/**
	 * 
	 * @param profile
	 * @param auth
	 * @param handler
	 */
	ImapProtocolImpl(ConnectionProfile profile, AuthProfile auth, ConnectionMetaHandler handler, String folder) {
		this.profile = profile;
		this.handler = handler;
		this.auth = auth;
		this.folder = folder;
		
		if (auth == null || auth.getUsername() == null || imapFolders.get(auth.getUsername()) == null) {
			HashMap<String, Folder> imapUserFolders = new HashMap<String, Folder>();
			imapFolders.put(auth.getUsername(), imapUserFolders);
			log.info("ImapProtocolImpl, put "+auth.getUsername()+" user folders into imapFolders!");
		}
		
		if (this.folder == null || this.folder.trim().equals("") || this.folder.toLowerCase().equals(Constants.FOLDER_INBOX(profile).toLowerCase())) {
			this.folder = Constants.FOLDER_INBOX(profile);
		} else {
			if (!this.folder.startsWith(profile.getFolderNameSpace())) {
				this.folder = profile.getFolderNameSpace() + this.folder;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see eu.inn.pes.commons.mail.protocols.Protocol#connect(int)
	 */
	public ConnectionMetaHandler connect() throws SystemException, ConnectionException, ServerDownException {
		long startTime = System.currentTimeMillis();
		Folder folder = null;
		try {
			if (handler == null || handler.getStore() == null || !handler.getStore().isConnected()) {
				Properties props = new Properties();
				if (log.isTraceEnabled()) {
					props.setProperty("mail.debug", "true");
					System.setProperty("javax.net.debug", "all");
				}

				if (profile.getFetchSSL() != null && profile.getFetchSSL().toLowerCase().equals("true")) {
					//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
					//Security.setProperty("ssl.SocketFactory.provider", "eu.inn.pes.commons.mail.protocols.DummySSLSocketFactory");
					Security.setProperty("ssl.SocketFactory.provider", "javax.net.ssl.SSLSocketFactory");
					
					props.setProperty("mail.imaps.ssl.trust", profile.getFetchServer());
					//props.put("mail.imaps.ssl.socketFactory", new DummySSLSocketFactory());
					//props.setProperty("mail.imaps.ssl.socketFactory.class", "eu.inn.pes.commons.mail.protocols.DummySSLSocketFactory");
					//props.setProperty("mail.imaps.ssl.socketFactory.port", profile.getFetchPort());		
					
					//Controls whether the IMAP partial-fetch capability should be used. Defaults to true.
					props.setProperty("mail.imaps.partialfetch", "false");	
					//Connection and pool setup
					props.setProperty("mail.imaps.connectiontimeout", "15000");
					props.setProperty("mail.imaps.connectionpoolsize", "15");
					props.setProperty("mail.imaps.connectionpooltimeout", "15000");
					// Auth protocols setup	
					props.setProperty("mail.imaps.auth.login.disable", "true");
					props.setProperty("mail.imaps.auth.plain.disable", "true");						
					props.setProperty("mail.imaps.auth.ntlm.disable", "true");
		
				    props.setProperty("mail.mime.base64.ignoreerrors", "true");
				} else {
					//Controls whether the IMAP partial-fetch capability should be used. Defaults to true.
					props.setProperty("mail.imap.partialfetch", "false");	
					
					props.setProperty("mail.imap.connectiontimeout", "20000");
					props.setProperty("mail.imap.connectionpoolsize", "10");
					props.setProperty("mail.imap.connectionpooltimeout", "20000");
					
					props.setProperty("mail.imap.auth.login.disable", "true");
					props.setProperty("mail.imap.auth.plain.disable", "true");						
					props.setProperty("mail.imap.auth.ntlm.disable", "true");			
					
					//mail.imap.socketFactory	        SocketFactory	If set to a class that implements the javax.net.SocketFactory interface, this class will be used to create IMAP sockets. Note that this is an instance of a class, not a name, and must be set using the put method, not the setProperty method.
					//mail.imap.socketFactory.class	    String	If set, specifies the name of a class that implements the javax.net.SocketFactory interface. This class will be used to create IMAP sockets.
					//mail.imap.socketFactory.fallback	boolean	If set to true, failure to create a socket using the specified socket factory class will cause the socket to be created using the java.net.Socket class. Defaults to true.
					//mail.imap.socketFactory.port	    int	Specifies the port to connect to when using the specified socket factory. If not set, the default port will be used.

					props.setProperty("mail.imap.starttls.enable", "true");
				    props.setProperty("mail.mime.base64.ignoreerrors", "true");
				}

				Session session = Session.getInstance(props);
				log.info("session instance initiated");
				handler = new ConnectionMetaHandler();
				handler.setStore(session.getStore(profile.getProtocol()));  
				handler.setSessionProps(props);
				handler.getStore().connect(profile.getFetchServer(), profile.getIFetchPort(), auth.getUsername(), auth.getPassword());
				
				// check if the store is connected or not.
				if (handler.getStore().isConnected()) {
					log.info("Store has been connected... Successful");
				} else {
					log.warn("Connection unsuccessfull...!!");
				}
				Store store = handler.getStore();
                log.info("ImapProtocolImpl; abound to examine store...");
                log.info("getDefaultFolder() {"+store.getDefaultFolder()+"}");
                Folder[] olderz = store.getPersonalNamespaces();
                for (int i=0;i<olderz.length;i++) {
                	log.info("getPersonalNamespaces() {"+olderz[i].getFullName()+"}");	
                }
                olderz = store.getSharedNamespaces();
                for (int i=0;i<olderz.length;i++) {
                	log.info("getSharedNamespaces() {"+olderz[i].getFullName()+"}");	
                }
                olderz = store.getUserNamespaces(auth.getUsername());
                for (int i=0;i<olderz.length;i++) {
                	log.info("getUserNamespaces() {"+olderz[i].getFullName()+"}");	
                }
			}
            folder = handler.getStore().getFolder(Constants.FOLDER_INBOX(profile));
			log.info("Got the default incoming folder. Folder is: " + folder.getFullName());			
			HashMap<String, Folder> imapUserFolders = imapFolders.get(auth.getUsername());
			log.info("imapUserFolders:"+imapUserFolders);
			imapUserFolders.put(Constants.FOLDER_INBOX(profile), folder);
			imapFolders.put(auth.getUsername(), imapUserFolders);
			log.info("imapFolders:"+imapFolders);
			handler.setMbox(folder);
			// OK as long as we do not erase anything from the INBOX
			handler.setTotalMessagesCount(folder.getMessageCount());
			log.info("Ending connect duties: " +(System.currentTimeMillis()-startTime)+ "ms");
		} catch (FolderNotFoundException e) {
			log.error(profile.getProtocol() + " cannot identify the INBOX folder. Please check your folder-namespace variable at config.xml.");
			throw new SystemException(e);
		} catch (NoSuchProviderException e) {
			log.error(profile.getProtocol() + " provider could not be found.");
			throw new SystemException(e);
		} catch (MessagingException e) {
			Exception ne = e.getNextException();
			if (ne != null) {
				if (ne instanceof ConnectException || ne instanceof IOException) {
					throw new ServerDownException("Server is unreachable.");
				}
			}
			log.error("Connection could not be established." + e.getMessage());
//			throw new ConnectionException(e);
		} catch (Exception e) {
			log.error("An unknown exception while connect.", e);
		}
		return handler;
	}
	
	/**
	 * 
	 * @return
	 * @throws MessagingException 
	 * @throws ServerDownException 
	 * @throws ConnectionException 
	 * @throws SystemException 
	 * @throws Exception
	 */
	public Folder getFolder() throws SystemException, ConnectionException, ServerDownException, MessagingException {
		return getImapFolder(true);
	}

	/**
	 * 
	 * @return
	 * @throws ServerDownException 
	 * @throws ConnectionException 
	 * @throws SystemException 
	 * @throws MessagingException 
	 * @throws Exception
	 */
	public Folder getImapFolder(boolean useCache) throws SystemException, ConnectionException, ServerDownException, MessagingException {
		Folder myFold = null;
		if (folder == null) {
			//[AT] Ripristinata INBOX
			folder = Constants.FOLDER_INBOX(profile);
		}

		if (folder != null && handler != null) {
			Store store = handler.getStore();
			if (store == null || !store.isConnected()) {
				log.debug("Connection is closed. Restoring it...");
				handler = connect();
				log.debug("Connection re-established");
			}
			HashMap<String, Folder> imapUserFolders = null;
			if (useCache) {
				imapUserFolders = imapFolders.get(auth.getUsername());
				myFold = imapUserFolders.get(folder);
				log.debug("ImapProtocolImpl, got "+myFold+" folder using cache from imapFolders!");
			}
			if (myFold == null) {
				myFold = handler.getStore().getFolder(folder);
			}
			if (!myFold.isOpen()) {
				try {
					log.debug("Folder :" + folder + " is closed. Opening.");
					myFold.open(Constants.CONNECTION_READ_ONLY);
					log.debug("Folder is open.");
				} catch (Throwable e) {
					log.debug("nevermind go on");
					// nevermind go on...
				}
			}
			if (useCache) {
				try {
					imapUserFolders.put(folder, myFold);
					imapFolders.put(auth.getUsername(), imapUserFolders);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return myFold;
	}

	/**
	 * 
	 * @return
	 * @throws ProtocolNotAvailableException
	 */
	public List<EmailHeader> getHeadersSortedList(String sortCriteriaRaw, String sortDirectionRaw) throws ProtocolNotAvailableException {
		Folder fold = null;
		try {
			fold = getFolder();
			log.info("Called IMAP getFolder (cache) from getHeadersSortedList");
			IMAPFolder f = (IMAPFolder)fold;
			
			String sortCriteria = ImapSortProtocolCommand.SORT_DATE;
			if (sortCriteriaRaw == null || sortCriteriaRaw.equals("date")) {
				sortCriteria = ImapSortProtocolCommand.SORT_DATE;
			} else if (sortCriteriaRaw.equals("subject")) {
				sortCriteria = ImapSortProtocolCommand.SORT_SUBJECT;
			} else if (sortCriteriaRaw.equals("to")) {
				sortCriteria = ImapSortProtocolCommand.SORT_TO;
			} else if (sortCriteriaRaw.equals("from")) {
				sortCriteria = ImapSortProtocolCommand.SORT_FROM;
			} else if (sortCriteriaRaw.equals("size")) {
				sortCriteria = ImapSortProtocolCommand.SORT_SIZE;
			}
			
			boolean ascending = false;
			if (sortDirectionRaw != null && sortDirectionRaw.equals("asc")) {
				ascending = true;
			}
			
			ImapSortProtocolCommand sortCommand = new ImapSortProtocolCommand(sortCriteria, ascending, profile);
			f.doCommand(sortCommand);

			// this profile must be set to the session at the caller method.
			profile = sortCommand.getProfile();
			
			//TODO: Spectacular Unchecked Cast...fix it
			@SuppressWarnings("unchecked")
			List<EmailHeader> res = (List<EmailHeader>)sortCommand.getSortedList();
			if (res == null) {
				profile.setSupportSort(false);
				throw new ProtocolNotAvailableException();
			}
			return res;
		} catch (MessagingException e) {
			if (e.getCause() instanceof ProtocolException) {
				throw new ProtocolNotAvailableException();
			}
			log.error("Could not fetch message headers. Is mbox connection still alive???", e);
		} catch (Exception e) {
			log.error("Could not fetch message headers. Is mbox connection still alive???", e);
		}
		return null;
	}
	
	/**
	 * Fetches and returns message headers as message objects.
	 * @return
	 * @throws SystemException
	 * @throws ConnectionException
	 */
	public List<Message> fetchAllHeadersAsMessages() throws SystemException, ConnectionException {
		long startTime = System.currentTimeMillis();
		ArrayList<Message> headersAsMsgs = null;
		Folder fold = null;
		try {
			headersAsMsgs = new ArrayList<Message>();
			fold = getFolder();
			log.info("Called IMAP getFolder (cache) from fetchAllHeadersAsMessages");
			Message[] msgs = fold.getMessages();
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add(FetchProfile.Item.FLAGS);
			fp.add(FetchProfile.Item.CONTENT_INFO);
			fp.add("Size");
			fp.add("Date");
			fold.fetch(msgs, fp);

			Message msg = null;
			for (int i = 0; i < msgs.length; i++) {
				try {
					msg = msgs[i];

					boolean deleted = false;
					Flags.Flag flags[] = msg.getFlags().getSystemFlags();
					if (flags != null) {
						Flags.Flag flag = null;
						for (int m=0; m < flags.length; m++) {
							flag = flags[m];
							if (flag.equals(Flags.Flag.DELETED)) {
								deleted = true;
							}
						}
					}
					if (!deleted) {
						headersAsMsgs.add(msg);
					}
				} catch (Exception e) {
					log.debug("probably an error fetching list", e);
				}
			}
			log.info("Ending fetchAllHeadersAsMessages duties: " +(System.currentTimeMillis()-startTime)+ "ms");			
		} catch (MessagingException e) {
			log.error("Could not fetch message headers. Is mbox connection still alive???", e);
			throw new ConnectionException(e);
		} catch (Exception e) {
			log.error("Could not fetch message headers. Is mbox connection still alive???", e);
			throw new ConnectionException(e);
		}
		return headersAsMsgs;
	}
	
	/**
	 * 
	 */
	public List<EmailHeader> fetchHeaders(int[] msgIds) throws SystemException, ConnectionException, ServerDownException {
		List<EmailHeader> headers = new ArrayList<EmailHeader>();
		Folder folder = null;
		try {
			folder = getFolder();
			log.info("Called IMAP getFolder (cache) from fetchHeaders");
			Message[] msgs = null;
			if (msgIds == null) {
				msgs = folder.getMessages();
			} else {
				msgs = folder.getMessages(msgIds);
			}

			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add(FetchProfile.Item.FLAGS);
			fp.add(FetchProfile.Item.CONTENT_INFO);
			fp.add("Size");
			fp.add("Date");
			fp.add("Disposition-Notification-To");
			fp.add("X-Priority");
			fp.add("X-MSMail-Priority");
			fp.add("Sensitivity");	
			folder.fetch(msgs, fp);

			Message msg = null;
			for (int i = 0; i < msgs.length; i++) {
				EmailHeader header = new EmailHeader();
				msg = msgs[i];
				if (!msg.getFolder().isOpen()) 
					msg.getFolder().open(Constants.CONNECTION_READ_ONLY);
				
				boolean deleted = false;
				if (profile.getProtocol().startsWith(Constants.IMAP)) {
					Flags.Flag flags[] = msg.getFlags().getSystemFlags();
					if (flags != null) {
						Flags.Flag flag = null;
						for (int m=0; m < flags.length; m++) {
							flag = flags[m];
							if (flag.equals(Flags.Flag.SEEN)) {
								header.setUnread(new Boolean(false));
							}
							if (flag.equals(Flags.Flag.DELETED)) {
								deleted = true;
							}
						}
					}
				}
				
				if (header.getUnread() == null) {
					header.setUnread(new Boolean(true));
				}

                if(deleted) continue;
	
				String subject = Utility.updateTRChars(msg.getSubject());
				if (folder.getName().toUpperCase().equals(Constants.FOLDER_INBOX(profile).toUpperCase()) && 
						!(subject.startsWith(Constants.PREFIX_INBOX[0]) || subject.startsWith(Constants.PREFIX_INBOX[1]))) {
					log.debug("Fetched wrong header ('"+subject+"') for this folder ("+folder.getName()+")");
					continue;
		        } else if (folder.getName().toUpperCase().equals(Constants.FOLDER_DELIVERY_MSG(profile).toUpperCase()) && 
		        		!(subject.startsWith(Constants.PREFIX_DELIVERY_MSG[0]) || subject.startsWith(Constants.PREFIX_DELIVERY_MSG[1]))) {
					log.debug("Fetched wrong header ('"+subject+"') for this folder ("+folder.getName()+")");
					continue;
				} else if (folder.getName().toUpperCase().equals(Constants.FOLDER_ACCEPTANCE_MSG(profile).toUpperCase()) && 
						!(subject.startsWith(Constants.PREFIX_ACCEPTANCE_MSG[0]) || subject.startsWith(Constants.PREFIX_ACCEPTANCE_MSG[1]))) {
					log.debug("Fetched wrong header ('"+subject+"') for this folder ("+folder.getName()+")");
					continue;
				}
				
				//header.setMultipart((msg.isMimeType("multipart/*")) ? true : false);					
				header.setMessageId(msg.getMessageNumber());
				
		        //[AT] Gestione degli headers specifici della PEC
		        int headerMessageId = header.getMessageId();					
		        if(headerMessageId > -1){
					EmailPECHeader remHeader = MessageParser.setPECHeaders(msg, headerMessageId);			
					header.setHasAttach(false);
					if(remHeader != null){
						header.setRemHeader(remHeader);						
						if(!folder.getName().toUpperCase().equals(Constants.FOLDER_ACCEPTANCE_MSG(profile).toUpperCase()) && 
							remHeader.getX_Has_Attach() != null && 
							remHeader.getX_Has_Attach().equalsIgnoreCase("true"))
							header.setHasAttach(true);
					}
				}						
				header.setFrom(msg.getFrom());
				header.setTo(msg.getRecipients(Message.RecipientType.TO));
				header.setCc(msg.getRecipients(Message.RecipientType.CC));
				//[] rimozione bcc
                //header.setBcc(msg.getRecipients(Message.RecipientType.BCC));
				header.setDate(msg.getSentDate());
				header.setReplyTo(msg.getReplyTo());
				header.setSize(msg.getSize());
				header.setSubject(subject);
				header.setSubjectShown(msg.getSubject()); 
				// now set the human readables.
				header.setDateShown(Utility.formatDate(header.getDate(), "dd.MM.yyyy HH:mm"));
				header.setFromShown(Utility.updateTRChars(Utility.addressArrToStringShort(header.getFrom())));
				header.setToShort(Utility.addressArrToString(header.getTo()));
				header.setToShown(Utility.addressArrToStringShort(header.getTo()));
				header.setCcShown(Utility.addressArrToStringShort(header.getCc()));
				header.setSizeShown(Utility.sizeToHumanReadable(header.getSize()));
				
				MessageParser.setHeaders(msg, header);
                
				// it is time to add it to the arraylist
				headers.add(header);	

			}
		} catch (MessagingException e) {
			log.error("Could not fetch message headers. Got a MessagingException", e);
		} catch (Exception e) {
			log.error("Could not fetch message headers. Got a general Exception", e);
		}
		return headers;
	}

	/**
	 * 
	 */
	public Message getMessage(int messageId) throws MailboxActionException, SystemException, ConnectionException, Exception {
		Message msg = null;
		IMAPFolder fold = null;
		try {
			try {
				fold = (IMAPFolder)getFolder();
				log.debug("Called IMAP getFolder (cache) from getMessage");
				msg = fold.getMessageByUID(messageId);
			} catch (MessagingException e) {
				log.error("Could not fetch message body from remote server.", e);
				throw new MailboxActionException(e);
			}
		} catch (Exception e) {
			throw e;
		}
		return msg;
	}

	/**
	 * 
	 * @param buff
	 * @throws Exception
	 */
	public void appendEmail(MimeMessage msgToSave) throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
	    // Copy the message by writing into an byte array and
	    // creating a new MimeMessage object based on the contents
	    // of the byte array:
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    msgToSave.writeTo(bos);
	    bos.close();
	    SharedByteArrayInputStream bis = new SharedByteArrayInputStream(bos.toByteArray());
	    MimeMessage msg = new MimeMessage(session, bis);
	    bis.close();

	    // The cmsg object is disconnected from the server so
	    // setFlags will have no effect (for example).  Use
	    // the original msg object for such operations.  Use
		msg.setFlag(Flags.Flag.SEEN, true);

		ProtocolFactory factory = new ProtocolFactory(profile, auth, handler);
		ImapProtocolImpl imap = (ImapProtocolImpl)factory.getImap(folder);
		Folder f = imap.getFolder();
		log.info("Called IMAP getFolder (cache) from appendEmail");	
		try {
			f.appendMessages(new Message[] {msg});
		} catch (MessagingException e) {
			log.warn("appending msg to folder : " + folder + " failed.", e);
		} finally {
			bis.close();
		}
	}
	
	/**
	 * @return
	 */
	public int getTotalMessageCount() throws Exception {
		Folder f = getFolder();
		log.info("Called IMAP getFolder (cache) from getTotalMessageCount");
		if (f.exists()) {
			return f.getMessageCount();
		}
		return 0;
	}

}
