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
import co.brainz.itsm.zql.const.ZqlInstanceDateCriteria
import co.brainz.itsm.zql.const.ZqlPeriodType
import co.brainz.itsm.zql.dto.ZqlToken
import co.brainz.itsm.zql.service.Zql
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
import co.brainz.workflow.token.entity.WfTokenDataEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DisplayName("ZQL Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Transactional
class ZqlTest {
    @Autowired
    lateinit var zql: Zql

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
    private lateinit var tokenDataRepo: WfTokenDataRepository

    @Autowired
    private lateinit var numberingRuleRepository: NumberingRuleRepository

    @Autowired
    private lateinit var wfElementRepository: WfElementRepository

    val userKey = "0509e09412534a6e98f04ca79abb6424"
    val formName = "Test Form_"
    val groupName = "Test Group_"
    val tag = "tag"
    lateinit var form: WfFormEntity
    lateinit var comp: WfComponentEntity
    lateinit var document: WfDocumentEntity
    lateinit var process: WfProcessEntity
    lateinit var element: WfElementEntity
    lateinit var numbering: NumberingRuleEntity

    private fun init() {
        this.zql.setExpression("[tag]")
            .setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
            .setTo(LocalDateTime.of(2021, 12, 31, 21, 59, 59))
            .setPeriod(ZqlPeriodType.MONTH)
            .setInstanceStatus(InstanceStatus.FINISH)
            .setCriteria(ZqlInstanceDateCriteria.END)

        // 최소 1개의 프로세스가 이미 있다는 가정.
        this.process = wfProcessRepository.findAll()[0]
        this.numbering = numberingRuleRepository.findAll()[0]
        this.element = wfElementRepository.findAll()[0]

        this.form = createForm(WfFormConstants.FormStatus.USE.value)
        this.comp = createComponent(this.form)
        createTag(listOf(tag), this.comp)
        this.document = createDocument(this.form)
    }

    @Test
    @DisplayName("ZQL - replaceExpression Test")
    fun replaceExpressionTest() {
        this.init()

        // ZQL 표현식 설정
        val expression = "(([incident_level] == 3)? 10:0) + (([incident_level] == 2)? 20:0) + (([incident_level] == 1)? 10:0)"
        zql.setExpression(expression)

        // 추출된 토큰별 태그 역할을 하는 ZqlToken 생성.
        val token = ZqlToken(
            tokenId = "123",
            criteriaDT = LocalDateTime.now(),
            tagValues = mapOf("incident_level" to "1")
        )

        // 문서에 필요한 태그 달기.
        createTag(listOf("incident_level"), this.comp)

        val method = zql::class.java.getDeclaredMethod("replaceExpression", ZqlToken::class.java)
        method.trySetAccessible()

        val result = method.invoke(zql, token)
        val expected = "((1 == 3)? 10:0) + ((1 == 2)? 20:0) + ((1 == 1)? 10:0)"
        println("expected : $expected\n result : $result")
        Assertions.assertEquals(expected, result)
    }

    @Test
    @DisplayName("ZQL - getTokenDataByTag Test")
    fun getTokenDataByTagTest() {
        this.init()

        val startDt: LocalDateTime = LocalDateTime.of(2020, 12, 31, 12, 12, 0)
        val endDt: LocalDateTime = LocalDateTime.of(2021, 1, 3, 13, 17, 59)

        // 문서에 필요한 태그 달기
        createTag(listOf("incident_level1"), this.comp)
        createTag(listOf("incident_level2"), this.comp)

        // 토큰 및 컴포넌트 값 생성. (2020-12-31~2021-01-03)
        val token1 = createToken(document, InstanceStatus.FINISH, startDt, endDt)
        createTokenData(token1, this.comp, "1")
        val token2 = createToken(document, InstanceStatus.FINISH, startDt, endDt)
        createTokenData(token2, this.comp, "2")

        // 2개의 태그를 사용하는 ZQL 표현식 설정
        val expression = "(([incident_level1] == 3)? 10:0) + (([incident_level2] == 2)? 20:0) + (([incident_level1] == 1)? 10:0)"
        zql.setExpression(expression)

        // 위에서 생성된 2개의 토큰 검사
        val method = zql::class.java.getDeclaredMethod("getTokenDataByTag", String::class.java)
        method.trySetAccessible()

        var result = method.invoke(zql, token1.tokenId)
        var expected = "[ZqlComponentValue(tagValue=incident_level1, componentValue=1), ZqlComponentValue(tagValue=incident_level2, componentValue=1)]"
        println("expected : $expected")
        println("result : $result")
        Assertions.assertEquals(expected, result.toString())

        result = method.invoke(zql, token2.tokenId)
        expected = "[ZqlComponentValue(tagValue=incident_level1, componentValue=2), ZqlComponentValue(tagValue=incident_level2, componentValue=2)]"
        println("expected : $expected")
        println("result : $result")
        Assertions.assertEquals(expected, result.toString())
    }

    /**
     * ZQL 처리를 위한 대상 문서(인스턴스) 목록 구하기 테스트.
     *
     * 기간정보, 대상 인스턴스 상태, 기준일자 타입등을 기준으로 문서를 뽑아오는지 확인.
     */
    @Test
    @DisplayName("ZQL get Instance By ZQL Test")
    @Order(300)
    fun getInstanceByZQLTest() {
        this.init()

        val startDt: LocalDateTime = LocalDateTime.of(2020, 12, 31, 12, 12, 0)
        val endDt: LocalDateTime = LocalDateTime.of(2021, 1, 3, 13, 17, 59)
        val oldStartDt: LocalDateTime = LocalDateTime.of(1975, 6, 23, 12, 12, 0)
        val oldEndDt: LocalDateTime = LocalDateTime.of(1975, 6, 25, 12, 12, 0)

        // 정상적인 범위의 종료문서 1개 생성.
        createTag(listOf("incident_level"), this.comp)
        val token = createToken(document, InstanceStatus.FINISH, startDt, endDt)

        // 해당 태그와 정상적인 범위와 조건으로 검색
        this.zql.setExpression("[incident_level] = 1")
            .setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
            .setTo(LocalDateTime.of(2021, 12, 31, 21, 59, 59))
            .setPeriod(ZqlPeriodType.MONTH)
            .setInstanceStatus(InstanceStatus.FINISH)
            .setCriteria(ZqlInstanceDateCriteria.END)

        val method = zql::class.java.getDeclaredMethod("getInstanceByZQL")
        method.trySetAccessible()
        val result = method.invoke(zql)
        println("expected count : 1")
        println("expected count : " + setOf(result).size)
        println(result)
        Assertions.assertEquals(setOf(result).size, 1)
    }

    /**
     * 카운트 연산방식 계산 테스트.
     * <p>
     * 조건에 부합되는
     */
    @Test
    @DisplayName("ZQL Count Test")
    @Order(100)
    fun countTest() {
        this.init()
        val startDt: LocalDateTime = LocalDateTime.of(2020, 12, 31, 12, 12, 0)
        val endDt: LocalDateTime = LocalDateTime.of(2021, 1, 3, 13, 17, 59)

        // 2020-12-31 ~ 2021-01-03 사이에 진행된 종료문서 1개 생성.
        createTag(listOf("incident_level"), this.comp)
        val token = createToken(document, InstanceStatus.FINISH, startDt, endDt)

        // 해당 태그를 사용한 표현식, 2020~2021년 2년간 월별로 종료일 기준의 완료된 문서 카운트 검색
        val result = this.zql.setExpression("[incident_level] = 1")
            .setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
            .setTo(LocalDateTime.of(2021, 12, 31, 21, 59, 59))
            .setPeriod(ZqlPeriodType.MONTH)
            .setInstanceStatus(InstanceStatus.FINISH)
            .setCriteria(ZqlInstanceDateCriteria.END)
            .count()

        // 종료일 기준이기 때문에 문서의 종료일인 2021-01-03을 이용해서 2021년 1월에 1건이 카운트 되어야 한다.
        println(result)
    }

    @Test
    @DisplayName("ZQL Sum Test")
    @Order(200)
    fun sumTest() {
        this.init()
        val startDt: LocalDateTime = LocalDateTime.of(2020, 12, 31, 12, 12, 0)
        val endDt: LocalDateTime = LocalDateTime.of(2021, 1, 3, 13, 17, 59)

        // 2020-12-31 ~ 2021-01-03 사이에 진행된 종료문서 1개 생성.
        createTag(listOf("incident_level"), this.comp)
        val token = createToken(document, InstanceStatus.FINISH, startDt, endDt)

        // 합산을 위해서 사용할 컴포넌트의 값을 1로 설정
        createTokenData(token, this.comp, "1")

        // 해당 태그를 사용한 표현식, 2020~2021년 2년간 월별로 종료일 기준의 완료된 문서 카운트 검색
        val result = this.zql.setExpression("([incident_level] == 1)? 10:0")
            .setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
            .setTo(LocalDateTime.of(2021, 12, 31, 21, 59, 59))
            .setPeriod(ZqlPeriodType.MONTH)
            .setInstanceStatus(InstanceStatus.FINISH)
            .setCriteria(ZqlInstanceDateCriteria.END)
            .sum()

        // 종료일 기준이기 때문에 문서의 종료일인 2021-01-03을 이용해서 ZQL 표현식을 적용하면 해당 월의 값으로 10 예상.
        println(result)
    }

    @Test
    @DisplayName("ZQL Average Test")
    @Order(300)
    fun averageTest() {
        this.init()
        val startDt: LocalDateTime = LocalDateTime.of(2020, 12, 31, 12, 12, 0)
        val endDt: LocalDateTime = LocalDateTime.of(2021, 1, 3, 13, 17, 59)

        // 2020-12-31 ~ 2021-01-03 사이에 진행된 종료문서 2개 생성.
        createTag(listOf("incident_level"), this.comp)
        val token1 = createToken(document, InstanceStatus.FINISH, startDt.minusDays(1), endDt.minusDays(1))
        val token2 = createToken(document, InstanceStatus.FINISH, startDt, endDt)

        // 평균을 위해서 사용할 컴포넌트의 값을 각각 10,20으로 설정
        createTokenData(token1, this.comp, "10")
        createTokenData(token2, this.comp, "20")

        // 해당 태그를 사용한 표현식, 2020~2021년 2년간 월별로 종료일 기준의 완료된 문서 카운트 검색
        val result = this.zql.setExpression("[incident_level]")
            .setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
            .setTo(LocalDateTime.of(2021, 12, 31, 21, 59, 59))
            .setPeriod(ZqlPeriodType.MONTH)
            .setInstanceStatus(InstanceStatus.FINISH)
            .setCriteria(ZqlInstanceDateCriteria.END)
            .average()

        // 종료일 기준이기 때문에 문서의 종료일인 2021-01-03을 이용해서 ZQL 표현식을 적용하면 해당 월의 값으로 15 예상.
        println(result)
    }

    @Test
    @DisplayName("ZQL Percentage Test")
    @Order(400)
    fun percentageTest() {
    }

    private fun createForm(status: String): WfFormEntity {
        return wfFormRepository.save(
            WfFormEntity(
                formId = "",
                formName = this.formName + LocalDateTime.now(),
                formDesc = "테스트 코드로 등록된 임시 폼 데이터 입니다.",
                formStatus = status,
                createDt = LocalDateTime.now(),
                createUser = this.userKey.let { aliceUserRepository.findAliceUserEntityByUserKey(it) }
            )
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
                numberingRule = this.numbering,
                documentColor = "",
                documentGroup = "",
                documentIcon = ""
            )
        )
    }

    private fun createToken(
        document: WfDocumentEntity,
        status: InstanceStatus,
        startDt: LocalDateTime,
        endDt: LocalDateTime?
    ): WfTokenEntity {
        val user = userKey.let { aliceUserRepository.findAliceUserEntityByUserKey(it) }
        val instance = wfInstanceRepository.save(
            WfInstanceEntity(
                instanceId = AliceUtil().getUUID(),
                documentNo = AliceUtil().getUUID(),
                instanceStatus = status.code,
                instanceStartDt = startDt,
                instanceEndDt = endDt,
                instanceCreateUser = user,
                pTokenId = null,
                document = document,
                instancePlatform = "itsm"
            )
        )

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

    private fun createTokenData(token: WfTokenEntity, component: WfComponentEntity, value: String) {
        tokenDataRepo.save(
            WfTokenDataEntity(
                token = token,
                component = component,
                value = value
            )
        )
    }
}
