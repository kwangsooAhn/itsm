package co.brainz.framework.fileTransaction.entity

import co.brainz.framework.fileTransaction.entity.idclass.AliceFileCompositeKey
import java.io.Serializable
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_file_own_map")
@IdClass(AliceFileCompositeKey::class)
data class AliceFileOwnMapEntity(
    @Id @Column(name = "own_id")
    var ownId: String,

    @Id
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "file_seq")
    var fileLocEntity: AliceFileLocEntity
) : Serializable
