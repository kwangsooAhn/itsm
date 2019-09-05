package co.brainz.workflow.engine;

import co.brainz.workflow.process.ProcessManager;

public class WFEngine {

    public static ProcessManager getProcessManager() {

        return new ProcessManager();
    }
}
