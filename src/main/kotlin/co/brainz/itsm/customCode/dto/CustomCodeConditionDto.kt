package co.brainz.itsm.customCode.dto

import java.io.Serializable

data class CustomCodeConditionDto(
    var conditionKey: String,
    var conditionOperator: String,
    var conditionValue: Any
) : Serializable
