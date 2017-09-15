package com.android.pojo;

import java.io.Serializable;

public class Property implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int userid;
	private int propertyno;
	private String status;
	private String address;
	private String homesize;
	private String photo;
	private Double price;
	private String leaseTrem;
	private String startDate;
	private String expireDate;
	private String totalRent;
	private String deposit;
	private String propertyname;
	private double score;
	private String userimgpath;
	private String username;
	private String tel;
	
	public Property(){
		
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getPropertyno() {
		return propertyno;
	}

	public void setPropertyno(int propertyno) {
		this.propertyno = propertyno;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHomesize() {
		return homesize;
	}

	public void setHomesize(String homesize) {
		this.homesize = homesize;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getLeaseTrem() {
		return leaseTrem;
	}

	public void setLeaseTrem(String leaseTrem) {
		this.leaseTrem = leaseTrem;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getTotalRent() {
		return totalRent;
	}

	public void setTotalRent(String totalRent) {
		this.totalRent = totalRent;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getPropertyname() {
		return propertyname;
	}

	public void setPropertyname(String propertyname) {
		this.propertyname = propertyname;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	public String getUserimgpath() {
		return userimgpath;
	}

	public void setUserimgpath(String userimgpath) {
		this.userimgpath = userimgpath;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
}
