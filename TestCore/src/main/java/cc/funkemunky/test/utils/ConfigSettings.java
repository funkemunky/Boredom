package cc.funkemunky.test.utils;

import cc.funkemunky.api.utils.ConfigSetting;
import cc.funkemunky.api.utils.Init;

@Init
public class ConfigSettings {

    @ConfigSetting(name = "kauri-version")
    public static String kauriVersion = "b50";

    @ConfigSetting(path = "scoreboard", name = "enabled")
    public static boolean scoreboardEnabled = true;

    @ConfigSetting(path = "scoreboard", name = "anticheat")
    public static String anticheat = "Kauri";

    @ConfigSetting(path = "bungee", name = "enabled")
    public static boolean bungeeEnabled = true;

    @ConfigSetting(name = "currentServer")
    public static String currentServerip = "dev.funkemunky.cc";

    @ConfigSetting(path = "bungee", name = "backupServer")
    public static String backupServer = "test";

    @ConfigSetting(name = "server-restart")
    public static boolean serverRestart = false;
}
