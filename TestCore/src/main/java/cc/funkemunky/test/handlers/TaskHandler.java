package cc.funkemunky.test.handlers;

import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.Instance;
import cc.funkemunky.api.utils.RunUtils;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Init
public class TaskHandler {

    @Instance
    public static TaskHandler INSTANCE;

    private List<Task> tasks = new CopyOnWriteArrayList<>();

    public TaskHandler() {
        RunUtils.taskTimer(() -> {
            tasks.parallelStream().forEach(task -> {
                if(--task.ticks <= 0) {
                    task.runnable.run();
                    tasks.remove(task);
                } else task.onCountdown.accept(task.ticks);
            });
        }, 0L, 1L);
    }

    public void addTask(int ticks, Consumer<Integer> onCountdown, Runnable task) {
        tasks.add(new Task(task, onCountdown, ticks));
    }

    @AllArgsConstructor
    public class Task {
        public Runnable runnable;
        public Consumer<Integer> onCountdown;
        public int ticks;
    }

    private static long ticksToMillis(int ticks) {
        return ticks * 50L;
    }
}
