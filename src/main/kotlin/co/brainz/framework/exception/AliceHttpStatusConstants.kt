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
     * 200 성공
     */
    OK(HttpStatus.SC_OK, "OK"),

    /**
     * 400 클라이언트의 요청이 유효하지 않아 요청을 처리할 수 없음.
     * - 서버 에러를 막기 위한 유효성 등이 여기에 포함됨.
     */
    BAD_REQUEST(HttpStatus.SC_BAD_REQUEST, "Bad Request"),

    /**
     * 401 인증되지 않은 클라이언트라 요청을 처리할 수 없음.
     */
    UNAUTHORIZED(HttpStatus.SC_UNAUTHORIZED, "Unauthorized"),

    /**
     * 403 클라이언트가 인증은 되었으나 리소스에 접근 권한이 없음.
     */
    FORBIDDEN(HttpStatus.SC_FORBIDDEN, "Forbidden"),

    /**
     * 404 클라이언트가 요청한 리소스를 찾을 수 없음.
     * - 알려지지 않은 또는 알 수 없는 URL.
     * - 올바른 URL 이나 조회된 자원이 없는 경우.
     */
    NOT_FOUND(HttpStatus.SC_NOT_FOUND, "Not Found"),

    /**
     * 409 클라이언트의 요청으로 서버 내 충돌이 발생함.
     * - 서버에 이미 있는 파일보다 오래된 파일을 업로드하여 버전이 충돌되는 경우.
     * - 삭제하려는 데이터가 다른곳에서 사용중이라 삭제 불가한 경우
     */
    CONFLICT(HttpStatus.SC_CONFLICT, "Conflict"),

    /**
     * 500 서버 오류.
     * - 서버에서 발생하는 처리 방법을 알 수 없는 기본 오류가 여기에 해당함.
     */
    INTERNAL_SERVER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error")
    ;

    companion object {
        /**
         * http 상태 값으로 해당 값의 표현(reasonPhrase)을 돌려준다
         *
         * @param status http status
         */
        fun getHttpPhraseByStatus(status: Int): String {
            var phrase = ""
            for (httpStatus in values()) {
                if (httpStatus.status == status) {
                    phrase = httpStatus.reasonPhrase
                    break
                }
            }
            return phrase
        }
    }
}
