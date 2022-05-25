/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.zql.service

import co.brainz.itsm.zql.const.ZqlInstanceDateCriteria
import co.brainz.itsm.zql.const.ZqlPeriodType
import co.brainz.itsm.zql.dto.ZqlCalculatedData
import co.brainz.itsm.zql.dto.ZqlCategorizedData
import co.brainz.itsm.zql.dto.ZqlComponentValue
import co.brainz.itsm.zql.dto.ZqlToken
import co.brainz.itsm.zql.util.ZqlUtil
import co.brainz.workflow.instance.constants.InstanceStatus
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import java.time.LocalDateTime
import org.springframework.stereotype.Service

@Service
class Zql(
    private val instanceRepo: WfInstanceRepository,
    private val tokenRepo: WfTokenRepository,
    private val tokenDataRepo: WfTokenDataRepository
) {
    private var tagList = listOf<String>()
    private var from: LocalDateTime = LocalDateTime.now()
    private var to: LocalDateTime = LocalDateTime.now()
    private var period: ZqlPeriodType = ZqlPeriodType.MONTH
    private var instanceStatus: InstanceStatus = InstanceStatus.FINISH
    private var criteria: ZqlInstanceDateCriteria = ZqlInstanceDateCriteria.END
    private var expression: String =""

    fun setFrom(from: LocalDateTime):Zql {
        this.from = from
        return this
    }

    fun setTo(to: LocalDateTime): Zql {
        this.to = to
        return this
    }

    fun setPeriod(period: ZqlPeriodType): Zql {
        this.period = period
        return this
    }

    fun setInstanceStatus(instanceStatus: InstanceStatus): Zql {
        this.instanceStatus = instanceStatus
        return this
    }

    fun setCriteria(criteria: ZqlInstanceDateCriteria): Zql {
        this.criteria = criteria
        return this
    }

    fun setExpression(expression: String): Zql {
        this.expression = expression
        return this
    }

    /**
     * ZQL 처리를 위한 사전 작업으로 아래와 같은 내용을 수행.
     * <p>
     *   - 태그 파싱
     *        ZQL 표현식에 들어가 있는 태그들을 추출한다.
     *   - 토큰 리스트 가져오기
     *        파싱된 태그와 기간, 상태등의 조건들을 이용해서 태그별 토큰 리스트를 가져온다.
     *        List<ZqlToken> 형태로 반환.
     *   - 카테고리 생성
     *        기간과 간격 정보를 이용해서 카테고리를 만든다.
     *        List<ZqlCategorizedData> 형태로 반환.
     *   - 카테고리별 토큰 매핑
     *        수집된 토큰들을 카테고리별로 넣어준다.
     *
     * 위와 같은 처리가 끝나면 연산을 위해 카테고리화 된 데이터 리스트가 생성되고
     * 실제 연산은 각 연산 방법별로 처리한다.
     *
     * @param
     * @return 카테고리 날짜별로 관련된 토큰이 연결된 List<ZqlCategorizedData>
     */
    private fun init(): List<ZqlCategorizedData> {
        // 태그 파싱
        this.tagList = ZqlUtil.tagParser(this.expression)

        // 토큰 리스트 가져오기
        val tokenList = this.fetch()

        // 카테고리 생성
        val categoryList = ZqlUtil.makeCategory(this.from, this.to, this.period)

        // 카테고리 별 토큰 매핑
        tokenList.forEach { token ->
            categoryList.find { categoryData ->
                categoryData.categorizedDT == ZqlUtil.changeToCategoryDT(token.criteriaDT, this.period)
            }?.zqlTokenList?.add(token)
        }

        return categoryList
    }

    /**
     * 카운트 계산
     * <p>
     * 모아진 데이터를 가지고 카테고리별 토큰의 숫자를 카운트하여 최종 결과를 반환한다.
     *
     * @param
     * @return 카테고리 일시와 카테고리별 토큰 카운트된 데이터의 리스트
     */
    fun count(): List<ZqlCalculatedData> {
        return init().map {
            ZqlCalculatedData(it.categorizedDT, it.zqlTokenList.size.toFloat())
        }
    }

    /**
     * 합산 계산
     * <p>
     * ZQL 표현식을 적용해서 나온 값을 카테고리별로 리스트로 만들어서 반환한다.
     *
     * @param
     * @return 카테고리 일시와 카테고리별 표현식 적용 값의 합산 리스트
     */
    fun sum(): List<ZqlCalculatedData> {
        val calculatedDataList = listOf<ZqlCalculatedData>()

        init().map {
            ZqlCalculatedData(it.categorizedDT, (it.zqlTokenList.fold(0f) { acc, token ->
                val expression = this.replaceExpression(token)
                acc + ZqlUtil.checkExpression(expression) as Float
            }))
        }
        return calculatedDataList
    }

    /**
     * 평균 계산
     * <p>
     * ZQL 표현식을 적용해서 나온 값을 카테고리별로 평균을 계산하여 리스트로 만들어서 반환한다.
     *
     * @param
     * @return 카테고리 일시와 카테고리별 표현식 적용 값의 평균 리스트
     */
    fun average(): List<ZqlCalculatedData> {
        val calculatedDataList = listOf<ZqlCalculatedData>()

        init().map {
            ZqlCalculatedData(
                it.categorizedDT, (it.zqlTokenList.map { token ->
                    val expression = this.replaceExpression(token)
                    ZqlUtil.checkExpression(expression) as Float
                }.average().toFloat())
            )
        }
        return calculatedDataList
    }

    /**
     * 백분율 계산
     * <p>
     * 카테고리별로 ZQL 표현식을 만족하는 토큰의 목록과 전체 목록의 비율을 백분율로 매핑한 리스트를 반환한다.
     *
     * @param
     * @return 카테고리별 일시와 카테고리별 전체 토큰에서 표현식이 참인 토큰의 비율 리스트
     */
    fun percentage(): List<ZqlCalculatedData> {
        val calculatedDataList = listOf<ZqlCalculatedData>()

        init().map {
            ZqlCalculatedData(
                it.categorizedDT, (it.zqlTokenList.filter { token ->
                    val expression = this.replaceExpression(token)
                    ZqlUtil.checkExpression(expression) as Boolean
                }.size.toFloat()
                    / it.zqlTokenList.size) * 100
            )
        }

        return calculatedDataList
    }

    /**
     * ZQL 기본 토큰 데이터 수집 처리.
     * <p>
     * ZQL 처리에서 가장 기본 데이터인 ZqlToken 리스트를 반환한다.
     * ZQL 표현식과 태그, 기간, 상태등의 정보로 토큰 리스트와 태그의 실제 컴포넌트 값들을 구한다.
     *
     * 1. 일단 기간, 문서상태, 기준일자등을 이용하여 해당되는 인스턴스들을 뽑는다.
     * 2. 그 인스턴스들의 마지막 토큰들을 가져온다.
     * 3. 인스턴스별로 토큰에서 해당 태그의 값을 읽어와서 사용한다.
     *
     * @param
     * @return 연산 대상이 되는 ZqlToken 형태의 토큰 리스트
     */
    fun fetch(): List<ZqlToken> {
        val instances = this.getInstanceByZQL()
        val tokens = tokenRepo.getEndTokenList(instances.map { it.instanceId }.toSet())

        return instances.map { instance ->
            val tokenId = tokens.filter { it.instance.instanceId == instance.instanceId }[0].tokenId
            val tagValues = mutableMapOf<String, String>()
            this.getTokenDataByTag(tokenId).forEach {
                tagValues[it.tagValue] = it.componentValue
            }

            ZqlToken(
                tokenId,
                criteriaDT = if (this.criteria == ZqlInstanceDateCriteria.START) {
                    instance.instanceStartDt!!
                } else {
                    instance.instanceEndDt!!
                },
                tagValues = tagValues
            )
        }
    }

    /**
     * ZQL 표현식 처리의 대상이 되는 인스턴스 목록 구하기.
     * <p>
     * 태그가 달린 업무흐름을 찾은 후 해당 업무흐름의 인스턴스를 찾는다.
     * 이때 기간정보와 인스턴스 상태, 기준일자가 시작일인지 종료일지등의 설정을 이용한다.
     *
     * @param
     * @return 찾은 인스턴스 엔티티 목록
     */
    private fun getInstanceByZQL(): Set<WfInstanceEntity> {
        var docIds = mutableSetOf<String>()
        val listOfDocIdsByTag = tagList.map { instanceRepo.getDocumentIdsByTag(it) }
        listOfDocIdsByTag.forEach { docIds.addAll(it) }
        listOfDocIdsByTag.forEach { docIdsByTag ->
            docIds = docIdsByTag.filter { docIds.contains(it) }.toMutableSet()
        }
        return instanceRepo.getInstanceByZQL(docIds, from, to, instanceStatus, criteria)
    }

    /**
     * 태그가 달린 컴포넌트 값 구하기.
     * <p>
     * ZQL 에서 사용되는 태그 리스트를 이용해서 특정 토큰의 태그가 달린 컴포넌트 값을 구해온다.
     *
     * @param tokenId 검색대상이 되는 토큰 아이디
     * @return 태그와 컴포넌트가 쌍으로 있는 ZqlComponentValue 리스트
     */
    private fun getTokenDataByTag(tokenId: String): List<ZqlComponentValue> {
        return tokenDataRepo.getTokenDataByTag(tokenId, this.tagList)
    }

    /**
     * ZQL 표현식에서 토큰을 컴포넌트 값으로 치환하기.
     *
     * @param token 태그와 컴포넌트 값을 가지고 있는 토큰 정보
     * @return 컴포넌트 값으로 치환된 표현식
     */
    private fun replaceExpression(token: ZqlToken): String {
        val replacedExpression = this.expression
        token.tagValues.forEach { tag ->
            replacedExpression.replace(tag.key, tag.value)
        }
        return replacedExpression
    }
}
