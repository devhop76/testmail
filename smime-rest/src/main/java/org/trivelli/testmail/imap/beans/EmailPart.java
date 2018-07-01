package org.trivelli.testmail.imap.beans;

import javax.activation.DataSource;

import org.trivelli.testmail.Utility;

/**
 * @author Andrea Trivelli, �mit Daival�
 *
 */
public class EmailPart {
	private int id;
	private Object content;
	private String disposition;
	private String contentType;
	private String contentId;
	private long size;
	private String sizeReadable;
	private String filename;
	private String shortname;
	private DataSource dataSource;
	private boolean hasAttachment;

	public EmailPart() {
		super();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
		this.sizeReadable = Utility.sizeToHumanReadable(size);
	}

	/**
	 * @return
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param string
	 */
	public void setFilename(String string) {
		filename = string;
		if (filename != null && filename.length() > 9) {
			shortname = filename.substring(0,9) + "...";
		} else {
			shortname = filename;
		}
	}

	public boolean isPlainText() {
		if (this.contentType != null && this.contentType.toLowerCase().indexOf("text/plain") >=0) {
			return true;
		}
		return false;
	}

	public boolean isHTMLText() {
		if (this.contentType != null && this.contentType.toLowerCase().indexOf("text/html") >=0) {
			return true;
		}
		return false;
	}

	public boolean isImage() {
		if (this.contentType != null && this.contentType.toLowerCase().indexOf("image/") >=0) {
			return true;
		}
		return false;
	}

	public boolean isAudio() {
		if (this.contentType != null && this.contentType.toLowerCase().indexOf("audio/") >=0) {
			return true;
		}
		return false;
	}

	/**
	 * @return
	 */
	public String getSizeReadable() {
		return sizeReadable;
	}

	/**
	 * @param string
	 */
	public void setSizeReadable(String string) {
		sizeReadable = string;
	}

	/**
	 * @return
	 */
	public String getShortname() {
		return shortname;
	}

	/**
	 * @param string
	 */
	public void setShortname(String string) {
		shortname = string;
	}
	/**
	 * @return
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param source
	 */
	public void setDataSource(DataSource source) {
		dataSource = source;
	}

	/**
	 * @return the hasAttachment
	 */
	public boolean hasAttachment() {
		return hasAttachment;
	}

	/**
	 * @param hasAttachment the hasAttachment to set
	 */
	public void setHasAttachment(boolean hasAttachment) {
		this.hasAttachment = hasAttachment;
	}

}
