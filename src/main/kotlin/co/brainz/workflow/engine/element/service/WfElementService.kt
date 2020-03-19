package co.brainz.workflow.engine.element.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.engine.component.repository.WfComponentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.token.dto.WfTokenDto
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WfElementService(
    private val wfElementRepository: WfElementRepository,
    private val wfComponentRepository: WfComponentRepository,
    private val wfTokenDataRepository: WfTokenDataRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * get element by ProcessId, ElementType
     *
     * @param processId
     * @return ElementEntity
     */
    fun getStartElement(processId: String): WfElementEntity {
        // TODO
        // start element는 몇 종류가 있는데 1개의 프로세스에 commonStart는 1개만 존재하지만 다른 종류의 start는 복수개가 존재가능.
        // 이에 대한 처리를 위해 getStartElement에 파라미터가 추가될 필요가 있다.
        return wfElementRepository.findByProcessIdAndElementType(
            processId,
            WfElementConstants.ElementType.COMMON_START_EVENT.value
        )
    }

    /**
     * 다음 엘리먼트 정보 가져오기
     *
     * @param elementId 종료된 토큰의 엘리먼트 아이디
     * @param wfTokenDto  종료된 토큰
     * @return WfElementEntity 조회된 엘리먼트 리턴
     */
    fun getNextElement(elementId: String, wfTokenDto: WfTokenDto): WfElementEntity {
        lateinit var nextElement: WfElementEntity
        val arrowConnectors = wfElementRepository.findAllArrowConnectorElement(elementId)

        // 게이트웨이인 경우 connector의 condition으로 다음 element를 선택한다.
        if (arrowConnectors.size > 1 && arrowConnectors[0].elementType == WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value) {
            run main@{
                arrowConnectors.forEach connector@{ arrowConnector ->
                    arrowConnector.elementDataEntities.forEach {
                        // 엘리먼트 세부 설정 속성값이 분기 조건 데이터일 때
                        if (it.attributeId == WfElementConstants.AttributeId.CONDITION.value) {
                            val regexGeneral = "\\x22[^\\x22]+\\x22".toRegex() // "" 따옴표 값을 모두 찾아보자.
                            val regexComponentMappingId = "\\x24\\x7b[^\\x22\\x24\\x7b\\x7d]+\\x7d".toRegex()//${value}
                            val regexConstant = "\\x23\\x7b[^\\x22\\x24\\x7b\\x7d]+\\x7d".toRegex() // #{value}

                            val getDoubleQuotationValue = regexGeneral.findAll(it.attributeValue)

                            val compareValues = mutableListOf<String>()

                            var symbol = it.attributeValue
                            getDoubleQuotationValue.forEach { doubleQuotation ->
                                // 부등호만 가져온다.
                                symbol = symbol.replace(doubleQuotation.value, "")

                                // 분기 조건 값에서 따옴표(")를 제거한다.
                                val mappingId = doubleQuotation.value.replace("\"", "")

                                // 값(${value}, #{value}, 일반)에 따라 비교할 데이터를 조회
                                val value = when {
                                    mappingId.matches(regexComponentMappingId) -> { // ${value} 값
                                        val tokenId = wfTokenDto.tokenId
                                        val tokenDatas = wfTokenDataRepository.findTokenDataEntityByTokenId(tokenId)
                                        val componentIds = tokenDatas.map { tokenData ->
                                            tokenData.componentId
                                        }
                                        val instanceId = tokenDatas[0].instanceId
                                        val wfComponentEntity =
                                            wfComponentRepository.findByComponentIdInAndMappingId(
                                                componentIds,
                                                mappingId
                                            )
                                        val componentId = wfComponentEntity.componentId

                                        val tokenData = wfTokenDataRepository.findByInstanceIdAndTokenIdAndComponentId(
                                            instanceId,
                                            tokenId,
                                            componentId
                                        )
                                        tokenData.value
                                    }

                                    mappingId.matches(regexConstant) -> { // #{value} 값
                                        wfTokenDto.assigneeId as String
                                    }
                                    else -> mappingId // 일반 값
                                }

                                compareValues.add(value)
                            }

                            if (compareValues.size != 2) {
                                throw AliceException(
                                    AliceErrorConstants.ERR,
                                    "There are two values for comparing gateway branch conditions."
                                )
                            }

                            // TODO 2020-03-19 kbh - 부등호(symbol)에 따라 기능 추가 해야함. 현재는 동일 조건(== 만 동작한다.
                            symbol = symbol.trim()

                            // 최종 값을 비교하고
                            if (compareValues[0] == compareValues[1]) {
                                nextElement = arrowConnector
                                return@main
                            } else {
                                return@connector
                            }
                        }
                    }
                }
            }

        } else { // 게이트웨이 외 일반적인 경우
            nextElement = wfElementRepository.findTargetElement(arrowConnectors[0].elementId)
        }
        return nextElement
    }
}
