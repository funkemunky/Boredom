package cc.funkemunky.test.listeners;

import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.events.Listen;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.test.user.User;
import cc.funkemunky.test.user.Violation;
import dev.brighten.api.listener.KauriFlagEvent;

@Init
public class CheatListeners implements AtlasListener {

    @Listen
    public void onCheat(KauriFlagEvent event) {
        User user = User.getUser(event.player.getUniqueId());

        user.vls.add(new Violation(event.check.getName(), event.check.getVl()));
    }
}
