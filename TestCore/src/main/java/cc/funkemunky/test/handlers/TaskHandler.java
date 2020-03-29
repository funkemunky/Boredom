package cc.funkemunky.test.handlers;

import cc.funkemunky.api.utils.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
