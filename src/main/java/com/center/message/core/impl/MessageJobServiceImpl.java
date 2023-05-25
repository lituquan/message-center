package com.center.message.core.impl;

import cn.hutool.json.JSONUtil;
import com.center.message.core.MessageJobService;
import com.center.message.model.MessageJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Slf4j
@Service
public class MessageJobServiceImpl implements MessageJobService {

    @Override
    public void saveJob(MessageJob job) {
        log.info("job:{}", JSONUtil.toJsonStr(job));
    }
}
