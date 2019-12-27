package co.brainz.itsm.certification

enum class ServiceTypeEnum(val code: String, val value: String) {
    ALICE("user.serviceType.alice", "alice"),
    GOOGLE("user.serviceType.google", "google"),
    FACEBOOK("user.serviceType.facebook", "facebook")
}
