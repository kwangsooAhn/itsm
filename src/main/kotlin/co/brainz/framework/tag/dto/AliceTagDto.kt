/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag.dto

import java.io.Serializable

// 2021.04.24 Jung Hee Chan
// 프론트에서 사용하는 태그 라이브러리(tagify)에서 value 라는 이름으로 태그를 처리하므로 변수명 고정.
data class AliceTagDto(
    var tagId: String? = null,
    var tagType: String = "",
    var value: String = "",
    var targetId: String = ""
) : Serializable
