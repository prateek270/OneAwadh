package com.oneawadhcenter.halwits;

/**
 * Created by User on 6/30/2017.
 */
public class eventDetails {
    String name;
    String desc;
    private String image;
    String date,time,url;
    public eventDetails()
    {

    }



    public eventDetails(String name,String desc,String image,String date,String time,String url) {
        this.name = name;
        this.image=image;

        this.desc = desc;
        this.date=date;
        this.time=time;
        this.url=url;

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
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date =date;
    }

    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time =time;
    }

    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url =url;
    }
}
