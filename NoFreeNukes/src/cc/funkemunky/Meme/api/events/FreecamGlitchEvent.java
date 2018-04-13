package cc.funkemunky.Meme.api.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FreecamGlitchEvent extends Event implements Cancellable {

    private static HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Player player;
    private Block clickedBlock;

    public FreecamGlitchEvent(Player player, Block clickedBlock) {
        this.player = player;
        this.clickedBlock = clickedBlock;
    }

    public Player getPlayer() {
        return player;
    }

    public Block getClickedBlock() {
        return clickedBlock;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
