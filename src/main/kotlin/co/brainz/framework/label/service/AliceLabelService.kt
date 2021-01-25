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

    /**
     * Label 조회.
     */
    fun getLabels(labelTarget: String, labelTargetId: String, labelKey: String?): MutableList<AliceLabelEntity> {
        logger.info(
            "select labels : label target : $labelTarget, label target id : $labelTargetId, label key : $labelKey"
        )
        return aliceLabelRepository.findLabels(labelTarget, labelTargetId, labelKey)
    }

    /**
     * Label 추가.
     */
    fun addLabels(aliceLabelDto: AliceLabelDto): Boolean {
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

    /**
     * Label 값 수정.
     *  - 라벨은 Key, Value 구성.
     */
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

    /**
     * Label 삭제.
     */
    fun deleteLabels(aliceLabelDto: AliceLabelDto): Boolean {
        logger.info("delete labels : $aliceLabelDto")
        var aliceLabelEntity: AliceLabelEntity?

        // 라벨 대상과 대상 아이디만을 이용해서 거기에 달린 라벨을 다 지우는 경우.
        if (aliceLabelDto.labels == null) {
            aliceLabelRepository.deleteLabels(aliceLabelDto.labelTarget, aliceLabelDto.labelTargetId)
        }
        // 특정 라벨 1개만 삭제하는 경우.
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
}
