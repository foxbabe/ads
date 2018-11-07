package com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb;

import java.io.Serializable;

/**
 * 广告位参数类
 * 
 */
public class AdSlot implements Serializable {

	/** 素材类型(0:随意 1:图片 2:视频) */
	private Short type = null;
	/** 空闲时长(秒) */
	private Integer duration = null;
	/** 广告位屏幕宽(像素) */
	private Integer adslotWidth = null;
	/** 广告位屏幕高(像素) */
	private Integer adslotHeight = null;

	/**
	 * 获取素材类型(0:随意 1:图片 2:视频)
	 * 
	 * @return 素材类型(0:随意 1:图片 2:视频)
	 */
	public Short getType() {
		return type;
	}

	/**
	 * 设置素材类型(0:随意 1:图片 2:视频)
	 * 
	 * @param type 素材类型(0:随意 1:图片 2:视频)
	 */
	public void setType(Short type) {
		this.type = type;
	}

	/**
	 * 获取空闲时长(秒)
	 * 
	 * @return 空闲时长(秒)
	 */
	public Integer getDuration() {
		return duration;
	}

	/**
	 * 设置空闲时长(秒)
	 * 
	 * @param duration 空闲时长(秒)
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * 获取广告位屏幕宽(像素)
	 * 
	 * @return 广告位屏幕宽(像素)
	 */
	public Integer getAdslotWidth() {
		return adslotWidth;
	}

	/**
	 * 设置广告位屏幕宽(像素)
	 * 
	 * @param adslotWidth 广告位屏幕宽(像素)
	 */
	public void setAdslotWidth(Integer adslotWidth) {
		this.adslotWidth = adslotWidth;
	}

	/**
	 * 获取广告位屏幕高(像素)
	 * 
	 * @return 广告位屏幕高(像素)
	 */
	public Integer getAdslotHeight() {
		return adslotHeight;
	}

	/**
	 * 设置广告位屏幕高(像素)
	 * 
	 * @param adslotHeight 广告位屏幕高(像素)
	 */
	public void setAdslotHeight(Integer adslotHeight) {
		this.adslotHeight = adslotHeight;
	}

}
