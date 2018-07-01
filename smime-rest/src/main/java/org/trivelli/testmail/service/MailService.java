package org.trivelli.testmail.service;

import java.util.Date;
import java.util.List;

import org.trivelli.testmail.MessageDetail;

public interface MailService 
{
	public List<MessageDetail> fetchByDate(Long boxId, Date searchDate);
}
