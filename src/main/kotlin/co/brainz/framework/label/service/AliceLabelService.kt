/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.label.service

import co.brainz.framework.label.dto.AliceLabelDto
import co.brainz.framework.label.entity.AliceLabelEntity
import co.brainz.framework.label.entity.AliceLabelEntityPk
import co.brainz.framework.label.repository.AliceLabelRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AliceLabelService(
    private val aliceLabelRepository: AliceLabelRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun getLabels(labelTarget: String, labelTargetId: String, labelKey: String): MutableList<AliceLabelEntity> {
        logger.info(
            "select labels : label target : %s, label target id : %s, label key : %s",
            labelTarget,
            labelTargetId,
            labelKey
        )
        return aliceLabelRepository.findLabels(labelTarget, labelTargetId, labelKey)
    }

    fun setLabels(aliceLabelDto: AliceLabelDto): Boolean {
        logger.info("insert labels : $aliceLabelDto")
        aliceLabelDto.labels?.forEach {
            aliceLabelRepository.save(
                AliceLabelEntity(
                    labelTarget = aliceLabelDto.labelTarget,
                    labelTargetId = aliceLabelDto.labelTargetId,
                    labelKey = it.key,
                    labelValue = it.value
                )
            )
        }
        return true
    }

    fun deleteLabels(aliceLabelDto: AliceLabelDto): Boolean {
        logger.info("delete labels : $aliceLabelDto")
        var aliceLabelEntity: AliceLabelEntity?

        if (aliceLabelDto.labels == null) {
            aliceLabelRepository.removeLabels(aliceLabelDto.labelTarget, aliceLabelDto.labelTargetId)
        }
        aliceLabelDto.labels?.forEach { label ->
            aliceLabelEntity = aliceLabelRepository.findByIdOrNull(
                AliceLabelEntityPk(
                    aliceLabelDto.labelTarget,
                    aliceLabelDto.labelTargetId,
                    label.key
                )
            )
            aliceLabelEntity?.let {
                aliceLabelRepository.delete(it)
            }

        }
        return true
    }

    fun updateLabels(aliceLabelDto: AliceLabelDto): Boolean {
        logger.info("update labels : $aliceLabelDto")
        var aliceLabelEntity: AliceLabelEntity?

        aliceLabelDto.labels?.forEach { label ->
            aliceLabelEntity = aliceLabelRepository.findByIdOrNull(
                AliceLabelEntityPk(
                    aliceLabelDto.labelTarget,
                    aliceLabelDto.labelTargetId,
                    label.key
                )
            )

            aliceLabelEntity?.let {
                it.labelValue = label.value
                aliceLabelRepository.save(it)
            }
        }
        return true
    }
}