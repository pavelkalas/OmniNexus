package main.java.cz.pavelkalas.commands.auth;

import main.java.cz.pavelkalas.core.DbContext;
import main.java.cz.pavelkalas.provider.DbUserProvider;
import main.java.cz.pavelkalas.provider.DbUserProvider.User;
import main.java.cz.pavelkalas.utils.MessageUtils;
import main.java.cz.pavelkalas.utils.MessageUtils.Color;
import main.java.cz.pavelkalas.utils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

/**
 * Příkaz pro přihlášení hráče k serveru.
 */
public class ChangePasswordCommand extends CommandBase {

	private final DbUserProvider userDb = DbContext.users;

	@Override
	public String getName() {
		return "changepw";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "changepw";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (args.length > 0) {
				String password = args[0].trim();
				if (DbContext.users.exists(player.getName())) {
					User user = DbContext.users.get(player.getName());
					if (user != null) {
						if (user._isLogged) {
							password = TextUtils.stringToHex(password);
							DbContext.users.changePassword(player.getName(), password);
							MessageUtils.sendMessage(player, "Heslo uspesne zmeneno!", Color.Green);
						} else {
							MessageUtils.sendMessage(player, "Pro zmenu hesla se nejprve musis prihlasit!",
									Color.Yellow);
						}
					}
				} else {
					MessageUtils.sendMessage(player, "Nejsi zaregistrovany! Pouzij /register <heslo>", Color.Red);
				}
			} else {
				MessageUtils.sendMessage(player, "Pouziti: /changepw <nove heslo>", Color.Yellow);
			}
		}
	}
}