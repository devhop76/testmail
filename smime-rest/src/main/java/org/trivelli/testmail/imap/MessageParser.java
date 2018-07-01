package org.trivelli.testmail.imap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.trivelli.testmail.Utility;
import org.trivelli.testmail.imap.beans.Email;
import org.trivelli.testmail.imap.beans.EmailHeader;
import org.trivelli.testmail.imap.beans.EmailPart;
import org.trivelli.testmail.imap.beans.EmailPriority;
import org.trivelli.testmail.imap.beans.EmailREMHeader;
import org.trivelli.testmail.imap.beans.EmailSensitivity;
import org.trivelli.testmail.imap.exception.FatalException;


public class MessageParser {
    final static Logger log = LoggerFactory.getLogger(MessageParser.class);
    /**
     * 
     */
    public final static int REM_PART_SMIME_SIGNATURE = 0;
    public final static int REM_PART_XML_EXTENSIONS = 1;
    public final static int REM_PART_EVIDENCE = 2;
    public final static int REM_PART_ORIGINAL_MESSAGE = 3;
    public final static int REM_PART_TXT_INTRODUCTION = 4;
    public final static int REM_PART_HTM_INTRODUCTION = 5;
    public final static int DOWNLOADABLE = 6;
    public final static int REM_PART_ORIGINAL_MESSAGE_TXT = 7;
    public final static int UNKNOWN_UNMANAGED = 10;
    
    public MessageParser() {
        super();
    }

	@SuppressWarnings("unchecked")
	public static final Email parseMessage(Message serverMessage, EmailHeader eHeader) throws MessagingException {
		Email email = new Email();
		MimeMessage cmsg = new MimeMessage((MimeMessage)serverMessage);
		email.setOriginalMsg(cmsg);
        email.setBaseHeader(eHeader);
		//Parsing of the REM message
        HashMap<String, Object> multiParts = inspectMessage(cmsg);
        HashMap<Integer, EmailPart> parts = (HashMap<Integer, EmailPart>)multiParts.get("message_parts");
        multiParts.remove("message_parts");
    	log.info("Message Parser: we have "+parts.size()+" message partz!");
    	boolean htmlFound = false;
		boolean plainTextFound = false;
		boolean originalMessageFound = false;		
		boolean xmlEvidenceFound = false;		
		boolean pkcs7SignFound = false;	
    	if(multiParts.size() > 2){
			if (parts.containsKey(MessageParser.REM_PART_HTM_INTRODUCTION)){
				htmlFound = true;
			} 
			if (parts.containsKey(MessageParser.REM_PART_TXT_INTRODUCTION)){
				plainTextFound = true;
			}
			if (parts.containsKey(MessageParser.REM_PART_ORIGINAL_MESSAGE)){
				originalMessageFound = true;			
			}
			if (parts.containsKey(MessageParser.REM_PART_EVIDENCE)){
				xmlEvidenceFound = true;			
			} 
			if (parts.containsKey(MessageParser.REM_PART_SMIME_SIGNATURE)){
				pkcs7SignFound = true;			
			}
			if (parts.containsKey(MessageParser.UNKNOWN_UNMANAGED)){
				log.warn("Unknown part found!");
			}				
        } else {
        	log.info("Found a standard (SENT) message...");
			if (parts.containsKey(new Integer(MessageParser.REM_PART_HTM_INTRODUCTION)))
				htmlFound = true;
			if (parts.containsKey(MessageParser.REM_PART_TXT_INTRODUCTION)){
				plainTextFound = true;
			}
        }
		log.info("htmlFound : "+htmlFound);
		log.info("plainTextFound : "+plainTextFound);
		log.info("originalMessageFound : "+originalMessageFound);		
        log.info("xmlEvidenceFound : "+xmlEvidenceFound);
        log.info("pkcs7SignFound : "+pkcs7SignFound);  
		email.setParts(parts);
        List<Part> attachedParts = (List<Part>)multiParts.get("standard_message_atts");
        multiParts.remove("standard_message_atts");
        email.setAttachedParts(attachedParts);
		if (htmlFound && plainTextFound && xmlEvidenceFound && pkcs7SignFound){
            email.setREMMsg(true);
            if(email.getBaseHeader().getRemHeader().getX_REM_Msg_Type().equals(EmailREMHeader.XREM_MSG_DISPATCH)){
            	email.setMessageType(EmailREMHeader.REM_MSG_DISPATCH);
            	log.info("Message labeled as REM MSG DISPATCH");
            }
            if(email.getBaseHeader().getRemHeader().getX_REM_Msg_Type().equals(EmailREMHeader.XREM_MSG_MESSAGE)){
            	email.setMessageType(EmailREMHeader.REM_MSG_MESSAGE);
               	log.info("Message labeled as REM MSG MESSAGE");
            }
            if(email.getMessageType() == 0)
                log.error("Message type non recognized");
        } else {
        	email.setREMMsg(false);
        	email.setMessageType(EmailREMHeader.REM_MSG_STANDARD);
        }
		//store all headers
		try {
            Enumeration en = cmsg.getAllHeaders();
            String name, val = "";
            Object tmp = null;
            while (en.hasMoreElements()) {
            	tmp = en.nextElement();
            	name = (tmp == null) ? "" : tmp.toString();
            	tmp = cmsg.getHeader(name);
            	val = (tmp == null) ? "" : tmp.toString();
            	email.addHeader(name, val);
            }
        } catch (MessagingException e1) {
            log.error("Exception occured while parsing the message generic all headers", e1);
        }
		return email;
	}

	/**
	 * Split the original function into two methods
	 * This is specialized in detecting and isolating the multipart parts of the REM message
	 * Returns true if the structure is REM compliant
	 * @param p
	 * @param multiparts
	 * @return ArrayList of Multipart
	 * @throws MessagingException 
	 * @throws IOException 
	 */
	public static HashMap<String, Object> inspectMessage(Part p) {
		if (p == null) return null;			
		
		//TODO: [AT]Ricordarsi di provare con un messaggio normale ed uno con allegato
    	HashMap<String, Object> multiParts = new HashMap<String, Object>();
    	HashMap<Integer, EmailPart> parts = new HashMap<Integer, EmailPart>();
		try {
			//Il multipart/signed � l'header primario; il body � l'intera busta firmata
        	if (p.isMimeType("multipart/signed")) {
        		Multipart mpSignedBody = (Multipart) p.getContent();
        		int mpSignedBodyCount = mpSignedBody.getCount();
        		log.debug("{inspectMessage} mpSignedBody got count: "+mpSignedBodyCount);
        		multiParts.put("multipart/signed", mpSignedBody);
        		if (mpSignedBodyCount == 2){
        			BodyPart mpSignedBodyPart0 = mpSignedBody.getBodyPart(0);
        			BodyPart mpSignedBodyPart1 = mpSignedBody.getBodyPart(1);
                    if (mpSignedBodyPart0.isMimeType("multipart/mixed") && 
                    	mpSignedBodyPart1.isMimeType("application/pkcs7-signature")) {
                    	Multipart mpMixedBody = (Multipart) mpSignedBodyPart0.getContent();
                    	multiParts.put("multipart/mixed", mpMixedBody);
                    	//TODO: Don't forget to add the controls over part's specific features
                    	parts.putAll(fetchParts(mpSignedBodyPart1));
                		int mpMixedBodyCount = mpMixedBody.getCount();
                		log.debug("{inspectMessage} mpMixedBody got count: "+mpMixedBodyCount);

                		if (mpMixedBodyCount == 3 || mpMixedBodyCount == 4){
                    		BodyPart mpMixedBodyPart0 = mpMixedBody.getBodyPart(0);
                    		BodyPart mpMixedBodyPart1 = mpMixedBody.getBodyPart(1);
                    		BodyPart mpMixedBodyPart2 = mpMixedBody.getBodyPart(2);
                    		BodyPart mpMixedBodyPart3 = null;
                    		if(mpMixedBodyCount == 4) mpMixedBodyPart3 = mpMixedBody.getBodyPart(3);                    		
                    		if (mpMixedBodyPart0.isMimeType("multipart/alternative") &&
                            	mpMixedBodyPart1.isMimeType("application/xml")       &&
                            	mpMixedBodyPart2.isMimeType("application/xml")) {
                            		Multipart mpAlternativeBody = (Multipart) mpMixedBodyPart0.getContent();
                            		multiParts.put("multipart/alternative", mpAlternativeBody);
                                    //TODO: Don't forget to add the controls over part's specific features
                            		parts.putAll(fetchParts(mpMixedBodyPart1));
                            		parts.putAll(fetchParts(mpMixedBodyPart2));
                            	    if(mpMixedBodyPart3 != null && mpMixedBodyPart3.isMimeType("message/rfc822")){
                            	    	parts.putAll(fetchParts(mpMixedBodyPart3));
                    					if(mpMixedBodyPart3.getContent() instanceof MimeMessage){
                    						MimeMessage mime = ((MimeMessage)mpMixedBodyPart3.getContent());
                    						if (mime.getContent() instanceof Multipart) {
                    							MimeMultipart mimeMultipart = (MimeMultipart)mime.getContent();
                    							int multiCount = mimeMultipart.getCount();
                    							List<MimeBodyPart> att_list = new ArrayList<MimeBodyPart>();
                    							//Now I must start from 2 because the first bodyPart marked as attachment is always dummy
                    							for(int y=2; y<multiCount; y++) {
                    								att_list.add((MimeBodyPart)mimeMultipart.getBodyPart(y));
                    							}
                    		        			multiParts.put("standard_message_atts", att_list);
                    						}
                    					}	
                            	    }	
                            		int mpAlternativeBodyCount = mpAlternativeBody.getCount();
                            		log.debug("FETCHA: mpAlternativeBody got count: "+mpAlternativeBodyCount);
                            		if (mpAlternativeBodyCount == 2){
                            			BodyPart mpAlternativeBodyPart0 = mpAlternativeBody.getBodyPart(0);
                            			BodyPart mpAlternativeBodyPart1 = mpAlternativeBody.getBodyPart(1);
                                        if (mpAlternativeBodyPart0.isMimeType("text/plain") && mpAlternativeBodyPart1.isMimeType("text/html")) {
                                        	 //TODO: Don't forget to add the controls over part's specific features
                                        	parts.putAll(fetchParts(mpAlternativeBodyPart0));
                                        	parts.putAll(fetchParts(mpAlternativeBodyPart1));
                                        }                       			
                            		}
                            }     			
                		}
               		
                    }
        		}    
        	} else if(p.isMimeType("multipart/mixed")){
        		MimeMultipart mpMixed = (MimeMultipart) p.getContent();
        		int mpMixedCount = mpMixed.getCount();
        		log.debug("{inspectMessage} mpMixed got count: "+mpMixedCount);
        		multiParts.put("multipart/mixed", mpMixed);
        		if (mpMixedCount >= 1){
        			BodyPart mpMixedPart0 = mpMixed.getBodyPart(0);
        			BodyPart mpMixedPart1 = mpMixed.getBodyPart(1);
        			int counter = 0;
        			if (mpMixedPart0 != null && mpMixedPart0.isMimeType("text/html")) {
                    	parts.putAll(fetchParts(mpMixedPart0));
                    	counter++;
        			}
        			if (mpMixedPart1 != null && mpMixedPart1.isMimeType("text/plain")) {
                    	parts.putAll(fetchParts(mpMixedPart1));
                    	counter++;
        			}
        			List<MimeBodyPart> att_list = new ArrayList<MimeBodyPart>();	
        			for (int k=0;k<mpMixedCount; k++) {
        				if(k < counter) continue;
        				att_list.add((MimeBodyPart)mpMixed.getBodyPart(k));
        			}
        			multiParts.put("standard_message_atts", att_list);
        		}
        	}
            int partsSize = parts.size();
        	log.info("Parts size after inspect={"+partsSize+"}");    	

        	multiParts.put("message_parts", parts);
         } catch (Exception e) {
        	 e.printStackTrace();
             log.error("Part is mimeType multipart/* but exception occured", e);
         }
         return multiParts;
	} 

	/**
	 * A recursive algorithm travelling through a MIME message, looking
	 * at the mime types of each part and decodes it into a text content.
	 * @param p
	 * @param parts
	 * @return ArrayList of EmailParts
	 */
	private static Map<Integer, EmailPart> fetchParts(Part p) {		
		if (p == null) return null;
		HashMap<Integer, EmailPart> fetchedPart = new HashMap<Integer, EmailPart>();
		try {
        	if (p.isMimeType("application/pkcs7-signature")) {
        		try {
        			EmailPart aPart = new EmailPart();  
            		aPart.setContent(p.getContent());
                    String contentIDHeader[] = p.getHeader("Content-ID");
                    if (contentIDHeader != null) {
                    	aPart.setContentId(contentIDHeader[0]);
                    }
                    aPart.setContentType(p.getContentType());
                    //By now I don't set the Data Source, I intend all the parts as "parts" and not as attachments
                    aPart.setDisposition(p.getDisposition());
                    aPart.setFilename(p.getFileName());
                    aPart.setId(MessageParser.REM_PART_SMIME_SIGNATURE);
                    aPart.setSize(p.getSize());
                    aPart.setSizeReadable(Utility.sizeToHumanReadable(p.getSize()));
                    fetchedPart.put(MessageParser.REM_PART_SMIME_SIGNATURE, aPart);
                    log.info("Added application/pkcs7-signature part to the partz");   
                } catch (Exception e) {
                    log.error("Part is mimeType application/pkcs7-signature but exception occured", e);
                }   
		    } else if (p.isMimeType("application/xml")) {
		    	try {
//              	InputStream is = (InputStream)p.getContent();
//                  int k = p.getSize();
//					ByteArrayOutputStream baos = new ByteArrayOutputStream();
//					byte[] buffer = new byte[k];
//					int read;
//					while ((read = is.read(buffer)) != -1){
//						baos.write(buffer);
//						baos.flush();
//					}	
//                  log.info("application/xml evidence prima:"+baos.toString());			    		
                 	EmailPart aPart = new EmailPart();
            		aPart.setContent(p.getContent());
                    String contentIDHeader[] = p.getHeader("Content-ID");
                    if (contentIDHeader != null) {
                    	aPart.setContentId(contentIDHeader[0]);
                    }
                    aPart.setContentType(p.getContentType());
                    //By now I don't set the Data Source, I intend all the parts as "parts" and not as attachments
                    aPart.setDisposition(p.getDisposition());
                    aPart.setFilename(p.getFileName());
                    if(aPart.getFilename() != null){
                        if(aPart.getFilename().equals("REMExtensions.xml"))
                            aPart.setId(MessageParser.REM_PART_XML_EXTENSIONS);
                        else
                            aPart.setId(MessageParser.REM_PART_EVIDENCE);
                    } else {
                    	throw new FatalException("No filename specified");
                    }
                    aPart.setSize(p.getSize());
                    aPart.setSizeReadable(Utility.sizeToHumanReadable(p.getSize()));
                    if(aPart.getId() == MessageParser.REM_PART_EVIDENCE){
                    	fetchedPart.put(MessageParser.REM_PART_EVIDENCE, aPart);
                        log.info("Added application/xml evidence part to the partz");    
                    } else {
                    	fetchedPart.put(MessageParser.REM_PART_XML_EXTENSIONS, aPart);
                        log.info("Added application/xml extension part to the partz");                  	
                    }                  
                } catch (Exception e) {
                    log.error("Part is mimeType application/xml but exception occured", e);
                }  
		    } else if (p.isMimeType("message/rfc822")) {
//              Enumeration matchingHeaders = p.getMatchingHeaders(new String[]{"Content-Type", "Content-Disposition"});
//        		boolean found1 = false;
//        		boolean found2= false;
//                if(matchingHeaders != null){	
//	        		while(matchingHeaders.hasMoreElements()){
//	        			javax.mail.Header msgHeader = (javax.mail.Header)matchingHeaders.nextElement();
//	        			if(msgHeader.getName().equals("Content-Type") &&
//	        			   msgHeader.getValue().equals("message/rfc822; charset=UTF-8; name=AttachedMimeMessage.eml")) 
//	        				found1 = true;
//	        			if(msgHeader.getName().equals("Content-Disposition") &&
//	             		   msgHeader.getValue().equals("attachment; filename=AttachedMimeMessage.eml")) 
//	             		    found2 = true;
//	        		}
//	        	}
                try {
					EmailPart aPart = new EmailPart();  
					aPart.setContent(p.getContent());
					String contentIDHeader[] = p.getHeader("Content-ID");
					if (contentIDHeader != null) {
						aPart.setContentId(contentIDHeader[0]);
					}
					aPart.setContentType(p.getContentType());
					aPart.setDataSource(p.getDataHandler().getDataSource());
					aPart.setDisposition(p.getDisposition());
					log.debug("GOT EML?"+p.getFileName());
					aPart.setFilename(p.getFileName());
					aPart.setId(MessageParser.REM_PART_ORIGINAL_MESSAGE);
					aPart.setSize(p.getSize());
					aPart.setSizeReadable(Utility.sizeToHumanReadable(p.getSize()));
					aPart.setHasAttachment(false);
					try {
    					if(p.getContent() instanceof MimeMessage){
    						MimeMessage mime = ((MimeMessage)p.getContent());
    						if (mime.getContent() instanceof Multipart) {
    							MimeMultipart multipart = (MimeMultipart)mime.getContent();
    							int multiCount = multipart.getCount();
    							if(multiCount > 1){
    								if(!(multiCount==2 && multipart.getBodyPart(1).getFileName()==null))
    									aPart.setHasAttachment(true);
    							}
    						}
    					}	
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
	
					fetchedPart.put(MessageParser.REM_PART_ORIGINAL_MESSAGE, aPart);
					log.info("Added message/rfc822 part to the partz");
				} catch (Exception e) {
                    log.error("Part is mimeType message/rfc822 but exception occured", e);
				}
                //If the message is a MD-Dispatch I have to extract the original body and put it aside...
				try {
	                EmailPart aPart = new EmailPart();  
	                aPart.setContentType("text/x-html; charset=iso-8859-1");
	                aPart.setId(MessageParser.REM_PART_ORIGINAL_MESSAGE_TXT);
	                aPart.setSize(p.getSize());
	                aPart.setSizeReadable(Utility.sizeToHumanReadable(p.getSize()));
					MimeMessage message = ((MimeMessage)p.getContent());
					String content = null;
					//[AT] I think this is righteous just for REM mode, anyway now we support'em both 
					if (message.getContent() instanceof Multipart) {
						MimeMultipart multi = (MimeMultipart)message.getContent();
						int multiCount = multi.getCount();
						if(multiCount > 0){
							content = multi.getBodyPart(0).getContent().toString();
						}
					} else if (message.getContent() instanceof String) {
						content = message.getContent().toString();
					}
					if(content == null)
						log.warn("HURRY UP:::::NULL CONTENT");
					aPart.setContent(content);
                    fetchedPart.put(MessageParser.REM_PART_ORIGINAL_MESSAGE_TXT, aPart);
                    log.info("Added original message text/html part to the partz");
                } catch (Exception e) {
                    log.error("Part is mimeType original message text/html but exception occured", e);
                }
			} else if (p.isMimeType("text/plain")) {
				try {
	                EmailPart aPart = new EmailPart();  
	                String contentIDHeader[] = p.getHeader("Content-ID");
	                if (contentIDHeader != null) {
	                	aPart.setContentId(contentIDHeader[0]);
	                }
	                aPart.setContentType(p.getContentType());
	                //By now I don't set the Data Source, I intend all the parts as "parts" and not as attachments
	                aPart.setDisposition(p.getDisposition());           
	                aPart.setId(MessageParser.REM_PART_TXT_INTRODUCTION);
	                aPart.setSize(p.getSize());
	                aPart.setSizeReadable(Utility.sizeToHumanReadable(p.getSize()));
                    Object pContent;
					try {
						pContent = p.getContent();
					} catch (UnsupportedEncodingException e) {
						pContent = "Message has an illegal encoding. " + e.getLocalizedMessage();
					}
                    if (pContent != null) {      
                    	aPart.setContent(pContent.toString());
                    } else {
                    	aPart.setContent("Illegal content");
                    }
                    fetchedPart.put(MessageParser.REM_PART_TXT_INTRODUCTION, aPart);
                    log.info("Added text/plain part to the partz");
                } catch (Exception e) {
                    log.error("Part is mimeType text/plain but exception occured", e);
                }
			} else if (p.isMimeType("text/html")) {
				try {
	                EmailPart aPart = new EmailPart();  
	                String contentIDHeader[] = p.getHeader("Content-ID");
	                if (contentIDHeader != null) {
	                	aPart.setContentId(contentIDHeader[0]);
	                }
	                aPart.setContentType(p.getContentType());
	                //By now I don't set the Data Source, I intend all the parts as "parts" and not as attachments
	                aPart.setDisposition(p.getDisposition());
	                aPart.setId(MessageParser.REM_PART_HTM_INTRODUCTION);
	                aPart.setSize(p.getSize());
	                aPart.setSizeReadable(Utility.sizeToHumanReadable(p.getSize()));
                    Object pContent;
					try {
						pContent = p.getContent();
					} catch (UnsupportedEncodingException e) {
						pContent = "Message has an illegal encoding. " + e.getLocalizedMessage();
					}
                    if (pContent != null) {      
                    	aPart.setContent(pContent.toString());
                    } else {
                    	aPart.setContent("Illegal content");
                    }
                    fetchedPart.put(MessageParser.REM_PART_HTM_INTRODUCTION, aPart);
                    log.info("Added text/html part to the partz");
                } catch (Exception e) {
                    log.error("Part is mimeType text/html but exception occured", e);
                }                
		    } else {
		    	try {
                    EmailPart myPart = new EmailPart();
                    myPart.setSize(p.getSize());
                    myPart.setContentType(p.getContentType());
                    myPart.setFilename(p.getFileName());
                    myPart.setDisposition(p.getDisposition());
                    String headContentID[] = p.getHeader("Content-ID");
                    if (headContentID != null) {
                    	myPart.setContentId(headContentID[0]);
                    }
                    InputStream is = p.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int c;
                    while ((c = is.read()) != -1) {
                    	baos.write(c);
                    }
                    myPart.setContent(baos);
                    myPart.setId(MessageParser.UNKNOWN_UNMANAGED);
                    fetchedPart.put(MessageParser.UNKNOWN_UNMANAGED, myPart);
                    log.info("Added non-recognizable MIME part to the partz");  
                    is.close();
                    baos.close();
                } catch (Exception e) {
                    log.error("An exception occured while parsing this part.", e);
                }
            }
        } catch (Exception e) {
            log.error("An exception occured while parsing the parts of the message.", e);
        }
		if(fetchedPart.isEmpty()) return null;
	    return fetchedPart;
	}

	/**
	 * @param msg
	 * @param header
	 */
	public static void setHeaders(Message msg, EmailHeader header) throws javax.mail.MessagingException {
        Enumeration msgHeaders = msg.getAllHeaders();
		javax.mail.Header msgHeader;
		String key;
		String value;
		log.debug("Header Start #################");
		while(msgHeaders.hasMoreElements()){
			msgHeader = (javax.mail.Header)msgHeaders.nextElement();
			key = msgHeader.getName().toLowerCase();
			value = msgHeader.getValue().trim();
			log.debug("Header found ["+key+"] und value ["+value+"]");
			/*			if(key.equals("disposition-notification-to")){
				value = msgHeader.getValue().trim();
				if(value!=null && value.length() > 0){
					header.setRequestReceiptNotification(true);
					header.setReceiptNotificationEmail(value);
				}
			}else */
			if(key.equals("x-has-attach")){
				try{
					header.setHasAttach(Boolean.parseBoolean(value));
				}catch(Exception e){}
			}	
			else if(key.equals("importance")){
				try{
					header.setPriority(EmailPriority.valueOf(value));
				}catch(Exception e){
					log.warn("Priority Error :"+e.getMessage());
				}
			}
			else if(key.equals("sensitivity")){
				try{
					header.setSensitivity(EmailSensitivity.valueOf(value));
				}catch(Exception e){
					log.warn("Sensitivity Error :"+e.getMessage());
				}
			}
		}
		log.debug("################# Header end");
	}

	@SuppressWarnings("unchecked")
	public static EmailREMHeader setREMHeaders(Message msg, int msgId) {
    	log.info("setREMHeaders wt. messageId=["+msgId+"]");
		EmailREMHeader header = new EmailREMHeader(msgId);
    	Enumeration<Header> msgHeaders = null;
		try {
			msgHeaders = (Enumeration<Header>) msg.getAllHeaders();
		} catch(MessagingException me){
			me.printStackTrace();
		}
		if(msgHeaders != null){
			Header msgHeader;
			String key;
			String value;
			while(msgHeaders.hasMoreElements()){
				msgHeader = msgHeaders.nextElement();
				key = msgHeader.getName().toLowerCase();
		    	value = msgHeader.getValue().trim();
				if(key.equals("x-priority")){
					header.setX_Priority(Integer.parseInt(value));
				} else if(key.equals("x-msmail-priority")){
					if(header.getX_Priority() == 0){
						header.setX_Priority(Integer.parseInt(value));
					}
				} else if(key.equals("x-read-confirmation")){
					header.setX_Read_Confirmation(value);				
			    } else if(key.equals("x-rem-digestmethod")){
			    	value = msgHeader.getValue().trim();
					header.setX_REM_DigestMethod(value);
			    } else if(key.equals("x-rem-digestvalue")){
			    	value = msgHeader.getValue().trim();
					header.setX_REM_DigestValue(value);			
				} else if(key.equals("x-rem-event")){
			    	value = msgHeader.getValue().trim();
			    	//[AT]TODO:creare l'enumeration per i valori di X_REM_Event
					if(value.equals(EmailREMHeader.XREM_EVENT_ACCEPTANCE)){
						header.setX_REM_Event(EmailREMHeader.XREM_EVENT_ACCEPTANCE);
					} else if(value.equals(EmailREMHeader.XREM_EVENT_NON_ACCEPTANCE)){
						header.setX_REM_Event(EmailREMHeader.XREM_EVENT_NON_ACCEPTANCE);
					} else if(value.equals(EmailREMHeader.XREM_EVENT_DELIVERY)){
						header.setX_REM_Event(EmailREMHeader.XREM_EVENT_DELIVERY);
					} else if(value.equals(EmailREMHeader.XREM_EVENT_NON_DELIVERY)){
						header.setX_REM_Event(EmailREMHeader.XREM_EVENT_NON_DELIVERY);
					} else{
						//throw new MessagingException("X-REM Event unknown");
					}		    	
					header.setX_REM_Event(value);
			    } else if(key.equals("x-rem-evidence-identifier")){
			    	value = msgHeader.getValue().trim();
					header.setX_REM_Evidence_Identifier(value);	    			
			    } else if(key.equals("x-rem-evidence-type")){
			    	value = msgHeader.getValue().trim();
					if(value.equals(EmailREMHeader.XREM_EVD_MESSAGE_DELIVERY)){
						header.setX_REM_Evidence_Type(EmailREMHeader.XREM_EVD_MESSAGE_DELIVERY);
					} else if(value.equals(EmailREMHeader.XREM_EVD_MESSAGE_ACCEPTANCE)){
						header.setX_REM_Evidence_Type(EmailREMHeader.XREM_EVD_MESSAGE_ACCEPTANCE);
					} else if(value.equals(EmailREMHeader.XREM_EVD_MESSAGE_READ)){
						header.setX_REM_Evidence_Type(EmailREMHeader.XREM_EVD_MESSAGE_READ);					
					} else{
						//throw new MessagingException("X-REM Evidence Type unknown");
					}	
			    } else if(key.equals("x-rem-extension-code")){
			    	value = msgHeader.getValue().trim();
	                header.setX_REM_Extension_Code(value);
			    } else if(key.equals("x-rem-message-identifier")){
			    	value = msgHeader.getValue().trim();
	                header.setX_REM_Message_Identifier(value);               
				} else if(key.equals("x-rem-msg-type")){
					//[AT]TODO:creare l'enumeration per i valori di X_REM_Message_Type
					value = msgHeader.getValue().trim();
					if(value.equals(EmailREMHeader.XREM_MSG_DISPATCH)){
						header.setX_REM_Msg_Type(EmailREMHeader.XREM_MSG_DISPATCH);
					} else if(value.equals(EmailREMHeader.XREM_MSG_MESSAGE)){
						header.setX_REM_Msg_Type(EmailREMHeader.XREM_MSG_MESSAGE);
					} else{
						//throw new MessagingException("X_REM Message Type unknown");
					}
			    } else if(key.equals("x-rem-section-type")){
			    	value = msgHeader.getValue().trim();
	                header.setX_REM_Section_Type(value);			
			    } else if(key.equals("x-sent-confirmation")){
			    	value = msgHeader.getValue().trim();
					header.setX_Sent_Confirmation(value);
				} else if(key.equals("x-virus-scanned")){
					value = msgHeader.getValue().trim();
					header.setX_Virus_Scanned(value);				
				} else if(key.equals("x-virus-status")){
					value = msgHeader.getValue().trim();	
					header.setX_Virus_Status(value);
				} else if(key.equals("x-tiporicevuta")){
			    	value = msgHeader.getValue().trim();	
			    	header.setX_Tipo_Ricevuta(value);				
			    } else if(key.equals("x-riferimento-message-id")){
					value = msgHeader.getValue().trim();
					if(value!=null && value.length() > 0)
						header.setX_Riferimento_Message_ID(value);
					//else throw new MessagingException("Message ID reference not found");
				} else if(key.equals("x-trasporto")){
					value = msgHeader.getValue().trim();	
					header.setX_Trasporto(value);				
				} else if(key.equals("x-has-attach")){
					value = msgHeader.getValue().trim();	
					header.setX_Has_Attach(value);			
				}
			}
		}
		dump(header);
		if(header.getX_REM_Msg_Type() == null)
			return null;
		else return header;
	}

	private static void dump(EmailREMHeader header) {
        String arg0 = "EmailREMHeader=";
        Field[] fields = header.getClass().getDeclaredFields();
        for (Field sallyField : fields){
		    for (Method method : header.getClass().getMethods())
		    {
		        if ((method.getName().startsWith("get")) && (method.getName().length() == (sallyField.getName().length() + 3)))
		        {
		            if (method.getName().toLowerCase().endsWith(sallyField.getName().toLowerCase()))
		            {
		            	try
		                {
		            		arg0 += "field is "+sallyField.getName()+", value is "+method.invoke(header)+ ";";
		                }
		                catch (Throwable tee)
		                {
		                    tee.printStackTrace();
		                }
		            }
		        }
		    }
        }
        log.debug(arg0);
	}
	
}
