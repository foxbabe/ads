package com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb;

import java.io.Serializable;

/**
 * 移动网络参数类
 *
 */
public class MediaNetwork implements Serializable {

	/** 设备类型(1:手机；2:平板；3：智能电视;4:户外媒体) */
	private String ipv4 = null;
	/**
	 * 联网方式(0:未知; 1:移动接入类型未知; 2:2G; 3:3G; 4:4G; 5:5G; 100:wifi; 101:以太网;
	 * 999:新型网络)
	 */
	private Short connectionType = null;
	/** 运营商类型(1:移动;2:电信;3:联通;99:其他) */
	private Short operatorType = null;
	/** 基站id */
	private String cellularId = null;

	/**
	 * 获取设备类型(1:手机；2:平板；3：智能电视;4:户外媒体)
	 * 
	 * @return 设备类型(1:手机；2:平板；3：智能电视;4:户外媒体)
	 */
	public String getIpv4() {
		return ipv4;
	}

	/**
	 * 设置设备类型(1:手机；2:平板；3：智能电视;4:户外媒体)
	 * 
	 * @param ipv4 设备类型(1:手机；2:平板；3：智能电视;4:户外媒体)
	 */
	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}

	/**
	 * 获取联网方式(0:未知; 1:移动接入类型未知; 2:2G; 3:3G; 4:4G; 5:5G; 100:wifi; 101:以太网;
	 * 999:新型网络)
	 * 
	 * @return 联网方式(0:未知; 1:移动接入类型未知; 2:2G; 3:3G; 4:4G; 5:5G; 100:wifi; 101:以太网;
	 *         999:新型网络)
	 */
	public Short getConnectionType() {
		return connectionType;
	}

	/**
	 * 设置联网方式(0:未知; 1:移动接入类型未知; 2:2G; 3:3G; 4:4G; 5:5G; 100:wifi; 101:以太网;
	 * 999:新型网络)
	 * 
	 * @param connectionType 联网方式(0:未知; 1:移动接入类型未知; 2:2G; 3:3G; 4:4G; 5:5G;
	 *            100:wifi; 101:以太网; 999:新型网络)
	 */
	public void setConnectionType(Short connectionType) {
		this.connectionType = connectionType;
	}

	/**
	 * 获取运营商类型(1:移动;2:电信;3:联通;99:其他)
	 * 
	 * @return 运营商类型(1:移动;2:电信;3:联通;99:其他)
	 */
	public Short getOperatorType() {
		return operatorType;
	}

	/**
	 * 设置运营商类型(1:移动;2:电信;3:联通;99:其他)
	 * 
	 * @param operatorType 运营商类型(1:移动;2:电信;3:联通;99:其他)
	 */
	public void setOperatorType(Short operatorType) {
		this.operatorType = operatorType;
	}

	/**
	 * 获取基站id
	 * 
	 * @return 基站id
	 */
	public String getCellularId() {
		return cellularId;
	}

	/**
	 * 设置基站id
	 * 
	 * @param cellularId 基站id
	 */
	public void setCellularId(String cellularId) {
		this.cellularId = cellularId;
	}

	/**
	 * @return
	 */
	@Override
	public String toString() {
		return "[ipv4=" + ipv4 + ", connectionType=" + connectionType + ", operatorType=" + operatorType
				+ ", cellularId=" + cellularId + "]";
	}
}
