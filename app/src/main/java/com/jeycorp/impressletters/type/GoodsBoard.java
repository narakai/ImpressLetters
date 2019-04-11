package com.jeycorp.impressletters.type;


public class GoodsBoard {
    private long seq;
    private String createDate;
    private String updateDate;
    private String title;
    private String contents;
    private int likeCount;
    private int commentCount;
    private int shareCount;
    private String type;
    private long goodsCategorySeq;
    private int orderNum;
    private int readCount;
    private String userUid;
    private String imgBannerUrl;
    private String imgUrl;
    private String imgThumbUrl;
    private String movieUrl;
    private int bookCount;
    private int myLikeCount;
    private String reservationDate;

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }



    public int getMyLikeCount() {
        return myLikeCount;
    }

    public void setMyLikeCount(int myLikeCount) {
        this.myLikeCount = myLikeCount;
    }

    public String getImgBannerUrl() {
        return imgBannerUrl;
    }

    public void setImgBannerUrl(String imgBannerUrl) {
        this.imgBannerUrl = imgBannerUrl;
    }

    public String getMovieUrl() {
        return movieUrl;
    }

    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }

    public String getImgThumbUrl() {
        return imgThumbUrl;
    }

    public void setImgThumbUrl(String imgThumbUrl) {
        this.imgThumbUrl = imgThumbUrl;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getGoodsCategorySeq() {
        return goodsCategorySeq;
    }

    public void setGoodsCategorySeq(long goodsCategorySeq) {
        this.goodsCategorySeq = goodsCategorySeq;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
