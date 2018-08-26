package com.keywordstone.framework.common.queue.rocketmq.client;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public abstract class RocketMqConsumer<T> implements MessageListenerConcurrently {

    @Setter(AccessLevel.PROTECTED)
    private Class<T> tClass;

    public abstract boolean consume(T t);

    public boolean consume(MessageExt messageExt) {
        if (null != messageExt) {
            byte[] bytes = messageExt.getBody();
            if (null != bytes) {
                String json = new String(bytes);
                if (StringUtils.isNotEmpty(json)) {
                    T t = new Gson().fromJson(json, tClass);
                    if (!consume(t)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public ConsumeConcurrentlyStatus consume(List<MessageExt> messageExtList) {
        if (null != messageExtList && !messageExtList.isEmpty()) {
            for (MessageExt messageExt : messageExtList) {
                if (!consume(messageExt)) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList,
                                                    ConsumeConcurrentlyContext context) {
        return consume(messageExtList);
    }
}
