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

package cn.herodotus.engine.pay.core.constants;

import cn.herodotus.engine.assistant.core.constants.BaseConstants;

/**
 * <p>Description: 支付模块常量 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/19 17:14
 */
public interface PayConstants extends BaseConstants {

    String PROPERTY_PREFIX_PAY = PROPERTY_PREFIX_HERODOTUS + ".pay";

    String PROPERTY_PAY_ALIPAY = PROPERTY_PREFIX_PAY + ".alipay";
    String PROPERTY_PAY_WXPAY = PROPERTY_PREFIX_PAY + ".wxpay";

    String CACHE_NAME_TOKEN_PAY = CACHE_TOKEN_BASE_PREFIX + "pay:";

    String ITEM_ALIPAY_ENABLED = PROPERTY_PAY_ALIPAY + PROPERTY_ENABLED;
    String ITEM_WXPAY_ENABLED = PROPERTY_PAY_WXPAY + PROPERTY_ENABLED;


}
