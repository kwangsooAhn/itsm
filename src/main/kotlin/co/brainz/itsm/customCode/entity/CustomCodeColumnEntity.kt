package co.brainz.itsm.customCode.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "awf_custom_code_column")
@IdClass(CustomCodeColumnPk::class)
data class CustomCodeColumnEntity(
        @Id
        @Column(name = "custom_code_table", length = 128)
        var customCodeTable: String = "",

        @Id
        @Column(name = "custom_code_type", length = 128)
        var customCodeType: String = "",

        @Id
        @Column(name = "custom_code_column", length = 128)
        var customCodeColumn: String = "",

        @Column(name = "custom_code_column_name", length = 128)
        var customCodeColumnName: String = ""
): Serializable

data class CustomCodeColumnPk(
        var customCodeTable: String = "",
        var customCodeType: String = "",
        var customCodeColumn: String = ""
): Serializable