package com.qsolutions.gpsclient.ui.scheduler;

import com.qsolutions.gpsclient.model.Unidad;
import com.qsolutions.gpsclient.ui.view.EventLogView;
import java.util.HashMap;
import java.util.Map;

public class UnitSchedulerRegistry {

    private static final UnitSchedulerRegistry INSTANCE = new UnitSchedulerRegistry();
    private final Map<String, UnitScheduler> schedulers = new HashMap<>();

    private UnitSchedulerRegistry() {}

    public static UnitSchedulerRegistry getInstance() {
        return INSTANCE;
    }

    public UnitScheduler getOrCreate(Unidad unidad, EventLogView eventLog) {
        return schedulers.computeIfAbsent(
            unidad.getNumUnidad(),
            k -> new UnitScheduler(unidad, eventLog)
        );
    }

    public UnitScheduler get(String numUnidad) {
        return schedulers.get(numUnidad);
    }

    public void stopAll() {
        schedulers.values().forEach(s -> {
            if (s.getState() == UnitScheduler.State.RUNNING) s.stop();
        });
    }
}
