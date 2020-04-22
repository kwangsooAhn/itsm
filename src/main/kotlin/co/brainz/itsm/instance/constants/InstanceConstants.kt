package co.brainz.itsm.instance.constants

class InstanceConstants {
    /**
     * Element type list for instance history viewing.
     */
    enum class ElementListForHistoryViewing(val value: String) {
        USER_TASK("userTask"),
        COMMON_START_EVENT("commonStart"),
        COMMON_END_EVENT("commonEnd"),
        SUB_PROCESS("subprocess"),
        SIGNAL_SEND("signalSend")
    }

    object InstanceHistory {
        private val map = ElementListForHistoryViewing.values().associateBy(ElementListForHistoryViewing::value)

        fun isHistoryElement(elementType: String?): Boolean {
            return map[elementType] != null
        }
    }
}
