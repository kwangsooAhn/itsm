/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "awf_plugin_history")
data class PluginHistoryEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "history_id")
    val historyId: String,

    @Column(name = "plugin_id")
    val pluginId: String,

    @Column(name = "startdt")
    val startDt: LocalDateTime?,

    @Column(name = "enddt")
    val endDt: LocalDateTime? = null,

    @Column(name = "plugin_param")
    val pluginParam: String? = null,

    @Column(name = "plugin_result")
    val pluginResult: String? = null,

    @Column(name = "message")
    val message: String? = null,

    @Column(name = "plugin_data")
    val pluginData: String?
) : Serializable
