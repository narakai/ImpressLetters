package com.jeycorp.impressletters.result;

import com.jeycorp.impressletters.type.Finish;

import java.util.List;



public class GetFinishResult extends BaseResult {
	private Finish banner;
	private List<Finish> banners;

	public Finish getBanner() {
		return banner;
	}

	public void setBanner(Finish banner) {
		this.banner = banner;
	}

	public List<Finish> getBanners() {
		return banners;
	}

	public void setBanners(List<Finish> banners) {
		this.banners = banners;
	}

}
