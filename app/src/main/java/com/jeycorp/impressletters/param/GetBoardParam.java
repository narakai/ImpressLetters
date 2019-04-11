package com.jeycorp.impressletters.param;


public class GetBoardParam {
    private long seq;
    private String minCreateDate;
    private String uid;
    private String category;
    private String title;
    private String contents;
    private int startRow;
    private int fetchSize;
    private long minSeq;
    private String type;
    private int weatherCode;

    public String getMinCreateDate() {
        return minCreateDate;
    }

    public void setMinCreateDate(String minCreateDate) {
        this.minCreateDate = minCreateDate;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

