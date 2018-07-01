package org.trivelli.testmail.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trivelli.testmail.MessageDetail;
import org.trivelli.testmail.imap.ImapProtocol;

@Service ("mailService")
public class MailServiceImpl implements MailService {

  @Autowired
  private ImapProtocol imap;
	
  public List<MessageDetail> fetchByDate(Long boxId, Date searchDate){
	
  }

}
