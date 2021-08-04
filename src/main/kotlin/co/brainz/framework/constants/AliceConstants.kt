/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.constants

/**
 * 제품 전체에서 공통으로 사용할 상수 선언 클래스
 *
 *  - 2021.08.03 Jung Hee Chan
 *     - 세부 목적에 따라 내부에 object 추가해서 사용 권장.
 *     - AliceConstants 도 수정할 일이 있으면 목적에 따라 세분화 필요.
 */
object AliceConstants {

    /**
     * RSA key 정의
     */
    enum class RsaKey(val value: String) {
        /** PRIVATE KEY */
        PRIVATE_KEY("_rsaPrivateKey"),

        /** PUBLIC MODULE */
        PUBLIC_MODULE("_publicKeyModulus"),

        /** PUBLIC EXPONENT */
        PUBLIC_EXPONENT("_publicKeyExponent"),

        /** RSA 사용할 대상인지 확인할 파라미터 이름 */
        USE_RSA("RSA")
        ;
    }

    /**
     * 접근 허용 url Patten 정의.
     */
    enum class AccessAllowUrlPatten(val value: String) {

        PATTEN1("/assets/**"),
        PATTEN2("/favicon.ico"),
        PATTEN3("/"),
        PATTEN4("/logout"),
        PATTEN6("/oauth/**"),
        PATTEN7("/portal/**"),
        PATTEN8("/layout/**"),
        PATTEN9("/layout**"),
        PATTEN10("/index**"),
        PATTEN11("/exception/**"),
        PATTEN12("/error"),
        PATTEN13("/login"),
        PATTEN14("/sessionInValid"),
        PATTEN15("/token/tokenSearch"),
        PATTEN94("/files"),
        PATTEN95("/fileupload"),
        PATTEN96("/filedownload"),
        PATTEN97("/filelist"),
        PATTEN98("/filedel"),
        PATTEN99("/fileSubmit"),
        PATTEN100("/i18n/**"),
        PATTEN101("/fileImages/**")
        ;

        companion object {
            fun getAccessAllowUrlPatten(): List<String> {
                val pattens = mutableListOf<String>()
                values().forEach {
                    pattens.add(it.value)
                }
                return pattens
            }
        }
    }

    /**
     * 작업 스케줄러 작업 유형 코드.
     */
    const val SCHEDULE_TASK_TYPE = "scheduler.taskType"

    /**
     * 작업 스케줄러 실행 사이클 타입 코드.
     */
    const val SCHEDULE_EXECUTE_CYCLE_TYPE = "scheduler.executeCycleType"

    /**
     * 작업 스케줄러 플러그인 홈.
     */
    const val SCHEDULE_PLUGINS_HOME = "plugins"

    /**
     * 작업 스케줄러 실행 사이클 타입.
     */
    enum class ScheduleExecuteCycleType(val code: String) {
        FIXED_DELAY("fixedDelay"),
        FIXED_RATE("fixedRate"),
        CRON("cron")
    }

    /**
     * 작서 스케줄러 작업 유형.
     */
    enum class ScheduleTaskType(val code: String) {
        CLASS("class"),
        QUERY("query"),
        JAR("jar")
    }

    /**
     * 스케줄러 command.
     */
    const val SCHEDULER_COMMAND_JAR = "-jar"

    /**
     * 플러그인 VM 옵션 log 경로
     */
    const val PLUGINS_VM_OPTIONS_LOG_HOME = "-Dlog.home"

    /**
     * 플러그인 VM 옵션 log 설정파일
     */
    const val PLUGINS_VM_OPTIONS_LOG_CONFIG_FILE = "-Dlogback.configurationFile"

    /**
     * 세션 만료시 로그인 페이지 자동 이동 시간.
     */
    const val SESSION_INVALID_AUTO_REDIRECT_TIME = 3

    /**
     * Email 인증 키 자릿수.
     */
    const val EMAIL_CERTIFICATION_KEY_SIZE = 50

    /**
     * 알림 개수.
     */
    const val NOTIFICATION_SIZE = 50L
}

/**
 * 페이징 처리와 관련된 상수
 */
object PagingConstants {
    /**
     * 페이징 리스트 화면에서 페이지별 출력 건수.
     */
    const val COUNT_PER_PAGE = 15L

    /**
     * 페이징 리스트 화면에서 정렬 순서 출력용 메시지 코드.
     */
    enum class ListOrderTypeCode(val code: String) {
        CREATE_DESC("common.label.createDtDesc")
    }
}