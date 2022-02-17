package co.brainz.itsm.plugin.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_plugin_history")
data class PluginHistoryEntity(
    @Id
    @Column(name = "history_id")
    val historyId: String,

    @Column(name = "plugin_id")
    val pluginId: String,

    @Column(name = "startdt")
    val startDt: LocalDateTime,

    @Column(name = "enddt")
    val endDt: LocalDateTime,

    @Column(name = "plugin_param")
    val pluginParam: String,

    @Column(name = "plugin_result")
    val pluginResult: String,

    @Column(name = "message")
    val message: String
) : Serializable
