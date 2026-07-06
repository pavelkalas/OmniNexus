package main.java.cz.pavelkalas.commands.teleport;

import java.util.Random;
import main.java.cz.pavelkalas.utils.MessageUtils;
import main.java.cz.pavelkalas.utils.MessageUtils.Color;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

/**
 * Příkaz pro teleport na náhodné souřadnice s maximálním rozsahem vzdálenosti v
 * + i - hodnotách
 */
public class PortRandomCommand extends CommandBase {

	private final Random random = new Random();

	@Override
	public String getName() {
		return "portrand";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/portrand [rozsah+-]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayerMP))
			return;

		EntityPlayerMP player = (EntityPlayerMP) sender;

		if (args.length != 1) {
			MessageUtils.sendMessage(player, "Pouziti: /portrand [rozsah+-]", Color.Yellow);
			return;
		}

		int range;

		try {
			range = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			MessageUtils.sendMessage(player, "Range musi byt cislo.", Color.Red);
			return;
		}

		for (int attempt = 0; attempt < 50; attempt++) {

			int x = random.nextInt(range * 2 + 1) - range;
			int z = random.nextInt(range * 2 + 1) - range;

			int y = player.world.getHeight();

			while (y > 1 && player.world.isAirBlock(new BlockPos(x, y - 1, z))) {
				y--;
			}

			if (y <= 1)
				continue;

			BlockPos feet = new BlockPos(x, y, z);
			BlockPos head = feet.up();

			if (!player.world.isAirBlock(feet))
				continue;

			if (!player.world.isAirBlock(head))
				continue;

			player.setPositionAndUpdate(x + 0.5D, y, z + 0.5D);
			return;
		}

		MessageUtils.sendMessage(player, "Nepodarilo se najit bezpecne misto pro teleport.", Color.Red);
	}
}
