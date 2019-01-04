package cc.funkemunky.kit.commands.kit;

import cc.funkemunky.api.commands.FunkeCommand;
import cc.funkemunky.kit.Kit;
import cc.funkemunky.kit.commands.kit.args.SetSpawnArgs;
import cc.funkemunky.kit.commands.kit.args.SpawnArgs;

public class KitCommand extends FunkeCommand {
    public KitCommand() {
        super(Kit.getInstance(), "kit", "Gives you a kit when in spawn.", "kit.command.kit", "Gives you a kit when in spawn.");

        setPlayerOnly(true);
    }

    @Override
    protected void addArguments() {
        getArguments().add(new SetSpawnArgs());
        getArguments().add(new SpawnArgs());
    }
}
