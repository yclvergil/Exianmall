package com.cnmobi.exianmall.bean;

import java.io.Serializable;

/**
 * 
 * @author gwk
 * 商品评价
 * 2016-4-15
 */
public class Pingjia implements Serializable{
     private String evaluatecontent;//商品评价内容
     private String evaluatename; //评价人
     private String evaluatetime;//评价时间
	public String getEvaluatecontent() {
		return evaluatecontent;
	}
	public void setEvaluatecontent(String evaluatecontent) {
		this.evaluatecontent = evaluatecontent;
	}
	public String getEvaluatename() {
		return evaluatename;
	}
	public void setEvaluatename(String evaluatename) {
		this.evaluatename = evaluatename;
	}
	public String getEvaluatetime() {
		return evaluatetime;
	}
	public void setEvaluatetime(String evaluatetime) {
		this.evaluatetime = evaluatetime;
	}
     
}
