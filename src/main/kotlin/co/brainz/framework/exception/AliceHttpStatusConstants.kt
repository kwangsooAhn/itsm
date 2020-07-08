package co.brainz.framework.exception

import org.apache.http.HttpStatus

/**
 * Http 상태 코드
 *
 * response 시 내부적으로 사용할 값을 정의한다.
 * 이정도면 적당하다 생각되나 추가가 필요해진 경우 아래 사이트를 참고하여 추가한다.
 * 상태값은 org.springframework.http.HttpStatus 를 참고한다.
 *
 * @site https://ko.wikipedia.org/wiki/HTTP_%EC%83%81%ED%83%9C_%EC%BD%94%EB%93%9C#3xx_(%EB%A6%AC%EB%8B%A4%EC%9D%B4%EB%A0%89%EC%85%98_%EC%99%84%EB%A3%8C)
 *       https://ko.wikipedia.org/wiki/HTTP
 */
enum class AliceHttpStatusConstants(val status: Int, val reasonPhrase: String) {

    /**
     * 전송 성공
     */
    OK(HttpStatus.SC_OK, "OK"),

    /**
     * 요청 실패. 서버가 요청사항을 처리할 수 없음.
     */
    BAD_REQUEST(HttpStatus.SC_BAD_REQUEST, "Bad Request"),

    /**
     * 인증실패. 비로그인 사용자
     */
    UNAUTHORIZED(HttpStatus.SC_UNAUTHORIZED, "Unauthorized"),

    /**
     * 인증은 되었으나 리소스에 접근 권한이 없음
     */
    FORBIDDEN(HttpStatus.SC_FORBIDDEN, "Forbidden"),

    /**
     * 리소스를 찾을 수 없음
     */
    NOT_FOUND(HttpStatus.SC_NOT_FOUND, "Not Found"),

    /**
     * 다양한 비지니스 원인으로 인해 요청한 작업이 불가능함
     */
    NOT_ACCEPTABLE(HttpStatus.SC_NOT_ACCEPTABLE, "Not Acceptable"),

    /**
     * 서버 오류. 400번대 외 기타 나머지 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"),

    /**
     * 요청을 수행할 수 있는 기능이 없음.
     */
    NOT_IMPLEMENTED(HttpStatus.SC_NOT_IMPLEMENTED, "Not Implemented")
    ;

    companion object {
        /**
         * http 상태 값으로 해당 값의 표현(reasonPhrase)을 돌려준다
         *
         * @param status http status
         */
        fun getHttpPhraseByStatus(status: Int): String {
            var phrase = ""
            values().forEach {
                if (it.status == status) {
                    phrase = it.reasonPhrase
                    return@forEach
                }
            }
            return phrase
        }
    }
}
