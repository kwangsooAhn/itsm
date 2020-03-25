package co.brainz.framework.exception

import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.WebRequest
import java.lang.reflect.UndeclaredThrowableException

/**
 * 에러 속성을 정의한다.
 *
 * 속성
 * timestamp: 시간
 * status: 상태
 * error: 에러 종류
 * message: 에러 메시지
 * path: 호출URL
 * exceptionType: 종류
 */
@Component
class AliceErrorAttributes : DefaultErrorAttributes() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getErrorAttributes(webRequest: WebRequest, includeStackTrace: Boolean): MutableMap<String, Any?> {
        val exception = getError(webRequest)
        val errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace) as LinkedHashMap<String, Any?>
        errorAttributes["exceptionType"] = exception::class.java.canonicalName
        when (exception) {
            is AliceException -> {
                val knownErrMsg = exception.getCode() + " (" + exception.getCodeDetail() + ")"
                logger.error("Known Alice error. {}", knownErrMsg)
                val status = exception.getHttpStatusCode()
                errorAttributes["status"] = status
                errorAttributes["error"] = AliceHttpStatusConstants.getHttpPhraseByStatus(status)
                errorAttributes["message"] = exception.message
                errorAttributes["knownError"] = knownErrMsg
            }
            else -> {

                var throwable = exception.cause
                var msg = ""
                while (throwable !== null) {
                    msg += "\n" + throwable.message
                    throwable = throwable.cause
                }
                errorAttributes["message"] = msg
            }
        }
        logger.error("Error attribute {}", errorAttributes)

        /**
         * BasicErrorController 에서 ResponseEntity 생성시 request 파라미터중 status 값으로 HttpStatus 를 셋업한다.
         * request 에 status를 셋팅할 필요성이 있어서 추가함
         * @since 2020-03-24 beom
         */
        errorAttributes["status"]?.let {
            webRequest.setAttribute("javax.servlet.error.status_code", it, RequestAttributes.SCOPE_REQUEST)
        }
        return errorAttributes
    }

    /**
     * DefaultErrorAttributes.getError() 오버라이드.
     *
     * UndeclaredThrowableException 이 오는경우가 있다.
     * try catch (Exception) 으로 잡아서 throw 하는 경우 발생한다. Exception으로 통으로 잡아서 발생하는 듯 하다.
     * AliceException 을 가져오기 위해 오버라이드 했다.
     *
     * @since 2020-03-25 beom
     */
    override fun getError(webRequest: WebRequest): Throwable {
        var exception = super.getError(webRequest)
        exception = when (exception) {
            is UndeclaredThrowableException -> exception.cause
            else -> exception
        }
        return exception
    }
}
