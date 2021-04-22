package co.brainz.itsm.code.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_code_lang")
data class CodeLangEntity(
    @Id @Column(name = "code", length = 100)
    var code: String = "",

    @Column(name = "code_value", length = 256)
    var codeValue: String? = null,

    @Id @Column(name = "lang", length = 100)
    var lang: String = ""
) : Serializable
