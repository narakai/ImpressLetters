package com.jeycorp.impressletters.result;


import com.jeycorp.impressletters.type.GoodsComment;
import com.jeycorp.impressletters.type.GoodsBoard;

import java.util.List;

public class GetGoodsBoardResult extends BaseResult{
    private GoodsBoard goodsBoard;
    private GoodsComment goodsComment;
    private List<GoodsBoard> goodsBoardList;
    private List<GoodsComment> goodsCommentList;
    private String weatherWord;
    private int commentCount;

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getWeatherWord() {
        return weatherWord;
    }

    public void setWeatherWord(String weatherWord) {
        this.weatherWord = weatherWord;
    }

    public GoodsComment getGoodsComment() {
        return goodsComment;
    }

    public void setGoodsComment(GoodsComment goodsComment) {
        this.goodsComment = goodsComment;
    }

    public List<GoodsComment> getGoodsCommentList() {
        return goodsCommentList;
    }

    public void setGoodsCommentList(List<GoodsComment> goodsCommentList) {
        this.goodsCommentList = goodsCommentList;
    }

    public GoodsBoard getGoodsBoard() {
        return goodsBoard;
    }

    public void setGoodsBoard(GoodsBoard goodsBoard) {
        this.goodsBoard = goodsBoard;
    }

    public List<GoodsBoard> getGoodsBoardList() {
        return goodsBoardList;
    }

    public void setGoodsBoardList(List<GoodsBoard> goodsBoardList) {
        this.goodsBoardList = goodsBoardList;
    }
}
