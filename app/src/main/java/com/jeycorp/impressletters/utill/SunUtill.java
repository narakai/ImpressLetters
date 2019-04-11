package com.jeycorp.impressletters.utill;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

import com.jeycorp.impressletters.define.DirectoryDefine;

public class SunUtill {
	private Context context;
	private String mPath;
	private String cashImage;


	public SunUtill(Context context) {

		this.context = context;
		mPath = Environment.getExternalStorageDirectory().toString()+ DirectoryDefine.GOODTHINGS;
		cashImage= mPath+"/cashImage";

		File f = new File(mPath);
		if (!f.isDirectory()) {
			f.mkdir();
		}
		makeNomedia(mPath);
		
		File fCash = new File(cashImage);
		if (!fCash.isDirectory()) {
			fCash.mkdir();
		}
		makeNomedia(cashImage);
	}
	
	private void makeNomedia(String path){
		File file = new File(path+".nomedia");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getPath() {
		return mPath;
	}
	public String getCashImage(){
		return cashImage;
	}


}
