/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider

import co.brainz.cmdb.ciType.repository.CITypeRepository
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CIAttributeDto
import co.brainz.cmdb.provider.dto.CIClassDto
import co.brainz.cmdb.provider.dto.CIDto
import co.brainz.cmdb.provider.dto.CIListDto
import co.brainz.cmdb.provider.dto.CmdbTypeDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class CmdbDummyProvider(
    private val ciTypeRepository: CITypeRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getDummyAttributes(searchValue: String): List<CIAttributeDto> {
        val file = this.getDummyFile(RestTemplateConstants.CmdbObject.ATTRIBUTE.value)
        var attributeDataList = mutableListOf<CIAttributeDto>()
        if (file != null) {
            attributeDataList = mapper.readValue(
                file,
                mapper.typeFactory.constructCollectionType(List::class.java, CIAttributeDto::class.java)
            )
            for (attribute in attributeDataList) {
                attribute.createDt = LocalDateTime.now()
            }
        }
        return attributeDataList
    }

    fun getDummyAttribute(attributeId: String): CIAttributeDto {
        val file = this.getDummyFile(RestTemplateConstants.CmdbObject.ATTRIBUTE.value)
        var attributeData = CIAttributeDto()
        if (file != null) {
            val attributeDataList: List<CIAttributeDto> = mapper.readValue(
                file,
                mapper.typeFactory.constructCollectionType(List::class.java, CIAttributeDto::class.java)
            )
            if (attributeDataList.isNotEmpty()) {
                val searchDataList = attributeDataList.filter { it.attributeId == attributeId }
                if (!searchDataList.isNullOrEmpty()) {
                    attributeData = searchDataList[0]
                    attributeData.createDt = LocalDateTime.now()
                }
            }
        }
        return attributeData
    }

    fun getDummyTypes(searchValue: String): List<CmdbTypeDto> {
        val file = this.getDummyFile(RestTemplateConstants.CmdbObject.TYPE.value)
        var typeDataList = mutableListOf<CmdbTypeDto>()
        if (file != null) {
            typeDataList = mapper.readValue(
                file,
                mapper.typeFactory.constructCollectionType(List::class.java, CmdbTypeDto::class.java)
            )
        }
        return typeDataList
    }

    fun getDummyType(typeId: String): CmdbTypeDto {
        val file = this.getDummyFile(RestTemplateConstants.CmdbObject.TYPE.value)
        var typeData = CmdbTypeDto()
        if (file != null) {
            val typeDataList: List<CmdbTypeDto> = mapper.readValue(
                file,
                mapper.typeFactory.constructCollectionType(List::class.java, CmdbTypeDto::class.java)
            )
            if (typeDataList.isNotEmpty()) {
                val searchDataList = typeDataList.filter { it.typeId == typeId }
                if (!searchDataList.isNullOrEmpty()) {
                    typeData = searchDataList[0]
                    typeData.createDt = LocalDateTime.now()
                }
            }
        }
        return typeData
    }

    fun getDummyClasses(searchValue: String): List<CIClassDto> {
        val file = this.getDummyFile(RestTemplateConstants.CmdbObject.CLASS.value)
        var classDataList = mutableListOf<CIClassDto>()
        if (file != null) {
            classDataList = mapper.readValue(
                file,
                mapper.typeFactory.constructCollectionType(List::class.java, CIClassDto::class.java)
            )
        }
        return classDataList
    }

    fun getDummyClass(classId: String): CIClassDto {
        val file = this.getDummyFile(RestTemplateConstants.CmdbObject.CLASS.value)
        var classData = CIClassDto()
        if (file != null) {
            val classDataList: List<CIClassDto> = mapper.readValue(
                file,
                mapper.typeFactory.constructCollectionType(List::class.java, CIClassDto::class.java)
            )
            if (classDataList.isNotEmpty()) {
                val searchDataList = classDataList.filter { it.classId == classId }
                if (!searchDataList.isNullOrEmpty()) {
                    classData = searchDataList[0]
                    classData.createDt = LocalDateTime.now()
                }
            }
        }
        return classData
    }

    fun getDummyCis(searchValue: String): List<CIListDto> {
        val file = this.getDummyFile(RestTemplateConstants.CmdbObject.CI.value)
        var ciDataList = mutableListOf<CIListDto>()
        if (file != null) {
            ciDataList = mapper.readValue(
                file,
                mapper.typeFactory.constructCollectionType(List::class.java, CIListDto::class.java)
            )
            if (ciDataList.isNotEmpty()) {
                for (ciData in ciDataList) {
                    val typeData = ciData.typeId?.let { ciTypeRepository.findById(it) }
                    if (typeData != null) {
                        ciData.typeName = typeData.get().typeName
                    }
                }
                // 태그
            }
        }
        return ciDataList
    }

    fun getDummyCi(ciId: String): CIDto {
        val file = this.getDummyFile(RestTemplateConstants.CmdbObject.CI.value)
        var ciData = CIDto()
        if (file != null) {
            val ciDataList: List<CIDto> = mapper.readValue(
                file,
                mapper.typeFactory.constructCollectionType(List::class.java, CIDto::class.java)
            )
            if (ciDataList.isNotEmpty()) {
                val searchDataList = ciDataList.filter { it.ciId == ciId }
                if (!searchDataList.isNullOrEmpty()) {
                    ciData = searchDataList[0]
                    ciData.createDt = LocalDateTime.now()
                    val typeData = ciData.typeId?.let { ciTypeRepository.findById(it) }
                    if (typeData != null) {
                        ciData.typeName = typeData.get().typeName
                    }
                }
            }
        }
        return ciData
    }

    private fun getDummyFile(fileName: String): File? {
        var dataFile: File? = null
        val dummyDir = ClassPathResource("/public/assets/js/cmdb/dummy").file
        if (dummyDir.exists() && dummyDir.isDirectory) {
            val fileArray = dummyDir.listFiles { _, file -> file == "$fileName.json" }
            if (!fileArray.isNullOrEmpty()) {
                dataFile = fileArray[0]
            }
        }
        return dataFile
    }
}
