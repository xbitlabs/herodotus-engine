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

package cn.herodotus.engine.protect.web.secure.stamp;

import cn.herodotus.engine.cache.jetcache.stamp.AbstractStampManager;
import cn.herodotus.engine.protect.core.constants.ProtectConstants;
import cn.herodotus.engine.protect.core.properties.SecureProperties;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;

/**
 * <p>Description: 防刷签章管理器 </p>
 * <p>
 * 这里使用Long类型作为值的存储类型，是为了解决该Cache 同时可以存储Duration相关的数据
 *
 * @author : gengwei.zheng
 * @date : 2021/8/25 21:43
 */
public class AccessLimitedStampManager extends AbstractStampManager<String, Long> {

    private SecureProperties secureProperties;

    public AccessLimitedStampManager(SecureProperties secureProperties) {
        this.secureProperties = secureProperties;
    }

    public SecureProperties getSecureProperties() {
        return secureProperties;
    }

    @CreateCache(name = ProtectConstants.CACHE_NAME_TOKEN_ACCESS_LIMITED, cacheType = CacheType.BOTH)
    protected Cache<String, Long> cache;

    @Override
    protected Cache<String, Long> getCache() {
        return this.cache;
    }

    @Override
    public Long nextStamp(String key) {
        return 1L;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.setExpire(secureProperties.getAccessLimited().getExpire());
    }
}
