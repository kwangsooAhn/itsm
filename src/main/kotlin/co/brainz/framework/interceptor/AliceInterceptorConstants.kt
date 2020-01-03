package co.brainz.framework.interceptor

enum class AliceInterceptorConstants(val value: String) {

    PATTEN1("/assets/**"),
    PATTEN2("/favicon.ico"),
    PATTEN3("/"),
    PATTEN4("/logout"),
    PATTEN5("/certification/**"),
    PATTEN6("/oauth/**"),
    PATTEN7("/portal/**"),
    PATTEN8("/layout/**"),
    PATTEN9("/index**"),
    PATTEN10("/document/**"),
    PATTEN11("/exception/**"),
    PATTEN12("/error");

    companion object {
        fun getExcludePattens(): MutableList<String> {
            val pattens = mutableListOf<String>()
            values().forEach {
                pattens.add(it.value)
            }
            return pattens
        }
    }
}