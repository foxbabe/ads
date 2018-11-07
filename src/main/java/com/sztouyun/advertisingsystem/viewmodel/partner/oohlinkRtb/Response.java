package com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb;

import lombok.Data;

@Data
public class Response {

	/**
	 * 应答编码   0 为成功，非 0 为错误码
	 */
	private Integer code;

	/**
	 * 应答消息
	 */
	private String message;

	/**
	 * 广告信息
	 */
	private RTBPlanInfo data;
}
