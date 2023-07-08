package com.center.message.model.entity;

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
public class MessagePath implements Serializable {

	private static final long serialVersionUID = 931252093589546245L;

	/**
	 * 创建时间
	 */
	private Date createTime;

	private Date updateTime;

	private boolean isCorn;

	private String cron;

	private Integer templateId;

	private Integer sceneId;

	private Integer id;
}
