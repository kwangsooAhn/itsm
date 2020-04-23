package co.brainz.itsm.customCode.entity

import co.brainz.framework.auditor.AliceMetaEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_custom_code")
data class CustomCodeEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "custom_code_id", length = 128)
    var customCodeId: String = "",

    @Column(name = "custom_code_name", length = 128)
    var customCodeName: String = "",

    @Column(name = "target_table", length = 128)
    var targetTable: String = "",

    @Column(name = "search_column", length = 128)
    var searchColumn: String = "",

    @Column(name = "value_column", length = 128)
    var valueColumn: String = ""
) : Serializable, AliceMetaEntity()
