package co.brainz.itsm.code.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction

@Entity
@Table(name = "awf_code")
data class CodeEntity(
    @Id @Column(name = "code")
    var code: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_code")
    @NotFound(action = NotFoundAction.IGNORE)
    var pCode: CodeEntity? = null,

    @Column(name = "code_value")
    var codeValue: String? = null,

    @Column(name = "editable")
    var editable: Boolean? = true
) : Serializable, AliceMetaEntity()
