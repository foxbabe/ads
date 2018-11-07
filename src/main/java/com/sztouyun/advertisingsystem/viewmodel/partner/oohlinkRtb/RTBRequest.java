package com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb;

import java.io.Serializable;
import java.util.Map;

/**
 * 广告信息类
 * 
 */
public class RTBRequest implements Serializable {

	/** 播控器编码 */
	private String playCode = null;
	/** 广告请求标识 */
	private String requestId = null;
	/** 渠道id */
	private String channelId = null;
	/** 身份认证令牌 */
	private String token = null;
	/** 设备参数 */
	private MediaDevice device = null;
	/** 设备识别码 */
	private MediaUdId udid = null;
	/** 移动网络参数 */
	private MediaNetwork network = null;
	/** 广告位参数 */
	private AdSlot adSlot = null;
	/** 扩展参数 */
	private Map<String, String> ext = null;

	/**
	 * 获取播控器编码
	 * 
	 * @return 播控器编码
	 */
	public String getPlayCode() {
		return playCode;
	}

	/**
	 * 设置播控器编码
	 * 
	 * @param playCode 播控器编码
	 */
	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}

	/**
	 * 获取广告请求标识
	 * 
	 * @return 广告请求标识
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * 设置广告请求标识
	 * 
	 * @param requestId 广告请求标识
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * 获取渠道id
	 * 
	 * @return 渠道id
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * 设置渠道id
	 * 
	 * @param channelId 渠道id
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	/**
	 * 获取身份认证令牌
	 * 
	 * @return 身份认证令牌
	 */
	public String getToken() {
		return token;
	}

	/**
	 * 设置身份认证令牌
	 * 
	 * @param token 身份认证令牌
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * 获取设备参数
	 * 
	 * @return 设备参数
	 */
	public MediaDevice getDevice() {
		return device;
	}

	/**
	 * 设置设备参数
	 * 
	 * @param device 设备参数
	 */
	public void setDevice(MediaDevice device) {
		this.device = device;
	}

	/**
	 * 获取设备识别码
	 * 
	 * @return 设备识别码
	 */
	public MediaUdId getUdid() {
		return udid;
	}

	/**
	 * 设置设备识别码
	 * 
	 * @param udid 设备识别码
	 */
	public void setUdid(MediaUdId udid) {
		this.udid = udid;
	}

	/**
	 * 获取移动网络参数
	 * 
	 * @return 移动网络参数
	 */
	public MediaNetwork getNetwork() {
		return network;
	}

	/**
	 * 设置移动网络参数
	 * 
	 * @param network 移动网络参数
	 */
	public void setNetwork(MediaNetwork network) {
		this.network = network;
	}

	/**
	 * 获取广告位参数
	 * 
	 * @return 广告位参数
	 */
	public AdSlot getAdSlot() {
		return adSlot;
	}

	/**
	 * 设置广告位参数
	 * 
	 * @param adSlot 广告位参数
	 */
	public void setAdSlot(AdSlot adSlot) {
		this.adSlot = adSlot;
	}

	/**
	 * 获取扩展参数
	 * 
	 * @return 扩展参数
	 */
	public Map<String, String> getExt() {
		return ext;
	}

	/**
	 * 设置扩展参数
	 * 
	 * @param ext 扩展参数
	 */
	public void setExt(Map<String, String> ext) {
		this.ext = ext;
	}

}
