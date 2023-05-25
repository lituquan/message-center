# message-center
a system to send message

## 设计模式

### 构建者 builder + 责任链 responsibility chain
    com.center.message.core.AbstractMessageHandlerChain.Builder
    com.center.message.core.AbstractMessageHandlerChain
    [参考](https://juejin.cn/post/7023691352223252488)

### 策略模式+简单工厂
    Sender:    com.center.message.core.SenderFactory   
    上下文:    com.center.message.core.SendClient

### 模板方法
    com.center.message.core.AbstractMessageHandler.handle
    
### 代理
    com.center.message.log.LogAspect    
    这里使用了切面写日志

### 观察者:发布-订阅模式
    com.center.message.core.AbstractMessageHandler.onApplicationEvent    