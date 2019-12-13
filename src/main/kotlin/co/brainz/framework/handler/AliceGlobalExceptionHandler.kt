package co.brainz.framework.handler

import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.thymeleaf.exceptions.TemplateInputException
import org.thymeleaf.exceptions.TemplateProcessingException
import javax.servlet.http.HttpServletRequest


/**
 * 컨트롤러 에러 공통 처리 클래스
 *
 * TODO type에 따라 응답만 리턴할지 에러페이지로 포워딩할지 결정한다.
 */
@Controller
@ControllerAdvice
class AliceGlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(Exception::class)
    fun handleException(request: HttpServletRequest, e: Exception): Any {
        logger.debug(">>> AliceGlobalExceptionHandler ERROR HANDLER <<<")

        // TODO 응답만 줄지 페이지로 포워딩할지 결정하는 기능 구현 요망
        val type = "" // "json"
        val errorMsg = when (e) {
            is EmptyResultDataAccessException -> {
                logger.error("EmptyResultDataAccessException")
                "데이터 없음"
            }
            is NullPointerException -> {
                logger.error("EmptyResultDataAccessException")
                "널 포인터"
            }
            is TemplateInputException -> {
                logger.error("An error happened during template parsing")
                "타임리프 입력 오류"
            }
            is TemplateProcessingException -> {
                logger.error("An error happened during template parsing")
                "타임리프 파싱 오류"
            }
            else -> {
                logger.error("Unknown error code. Add after checking.\n{}", e)
                "알 수 없는 오류. 확인 후 오류 코드 추가 요망."
            }
        }

        var exception: Any = "error"
        when (type) {
            "json" -> exception = ResponseEntity<Any>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR)
            else -> {
                request.setAttribute("errorMsg", errorMsg)
            }
        }

        return exception
    }

}