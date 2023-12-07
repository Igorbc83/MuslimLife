package com.muslimlife.app.data.model;

import com.muslimlife.app.R;

public class Tv {

    public String[] title ={"Al-Huda.tv","Al-Huda.tv","Al-Huda.tv","Al-Huda.tv","Al-Huda.tv"};
    public String[] subTitle={"Что было до человека?","Что было до человека?","Что было до человека?","Что было до человека?","Что было до человека?"};
    public String[] time={"9:30-11:30","9:30-11:30","9:30-11:30","9:30-11:30","9:30-11:30"};
    public int[] icon = {R.drawable.radio_test,R.drawable.radio_test,R.drawable.radio_test,R.drawable.radio_test,R.drawable.radio_test};

    public String[] getTitle() {
        return title;
    }

    public void setTitle(String[] title) {
        this.title = title;
    }

    public String[] getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String[] subTitle) {
        this.subTitle = subTitle;
    }

    public String[] getTime() {
        return time;
    }

    public void setTime(String[] time) {
        this.time = time;
    }

    public int[] getIcon() {
        return icon;
    }

    public void setIcon(int[] icon) {
        this.icon = icon;
    }
}
