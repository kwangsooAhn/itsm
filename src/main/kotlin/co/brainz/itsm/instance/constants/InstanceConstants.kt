package co.brainz.itsm.instance.constants

class InstanceConstants {
    /**
     * Element type list for instance history viewing.
     */
    enum class ElementListForHistoryViewing(val value: String) {
        COMMON_END_EVENT("commonEnd"),
        TIMER_START_EVENT("timerStart"),
        USER_TASK("userTask"),
        SCRIPT_TASK("scriptTask"),
        MANUAL_TASK("manualTask"),
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
