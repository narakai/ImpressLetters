package com.jeycorp.impressletters.type;


public class GoodsComment {
    private long seq;
    private String createDate;
    private String updateDate;
    private String contents;
    private String userUid;
    private long goodsBoardSeq;
    private String imgThumbUrl;
    private String imgUrl;
    private String nickname="GUEST";

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public long getGoodsBoardSeq() {
        return goodsBoardSeq;
    }

    public void setGoodsBoardSeq(long goodsBoardSeq) {
        this.goodsBoardSeq = goodsBoardSeq;
    }

    public String getImgThumbUrl() {
        return imgThumbUrl;
    }

    public void setImgThumbUrl(String imgThumbUrl) {
        this.imgThumbUrl = imgThumbUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
