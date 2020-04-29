package co.brainz.workflow.engine.process.service.simulation.element.impl

import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.process.service.simulation.element.WfProcessSimulationElement

/**
 * * 시뮬레이션 검증 - gateway
 *
 *  게이트웨이의 분기조건들은 서로 중첩되지 않아야 한다.
 * 게이트웨이에서 나가는 조건으로 map_id를 이용하는 경우 해당 map_id가 문서에 있는지 체크한다.
 * 게이트웨이에서 나가는 조건으로 map_id를 이용한다면 들어오는 화살표에 action 필드가 있어야 한다.
 * 게이트웨이에서 나가는 조건중에 action을 이용하는 경우 해당 action이 있는지 체크한다.
 * 게이트웨이는 입출력이 모두 1개 이상씩 존재 해야 하며, 입력이 1개거나 출력이 1개여야 한다
 *
 */
class WfProcessSimulationGateway : WfProcessSimulationElement() {
    override fun validate(element: WfElementEntity): Boolean {

        return false
    }

}
