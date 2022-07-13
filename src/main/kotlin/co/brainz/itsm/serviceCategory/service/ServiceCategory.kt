/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.serviceCategory.service

import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.serviceCategory.dto.ServiceCategoryDto
import co.brainz.itsm.serviceCategory.dto.ServiceCategoryReturnDto
import co.brainz.itsm.serviceCategory.entity.ServiceCategoryEntity
import co.brainz.itsm.serviceCategory.repository.ServiceCategoryRepo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ServiceCategory(
    private val serviceCategoryRepo: ServiceCategoryRepo
) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 서비스 카테고리 트리 조회
     */
    fun getServiceList(searchValue: String): ZResponse {
        val treeServiceList = mutableListOf<ServiceCategoryDto>()
        val pServiceList = mutableListOf<ServiceCategoryEntity>()

        var serviceSearchList = serviceCategoryRepo.findServiceCategorySearchList(searchValue)
        val count: Long = serviceSearchList.size.toLong()
        for (service in serviceSearchList) {
            var tempService = service.pServiceCode
            do {
                if (tempService !== null) {
                    pServiceList.add(tempService)
                    tempService = tempService.pServiceCode
                }
            } while (tempService !== null)
        }
        if (pServiceList.isNotEmpty()) {
            serviceSearchList += pServiceList
            serviceSearchList = serviceSearchList.distinct()
        }
        for (service in serviceSearchList) {
            treeServiceList.add(
                ServiceCategoryDto(
                    serviceCode = service.serviceCode,
                    pServiceCode = service.pServiceCode?.serviceCode,
                    serviceName = service.serviceName,
                    serviceDesc = service.serviceDesc,
                    availabilityGoal = service.availabilityGoal,
                    startDate = service.startDate,
                    endDate = service.endDate,
                    editable = service.editable,
                    useYn = service.useYn,
                    level = service.level,
                    seqNum = service.seqNum
                )
            )
        }
        return ZResponse(
            data = ServiceCategoryReturnDto(
                data = treeServiceList,
                totalCount = count
            )
        )
    }

    /**
     * 서비스 카테고리 상세 조회
     */
    fun getServiceDetail(serviceCode: String): ServiceCategoryDto {
        return serviceCategoryRepo.findService(serviceCode)
    }
}
