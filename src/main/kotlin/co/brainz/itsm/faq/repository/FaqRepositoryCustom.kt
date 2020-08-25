/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.faq.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.dto.FaqSearchRequestDto
import co.brainz.itsm.faq.entity.FaqEntity

interface FaqRepositoryCustom : AliceRepositoryCustom {

    /**
     * FAQ 목록을 조회한다.
     */
    fun findFaqs(searchRequestDto: FaqSearchRequestDto): List<FaqListDto>

    /**
     * Portal FAQ 목록 조회 (갯수).
     */
    fun findFaqTopList(limit: Long): List<FaqEntity>
}
