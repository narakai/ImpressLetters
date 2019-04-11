package com.jeycorp.impressletters.type;

/**
 * Created by cha on 2016-11-17.
 */
public class GoodsCategory {
    private long seq;
    private String name;
    private boolean isSelected = false;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
