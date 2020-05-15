package co.brainz.itsm.customCode.entity

import co.brainz.framework.auditor.AliceMetaEntity
import co.brainz.itsm.customCode.constants.CustomCodeConstants
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "awf_custom_code")
data class CustomCodeEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "custom_code_id", length = 128)
    var customCodeId: String = "",

    @Column(name = "type", length = 100)
    var type: String = CustomCodeConstants.Type.TABLE.code,

    @Column(name = "custom_code_name", length = 128)
    var customCodeName: String? = null,

    @Column(name = "target_table", length = 128)
    var targetTable: String? = null,

    @Column(name = "search_column", length = 128)
    var searchColumn: String? = null,

    @Column(name = "value_column", length = 128)
    var valueColumn: String? = null,

    @Column(name = "p_code", length = 128)
    var pCode: String? = null,

    @Column(name = "condition", length = 512)
    var condition: String? = null
) : Serializable, AliceMetaEntity()