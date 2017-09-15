package com.android.pojo;

import java.io.Serializable;

public class Apply implements Serializable{

	private static final long serialVersionUID = 1L;
	private int applyno;
	private String username;
	private String realname;
	private int houseno;
	private int hostno;
	private String tel;
	private String numberpeople;
	private String startdate;
	private String enddate;
	private String status;
	public Apply() {
		
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getApplyno() {
		return applyno;
	}
	public void setApplyno(int applyno) {
		this.applyno = applyno;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public int getHouseno() {
		return houseno;
	}
	public void setHouseno(int houseno) {
		this.houseno = houseno;
	}
	public int getHostno() {
		return hostno;
	}
	public void setHostno(int hostno) {
		this.hostno = hostno;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getNumberpeople() {
		return numberpeople;
	}
	public void setNumberpeople(String numberpeople) {
		this.numberpeople = numberpeople;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	

}
