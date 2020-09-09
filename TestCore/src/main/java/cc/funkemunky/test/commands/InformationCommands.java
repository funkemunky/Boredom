package cc.funkemunky.test.commands;

import cc.funkemunky.api.commands.ancmd.Command;
import cc.funkemunky.api.commands.ancmd.CommandAdapter;
import cc.funkemunky.api.utils.ConfigSetting;
import cc.funkemunky.api.utils.Init;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Init(commands = true)
public class InformationCommands {

    @Command(name = "discord", description = "Get the discord link", usage = "/<command>")
    public void onCommand(CommandAdapter cmd) {
        TextComponent discord = new TextComponent("Join: "), link = new TextComponent("https://discord.me/Brighten");

        discord.setColor(ChatColor.GRAY);
        link.setColor(ChatColor.WHITE);
        link.setItalic(true);

        if(cmd.getSender() instanceof Player) {
            cmd.getPlayer().sendMessage(discord, link);
        } else cmd.getSender().sendMessage(discord.getColor() + discord.getText()
                + link.getColor() + link.getText());
    }

    @ConfigSetting(name = "testIps")
    public static List<String> testServers = Arrays.asList("stable|us.kauri.ac|US", "stable|eu.kauri.ac|EU",
            "dev|dev.funkemunky.cc|US");

    @Command(name = "test", description = "View test server IPs.", usage = "/<command>",
            aliases = {"ips", "testips", "tests", "testservers"}, playerOnly = true)
    public void onTestCommand(CommandAdapter cmd) {
        List<TextComponent[]> stableIps = new ArrayList<>(), devIps = new ArrayList<>();

        testServers.forEach(string -> {
            String[] split = string.split("\\|");

            TextComponent proxy = new TextComponent(split[2] + ":");
            proxy.setColor(ChatColor.GRAY);
            TextComponent ip = new TextComponent(split[1]);
            ip.setColor(ChatColor.WHITE);
            ip.setColor(ChatColor.ITALIC);
            ip.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, split[1]));
            if(split[0].equals("stable")) {
                stableIps.add(new TextComponent[] {proxy, ip});
            } else devIps.add(new TextComponent[] {});
        });

        TextComponent stable = new TextComponent("Stable:"), dev = new TextComponent("Development:");
        stable.setColor(ChatColor.GOLD);
        dev.setColor(ChatColor.GOLD);

        cmd.getPlayer().sendMessage(stable);
        for (TextComponent[] stableIp : stableIps) {
            cmd.getPlayer().sendMessage(stableIp);
        }

        cmd.getPlayer().sendMessage("");
        cmd.getPlayer().sendMessage(dev);

        for (TextComponent[] devIp : devIps) {
            cmd.getPlayer().sendMessage(devIp);
        }
    }
}
