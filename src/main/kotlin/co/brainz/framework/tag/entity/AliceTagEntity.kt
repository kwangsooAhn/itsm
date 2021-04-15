/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "awf_tag")
data class AliceTagEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "tag_id", length = 128)
    var tagId: String = "",

    @Column(name = "tag_type", length = 128)
    val tagType: String,

    @Column(name = "tag_value", length = 256)
    val tagValue: String,

    @Column(name = "target_id", length = 128)
    val targetId: String
) : Serializable
