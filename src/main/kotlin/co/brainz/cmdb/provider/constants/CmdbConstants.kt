package co.brainz.cmdb.provider.constants

object CmdbConstants {

    /**
     * CMDB Object.
     */
    enum class CmdbObject(val value: String) {
        ATTRIBUTE("attribute"),
        CI("ci"),
        CLASS("class"),
        TYPE("type")
    }
}
