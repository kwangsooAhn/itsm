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
}