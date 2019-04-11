package com.jeycorp.impressletters.param;

public class GetCommentParam extends BaseParam{
    private long seq;
    private String contents;
    private String userUid;
    private long goodsBoardSeq;
    private int commentCount;

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
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
}
