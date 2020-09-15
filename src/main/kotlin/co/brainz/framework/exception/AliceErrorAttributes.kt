package co.brainz.framework.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.lang.reflect.UndeclaredThrowableException
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.context.request.WebRequest

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
 * info: 기타 추가로 넘겨줄 정보
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
                errorAttributes["errorInfo"] = exception.getErrorInfo()
            }
            is HttpStatusCodeException -> {
                val responseBodyAsJson = exception.responseBodyAsString
                val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
                val jsonToMap: MutableMap<String, Any> = mapper.readValue(responseBodyAsJson)
                errorAttributes["status"] = jsonToMap["status"]
                errorAttributes["error"] = jsonToMap["error"]
                errorAttributes["message"] = jsonToMap["message"]
                errorAttributes["knownError"] = jsonToMap["knownError"]
                errorAttributes["exceptionType"] = jsonToMap["exceptionType"]
                errorAttributes["errorInfo"] = jsonToMap["errorInfo"]
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
