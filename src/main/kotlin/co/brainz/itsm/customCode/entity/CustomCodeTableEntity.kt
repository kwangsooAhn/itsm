package co.brainz.itsm.customCode.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_custom_code_table")
data class CustomCodeTableEntity(
    @Id
    @Column(name = "custom_code_table", length = 128)
    var customCodeTable: String = "",

    @Column(name = "custom_code_table_name", length = 128)
    var customCodeTableName: String = ""
) : Serializable
