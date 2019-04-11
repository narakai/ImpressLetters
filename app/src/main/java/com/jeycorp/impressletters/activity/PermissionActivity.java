package com.jeycorp.impressletters.activity;


import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jeycorp.impressletters.R;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class PermissionActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            finish();
            //   User user = RegisterPreference.getUser(getApplicationContext());
            Intent intent = new Intent(PermissionActivity.this, IntroActivity.class);
                      /*  if (user != null)
                                intent = new Intent(PermissionActivity.this, MainActivity.class);
                        else
                                intent = new Intent(PermissionActivity.this, IntroActivity.class);*/

            startActivity(intent);
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(PermissionActivity.this, "권한거부시 앱을 정상적으로 실행할 수 없습니다.", Toast.LENGTH_SHORT)
                    .show();
            finish();
        }
    };

    public void permission_check(View v) {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                /* .setRationaleTitle(R.string.rationale_title)
                 .setRationaleMessage(R.string.rationale_message)*/
                .setDeniedTitle("권한 거부")
                .setDeniedMessage(
                        "서비스 정상 실행을 위한 권한 허용이 필요합니다. \n\n [설정] > [애플리케이션]-> [권한]에서 권한을 허용합니다.")
                .setGotoSettingButtonText("설정")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

}
