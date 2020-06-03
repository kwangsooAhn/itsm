package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.service.WfTokenManagerService

class WfCommonStartEventTokenManager(
    wfTokenManagerService: WfTokenManagerService
) : WfTokenManager(wfTokenManagerService) {

}
