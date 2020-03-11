package co.brainz.itsm.code.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_code")
data class CodeEntity(
        @Id @Column(name = "code") val code: String,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "p_code")
        val pCode: CodeEntity? = null,
        @Column(name = "code_value") val codeValue: String? = null
) : Serializable
