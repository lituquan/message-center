package com.center.message.core.impl;

import com.center.message.core.ProhibitionService;
import com.center.message.model.MessageBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Slf4j
@Service
public class ProhibitionServiceImpl implements ProhibitionService {
    private ConcurrentMap stopMap = new ConcurrentHashMap();

    @Override
    public Boolean checkProhibition(MessageBody messageBody) {
        return false;
    }
}
