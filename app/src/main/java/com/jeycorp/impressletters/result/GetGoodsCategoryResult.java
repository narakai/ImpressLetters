package com.jeycorp.impressletters.result;


import com.jeycorp.impressletters.type.GoodsCategory;

import java.util.List;

public class GetGoodsCategoryResult extends BaseResult{
    private List<GoodsCategory> goodsCategoryList;
    private GoodsCategory goodsCategory;

    public List<GoodsCategory> getGoodsCategoryList() {
        return goodsCategoryList;
    }

    public void setGoodsCategoryList(List<GoodsCategory> goodsCategoryList) {
        this.goodsCategoryList = goodsCategoryList;
    }

    public GoodsCategory getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(GoodsCategory goodsCategory) {
        this.goodsCategory = goodsCategory;
    }
}
