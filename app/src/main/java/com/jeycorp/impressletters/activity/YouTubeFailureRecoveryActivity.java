/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeycorp.impressletters.activity;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.jeycorp.impressletters.define.DeveloperKey;
import com.jeycorp.impressletters.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

/**
 * An abstract activity which deals with recovering from errors which may occur during API
 * initialization, but can be corrected through user action.
 */
public abstract class YouTubeFailureRecoveryActivity extends YouTubeBaseActivity implements
    YouTubePlayer.OnInitializedListener {

  private static final int RECOVERY_DIALOG_REQUEST = 1;


  @Override
  public void onInitializationFailure(YouTubePlayer.Provider provider,
      YouTubeInitializationResult errorReason) {
    if (errorReason.isUserRecoverableError()) {
      errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
    } else {
      String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
      Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == RECOVERY_DIALOG_REQUEST) {
      // Retry initialization if user performed a recovery action
      getYouTubePlayerProvider().initialize(DeveloperKey.DEVELOPER_KEY, this);
    }
  }
  public void makeImageDownload(View v, String filename){

    String savePath = Environment.getExternalStorageDirectory().toString()+"/"+Environment.DIRECTORY_DOWNLOADS;
    File f = new File(savePath);
    if (!f.isDirectory())f.mkdirs();

    v.buildDrawingCache();
    Bitmap bitmap = v.getDrawingCache();
    FileOutputStream fos;
    Log.e("경로","경로당:"+savePath+"/"+filename);
    try{
      fos = new FileOutputStream(savePath+"/"+filename);
      bitmap.compress(Bitmap.CompressFormat.JPEG,80,fos);

    }catch (Exception e){
      e.printStackTrace();
    }
  }
  public String getStr(int resId) {
    return getResources().getString(resId);
  }
  protected abstract YouTubePlayer.Provider getYouTubePlayerProvider();

}
