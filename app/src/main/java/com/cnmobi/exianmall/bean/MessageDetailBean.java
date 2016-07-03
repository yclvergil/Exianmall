package com.cnmobi.exianmall.bean;

import java.util.List;

/**
 * 消息详情
 */
public class MessageDetailBean {

	/** 消息标题 **/
	private String msgtitle;
	/** 消息类型 **/
	private String msgtype;
	/** 消息发布时间 **/
	private String releasetime;
	/** 消息内容 **/
	private List<MessageContentBean> sysmsgcontent;

	public String getMsgtitle() {
		return msgtitle;
	}

	public void setMsgtitle(String msgtitle) {
		this.msgtitle = msgtitle;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getReleasetime() {
		return releasetime;
	}

	public void setReleasetime(String releasetime) {
		this.releasetime = releasetime;
	}

	public List<MessageContentBean> getSysmsgcontent() {
		return sysmsgcontent;
	}

	public void setSysmsgcontent(List<MessageContentBean> sysmsgcontent) {
		this.sysmsgcontent = sysmsgcontent;
	}
}
