package com.center.message.mock.queue;

import lombok.Data;

/*事件对象*/
@Data
public class ObjectEvent<T> {
    private T obj;
}