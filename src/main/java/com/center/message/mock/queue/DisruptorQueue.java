package com.center.message.mock.queue;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.Data;

import java.util.Iterator;
import java.util.List;

/*Disruptor队列操作工具类*/
@Data
public class DisruptorQueue<T> {
    private Disruptor<ObjectEvent<T>> disruptor;
    private RingBuffer<ObjectEvent<T>> ringBuffer;

    public DisruptorQueue(Disruptor<ObjectEvent<T>> disruptor) {
        this.disruptor = disruptor;
        this.ringBuffer = disruptor.getRingBuffer();
        this.disruptor.start();
    }

    public void add(T t) {
        if (t != null) {
            long sequence = this.ringBuffer.next();

            try {
                ObjectEvent<T> event = (ObjectEvent) this.ringBuffer.get(sequence);
                event.setObj(t);
            } finally {
                this.ringBuffer.publish(sequence);
            }
        }
    }

    public void addAll(List<T> ts) {
        if (ts != null) {
            Iterator<T> var2 = ts.iterator();

            while (var2.hasNext()) {
                T t = var2.next();
                if (t != null) {
                    this.add(t);
                }
            }
        }
    }

    public long cursor() {
        return this.disruptor.getRingBuffer().getCursor();
    }

    public void shutdown() {
        this.disruptor.shutdown();
    }
}