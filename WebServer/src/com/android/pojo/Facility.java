package com.android.pojo;

import java.io.Serializable;

public class Facility implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int propertyno;//Íâ¼ü
	private String tv;
	private String aircondition;
	private String washer;
	private String network;
	private String computer;
	private String dryer;
	private int propertyid;//Ö÷¼ü
	public Facility() {
		
	}
	public int getPropertyno() {
		return propertyno;
	}
	public void setPropertyno(int propertyno) {
		this.propertyno = propertyno;
	}
	public String getTv() {
		return tv;
	}
	public void setTv(String tv) {
		this.tv = tv;
	}
	
	public String getAircondition() {
		return aircondition;
	}
	public void setAircondition(String aircondition) {
		this.aircondition = aircondition;
	}
	public String getWasher() {
		return washer;
	}
	public void setWasher(String washer) {
		this.washer = washer;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getComputer() {
		return computer;
	}
	public void setComputer(String computer) {
		this.computer = computer;
	}
	public String getDryer() {
		return dryer;
	}
	public void setDryer(String dryer) {
		this.dryer = dryer;
	}
	public int getPropertyid() {
		return propertyid;
	}
	public void setPropertyid(int propertyid) {
		this.propertyid = propertyid;
	}
	
	
}