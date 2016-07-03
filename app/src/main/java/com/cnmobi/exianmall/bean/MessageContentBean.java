package com.cnmobi.exianmall.bean;

import java.util.List;

/**
 * 消息内容
 */
public class MessageContentBean {

	/** 消息内容主键 **/
	private int idSysMessageContent;
	/** 消息内容子标题 **/
	private String msgcontent;
	/** 消息内容子标题 **/
	private String subTitle;
	private List<MessageDetailImageBean> sysmsgimages;

	public int getIdSysMessageContent() {
		return idSysMessageContent;
	}

	public void setIdSysMessageContent(int idSysMessageContent) {
		this.idSysMessageContent = idSysMessageContent;
	}

	public String getMsgcontent() {
		return msgcontent;
	}

	public void setMsgcontent(String msgcontent) {
		this.msgcontent = msgcontent;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public List<MessageDetailImageBean> getSysmsgimages() {
		return sysmsgimages;
	}

	public void setSysmsgimages(List<MessageDetailImageBean> sysmsgimages) {
		this.sysmsgimages = sysmsgimages;
	}

}
