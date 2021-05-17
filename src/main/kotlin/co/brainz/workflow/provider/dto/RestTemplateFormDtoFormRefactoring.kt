/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateFormDtoFormRefactoring(
    var id: String = "",
    var name: String = "",
    var status: String? = "",
    var desc: String? = null,
    var category: String = "",
    var display: LinkedHashMap<String, Any> = LinkedHashMap(),
    var createUserKey: String? = null,
    var createUserName: String? = null,
    var createDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var updateUserName: String? = null,
    var updateDt: LocalDateTime? = null
) : Serializable
