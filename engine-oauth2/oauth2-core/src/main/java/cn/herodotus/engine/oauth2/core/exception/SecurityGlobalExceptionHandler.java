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

package cn.herodotus.engine.oauth2.core.exception;

import cn.herodotus.engine.assistant.core.domain.Result;
import cn.herodotus.engine.assistant.core.enums.ResultErrorCodes;
import cn.herodotus.engine.assistant.core.exception.GlobalExceptionHandler;
import cn.herodotus.engine.assistant.core.exception.PlatformException;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 统一异常处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/18 8:12
 */
@RestControllerAdvice
public class SecurityGlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(SecurityGlobalExceptionHandler.class);

    private static final Map<String, Result<String>> EXCEPTION_DICTIONARY = new HashMap<>();

    static {
        EXCEPTION_DICTIONARY.put(OAuth2ErrorCodes.ACCESS_DENIED, GlobalExceptionHandler.getUnauthorizedResult(ResultErrorCodes.ACCESS_DENIED));
        EXCEPTION_DICTIONARY.put(OAuth2ErrorCodes.INSUFFICIENT_SCOPE, GlobalExceptionHandler.getForbiddenResult(ResultErrorCodes.INSUFFICIENT_SCOPE));
        EXCEPTION_DICTIONARY.put(OAuth2ErrorCodes.INVALID_CLIENT, GlobalExceptionHandler.getPreconditionFailedResult(ResultErrorCodes.INVALID_CLIENT));
        EXCEPTION_DICTIONARY.put(OAuth2ErrorCodes.INVALID_GRANT, GlobalExceptionHandler.getPreconditionFailedResult(ResultErrorCodes.INVALID_GRANT));
        EXCEPTION_DICTIONARY.put(OAuth2ErrorCodes.INVALID_REDIRECT_URI, GlobalExceptionHandler.getPreconditionFailedResult(ResultErrorCodes.INVALID_REDIRECT_URI));
        EXCEPTION_DICTIONARY.put(OAuth2ErrorCodes.INVALID_REQUEST, GlobalExceptionHandler.getPreconditionFailedResult(ResultErrorCodes.INVALID_REQUEST));
        EXCEPTION_DICTIONARY.put(OAuth2ErrorCodes.INVALID_SCOPE, GlobalExceptionHandler.getPreconditionFailedResult(ResultErrorCodes.INVALID_SCOPE));
        EXCEPTION_DICTIONARY.put(OAuth2ErrorCodes.INVALID_TOKEN, GlobalExceptionHandler.getUnauthorizedResult(ResultErrorCodes.INVALID_TOKEN));
        EXCEPTION_DICTIONARY.put(OAuth2ErrorCodes.SERVER_ERROR, GlobalExceptionHandler.getInternalServerErrorResult(ResultErrorCodes.SERVER_ERROR));
        EXCEPTION_DICTIONARY.put(OAuth2ErrorCodes.TEMPORARILY_UNAVAILABLE, GlobalExceptionHandler.getServiceUnavailableResult(ResultErrorCodes.TEMPORARILY_UNAVAILABLE));
        EXCEPTION_DICTIONARY.put(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT, GlobalExceptionHandler.getUnauthorizedResult(ResultErrorCodes.UNAUTHORIZED_CLIENT));
        EXCEPTION_DICTIONARY.put(OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE, GlobalExceptionHandler.getNotAcceptableResult(ResultErrorCodes.UNSUPPORTED_GRANT_TYPE));
        EXCEPTION_DICTIONARY.put(OAuth2ErrorCodes.UNSUPPORTED_RESPONSE_TYPE, GlobalExceptionHandler.getNotAcceptableResult(ResultErrorCodes.UNSUPPORTED_RESPONSE_TYPE));
        EXCEPTION_DICTIONARY.put(OAuth2ErrorCodes.UNSUPPORTED_TOKEN_TYPE, GlobalExceptionHandler.getNotAcceptableResult(ResultErrorCodes.UNSUPPORTED_TOKEN_TYPE));
    }

    /**
     * Rest Template 错误处理
     *
     * @param ex       错误
     * @param request  请求
     * @param response 响应
     * @return Result 对象
     * @see <a href="https://www.baeldung.com/spring-rest-template-error-handling">baeldung</a>
     */
    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public static Result<String> restTemplateException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        Result<String> result = resolveException(ex, request.getRequestURI());
        response.setStatus(result.getStatus());
        return result;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public static Result<String> validationMethodArgumentException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response) {
        return validationBindException(ex, request, response);
    }

    @ExceptionHandler({BindException.class})
    public static Result<String> validationBindException(BindException ex, HttpServletRequest request, HttpServletResponse response) {
        Result<String> result = resolveException(ex, request.getRequestURI());

        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        //返回第一个错误的信息
        if (ObjectUtils.isNotEmpty(fieldError)) {
            result.validation(fieldError.getDefaultMessage(), fieldError.getCode(), fieldError.getField());
        }

        response.setStatus(result.getStatus());
        return result;
    }

    /**
     * 统一异常处理
     * AuthenticationException
     *
     * @param ex       错误
     * @param request  请求
     * @param response 响应
     * @return Result 对象
     */
    @ExceptionHandler({AuthenticationException.class, PlatformAuthenticationException.class})
    public static Result<String> authenticationException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        Result<String> result = resolveSecurityException(ex, request.getRequestURI());
        response.setStatus(result.getStatus());
        return result;
    }

    @ExceptionHandler({OAuth2AuthenticationException.class})
    public static Result<String> oAuth2AuthenticationException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        Result<String> result = resolveSecurityException(ex, request.getRequestURI());
        response.setStatus(result.getStatus());
        return result;
    }

    @ExceptionHandler({Exception.class, PlatformException.class})
    public static Result<String> exception(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        Result<String> result = resolveException(ex, request.getRequestURI());
        response.setStatus(result.getStatus());
        return result;
    }


    public static Result<String> resolveException(Exception ex, String path) {
        return GlobalExceptionHandler.resolveException(ex, path);
    }

    /**
     * 静态解析认证异常
     *
     * @param exception 错误信息
     * @return Result 对象
     */
    public static Result<String> resolveSecurityException(Exception exception, String path) {

        Exception reason = new Exception();
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException oAuth2AuthenticationException = (OAuth2AuthenticationException) exception;
            OAuth2Error oAuth2Error = oAuth2AuthenticationException.getError();
            if (EXCEPTION_DICTIONARY.containsKey(oAuth2Error.getErrorCode())) {
                Result<String> result = EXCEPTION_DICTIONARY.get(oAuth2Error.getErrorCode());
                result.path(oAuth2Error.getUri());
                result.stackTrace(exception.getStackTrace());
                result.detail(exception.getMessage());
                return result;
            }
        } else if (exception instanceof InsufficientAuthenticationException) {
            Throwable throwable = exception.getCause();
            if (ObjectUtils.isNotEmpty(throwable)) {
                reason = new Exception(throwable);
            } else {
                reason = exception;
            }
            log.debug("[Herodotus] |- InsufficientAuthenticationException cause content is [{}]", reason.getClass().getSimpleName());
        } else {
            reason = exception;
        }

        return resolveException(reason, path);
    }
}
