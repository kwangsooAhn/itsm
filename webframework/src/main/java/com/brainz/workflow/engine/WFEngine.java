package com.brainz.workflow.engine;

import com.brainz.workflow.process.ProcessManager;

public class WFEngine {

    public static ProcessManager getProcessManager() {

        return new ProcessManager();
    }
}
