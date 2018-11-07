package com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb;

import java.io.Serializable;
import java.util.List;

/**
 * 广告位参数类
 *
 */
public class AdTrack implements Serializable {

	/** 信息类型(0:开始播放 1:结束播放) */
	private Short type = null;
	/** 监控地址列表 */
	private List<String> trackList = null;

	/**
	 * 获取信息类型(0:开始播放 1:结束播放)
	 * 
	 * @return 信息类型(0:开始播放 1:结束播放)
	 */
	public Short getType() {
		return type;
	}

	/**
	 * 设置信息类型(0:开始播放 1:结束播放)
	 * 
	 * @param type 信息类型(0:开始播放 1:结束播放)
	 */
	public void setType(Short type) {
		this.type = type;
	}

	/**
	 * 获取监控地址列表
	 * 
	 * @return 监控地址列表
	 */
	public List<String> getTrackList() {
		return trackList;
	}

	/**
	 * 设置监控地址列表
	 * 
	 * @param trackList 监控地址列表
	 */
	public void setTrackList(List<String> trackList) {
		this.trackList = trackList;
	}

}
