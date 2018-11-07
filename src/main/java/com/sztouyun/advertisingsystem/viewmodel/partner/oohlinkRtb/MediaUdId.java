package com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb;

import java.io.Serializable;

/**
 * 媒体设备识别码类
 *
 */
public class MediaUdId implements Serializable {

	/** imei号 */
	private String imei = null;
	/** mac地址 */
	private String mac = null;
	/** AndroidId */
	private String androidId = null;

	/**
	 * 获取imei号
	 * 
	 * @return imei号
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * 设置imei号
	 * 
	 * @param imei imei号
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * 获取mac地址
	 * 
	 * @return mac地址
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * 设置mac地址
	 * 
	 * @param mac mac地址
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * 获取AndroidId
	 * 
	 * @return AndroidId
	 */
	public String getAndroidId() {
		return androidId;
	}

	/**
	 * 设置AndroidId
	 * 
	 * @param androidId AndroidId
	 */
	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}

	/**
	 * @return
	 */
	@Override
	public String toString() {
		return "[imei=" + imei + ", mac=" + mac + ", androidId=" + androidId + "]";
	}

}
