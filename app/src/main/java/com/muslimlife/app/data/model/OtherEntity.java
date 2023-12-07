package com.muslimlife.app.data.model;

public class OtherEntity implements Comparable<OtherEntity>{

    private int type;
    private int text;
    private int drawable;
    private int backgroundColor;
    private int usedCount;
    private boolean active;

    private String url;

    public OtherEntity(int type, int text, int drawable, int backgroundColor){
        this.type = type;
        this.text = text;
        this.drawable = drawable;
        this.backgroundColor = backgroundColor;
    }

    public OtherEntity(int type, int text, int drawable, int backgroundColor, int usedCount){
        this.type = type;
        this.text = text;
        this.drawable = drawable;
        this.backgroundColor = backgroundColor;
        this.usedCount = usedCount;
    }

    public OtherEntity(String url, int text, int drawable, int backgroundColor){
        this.type = -1;
        this.url = url;
        this.text = text;
        this.drawable = drawable;
        this.backgroundColor = backgroundColor;
        this.usedCount = usedCount;
    }

    @Override
    public int compareTo(OtherEntity f) {

        if (usedCount < f.getUsedCount()) {
            return 1;
        }
        else if (usedCount >  f.getUsedCount()) {
            return -1;
        }
        else {
            return 0;
        }

    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getType() {
        return type;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount){this.usedCount = usedCount;}

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
