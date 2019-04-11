package com.jeycorp.impressletters.utill;


import android.content.DialogInterface;

import com.jeycorp.impressletters.activity.GoodsDetailActivity;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.type.GoodsBoard;

public class ImageSelect {
    private GoodsDetailActivity activity;

    public ImageSelect(GoodsDetailActivity activity) {
        this.activity = activity;
    }

    public void selectDialog(final String type, final GoodsBoard goodsBoard, final String imageUrl) {
        CharSequence info[] = new CharSequence[]{"이미지 저장", "이미지 공유", "닫기"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity, android.app.AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("작업선택");
        builder.setItems(info, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (type.equals(ValueDefine.DETAIL_MAIN_IMAGE)) {
                            new DownloadTask(activity, UrlDefine.DATA + goodsBoard.getImgUrl());
                        } else {
                            new DownloadTask(activity, imageUrl);
                        }
                        break;
                    case 1:
                        activity.setImageShare(UrlDefine.DATA + goodsBoard.getImgUrl());
                        break;
                    case 2:
                        break;
                }
                dialog.dismiss();

            }

        });

        builder.show();
    }
}
