package com.center.message.mock.queue;

import com.lmax.disruptor.EventFactory;

/*事件生成工厂（用来初始化预分配事件对象）*/
public class ObjectEventFactory<T> implements EventFactory<ObjectEvent<T>> {
    public ObjectEventFactory() {
    }

    public ObjectEvent<T> newInstance() {
        return new ObjectEvent();
    }
}