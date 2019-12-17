package co.brainz.itsm.common

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awfCode")
data class CodeEntity(
        @Id
        val code: String,
        val pCode: String,
        val value: String? = null
) : Serializable