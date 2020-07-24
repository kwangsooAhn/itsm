package co.brainz.itsm.faq.repository

import co.brainz.itsm.faq.entity.FaqEntity

interface FaqRepositoryCustom {

    fun findFaqTopList(limit: Long): List<FaqEntity>
}
