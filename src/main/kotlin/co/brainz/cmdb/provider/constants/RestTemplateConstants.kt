package co.brainz.cmdb.provider.constants

object RestTemplateConstants {

    /**
     * CMDB 처리 상태.
     */
    enum class Status(val code: String) {
        STATUS_VALID_SUCCESS("-1"),
        STATUS_SUCCESS("0"),
        STATUS_ERROR_DUPLICATION("1"),
        STATUS_ERROR_NOT_EXIST("2")
    }

    /**
     * CMDB Object.
     */
    enum class CmdbObject(val value: String) {
        ATTRIBUTE("attribute"),
        CI("ci"),
        CIDetail("ciDetail"),
        CLASS("class"),
        TYPE("type")
    }

    /**
     * Attribute Url.
     *
     * @param url
     * @param method
     */
    enum class Attribute(val url: String, val method: String) {
        GET_ATTRIBUTES("/rest/cmdb/eg/attributes", "GET"),
        GET_ATTRIBUTE("/rest/cmdb/eg/attributes/{attributeId}", "GET"),
        POST_ATTRIBUTE("/rest/cmdb/eg/attributes", "POST"),
        PUT_ATTRIBUTE("/rest/cmdb/eg/attributes/{attributeId}", "PUT"),
        DELETE_ATTRIBUTE("/rest/cmdb/eg/attributes/{attributeId}", "DELETE")
    }

    /**
     * Class Url.
     *
     * @Param url
     * @Param method
     */
    enum class Class(val url: String, val method: String) {
        GET_CLASSES("/rest/cmdb/eg/classes", "GET"),
        GET_CLASS("/rest/cmdb/eg/classes/{classId}", "GET"),
        POST_CLASS("/rest/cmdb/eg/classes", "POST"),
        PUT_CLASS("/rest/cmdb/eg/classes/{classId}", "PUT"),
        DELETE_CLASS("/rest/cmdb/eg/classes/{classId}", "DELETE")
    }

    /**
     * Type Url.
     *
     * @param url
     * @param method
     */
    enum class Type(val url: String, val method: String) {
        GET_TYPES("/rest/cmdb/eg/types", "GET"),
        GET_TYPE("/rest/cmdb/eg/types/{typeId}", "GET"),
        POST_TYPE("/rest/cmdb/eg/types", "POST"),
        PUT_TYPE("/rest/cmdb/eg/types/{typeId}", "PUT"),
        DELETE_TYPE("/rest/cmdb/eg/types/{typeId}", "DELETE")
    }

    /**
     * Ci Url.
     *
     * @param url
     * @param method
     */
    enum class CI(val url: String, val method: String) {
        GET_CIS("/rest/cmdb/eg/cis", "GET"),
        GET_CI("/rest/cmdb/eg/cis/{ciId}", "GET"),
        POST_CI("/rest/cmdb/eg/cis", "POST"),
        PUT_CI("/rest/cmdb/eg/cis/{ciId}", "PUT"),
        DELETE_CI("/rest/cmdb/eg/cis/{ciId}", "DELETE"),
        PUT_CI_COMPONENT("/rest/cmdb/eg/cis/{ciId}/data", "PUT"),
        DELETE_CI_COMPONENT("/rest/cmdb/eg/cis/data", "DELETE")
    }
}
