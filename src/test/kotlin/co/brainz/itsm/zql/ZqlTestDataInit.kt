/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.zql

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.entity.AliceTagEntity
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.framework.util.AliceUtil
import co.brainz.itsm.numberingRule.entity.NumberingRuleEntity
import co.brainz.itsm.numberingRule.repository.NumberingRuleRepository
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.form.repository.WfFormRepository
import co.brainz.workflow.formGroup.entity.WfFormGroupEntity
import co.brainz.workflow.formGroup.repository.WfFormGroupRepository
import co.brainz.workflow.formRow.entity.WfFormRowEntity
import co.brainz.workflow.formRow.repository.WfFormRowRepository
import co.brainz.workflow.instance.constants.InstanceStatus
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.process.entity.WfProcessEntity
import co.brainz.workflow.process.repository.WfProcessRepository
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfTokenEntity
import co.brainz.workflow.token.repository.WfTokenRepository
import java.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ZqlTestDataInit {
    @Autowired
    private lateinit var wfFormRepository: WfFormRepository

    @Autowired
    private lateinit var aliceUserRepository: AliceUserRepository

    @Autowired
    private lateinit var wfFormGroupRepository: WfFormGroupRepository

    @Autowired
    private lateinit var wfFormRowRepository: WfFormRowRepository

    @Autowired
    private lateinit var wfComponentRepository: WfComponentRepository

    @Autowired
    private lateinit var aliceTagRepository: AliceTagRepository

    @Autowired
    private lateinit var wfDocumentRepository: WfDocumentRepository

    @Autowired
    private lateinit var wfProcessRepository: WfProcessRepository

    @Autowired
    private lateinit var wfInstanceRepository: WfInstanceRepository

    @Autowired
    private lateinit var wfTokenRepository: WfTokenRepository

    @Autowired
    private lateinit var numberingRuleRepository: NumberingRuleRepository

    @Autowired
    private lateinit var wfElementRepository: WfElementRepository

    val userKey = "0509e09412534a6e98f04ca79abb6424"
    val formName = "Test Form_"
    val groupName = "Test Group_"
    val numberTag = listOf("tag_number")
    val dateTag = listOf("tag_date")
    val tag = listOf("tag")
    val otherTag = listOf("otherTag")
    val exactTagAndOtherTag = listOf("tag", "otherTag")
    val startDt: LocalDateTime = LocalDateTime.of(2020, 12, 31, 12, 12, 0)
    val endDt: LocalDateTime = LocalDateTime.of(2021, 1, 3, 13, 17, 59)
    val oldStartDt: LocalDateTime = LocalDateTime.of(1975, 6, 23, 12, 12, 0)
    val oldEndDt: LocalDateTime = LocalDateTime.of(1975, 6, 25, 12, 12, 0)
    lateinit var form: WfFormEntity
    lateinit var comp: WfComponentEntity
    lateinit var document: WfDocumentEntity

    lateinit var formWithExactTag: WfFormEntity
    lateinit var formWithoutTag: WfFormEntity
    lateinit var formWithOtherTag: WfFormEntity
    lateinit var formWithExactTagAndOtherTag: WfFormEntity

    lateinit var compWithExactTag: WfComponentEntity
    lateinit var compWithoutTag: WfComponentEntity
    lateinit var compWithOtherTag: WfComponentEntity
    lateinit var compWithExactTagAndOtherTag: WfComponentEntity

    lateinit var instanceWithExactTag: WfInstanceEntity
    lateinit var instanceWithoutTag: WfInstanceEntity
    lateinit var instanceWithOtherTag: WfInstanceEntity
    lateinit var instanceWithExactTagAndOtherTag: WfInstanceEntity

    lateinit var tokenWithExactTag: WfTokenEntity
    lateinit var tokenWithoutTag: WfTokenEntity
    lateinit var tokenWithOtherTag: WfTokenEntity
    lateinit var tokenWithExactTagAndOtherTag: WfTokenEntity

    lateinit var process: WfProcessEntity
    lateinit var element: WfElementEntity
    lateinit var numbering: NumberingRuleEntity

    /**
     * ZQL 테스트를 위해서 준비되는 데이터는 아래와 같다.
     *
     * sum() 테스트 - 아래와 같은 인스턴스 개를 준비.
     *  - 종료된 인스턴스이고 태그 값에 숫자가 들어가 있음.
     *  - 진행중 인스턴스이고 태그 값에 숫자가 들어가 있음.
     *  - 종료된 인스턴스이고 태그 값에 문자가 들어가 있음.
     *  - 진행중 인스턴스이고 태그 값에 문자가 들어가 있음.
     *  - 1975-06-23일에 시작된 진행중 인스턴스
     *  - 1975-06-23일에 종료된 인스턴스
     *
     * average() 테스트 - 아래와 같은 인스턴스 개를 준비.
     *
     * percent() 테스트 - 아래와 같은 인스턴스 개를 준비.
     */
    fun init() {
        // document & instance & token
        // 최소 1개의 프로세스가 이미 있다는 가정.
        this.process = wfProcessRepository.findAll()[0]
        this.numbering = numberingRuleRepository.findAll()[0]
        this.element = wfElementRepository.findAll()[0]

        /**
         * count() 테스트 - 아래와 같은 인스턴스 4개를 준비.
         *  - 종료된 인스턴스
         *  - 진행중 인스턴스
         *  - 1975-06-23일에 시작된 진행중 인스턴스
         *  - 1975-06-23일에 종료된 인스턴스
         */
        this.form = createForm(WfFormConstants.FormStatus.USE.value)
        this.comp = createComponent(this.form)
        createTag(tag, compWithExactTag)
        this.document = createDocument(this.form)
        createInstance(document, InstanceStatus.FINISH, startDt, endDt)
        createInstance(document, InstanceStatus.RUNNING, startDt, null)
        createInstance(document, InstanceStatus.FINISH, oldStartDt, oldEndDt)
        createInstance(document, InstanceStatus.RUNNING, oldStartDt, null)

        // // form
        // this.formWithExactTag = createForm(WfFormConstants.FormStatus.USE.value)
        // this.formWithoutTag = createForm(WfFormConstants.FormStatus.USE.value)
        // this.formWithOtherTag = createForm(WfFormConstants.FormStatus.USE.value)
        // this.formWithExactTagAndOtherTag = createForm(WfFormConstants.FormStatus.USE.value)
        //
        // // component
        // this.compWithExactTag = createComponent(formWithExactTag)
        // this.compWithoutTag = createComponent(formWithoutTag)
        // this.compWithOtherTag = createComponent(formWithOtherTag)
        // this.compWithExactTagAndOtherTag = createComponent(formWithExactTagAndOtherTag)
        //
        // // tag
        // createTag(exactTag, compWithExactTag)
        // createTag(exactTagAndOtherTag, compWithExactTagAndOtherTag)
        // createTag(otherTag, compWithOtherTag)
        //
        //
        //
        // this.instanceWithExactTag = createInstance(formWithExactTag)
        // this.instanceWithoutTag = createInstance(formWithoutTag)
        // this.instanceWithOtherTag = createInstance(formWithOtherTag)
        // this.instanceWithExactTagAndOtherTag = createInstance(formWithExactTagAndOtherTag)
        //
        // this.tokenWithExactTag = createToken(instanceWithExactTag)
        // this.tokenWithoutTag = createToken(instanceWithoutTag)
        // this.tokenWithOtherTag = createToken(instanceWithOtherTag)
        // this.tokenWithExactTagAndOtherTag = createToken(instanceWithExactTagAndOtherTag)
    }

    private fun createForm(status: String): WfFormEntity {
        return WfFormEntity(
            formId = "",
            formName = this.formName + LocalDateTime.now(),
            formDesc = "테스트 코드로 등록된 임시 폼 데이터 입니다.",
            formStatus = status,
            createDt = LocalDateTime.now(),
            createUser = this.userKey.let { aliceUserRepository.findAliceUserEntityByUserKey(it) }
        )
    }

    private fun createDocument(form: WfFormEntity): WfDocumentEntity {
        return wfDocumentRepository.save(
            WfDocumentEntity(
                documentId = AliceUtil().getUUID(),
                documentType = "application-form",
                documentName = "Document_Test_" + LocalDateTime.now(),
                documentDesc = "",
                form = form,
                process = this.process,
                createDt = LocalDateTime.now(),
                createUserKey = this.userKey,
                documentStatus = WfDocumentConstants.Status.USE.code,
                apiEnable = true,
                numberingRule = numberingRuleRepository.findAll()[0],
                documentColor = "",
                documentGroup = "",
                documentIcon = ""
            )
        )
    }

    private fun createFinishInstance(document: WfDocumentEntity): WfInstanceEntity {
        return wfInstanceRepository.save(
            WfInstanceEntity(
                instanceId = AliceUtil().getUUID(),
                documentNo = AliceUtil().getUUID(),
                instanceStatus = InstanceStatus.FINISH.code,
                instanceStartDt = LocalDateTime.now(),
                instanceEndDt = LocalDateTime.now(),
                instanceCreateUser = this.userKey.let { aliceUserRepository.findAliceUserEntityByUserKey(it) },
                pTokenId = null,
                document = document,
                instancePlatform = "itsm"
            )
        )
    }

    private fun createInstance(
        document: WfDocumentEntity,
        status: InstanceStatus,
        startDt: LocalDateTime,
        endDt: LocalDateTime?
    ): WfInstanceEntity {
        return wfInstanceRepository.save(
            WfInstanceEntity(
                instanceId = AliceUtil().getUUID(),
                documentNo = AliceUtil().getUUID(),
                instanceStatus = status.code,
                instanceStartDt = startDt,
                instanceEndDt = endDt,
                instanceCreateUser = this.userKey.let { aliceUserRepository.findAliceUserEntityByUserKey(it) },
                pTokenId = null,
                document = document,
                instancePlatform = "itsm"
            )
        )
    }

    private fun createComponent(form: WfFormEntity): WfComponentEntity {
        return wfComponentRepository.save(
            WfComponentEntity(
                componentId = AliceUtil().getUUID(),
                componentType = WfComponentConstants.ComponentTypeCode.DATETIME.code,
                mappingId = "",
                isTopic = false,
                form = form,
                formRow = wfFormRowRepository.save(
                    WfFormRowEntity(
                        formRowId = AliceUtil().getUUID(),
                        formGroup = wfFormGroupRepository.save(
                            WfFormGroupEntity(
                                AliceUtil().getUUID(), this.groupName + LocalDateTime.now(), form
                            )
                        ),
                        rowDisplayOption = ""
                    )
                )
            )
        )
    }

    private fun createToken(instance: WfInstanceEntity): WfTokenEntity {
        // token
        return wfTokenRepository.save(
            WfTokenEntity(
                tokenId = "",
                tokenStatus = WfTokenConstants.Status.RUNNING.code,
                tokenStartDt = LocalDateTime.now(),
                tokenEndDt = LocalDateTime.now(),
                instance = instance,
                element = this.element
            )
        )
    }

    private fun createTag(tags: List<String>, component: WfComponentEntity) {
        tags.forEach { tag ->
            aliceTagRepository.save(
                AliceTagEntity(
                    tagType = AliceTagConstants.TagType.COMPONENT.code,
                    tagValue = tag,
                    targetId = component.componentId
                )
            )
        }
    }
}
