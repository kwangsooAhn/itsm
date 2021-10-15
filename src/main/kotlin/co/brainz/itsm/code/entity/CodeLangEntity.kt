/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.code.entity

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "awf_code_lang")
@IdClass(CodeLangEntityPk::class)
data class CodeLangEntity(
    @Id @Column(name = "code", length = 100)
    var code: String,

    @Column(name = "code_name", length = 256)
    var codeName: String? = null,

    @Id @Column(name = "lang", length = 100)
    var lang: String
) : Serializable

data class CodeLangEntityPk(
    var code: String = "",
    var lang: String = ""
) : Serializable
