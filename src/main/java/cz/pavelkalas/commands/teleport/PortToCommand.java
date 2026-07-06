package main.java.cz.pavelkalas.commands.teleport;

import main.java.cz.pavelkalas.utils.MessageUtils;
import main.java.cz.pavelkalas.utils.MessageUtils.Color;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

/**
 * Příkaz sloužící k teleportaci k hráči (kratší varianta /tp).
 */
public class PortToCommand extends CommandBase {

	@Override
	public String getName() {
		return "portto";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/portto [jmenohrace]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayerMP)) {
			MessageUtils.sendMessage(sender, "Tento prikaz muze pouzit pouze hrac.", Color.Red);
			return;
		}

		EntityPlayerMP issuer = (EntityPlayerMP) sender;

		if (args.length != 1) {
			MessageUtils.sendMessage(issuer, "Pouziti: /portto [jmenohrace]", Color.Yellow);
			return;
		}

		EntityPlayerMP target = server.getPlayerList().getPlayerByUsername(args[0]);

		if (target == null) {
			MessageUtils.sendMessage(issuer, "Hrac nebyl nalezen.", Color.Red);
			return;
		}

		issuer.connection.setPlayerLocation(target.posX, target.posY, target.posZ, target.rotationYaw, target.rotationPitch);

		MessageUtils.sendMessage(issuer, "Teleport k hraci " + target.getName() + " probehl.", Color.Green);
	}
}
