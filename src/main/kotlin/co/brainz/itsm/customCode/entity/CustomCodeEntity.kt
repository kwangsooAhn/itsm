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
        @Column(name = "custom_code_id")
        var customCodeId: String = "",

        @Column(name = "custom_code_name")
        var customCodeName: String = "",

        @Column(name = "target_table")
        var targetTable: String = "",

        @Column(name = "key_column")
        var keyColumn: String = "",

        @Column(name = "value_column")
        var valueColumn: String = ""

): Serializable, AliceMetaEntity()