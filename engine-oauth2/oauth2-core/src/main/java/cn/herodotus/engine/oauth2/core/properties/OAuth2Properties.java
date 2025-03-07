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

package cn.herodotus.engine.oauth2.core.properties;

import cn.herodotus.engine.oauth2.core.constants.OAuth2Constants;
import cn.herodotus.engine.oauth2.core.enums.Certificate;
import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Description: OAuth2 配置属性 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/6 16:36
 */
@ConfigurationProperties(prefix = OAuth2Constants.PROPERTY_PREFIX_OAUTH2)
public class OAuth2Properties {

    private Jwk jwk = new Jwk();

    public Jwk getJwk() {
        return jwk;
    }

    public void setJwk(Jwk jwk) {
        this.jwk = jwk;
    }

    public static class Jwk {

        private enum Strategy {
            STANDARD, CUSTOM
        }

        /**
         * 证书策略：standard OAuth2 标准证书模式；custom 自定义证书模式
         */
        private Certificate certificate = Certificate.CUSTOM;

        /**
         * jks证书文件路径
         */
        private String jksKeyStore = "classpath*:certificate/herodotus-cloud.jks";
        /**
         * jks证书密码
         */
        private String jksKeyPassword = "Herodotus-Cloud";
        /**
         * jks证书密钥库密码
         */
        private String jksStorePassword = "Herodotus-Cloud";
        /**
         * jks证书别名
         */
        private String jksKeyAlias = "herodotus-cloud";

        public Certificate getCertificate() {
            return certificate;
        }

        public void setCertificate(Certificate certificate) {
            this.certificate = certificate;
        }

        public String getJksKeyStore() {
            return jksKeyStore;
        }

        public void setJksKeyStore(String jksKeyStore) {
            this.jksKeyStore = jksKeyStore;
        }

        public String getJksKeyPassword() {
            return jksKeyPassword;
        }

        public void setJksKeyPassword(String jksKeyPassword) {
            this.jksKeyPassword = jksKeyPassword;
        }

        public String getJksStorePassword() {
            return jksStorePassword;
        }

        public void setJksStorePassword(String jksStorePassword) {
            this.jksStorePassword = jksStorePassword;
        }

        public String getJksKeyAlias() {
            return jksKeyAlias;
        }

        public void setJksKeyAlias(String jksKeyAlias) {
            this.jksKeyAlias = jksKeyAlias;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("certificate", certificate)
                    .add("jksKeyStore", jksKeyStore)
                    .add("jksKeyPassword", jksKeyPassword)
                    .add("jksStorePassword", jksStorePassword)
                    .add("jksKeyAlias", jksKeyAlias)
                    .toString();
        }
    }
}
