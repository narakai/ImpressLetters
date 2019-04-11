package com.jeycorp.impressletters.param;


public class GetGoodsCategoryParam {
    private long seq;
    private String title;
    private int startRow;
    private int fetchSize;
    private long minSeq;
    private String minCreateDate;
    private long goodsCategorySeq;

    public String getMinCreateDate() {
        return minCreateDate;
    }

    public void setMinCreateDate(String minCreateDate) {
        this.minCreateDate = minCreateDate;
    }

    public long getGoodsCategorySeq() {
        return goodsCategorySeq;
    }

    public void setGoodsCategorySeq(long goodsCategorySeq) {
        this.goodsCategorySeq = goodsCategorySeq;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public long getMinSeq() {
        return minSeq;
    }

    public void setMinSeq(long minSeq) {
        this.minSeq = minSeq;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
