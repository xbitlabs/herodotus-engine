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

package cn.herodotus.engine.oauth2.server.authorization.controller;

import cn.herodotus.engine.assistant.core.domain.Result;
import cn.herodotus.engine.oauth2.server.authorization.dto.Session;
import cn.herodotus.engine.oauth2.server.authorization.dto.SessionCreate;
import cn.herodotus.engine.oauth2.server.authorization.dto.SessionExchange;
import cn.herodotus.engine.oauth2.server.authorization.service.InterfaceSecurityService;
import cn.herodotus.engine.assistant.core.domain.SecretKey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gengwei.zheng
 * @see <a href="https://conkeyn.iteye.com/blog/2296406">参考文档</a>
 */
@RestController
@Tags({
        @Tag(name = "OAuth2 认证服务器接口"),
        @Tag(name = "OAuth2 认证服务器开放接口"),
        @Tag(name = "OAuth2 身份认证辅助接口")
})
public class IdentityController {

    private final Logger log = LoggerFactory.getLogger(IdentityController.class);

    @Autowired
    private InterfaceSecurityService interfaceSecurityService;

    @Operation(summary = "获取后台加密公钥", description = "根据未登录时的身份标识，在后台创建RSA公钥和私钥。身份标识为前端的唯一标识，如果为空，则在后台创建一个",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = {@ApiResponse(description = "自定义Session", content = @Content(mediaType = "application/json"))})
    @Parameters({
            @Parameter(name = "sessionCreate", required = true, description = "Session创建请求参数", schema = @Schema(implementation = SessionCreate.class)),
    })
    @PostMapping("/open/identity/session")
    public Result<Session> codeToSession(@Validated @RequestBody SessionCreate sessionCreate) {

        SecretKey secretKey = interfaceSecurityService.createSecretKey(sessionCreate.getClientId(), sessionCreate.getClientSecret(), sessionCreate.getSessionId());
        if (ObjectUtils.isNotEmpty(secretKey)) {
            Session session = new Session();
            session.setSessionId(secretKey.getIdentity());
            session.setPublicKey(secretKey.getPublicKey());

            return Result.content(session);
        }

        return Result.failure();
    }

    @Operation(summary = "获取AES秘钥", description = "用后台publicKey，加密前台publicKey，到后台换取AES秘钥",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = {@ApiResponse(description = "加密后的AES", content = @Content(mediaType = "application/json"))})
    @Parameters({
            @Parameter(name = "sessionExchange", required = true, description = "秘钥交换", schema = @Schema(implementation = SessionExchange.class)),
    })
    @PostMapping("/open/identity/exchange")
    public Result<String> exchange(@Validated @RequestBody SessionExchange sessionExchange) {

        String encryptedAesKey = interfaceSecurityService.exchange(sessionExchange.getSessionId(), sessionExchange.getConfidential());
        if (StringUtils.isNotEmpty(encryptedAesKey)) {
            return Result.content(encryptedAesKey);
        }

        return Result.failure();
    }
}
