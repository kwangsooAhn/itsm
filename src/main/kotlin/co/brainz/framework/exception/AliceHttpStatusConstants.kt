package co.brainz.framework.exception

/**
 * Http status
 *
 * response 시 내부적으로 사용할 값을 정의한다.
 * 이것 외에 쓸 생각은 없지만 추가가 필요해진 경우 아래 사이트를 참고하여 추가한다.
 * 상태값은 org.springframework.http.HttpStatus 를 참고한다.
 *
 * @site https://ko.wikipedia.org/wiki/HTTP_%EC%83%81%ED%83%9C_%EC%BD%94%EB%93%9C#3xx_(%EB%A6%AC%EB%8B%A4%EC%9D%B4%EB%A0%89%EC%85%98_%EC%99%84%EB%A3%8C)
 *       https://ko.wikipedia.org/wiki/HTTP
 */
enum class AliceHttpStatusConstants(val code: Int, val codeName: String) {


    /**
     * 전송 성공
     */
    OK(200, "OK"),

    /**
     * 요청 실패. 서버가 요청사항을 처리할 수 없음.
     */
    BAD_REQUEST(400, "Bad Request"),

    /**
     * 인증실패. 비로그인 사용자
     */
    UNAUTHORIZED(401, "Unauthorized"),

    /**
     * 인증은 되었으나 리소스에 접근 권한이 없음
     */
    FORBIDDEN(403, "Forbidden"),

    /**
     * 리소스를 찾을 수 없음
     */
    NOT_FOUND(404, "Not Found"),

    /**
     * 다양한 비지니스 원인으로 인해 요청한 작업이 불가능함
     */
    NOT_ACCEPTABLE(406, "Not Acceptable"),

    /**
     * 서버 오류. 400번대 외 기타 나머지 오류
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    /**
     * 요청을 수행할 수 있는 기능이 없음.
     */
    NOT_IMPLEMENTED(501, "Not Implemented")
}
