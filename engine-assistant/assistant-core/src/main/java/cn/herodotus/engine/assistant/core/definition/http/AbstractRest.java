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

package cn.herodotus.engine.assistant.core.definition.http;

import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.MsgConvertor;
import com.ejlchina.okhttps.jackson.JacksonMsgConvertor;

/**
 * <p>File: AbstractRestApiService </p>
 *
 * <p>Description: 外部Rest API抽象服务 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/4/10 15:33
 */
public abstract class AbstractRest {

    /**
     * 获取外部Rest API基础地址
     * @return 访问接口的统一BaseURL
     */
    protected abstract String getBaseUrl();

    protected HTTP http() {
        return HTTP.builder()
                .baseUrl(getBaseUrl())
                .addMsgConvertor(getMsgConvertor())
                .build();
    }

    protected MsgConvertor getMsgConvertor() {
        return new JacksonMsgConvertor();
    }
}
