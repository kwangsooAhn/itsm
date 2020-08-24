/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.faq.mapper

import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.entity.FaqEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface FaqMapper {

    @Mappings(
        Mapping(source = "createUser.userName", target = "createUserName"),
        Mapping(source = "updateUser.userName", target = "updateUserName"),
        Mapping(target = "totalCount", ignore = true)
    )
    fun toFaqListDto(faqEntity: FaqEntity): FaqListDto
}
