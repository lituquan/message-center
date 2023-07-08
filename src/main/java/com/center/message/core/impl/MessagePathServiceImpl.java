package com.center.message.core.impl;

import com.center.message.core.MessagePathService;
import com.center.message.model.MessageBody;
import com.center.message.model.MessagePathDTO;
import com.center.message.model.entity.MessagePath;
import com.center.message.model.entity.MessageScene;
import com.center.message.model.entity.MessageTemplate;
import com.center.message.persistent.PathMapper;
import com.center.message.persistent.SceneMapper;
import com.center.message.persistent.TemplateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessagePathServiceImpl implements MessagePathService {
    private final SceneMapper sceneMapper;
    private final TemplateMapper templateMapper;
    private final PathMapper pathMapper;

    @Override
    public void addScene(String remark) {
        MessageScene messageScene = MessageScene.builder()
                .createTime(new Date())
                .remark(remark)
                .build();
        sceneMapper.insert(messageScene);
    }

    @Override
    public void addTemplate(MessageTemplate template) {
        template.setCreateTime(new Date());
        templateMapper.insert(template);
    }

    @Override
    public void addPath(MessagePath path) {
        MessageTemplate messageTemplate = templateMapper.selectById(path.getTemplateId());
        Assert.notNull(messageTemplate, "template 不存在:" + path.getTemplateId());
        path.setCreateTime(new Date());
        pathMapper.insert(path);
    }

    @Override
    public List<MessagePathDTO> findPathBySceneAndType(MessageBody messageBody) {
        return pathMapper.findPathBySceneAndType(messageBody);
    }

}
