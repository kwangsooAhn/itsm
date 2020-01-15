package co.brainz.framework.constants

/**
 * 프레임워크에서 공통으로 사용할 상수 선언 클래스
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
        PATTEN5("/certification/**"),
        PATTEN6("/oauth/**"),
        PATTEN7("/portal/**"),
        PATTEN8("/layout/**"),
        PATTEN9("/layout**"),
        PATTEN10("/index**"),
        PATTEN11("/document/documentSearch"),
        PATTEN12("/exception/**"),
        PATTEN13("/error"),
        PATTEN94("/files"),
        PATTEN95("/fileupload"),
        PATTEN96("/filedownload"),
        PATTEN97("/filelist"),
        PATTEN98("/filedel"),
        PATTEN99("/fileSubmit"),
        FORM("/forms/**"), //form 권한 생성시 삭제
        REST_FORM("/rest/forms**") //form 권한 생성시 삭제
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

}
