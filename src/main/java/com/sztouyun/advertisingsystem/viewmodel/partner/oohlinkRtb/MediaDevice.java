package com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb;

import java.io.Serializable;

/**
 * 媒体设备信息类
 * 
 */
public class MediaDevice implements Serializable {

	/** 设备类型(1:手机；2:平板；3：智能电视;4:户外媒体) */
	private Short deviceType = null;
	/** 操作系统类型(1:Android; 2:IOS; 3:Windows) */
	private Short osType = null;
	/** 系统版本 */
	private String osVersion = null;
	/** 厂家(如:HUAWEI) */
	private String vendor = null;
	/** 设备型号(如HONOUR) */
	private String model = null;
	/** 设备屏幕宽(像素) */
	private Integer screenWidth = null;
	/** 设备屏幕高(像素) */
	private Integer screenHeight = null;

	/**
	 * 获取设备类型(1:手机；2:平板；3：智能电视;4:户外媒体)
	 * 
	 * @return 设备类型(1:手机；2:平板；3：智能电视;4:户外媒体)
	 */
	public Short getDeviceType() {
		return deviceType;
	}

	/**
	 * 设置设备类型(1:手机；2:平板；3：智能电视;4:户外媒体)
	 * 
	 * @param deviceType 设备类型(1:手机；2:平板；3：智能电视;4:户外媒体)
	 */
	public void setDeviceType(Short deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * 获取操作系统类型(1:Android; 2:IOS; 3:Windows)
	 * 
	 * @return 操作系统类型(1:Android; 2:IOS; 3:Windows)
	 */
	public Short getOsType() {
		return osType;
	}

	/**
	 * 设置操作系统类型(1:Android; 2:IOS; 3:Windows)
	 * 
	 * @param osType 操作系统类型(1:Android; 2:IOS; 3:Windows)
	 */
	public void setOsType(Short osType) {
		this.osType = osType;
	}

	/**
	 * 获取系统版本
	 * 
	 * @return 系统版本
	 */
	public String getOsVersion() {
		return osVersion;
	}

	/**
	 * 设置系统版本
	 * 
	 * @param osVersion 系统版本
	 */
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	/**
	 * 获取厂家(如:HUAWEI)
	 * 
	 * @return 厂家(如:HUAWEI)
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * 设置厂家(如:HUAWEI)
	 * 
	 * @param vendor 厂家(如:HUAWEI)
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * 获取设备型号(如HONOUR)
	 * 
	 * @return 设备型号(如HONOUR)
	 */
	public String getModel() {
		return model;
	}

	/**
	 * 设置设备型号(如HONOUR)
	 * 
	 * @param model 设备型号(如HONOUR)
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * 获取设备屏幕宽(像素)
	 * 
	 * @return 设备屏幕宽(像素)
	 */
	public Integer getScreenWidth() {
		return screenWidth;
	}

	/**
	 * 设置设备屏幕宽(像素)
	 * 
	 * @param screenWidth 设备屏幕宽(像素)
	 */
	public void setScreenWidth(Integer screenWidth) {
		this.screenWidth = screenWidth;
	}

	/**
	 * 获取设备屏幕高(像素)
	 * 
	 * @return 设备屏幕高(像素)
	 */
	public Integer getScreenHeight() {
		return screenHeight;
	}

	/**
	 * 设置设备屏幕高(像素)
	 * 
	 * @param screenHeight 设备屏幕高(像素)
	 */
	public void setScreenHeight(Integer screenHeight) {
		this.screenHeight = screenHeight;
	}

}
