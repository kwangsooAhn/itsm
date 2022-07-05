package co.brainz.itsm.resource.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.fileTransaction.provider.AliceResourceProvider
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.resource.dto.ResourceDto
import co.brainz.itsm.resource.dto.ResourceSearchDto
import co.brainz.itsm.resource.dto.ResourcesPagingDto
import kotlin.math.ceil
import org.springframework.stereotype.Service

@Service
class ResourceService(
    private val resourceProvider: AliceResourceProvider
) {

    /**
     * 리소스 전체 목록 가져오기
     */
    fun getResources(searchCondition: ResourceSearchDto): ResourcesPagingDto {
        val pagingResult = resourceProvider.getExternalResources("", searchCondition.searchValue ?: "")
        val pagingOffset = (searchCondition.pageNum * searchCondition.contentNumPerPage)
        val result: List<ResourceDto> = pagingResult.take(pagingOffset.toInt())

        return ResourcesPagingDto(
            data = result,
            paging = AlicePagingData(
                totalCount = result.size.toLong(),
                totalCountWithoutCondition = pagingResult.size.toLong(),
                currentPageNum = searchCondition.pageNum,
                totalPageNum = ceil(pagingResult.size.toDouble() / searchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code,
                orderColName = searchCondition.orderColName,
                orderDir = searchCondition.orderDir
            )
        )
    }
}
