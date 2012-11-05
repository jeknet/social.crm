package com.enapays.model;

import java.util.Date;

public class FacebookToken {
	private String token;
	private Date creationTime;
	private int tokenExpirationTime;
	private String apiKey;
	
	public FacebookToken(){}
	
	public FacebookToken(String token, String appKey, int tokenExpirationTime){
		this.token = token;
		this.apiKey = appKey;
		this.tokenExpirationTime = tokenExpirationTime;
		this.creationTime = new Date();
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public int getTokenExpirationTime() {
		return tokenExpirationTime;
	}
	public void setTokenExpirationTime(int tokenExpirationTime) {
		this.tokenExpirationTime = tokenExpirationTime;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	} 
}
