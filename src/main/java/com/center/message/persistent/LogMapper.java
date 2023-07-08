package com.center.message.persistent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.center.message.model.entity.MessageLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: lituquan
 * Date: 2023/7/8
 */
@Mapper
public interface LogMapper extends BaseMapper<MessageLog> {
    @Transactional
    @Update("update message_log set status=#{status} where message_id=#{messageId}")
    int updateStatusByMessageId(String messageId, String status);
}
