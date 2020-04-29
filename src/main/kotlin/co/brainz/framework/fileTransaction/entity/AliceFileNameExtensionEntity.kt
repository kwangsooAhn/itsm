package co.brainz.framework.fileTransaction.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_file_name_extension")
data class AliceFileNameExtensionEntity(
    @Id
    @Column(name = "file_name_extension", length = 128)
    var fileNameExtension: String
) : Serializable, AliceMetaEntity()
