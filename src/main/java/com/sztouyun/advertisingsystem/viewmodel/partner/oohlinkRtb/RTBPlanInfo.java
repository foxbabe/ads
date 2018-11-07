package com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb;

import java.io.Serializable;
import java.util.List;

/**
 * 广告返回信息类
 * 
 */
public class RTBPlanInfo implements Serializable {

	/** 广告请求标识 */
	private String requestId = null;
	/** 媒体位标识 */
	private Long positionId = null;
	/** 渠道id */
	private Short channelId = null;
	/** 广告计划标识 */
	private Long planId = null;
	/** 素材地址 */
	private String matUrl = null;
	/** 素材宽 */
	private Integer matWidth = null;
	/** 素材高 */
	private Integer matHeight = null;
	/** 素材类型(1:图片; 2:视频) */
	private Short matType = null;
	/** 文件名称 */
	private String fileName = null;
	/** 素材MD5 */
	private String matMd5 = null;
	/** 素材播放时长 */
	private Integer duration = null;
	/** 素材过期时间 */
	private Integer expTime = null;
	/** 展现通知url */
	private List<String> winNoticeUrlList = null;
	/** 广告效果监播url */
	private List<AdTrack> adTrackList = null;
	/** 点击url */
	private String clickUrl = null;
	/** 错误代码(0:正常,1:无素材) */
	private Short errorCode = null;

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
	 * 获取媒体位标识
	 * 
	 * @return 媒体位标识
	 */
	public Long getPositionId() {
		return positionId;
	}

	/**
	 * 设置媒体位标识
	 * 
	 * @param positionId 媒体位标识
	 */
	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	/**
	 * 获取渠道id
	 * 
	 * @return 渠道id
	 */
	public Short getChannelId() {
		return channelId;
	}

	/**
	 * 设置渠道id
	 * 
	 * @param channeId 渠道id
	 */
	public void setChannelId(Short channeId) {
		this.channelId = channeId;
	}

	/**
	 * 获取广告计划标识
	 * 
	 * @return 广告计划标识
	 */
	public Long getPlanId() {
		return planId;
	}

	/**
	 * 设置广告计划标识
	 * 
	 * @param planId 广告计划标识
	 */
	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	/**
	 * 获取素材地址
	 * 
	 * @return 素材地址
	 */
	public String getMatUrl() {
		return matUrl;
	}

	/**
	 * 设置素材地址
	 * 
	 * @param matUrl 素材地址
	 */
	public void setMatUrl(String matUrl) {
		this.matUrl = matUrl;
	}

	/**
	 * 获取素材宽
	 * 
	 * @return 素材宽
	 */
	public Integer getMatWidth() {
		return matWidth;
	}

	/**
	 * 设置素材宽
	 * 
	 * @param matWidth 素材宽
	 */
	public void setMatWidth(Integer matWidth) {
		this.matWidth = matWidth;
	}

	/**
	 * 获取素材高
	 * 
	 * @return 素材高
	 */
	public Integer getMatHeight() {
		return matHeight;
	}

	/**
	 * 设置素材高
	 * 
	 * @param matHeight 素材高
	 */
	public void setMatHeight(Integer matHeight) {
		this.matHeight = matHeight;
	}

	/**
	 * 获取素材类型(1:图片; 2:视频)
	 * 
	 * @return 素材类型(1:图片; 2:视频)
	 */
	public Short getMatType() {
		return matType;
	}

	/**
	 * 设置素材类型(1:图片; 2:视频)
	 * 
	 * @param matType 素材类型(1:图片; 2:视频)
	 */
	public void setMatType(Short matType) {
		this.matType = matType;
	}

	/**
	 * 获取文件名称
	 * 
	 * @return 文件名称
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 设置文件名称
	 * 
	 * @param fileName 文件名称
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 获取素材MD5
	 * 
	 * @return 素材MD5
	 */
	public String getMatMd5() {
		return matMd5;
	}

	/**
	 * 设置素材MD5
	 * 
	 * @param matMd5 素材MD5
	 */
	public void setMatMd5(String matMd5) {
		this.matMd5 = matMd5;
	}

	/**
	 * 获取素材播放时长
	 * 
	 * @return 素材播放时长
	 */
	public Integer getDuration() {
		return duration;
	}

	/**
	 * 设置素材播放时长
	 * 
	 * @param duration 素材播放时长
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * 获取素材过期时间
	 * 
	 * @return 素材过期时间
	 */
	public Integer getExpTime() {
		return expTime;
	}

	/**
	 * 设置素材过期时间
	 * 
	 * @param expTime 素材过期时间
	 */
	public void setExpTime(Integer expTime) {
		this.expTime = expTime;
	}

	/**
	 * 获取展现通知url
	 * 
	 * @return 展现通知url
	 */
	public List<String> getWinNoticeUrlList() {
		return winNoticeUrlList;
	}

	/**
	 * 设置展现通知url
	 * 
	 * @param winNoticeUrlList 展现通知url
	 */
	public void setWinNoticeUrlList(List<String> winNoticeUrlList) {
		this.winNoticeUrlList = winNoticeUrlList;
	}

	/**
	 * 获取广告效果监播url
	 * 
	 * @return 广告效果监播url
	 */
	public List<AdTrack> getAdTrackList() {
		return adTrackList;
	}

	/**
	 * 设置广告效果监播url
	 * 
	 * @param adTrackList 广告效果监播url
	 */
	public void setAdTrackList(List<AdTrack> adTrackList) {
		this.adTrackList = adTrackList;
	}

	/**
	 * 获取点击链接
	 * 
	 * @return 点击链接
	 */
	public String getClickUrl() {
		return clickUrl;
	}

	/**
	 * 设置点击链接
	 * 
	 * @param clickUrl 点击链接
	 */
	public void setClickUrl(String clickUrl) {
		this.clickUrl = clickUrl;
	}

	/**
	 * 获取错误代码(0:正常,1:无素材)
	 * 
	 * @return 错误代码(0:正常,1:无素材)
	 */
	public Short getErrorCode() {
		return errorCode;
	}

	/**
	 * 设置错误代码(0:正常,1:无素材)
	 * 
	 * @param errorCode 错误代码(0:正常,1:无素材)
	 */
	public void setErrorCode(Short errorCode) {
		this.errorCode = errorCode;
	}


	@Override
	public String toString() {
		return "RTBPlanInfo{" +
				"requestId='" + requestId + '\'' +
				", positionId=" + positionId +
				", channelId=" + channelId +
				", planId=" + planId +
				", matUrl='" + matUrl + '\'' +
				", matWidth=" + matWidth +
				", matHeight=" + matHeight +
				", matType=" + matType +
				", fileName='" + fileName + '\'' +
				", matMd5='" + matMd5 + '\'' +
				", duration=" + duration +
				", expTime=" + expTime +
				", winNoticeUrlList=" + winNoticeUrlList +
				", adTrackList=" + adTrackList +
				", clickUrl='" + clickUrl + '\'' +
				", errorCode=" + errorCode +
				'}';
	}
}
