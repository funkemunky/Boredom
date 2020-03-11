package cc.funkemunky.test.commands;

import cc.funkemunky.api.commands.ancmd.Command;
import cc.funkemunky.api.commands.ancmd.CommandAdapter;
import cc.funkemunky.api.reflections.Reflections;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.RunUtils;
import cc.funkemunky.test.TestCore;
import dev.brighten.db.utils.MiscUtils;
import dev.brighten.db.utils.security.hash.Hash;
import dev.brighten.db.utils.security.hash.HashType;
import dev.brighten.db.utils.security.hash.impl.SHA2;
import org.bukkit.scheduler.BukkitTask;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

@Init(commands = true)
public class LagCommand {

    public static BukkitTask lagTask;
    public static Random random = new Random();

    @Command(name = "lagserver", description = "Lag the server with a special task.", usage = "/<command>",
            permission = {"test.lagserver"})
    public void onCommand(CommandAdapter cmd) {
        if(lagTask == null) {
            lagTask = RunUtils.taskTimer(() -> {
                AtomicReference<Double> sqrt = new AtomicReference<>((double) 2);
                AtomicInteger times = new AtomicInteger();
                (ThreadLocalRandom.current().nextBoolean() ? IntStream.range(0, 18000).parallel() : IntStream.range(0, 8000))
                        .forEach(i -> {
                    sqrt.set(Math.sqrt(sqrt.get() * i) / 2.);
                    Reflections.getNMSClass("MinecraftServer").getMethod("a", String.class);

                    String hash = get_SHA_512_SecurePassword(UUID.randomUUID().toString(), UUID.randomUUID().toString().substring(0, 5));
                    times.getAndIncrement();
                });
                if(random.nextInt(50) > 43) {
                    try {
                        System.out.println("Sleeping");
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }, TestCore.INSTANCE, 1L, 1L);

            cmd.getSender().sendMessage(Color.Green + "Lagging server!");
        } else {
            lagTask.cancel();
            lagTask = null;
            cmd.getSender().sendMessage(Color.Red + "Stopped the lagging!");
        }
    }


    private static String get_SHA_512_SecurePassword(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
