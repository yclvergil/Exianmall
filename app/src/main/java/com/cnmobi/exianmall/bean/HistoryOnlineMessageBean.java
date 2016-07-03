package com.cnmobi.exianmall.bean;

/** 缓存留言 */
public class HistoryOnlineMessageBean {

	private String messageTime;
	private String messageContent;

	public String getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public HistoryOnlineMessageBean() {
		// TODO Auto-generated constructor stub
	}

	public HistoryOnlineMessageBean(String onlineMessage1, String onlineMessage2) {
		this.messageTime = onlineMessage1;
		this.messageContent = onlineMessage2;
	}

}
