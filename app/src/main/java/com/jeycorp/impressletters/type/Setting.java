package com.jeycorp.impressletters.type;


public class Setting {
    private long seq;
    private String introAd;
    private String finishAd;
    private String viewAd;
    private String finishPopAd;
    private String finishPopText;

    public String getFinishPopText() {
        return finishPopText;
    }

    public void setFinishPopText(String finishPopText) {
        this.finishPopText = finishPopText;
    }

    public String getFinishPopAd() {
        return finishPopAd;
    }

    public void setFinishPopAd(String finishPopAd) {
        this.finishPopAd = finishPopAd;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getIntroAd() {
        return introAd;
    }

    public void setIntroAd(String introAd) {
        this.introAd = introAd;
    }

    public String getFinishAd() {
        return finishAd;
    }

    public void setFinishAd(String finishAd) {
        this.finishAd = finishAd;
    }

    public String getViewAd() {
        return viewAd;
    }

    public void setViewAd(String viewAd) {
        this.viewAd = viewAd;
    }
}
