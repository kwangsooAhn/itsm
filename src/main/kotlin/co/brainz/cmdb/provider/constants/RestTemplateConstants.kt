package co.brainz.cmdb.provider.constants

object RestTemplateConstants {

    /**
     * CMDB Object.
     */
    enum class CmdbObject(val value: String) {
        ATTRIBUTE("attribute"),
        CI("ci"),
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
        GET_ATTRIBUTE("/rest/cmdb/eg/attriubtes/{attributeId}", "GET"),
        POST_ATTRIBUTE("/rest/cmdb/eg/attriubtes", "POST"),
        PUT_ATTRIBUTE("/rest/cmdb/eg/attriubtes/{attributeId}", "PUT"),
        DELETE_ATTRIBUTE("/rest/cmdb/eg/attriubtes/{attributeId}", "DELETE")
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
}
