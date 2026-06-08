package org.example.service;

import org.example.frames.TasksFrame;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ResetService{
    private final int hourReset;
    private final int minReset;
    private final TaskService taskService = TaskService.getInstance();
    private final PersistService persistService = PersistService.getInstance();
    private final TasksFrame tasksFrame;

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    public ResetService(int hourReset, int minReset, TasksFrame tasksFrame) {
        this.hourReset = hourReset;
        this.minReset = minReset;
        this.tasksFrame = tasksFrame;
    }

    public void init() {
        verifyReset();
        scheduler.scheduleAtFixedRate(this::verifyReset, 1, 1, TimeUnit.MINUTES);
    }

    private void verifyReset() {
        LocalDateTime lastReset =  LocalDateTime.parse(persistService.getLastResetDate());

        LocalDateTime actualTime = LocalDateTime.now();

        boolean itsTimeToReset = actualTime.getHour() >= hourReset && actualTime.getMinute() >= minReset;

        long daysBetween = 0;
        daysBetween = ChronoUnit.DAYS.between(lastReset, actualTime);

        boolean hasResetToday = daysBetween == 0;

        if (itsTimeToReset && !hasResetToday) {
            reset();
            tasksFrame.refreshList();
            persistService.saveLastResetDate(actualTime
                            .withHour(hourReset)
                            .withMinute(minReset)
                            .toString());
        }
    }

    private void reset() {
        taskService.resetTaskStatus();
    }
}
