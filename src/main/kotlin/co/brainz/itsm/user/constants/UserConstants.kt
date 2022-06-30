package co.brainz.itsm.user.constants

/**
 * 사용자 관련 상수를 정의한 클래스
 */
object UserConstants {

    /**
     * CREATE USER ID 기본값.
     */
    const val CREATE_USER_ID: String = "system"

    /**
     * 사용자 기본 언어셋.
     */
    const val USER_LOCALE_LANG: String = "ko"

    /**
     * 사용자 기본 날짜 포맷
     */
    const val USER_TIME_FORMAT: String = "yyyy-MM-dd HH:mm"

    /**
     * 사용자 기본 테마
     */
    const val USER_THEME: String = "default"

    /**
     * 사용자 식별 값
     */
    const val USER_ID: String = "user"

    /**
     * 관리자 식별 값
     */
    const val ADMIN_ID: String = "admin"

    /**
     * 사용자 아바타 URL
     */
    const val AVATAR_ID: String = "avatar"

    /**
     * 사용자 프로세스 URL
     */
    const val PROCESS_ID: String = "process"

    /**
     * 사용자 아바타 기본 디렉터리
     */
    const val AVATAR_BASIC_FILE_PATH: String = "/assets/media/images/avatar/"

    /**
     * 사용자 아바타 기본 파일명
     */
    const val AVATAR_BASIC_FILE_NAME: String = "img_avatar_01.png"

    /**
     * 사용자 아바타 이미지 임시 경로
     */
    const val AVATAR_IMAGE_TEMP_DIR: String = "avatar/temp/"

    /**
     * 사용자 아바타 이미지 경로
     */
    const val AVATAR_IMAGE_DIR: String = "avatar"

    /**
     * 사용자 타임존 기본값
     */
    const val DEFAULT_TIMEZONE = "Asia/Seoul"

    /**
     * 사용자 관련  부모 코드 정의
     */
    enum class UserPCode(val code: String) {
        P_THEME_CODE("user.theme"),
        P_LANG_CODE("user.lang"),
        P_DATE_CODE("user.date"),
        P_TIME_CODE("user.time"),
        PLATFORM_CATEGORY_P_CODE("user.platform")
    }

    /**
     * DB코드(messages.yml 과 동일한 값) - JPA Entity 맵핑 정보
     */
    enum class UserCodeAndColumnMap(val code: String, val column: String) {
        ID("user.search.id", "userId"),
        NAME("user.search.name", "userName"),
        POSITION("user.search.position", "position"),
        DEPARTMENT("user.search.department", "department"),
        OFFICE("user.search.officeNumber", "officeNumber"),
        MOBILE("user.search.mobileNumber", "mobileNumber")
        ;

        companion object {
            /**
             * code 로 JPA Entity 이름 가져오기
             */
            fun getUserCodeToColumn(code: String): String {
                for (key in values()) {
                    if (code == key.code) {
                        return key.column
                    }
                }
                return ""
            }
        }
    }

    /**
     * 사용자 설정 타입
     */
    enum class UserEdit(val code: String) {
        EDIT("edit"),
        VIEW("view"),
        EDIT_SELF("editself")
    }

    /**
     * 사용자 정의 타입.
     */
    enum class UserCustom(val code: String) {
        COLOR("color"),
        USER_ABSENCE("user_absence")
    }

    /**
     * 사용자 아바타 저장 유형
     */
    enum class AvatarType(val code: String) {
        FILE("FILE"),
        URL("URL")
    }

    /**
     * 사용자 상태.
     */
    enum class Status(val code: String, val value: Int) {
        SIGNUP("user.status.signup", 0),
        CERTIFIED("user.status.certified", 1),
        OVER("user.status.over", 2),
        ERROR("user.status.error", 3),
        EDIT("user.status.edit", 4)
    }

    /**
     * 사용자 등록 상태.
     */
    enum class SignUpStatus(val code: String) {
        STATUS_VALID_SUCCESS("-1"),
        STATUS_SUCCESS("Z-0000"),
        STATUS_ERROR("E-0000"),
        STATUS_ERROR_USER_ID_DUPLICATION("E-0001"),
        STATUS_ERROR_EMAIL_DUPLICATION("E-0007")
    }

    /**
     * 사용자 수정 상태
     */
    enum class UserEditStatus(val code: String) {
        STATUS_VALID_SUCCESS("-1")
    }

    /**
     * 사용자 수정 구분.
     */
    enum class UserEditType(val code: String) {
        ADMIN_USER_EDIT("userEdit"),
        SELF_USER_EDIT("selfEdit")
    }

    /**
     * 사용자 기본 권한.
     */
    enum class DefaultRole(val code: String) {
        USER_DEFAULT_ROLE("user.default.role")
    }

    /**
     * 사용자 기본 메뉴.
     */
    enum class DefaultMenu(val code: String) {
        USER_DEFAULT_MENU("user.default.menu")
    }

    /**
     * 사용자 기본 URL
     */
    enum class DefaultUrl(val code: String) {
        USER_DEFAULT_URL("user.default.url")
    }

    /**
     * 사용자 접근 플랫폼.
     */
    enum class Platform(val code: String, val value: String) {
        ALICE("user.platform.alice", "alice"),
        GOOGLE("user.platform.google", "google"),
        KAKAO("user.platform.kakao", "kakao")
    }

    /**
     * 사용자 자기정보 메일 관련 수정 상태
     */
    enum class SendMailStatus(val code: String) {
        CREATE_USER("0"),
        UPDATE_USER("1"),
        UPDATE_USER_EMAIL("2"),
        CREATE_USER_ADMIN("3"),
        UPDATE_USER_PASSWORD("4")
    }

    /**
     * 사용자 컴포넌트 > 검색 조건
     */
    enum class UserSearchTarget(val code: String) {
        ORGANIZATION("organization"),
        CUSTOM("custom")
    }
}
