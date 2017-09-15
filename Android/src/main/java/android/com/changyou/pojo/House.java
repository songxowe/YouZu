package android.com.changyou.pojo;

import java.io.Serializable;

public class House implements Serializable{
    private String propertyNo;
    private String userId;
    private double price;
    private String status;
    private String homeSize;
    private String photo;
    private String leaseTrem;
    private String startDate;
    private String expireDate;
    private String totalRent;
    private String deposit;
    private String tel;
    private String propertyName;
    private String address;
    private double score;
    private String userImgPath;
    private String userName;

    public House() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPropertyNo() {
        return propertyNo;
    }

    public void setPropertyNo(String propertyNo) {
        this.propertyNo = propertyNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHomeSize() {
        return homeSize;
    }

    public void setHomeSize(String homeSize) {
        this.homeSize = homeSize;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getUserImgPath() {
        return userImgPath;
    }

    public void setUserImgPath(String userImgPath) {
        this.userImgPath = userImgPath;
    }
}
