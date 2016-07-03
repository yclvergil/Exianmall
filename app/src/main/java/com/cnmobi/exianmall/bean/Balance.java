package com.cnmobi.exianmall.bean;

import java.util.List;

public class Balance {
	private String balance;

	private String idAccount;

	private List<Rechargerules> rechargerules;

	private String summary;

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getBalance() {
		return this.balance;
	}

	public void setIdAccount(String idAccount) {
		this.idAccount = idAccount;
	}

	public String getIdAccount() {
		return this.idAccount;
	}

	public void setRechargerules(List<Rechargerules> rechargerules) {
		this.rechargerules = rechargerules;
	}

	public List<Rechargerules> getRechargerules() {
		return this.rechargerules;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSummary() {
		return this.summary;
	}

}