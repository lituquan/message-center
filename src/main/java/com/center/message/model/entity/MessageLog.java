package com.center.message.model.entity;

import com.center.message.enums.MessageType;
import com.center.message.enums.SendStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: lituquan
 * @Date: 2023/07/08 01:24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageLog implements Serializable {

	private static final long serialVersionUID = 931252093589546245L;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 状态
	 */
	private SendStatusType status;

	/**
	 * 消息内容
	 */
	private String result;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 消息id
	 */
	private String messageId;

	/**
	 * 参数json
	 */
	private String param;

	private MessageType messageType;
}
