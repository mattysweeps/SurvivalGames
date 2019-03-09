package io.github.m0pt0pmatt.survivalgames.command.executor;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import io.github.m0pt0pmatt.survivalgames.game.PlayerRestorer;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.manipulator.mutable.RepresentedPlayerData;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.HashSet;
import java.util.Set;

public class TestCommand extends LeafCommand {

    boolean which = false;
    PlayerRestorer restorer;

    protected TestCommand() {
        super(RootCommand.getInstance(), "test", GenericArguments.none());
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {

            Player player = (Player) src;

            if (which) {
                src.sendMessage(Text.of("restore"));
                restorer.restore(player);
            } else {
                src.sendMessage(Text.of("create"));
                restorer = new PlayerRestorer(player);
            }
            which = !which;
        }

        return CommandResult.success();
    }
}
