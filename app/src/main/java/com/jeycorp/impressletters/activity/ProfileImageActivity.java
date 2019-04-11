package com.jeycorp.impressletters.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jeycorp.impressletters.result.BaseResult;
import com.jeycorp.impressletters.utill.Analytics;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.utill.SunUtill;
import com.jeycorp.impressletters.volleyimage.ImageSync;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.volleyimage.BitmapCustom;
import com.jeycorp.impressletters.volleyimage.MultiPartRequest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileImageActivity extends BaseActivity {
    private PreferenceManagers preferenceManagers;
    private String profileImage = "";
    private ImageView imgProfile;
    private String imagePath = "";
    private Bitmap rotatedSrc;
    private String ext;
    public static final int REQUEST_GALLEY = 0;
    private int selectPosition = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_profile_image);
            setInitView();
            new Analytics(getApplication()).getOutputEvent("프로필 이미지 변경 화면");
        }
    }

    private void setInitView() {
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        preferenceManagers = new PreferenceManagers(activity);

        selectPosition = preferenceManagers.getProfileImagePosition();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            profileImage = bundle.getString("image");
            getProfileImg();
        }
        getBgList();

        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProfileImgUrl("하잇");
            }
        });
    }

    private void getBgList() {
        final int[] bgImgArray = getXml(R.array.bg_img_array);
        final LinearLayout linearBg = (LinearLayout) findViewById(R.id.linearBg);
        for (int i = 0; i < bgImgArray.length; i++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            final View linearParentView = (View) inflater.inflate(R.layout.view_profile, null);
            RelativeLayout btnSelect = (RelativeLayout) linearParentView.findViewById(R.id.btnSelect);

            if (i == selectPosition) {
                btnSelect.setVisibility(View.VISIBLE);
            } else {
                btnSelect.setVisibility(View.INVISIBLE);
            }
            ImageView bgView = (ImageView) linearParentView.findViewById(R.id.imgBg);
            bgView.setBackgroundResource(bgImgArray[i]);

            linearBg.addView(linearParentView);
            bgView.setId(i);
            bgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectPosition = view.getId();

                    ViewGroup viewGroup = (ViewGroup) linearBg;
                    for (int a = 0; a < viewGroup.getChildCount(); a++) {
                        ViewGroup v1 = (ViewGroup) viewGroup.getChildAt(a);
                        //   ViewGroup v2 = (ViewGroup)viewGroup.getChildAt(0);
                        RelativeLayout v3 = (RelativeLayout) v1.getChildAt(0);
                        v3.setVisibility(View.GONE);
                        if (a == selectPosition) {
                            v3.setVisibility(View.VISIBLE);
                        }

                    }


                    ImageView imgPicture = (ImageView) findViewById(R.id.imgProfile);
                    ImageView imgSqr = (ImageView) findViewById(R.id.imgSqr);
                    imgSqr.setVisibility(View.GONE);

                    Bitmap bitmap = getBitmapResource(bgImgArray[view.getId()]);

                    rotatedSrc = bitmap;
                    imagePath = "resource";
                    BitmapCustom bitmapCustom = new BitmapCustom(activity);
                    Bitmap b = bitmapCustom.getRoundedBitmap(rotatedSrc);

                    imgPicture.setImageBitmap(b);
                    ext = "png";

                    new Analytics(getApplication()).getClickEvent("프로필 변경 이미지 화면", "기본프로필 변경 이벤트", "0");

                }
            });
        }

    }

    private void getProfileImg() {
        ImageSync imageSync = new ImageSync(this, imgProfile, true);
        imageSync.excute(profileImage);

        imgProfile.setVisibility(View.VISIBLE);

        ImageView imgSqr = (ImageView) findViewById(R.id.imgSqr);
        if (profileImage != null && !profileImage.equals("")) {
            imgSqr.setVisibility(View.VISIBLE);
        } else {
            imgSqr.setVisibility(View.VISIBLE);
        }
    }

    private void setProfileImgUrl(final String msg) {
        MultiPartRequest multiPartReq = new MultiPartRequest(activity, UrlDefine.API_SET_USER_PROFILE, BaseResult.class) {
            protected void onResult(BaseResult result) {
                super.onResult(result);
                Intent intent = new Intent(activity, SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                preferenceManagers.setProfileImagePosition(selectPosition);
                // Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        };

        if (imagePath != null && imagePath.length() > 1) {

            SunUtill mSunUtill = new SunUtill(activity);
            if (ext.equals("png")) {
                imagePath = mSunUtill.getPath() + "/tempImage.png";
            } else {
                imagePath = mSunUtill.getPath() + "/tempImage.jpg";
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(imagePath);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (ext.equals("png")) {
                rotatedSrc.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } else {
                rotatedSrc.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
            multiPartReq.addFile("file_profile", imagePath);

        }
        multiPartReq.addParam("uid", preferenceManagers.getUid());
        multiPartReq.submit();
    }

    public void menuClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnCamera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_GALLEY);
                new Analytics(getApplication()).getClickEvent("프로필 변경 이미지 화면", "카메라 변경 이벤트", "0");
                break;
            case R.id.btnGallery:
                Intent gallaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallaryIntent, REQUEST_GALLEY);
                new Analytics(getApplication()).getClickEvent("프로필 변경 이미지 화면", "갤러리 변경 이벤트", "0");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLEY) {
                Uri imgUri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(imgUri, projection, null, null, null);
                startManagingCursor(cursor);
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                imagePath = cursor.getString(columnIndex);

            }
            ImageView imgPicture = (ImageView) findViewById(R.id.imgProfile);
            ImageView imgSqr = (ImageView) findViewById(R.id.imgSqr);
            imgSqr.setVisibility(View.GONE);

            Bitmap bitmap = getBitmap(imagePath, 4);

            try {
                ExifInterface exif = new ExifInterface(imagePath);

                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = exifOrientationToDegrees(exifOrientation);
                rotatedSrc = rotate(bitmap, exifDegree);

            } catch (IOException e) {
                e.printStackTrace();
            }
            BitmapCustom bitmapCustom = new BitmapCustom(activity);
            Bitmap b = bitmapCustom.getRoundedBitmap(rotatedSrc);

            imgPicture.setImageBitmap(b);

            String extArray[] = imagePath.split("\\.");
            ext = extArray[1];

            selectPosition = 100;
            LinearLayout linearBg = (LinearLayout) findViewById(R.id.linearBg);
            ViewGroup viewGroup = (ViewGroup) linearBg;
            for (int a = 0; a < viewGroup.getChildCount(); a++) {
                ViewGroup v1 = (ViewGroup) viewGroup.getChildAt(a);
                //   ViewGroup v2 = (ViewGroup)viewGroup.getChildAt(0);
                RelativeLayout v3 = (RelativeLayout) v1.getChildAt(0);
                v3.setVisibility(View.GONE);
                if (a == selectPosition) {
                    v3.setVisibility(View.VISIBLE);
                }

            }
        }
    }

}
