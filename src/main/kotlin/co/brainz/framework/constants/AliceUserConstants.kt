/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.constants

/**
 * 사용자 기본 코드.
 */
object AliceUserConstants {

    /**
     * CREATE USER ID 기본값.
     */
    const val CREATE_USER_ID: String = "system"

    /**
     * 사용자 계정 만료일자 범위 설정값.
     */
    const val USER_EXPIRED_VALUE: Long = 3

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
     * Platform category p_code
     */
    const val PLATFORM_CATEGORY_P_CODE = "user.platform"

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
    const val AVATAR_BASIC_FILE_PATH: String = "/assets/media/image/avatar/"

    /**
     * 사용자 아바타 기본 파일명
     */
    const val AVATAR_BASIC_FILE_NAME: String = "default_profile.jpg"

    /**
     * 사용자 아바타 이미지 임시 경로
     */
    const val AVATAR_IMAGE_TEMP_DIR: String = "avatar/temp/"

    /**
     * 사용자 아바타 이미지 경로
     */
    const val AVATAR_IMAGE_DIR: String = "avatar"

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
        STATUS_SUCCESS("0"),
        STATUS_ERROR_USER_ID_DUPLICATION("1"),
        STATUS_ERROR("2"),
        STATUS_ERROR_EMAIL_DUPLICATION("3")
    }

    /**
     * 사용자 수정 상태
     */
    enum class UserEditStatus(val code: String) {
        STATUS_VALID_SUCCESS("-1"),
        STATUS_SUCCESS("0"),
        STATUS_ERROR_USER_ID_DUPLICATION("1"),
        STATUS_ERROR("2"),
        STATUS_ERROR_EMAIL_DUPLICATION("3"),
        STATUS_SUCCESS_EDIT_EMAIL("4"),
        STATUS_SUCCESS_EDIT_ADMIN("5")
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
        CREATE_USER_ADMIN("3")
    }
}
