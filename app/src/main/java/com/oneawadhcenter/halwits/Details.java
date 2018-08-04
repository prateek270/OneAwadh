package com.oneawadhcenter.halwits;

/**
 * Created by User on 6/8/2017.
 */
public class Details {
    String name;
    String desc;
    private String image;
    String date;
    /*public Details()
    {

    }*/



    public Details(String name,String desc,String image,String date) {
        this.name = name;
        this.image=image;

        this.desc = desc;
        this.date=date;

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


}



