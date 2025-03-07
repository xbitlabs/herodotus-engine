/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.websocket.accelerator.processor;

import cn.herodotus.engine.websocket.accelerator.domain.WebSocketMessage;
import cn.herodotus.engine.websocket.accelerator.exception.IllegalChannelException;
import cn.herodotus.engine.websocket.accelerator.exception.PrincipalNotFoundException;
import cn.herodotus.engine.websocket.accelerator.properties.WebSocketProperties;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.codec.JsonJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>Description: WebSocket集群处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/24 18:46
 */
public class WebSocketClusterProcessor implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(WebSocketClusterProcessor.class);

    private RedissonClient redissonClient;
    private WebSocketProperties webSocketProperties;
    private WebSocketMessageSender webSocketMessageSender;

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void setWebSocketProperties(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
    }

    public void setWebSocketMessageSender(WebSocketMessageSender webSocketMessageSender) {
        this.webSocketMessageSender = webSocketMessageSender;
    }

    /**
     * 发送给集群中指定用户信息。
     *
     * @param webSocketMessage 发送内容参数实体 {@link WebSocketMessage}
     */
    public void toClusterUser(WebSocketMessage<String> webSocketMessage) {
        try {
            webSocketMessageSender.toUser(webSocketMessage);
        } catch (PrincipalNotFoundException e) {
            RTopic rTopic = redissonClient.getTopic(webSocketProperties.getTopic(), new JsonJacksonCodec());
            rTopic.publish(webSocketMessage);
            log.debug("[Herodotus] |- Current instance can not found user [{}], publish message.", webSocketMessage.getTo());
        } catch (IllegalChannelException e) {
            log.error("[Herodotus] |- Web socket channel is incorrect.");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RTopic topic = redissonClient.getTopic(webSocketProperties.getTopic());
        topic.addListener(WebSocketMessage.class, (MessageListener<WebSocketMessage<String>>) (charSequence, webSocketMessage) -> {
            log.debug("[Herodotus] |- Redisson received web socket sync message [{}]", webSocketMessage);
            webSocketMessageSender.toUser(webSocketMessage);
        });
    }
}
