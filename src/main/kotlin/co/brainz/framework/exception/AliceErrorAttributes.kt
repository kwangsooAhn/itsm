package co.brainz.framework.exception

import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
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
 */
@Component
class AliceErrorAttributes : DefaultErrorAttributes() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getErrorAttributes(webRequest: WebRequest, includeStackTrace: Boolean): MutableMap<String, Any> {
        val exception = getError(webRequest)

        val errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace) as LinkedHashMap<String, Any>
        errorAttributes["exceptionType"] = exception::class.java.canonicalName

        when (exception) {
            is AliceException -> {
                logger.error("Known Alice error.")
                errorAttributes["knownError"] = exception.getCode() + " (" + exception.getCodeDetail() + ")"
            }
        }

        logger.error("Exception type: {}", errorAttributes["exceptionType"])
        return errorAttributes
    }


}