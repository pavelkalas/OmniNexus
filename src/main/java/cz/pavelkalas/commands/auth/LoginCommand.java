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
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

/**
 * Příkaz pro přihlášení hráče k serveru.
 */
public class LoginCommand extends CommandBase {

	private final DbUserProvider userDb = DbContext.users;

	@Override
	public String getName() {
		return "login";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "login";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (args.length > 0) {
				String password = args[0].trim();
				logIn(player, password);
			} else {
				MessageUtils.sendMessage(player, "Nezadal jsi heslo.", Color.Red);
			}
		}
	}

	/**
	 * Po validním přihlášení hráče.
	 * 
	 * @param player   - Hráč
	 * @param password - Heslo
	 */
	private void logIn(EntityPlayer player, String password) {
		if (!userDb.exists(player.getName())) {
			MessageUtils.sendMessage(player, "Nejsi zaregistrovany! Prosim pouzij /register <heslo>", Color.Red);
			return;
		}

		password = TextUtils.stringToHex(password);

		User user = userDb.get(player.getName());
		if (user != null) {
			if (user.passwordMatches(password)) {
				MessageUtils.sendMessage(player, "Uspesne prihlasen!", Color.Green);
				user._isLogged = true;
			} else {
				MessageUtils.sendMessage(player, "Spatne heslo!", Color.Red);
			}
		}
	}
}