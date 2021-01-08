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
}
