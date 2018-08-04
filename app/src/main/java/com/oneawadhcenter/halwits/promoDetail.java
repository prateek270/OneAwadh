package com.oneawadhcenter.halwits;

/**
 * Created by User on 6/8/2017.
 */
public class promoDetail {
    String name;
    String desc;
    private String image;




    public promoDetail(String name,String desc,String image) {
        this.name = name;
        this.image=image;

        this.desc = desc;
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
    //public String getButt() {
      //  return this.butt;
    //}
    //public void setButt(String butt) {
      //  this.butt =butt;
    //}


}



