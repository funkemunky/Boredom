package cc.funkemunky.test.listeners;

import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.events.Listen;
import cc.funkemunky.test.user.User;
import cc.funkemunky.test.user.Violation;
import dev.brighten.api.listener.KauriFlagEvent;

public class CheatListeners implements AtlasListener {

    @Listen
    public void onCheat(KauriFlagEvent event) {
        User user = User.getUser(event.player.getUniqueId());

        if(!user.violations.containsKey(event.check.getName())) {
            user.violations.put(event.check.getName(), event.check.getVl());
        } else if(user.violations.get(event.check.getName()) < event.check.getVl()) {
            user.violations.put(event.check.getName(), event.check.getVl());
        }
    }
}
