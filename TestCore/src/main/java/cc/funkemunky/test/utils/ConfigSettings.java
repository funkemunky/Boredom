package cc.funkemunky.test.utils;

import cc.funkemunky.api.utils.ConfigSetting;
import cc.funkemunky.api.utils.Init;

@Init
public class ConfigSettings {

    @ConfigSetting(name = "kauri-version")
    public static String kauriVersion = "b50";
}
