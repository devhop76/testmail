package org.trivelli.testmail;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Output {
	
    private List<Long> messageIds;
    private MessageDetail msgDetail;

    public Output() {
    }

	public List<Long> getMessageIds() {
		return messageIds;
	}

	public MessageDetail getMsgDetail() {
		return msgDetail;
	}

	public void setMessageIds(List<Long> messageIds) {
		this.messageIds = messageIds;
	}

	public void setMsgDetail(MessageDetail msgDetail) {
		this.msgDetail = msgDetail;
	}

	@Override
    public String toString() {
        return "Output{" +
                "messageIds='" + messageIds + '\'' +
                ", msgDetail=" + msgDetail +
                '}';
    }
}
