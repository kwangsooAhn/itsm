/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.label.controller

import co.brainz.framework.label.dto.AliceLabelDto
import co.brainz.framework.label.entity.AliceLabelEntity
import co.brainz.framework.label.service.AliceLabelService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/labels")
class AliceLabelRestController(private val aliceLabelService: AliceLabelService) {
    /**
     * 라벨 조회.
     */
    @GetMapping("")
    fun getLabels(
        @RequestParam(value = "label_target") labelTarget: String,
        @RequestParam(value = "label_target_id") labelTargetId: String,
        @RequestParam(value = "label_key") labelKey: String?
    ): MutableList<AliceLabelEntity> {
        return aliceLabelService.getLabels(labelTarget, labelTargetId, labelKey)
    }

    /**
     * 라벨 추가.
     */
    @PostMapping("")
    fun addLabels(@RequestBody aliceLabelDto: AliceLabelDto): Boolean {
        return aliceLabelService.addLabels(aliceLabelDto)
    }

    /**
     * 라벨 변경.
     */
    @PutMapping("")
    fun setLabels(@RequestBody aliceLabelDto: AliceLabelDto): Boolean {
        return aliceLabelService.updateLabels(aliceLabelDto)
    }

    /**
     * 라벨 삭제.
     */
    @DeleteMapping("")
    fun deleteLabels(@RequestBody aliceLabelDto: AliceLabelDto): Boolean {
        return aliceLabelService.deleteLabels(aliceLabelDto)
    }
}
