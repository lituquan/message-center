package com.center.message.core;

import com.center.message.model.MessageBody;

public interface ProhibitionService {
    Boolean checkProhibition(MessageBody messageBody);
}
