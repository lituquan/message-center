package com.center.message.persistent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.center.message.model.entity.MessageTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
 * Author: lituquan
 * Date: 2023/7/8
 */
@Mapper
public interface TemplateMapper extends BaseMapper<MessageTemplate> {

}
