package com.oneawadhcenter.halwits;

/**
 * Created by User on 7/1/2017.
 */
public class couponDetail {
    String name;
    String desc;
    private String image;
    String url;
    String date,redeem;





    public couponDetail(String name,String desc,String image,String url,String date,String redeem) {
        this.name = name;
        this.image=image;

        this.desc = desc;
        this.url=url;
        this.date=date;
        this.redeem=redeem;
        //this.butt=butt;

    }
    public void setName(String Name)
    {
        this.name = Name;
    }

    public void setDesc(String des)
    {
        this.desc = des;
    }

    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return this.name;
    }
    public String getDesc() {
        return this.desc;
    }

    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url =url;
    }

    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date =date;
    }
    public String getRed() {
        return this.redeem;
    }
    public void setRed(String redeem) {
        this.redeem =redeem;
    }
}
