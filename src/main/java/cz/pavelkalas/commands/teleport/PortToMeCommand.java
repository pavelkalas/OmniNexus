package main.java.cz.pavelkalas.commands.teleport;

import main.java.cz.pavelkalas.utils.MessageUtils;
import main.java.cz.pavelkalas.utils.MessageUtils.Color;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;


/**
 * Příkaz sloužící k teleportaci hráče k sobě (kratší varianta /tp).
 */
public class PortToMeCommand extends CommandBase {

	@Override
	public String getName() {
		return "porttome";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/porttome [jmenohrace]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayerMP)) {
			MessageUtils.sendMessage(sender, "Tento prikaz muze pouzit pouze hrac.", Color.Red);
			return;
		}

		EntityPlayerMP issuer = (EntityPlayerMP) sender;

		if (args.length != 1) {
			MessageUtils.sendMessage(issuer, "Pouziti: /porttome [jmenohrace]", Color.Yellow);
			return;
		}

		EntityPlayerMP target = server.getPlayerList().getPlayerByUsername(args[0]);

		if (target == null) {
			MessageUtils.sendMessage(issuer, "Hrac nebyl nalezen.", Color.Red);
			return;
		}

		target.connection.setPlayerLocation(issuer.posX, issuer.posY, issuer.posZ, issuer.rotationYaw, issuer.rotationPitch);

		MessageUtils.sendMessage(issuer, "Hrac " + target.getName() + " byl teleportovan k tobe.", Color.Green);
		MessageUtils.sendMessage(target, "Byl jsi teleportovan k hraci " + issuer.getName() + ".", Color.Green);
	}
}
