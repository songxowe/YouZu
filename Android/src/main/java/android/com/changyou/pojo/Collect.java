package android.com.changyou.pojo;


import java.io.Serializable;

public class Collect implements Serializable {

    private static final long serialVersionUID = 1L;
    private String housephoto;
    private int houseno;
    private String userimg;
    private double price;
    private String housename;
    private String score;
    private int userid;
    private String username;

    public Collect() {
        super();
    }
    public String getHousephoto() {
        return housephoto;
    }
    public void setHousephoto(String housephoto) {
        this.housephoto = housephoto;
    }
    public int getHouseno() {
        return houseno;
    }
    public void setHouseno(int houseno) {
        this.houseno = houseno;
    }
    public String getUserimg() {
        return userimg;
    }
    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getHousename() {
        return housename;
    }
    public void setHousename(String housename) {
        this.housename = housename;
    }
    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
