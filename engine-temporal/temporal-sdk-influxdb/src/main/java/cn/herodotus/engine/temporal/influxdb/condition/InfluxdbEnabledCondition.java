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

package cn.herodotus.engine.temporal.influxdb.condition;

import cn.herodotus.engine.assistant.core.support.PropertyResolver;
import cn.herodotus.engine.temporal.core.constants.TemporalConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * <p>Description: Influxdb 注入开启条件 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/11/17 18:06
 */
public class InfluxdbEnabledCondition implements Condition {

    private static final Logger log = LoggerFactory.getLogger(InfluxdbEnabledCondition.class);

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata metadata) {
        String url = PropertyResolver.getProperty(conditionContext, TemporalConstants.ITEM_INFLUXDB_URL);
        String database = PropertyResolver.getProperty(conditionContext, TemporalConstants.ITEM_INFLUXDB_DATABASE);
        String username = PropertyResolver.getProperty(conditionContext, TemporalConstants.ITEM_INFLUXDB_USERNAME);
        String password = PropertyResolver.getProperty(conditionContext, TemporalConstants.ITEM_INFLUXDB_PASSWORD);
        boolean result = StringUtils.isNotBlank(url) && StringUtils.isNotBlank(database) && StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password);
        log.debug("[Herodotus] |- Condition [Influxdb Enabled] value is [{}]", result);
        return result;
    }
}
