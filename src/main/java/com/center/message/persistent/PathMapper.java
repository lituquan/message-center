package com.center.message.persistent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.center.message.model.MessageBody;
import com.center.message.model.MessagePathDTO;
import com.center.message.model.entity.MessagePath;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Author: lituquan
 * Date: 2023/7/8
 */
@Mapper
public interface PathMapper extends BaseMapper<MessagePath> {
    List<MessagePathDTO> findPathBySceneAndType(MessageBody body);
}
