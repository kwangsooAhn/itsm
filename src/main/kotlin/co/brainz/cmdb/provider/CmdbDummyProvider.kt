/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider

import co.brainz.cmdb.ciType.repository.CITypeRepository
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.*
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

    fun getDummyAttributes(searchValue: String): List<CmdbAttributeDto> {
        val file = this.getDummyFile(RestTemplateConstants.CmdbObject.ATTRIBUTE.value)
        var attributeDataList = mutableListOf<CmdbAttributeDto>()
        if (file != null) {
            attributeDataList = mapper.readValue(
                file,
                mapper.typeFactory.constructCollectionType(List::class.java, CmdbAttributeDto::class.java)
            )
            for (attribute in attributeDataList) {
                attribute.createDt = LocalDateTime.now()
            }
        }
        return attributeDataList
    }

    fun getDummyAttribute(attributeId: String): CmdbAttributeDto {
        val file = this.getDummyFile(RestTemplateConstants.CmdbObject.ATTRIBUTE.value)
        var attributeData = CmdbAttributeDto()
        if (file != null) {
            val attributeDataList: List<CmdbAttributeDto> = mapper.readValue(
                file,
                mapper.typeFactory.constructCollectionType(List::class.java, CmdbAttributeDto::class.java)
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

    fun getDummyClasses(searchValue: String): List<CmdbClassDto> {
        val file = this.getDummyFile(RestTemplateConstants.CmdbObject.CLASS.value)
        var classDataList = mutableListOf<CmdbClassDto>()
        if (file != null) {
            classDataList = mapper.readValue(
                file,
                mapper.typeFactory.constructCollectionType(List::class.java, CmdbClassDto::class.java)
            )
        }
        return classDataList
    }

    fun getDummyClass(classId: String): CmdbClassDto {
        val file = this.getDummyFile(RestTemplateConstants.CmdbObject.CLASS.value)
        var classData = CmdbClassDto()
        if (file != null) {
            val classDataList: List<CmdbClassDto> = mapper.readValue(
                file,
                mapper.typeFactory.constructCollectionType(List::class.java, CmdbClassDto::class.java)
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

    fun getDummyCis(searchValue: String): List<CmdbCiListDto> {
        val file = this.getDummyFile(RestTemplateConstants.CmdbObject.CI.value)
        var ciDataList = mutableListOf<CmdbCiListDto>()
        if (file != null) {
            ciDataList = mapper.readValue(
                file,
                mapper.typeFactory.constructCollectionType(List::class.java, CmdbCiListDto::class.java)
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

    fun getDummyCi(ciId: String): CmdbCiDto {
        val file = this.getDummyFile(RestTemplateConstants.CmdbObject.CI.value)
        var ciData = CmdbCiDto()
        if (file != null) {
            val ciDataList: List<CmdbCiDto> = mapper.readValue(
                file,
                mapper.typeFactory.constructCollectionType(List::class.java, CmdbCiDto::class.java)
            )
            if (ciDataList.isNotEmpty()) {
                val searchDataList = ciDataList.filter { it.ciId == ciId }
                if (!searchDataList.isNullOrEmpty()) {
                    ciData = searchDataList[0]
                    ciData.createDt = LocalDateTime.now()
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
