package android.com.changyou.pojo;

import java.io.Serializable;

public class Citys implements Serializable {

    private String cityName;
    private String imgPath;

    public Citys() {
    }

    public Citys(String cityName, String imgPath) {
        this.cityName = cityName;
        this.imgPath = imgPath;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
