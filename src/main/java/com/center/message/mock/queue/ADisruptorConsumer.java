package com.center.message.mock.queue;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/*消费者抽象类*/
public abstract class ADisruptorConsumer<T>
        implements EventHandler<ObjectEvent<T>>, WorkHandler<ObjectEvent<T>> {
    public ADisruptorConsumer() {
    }

    public void onEvent(ObjectEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    public void onEvent(ObjectEvent<T> event) throws Exception {
        this.consume(event.getObj());
    }

    public abstract void consume(T var1);
}