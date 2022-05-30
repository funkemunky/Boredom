package cc.funkemunky.test.db;

import cc.funkemunky.api.utils.ConfigSetting;
import cc.funkemunky.api.utils.Init;

@Init
public class MongoConfig {

    @ConfigSetting(path = "database",name = "mongoURL")
    public static String mongoDBURL = "";
}
