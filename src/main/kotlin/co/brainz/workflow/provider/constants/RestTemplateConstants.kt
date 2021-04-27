/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.provider.constants

object RestTemplateConstants {

    /**
     * Form SaveType.
     *
     * @param code
     */
    enum class FormSaveType(val code: String) {
        SAVE_AS("saveas")
    }

    /**
     * Protocol.
     *
     * @param value
     */
    enum class Protocol(val value: String) {
        HTTPS("https"),
        HTTP("http")
    }

    /**
     * Form Status.
     *
     * @param value
     */
    enum class FormStatus(val value: String) {
        EDIT("form.status.edit"),
        PUBLISH("form.status.publish"),
        USE("form.status.use"),
        DESTROY("form.status.destroy")
    }

    /**
     * Process Status.
     *
     * @param value
     */
    enum class ProcessStatus(val value: String) {
        EDIT("process.status.edit"),
        PUBLISH("process.status.publish"),
        USE("process.status.use"),
        DESTROY("process.status.destroy")
    }

    /**
     * Document Status.
     *
     * @param value
     */
    enum class DocumentStatus(val value: String) {
        TEMPORARY("document.status.temporary"),
        USE("document.status.use"),
        DESTROY("document.status.destroy")
    }

    /**
     * Process SaveType.
     */
    enum class ProcessSaveType(val code: String) {
        SAVE_AS("saveas")
    }

    /**
     * 이미지 컴포넌트 디렉터리.
     */
    const val BASE_DIR: String = "public"
    const val FORM_IMAGE_DIR: String = "assets/media/images/form/"

    /**
     * 인스턴스 플랫폼
     */
    enum class InstancePlatform(val code: String) {
        ITSM("itsm"),
        API("api")
    }
}
