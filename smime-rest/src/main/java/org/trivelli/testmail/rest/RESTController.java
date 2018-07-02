package org.trivelli.testmail.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.trivelli.testmail.MessageDetail;
import org.trivelli.testmail.Output;
import org.trivelli.testmail.rest.exception.ScanInProgressException;
import org.trivelli.testmail.service.MailService;

@RestController
public class RESTController {
	
	@Autowired
	private MailService mailService;
	  
    private ConcurrentHashMap<Long, Date> scanMap = new ConcurrentHashMap<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    @RequestMapping("/scan")
    public Boolean scan(@RequestParam(value="boxId") Long boxId, @RequestParam(value="searchDate") String searchDate) 
    	   throws ScanInProgressException {
        if(boxId == null || StringUtils.isEmpty(searchDate)) 
            return false;
	    Date dateObj = null; 
	    try {
	    	dateObj = sdf.parse(searchDate);
		} catch (ParseException e) {
			return false;
		}
	    //Check if scan for this parameters is in progress
		if(scanMap.putIfAbsent(boxId, dateObj) == null) {
			Callable<Integer> scanCall = new ScannerCallable(boxId);
			FutureTask<Integer> ft = new FutureTask<Integer>(scanCall);
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.execute(ft);
			return true;
		} else throw new ScanInProgressException("Scan for boxId n."+boxId+" is currently in progress");
    }
    
    private class ScannerCallable implements Callable<Integer> {
	    private Long box2Scan;
	 
		public ScannerCallable(Long boxId) {
			this.box2Scan = boxId;
		}
	 
		@Override
		public Integer call(){
			Date searchDate = scanMap.get(box2Scan);
			List<MessageDetail> msgs = mailService.fetchByDate(box2Scan, searchDate);
			scanMap.remove(box2Scan);
			return msgs.size();
		}
    }
  
    @RequestMapping("/search")
    public List<Long> search(@RequestParam(value="boxId") Long boxId, @RequestParam(value="messageType") String messageType,
    		@RequestParam(value="searchDate") String searchDate) {
    	Date dateObj = null;
        Output out = null; 
    	if(boxId != null) {
	    	if(! StringUtils.isAllEmpty(searchDate)) {
	        	try {
					dateObj = sdf.parse(searchDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
	        }
    	}
    	return out.getMessageIds();
    }
    
    @RequestMapping("/get")
    public MessageDetail get(@RequestParam(value="boxId") Long boxId, @RequestParam(value="msgId") Long messageId) {
    	Date dateObj = null;
        Output out = null; 
    	if(boxId != null && messageId != null) {

	    	//servizio per 
    	}
    	return out.getMsgDetail();
    }
}
