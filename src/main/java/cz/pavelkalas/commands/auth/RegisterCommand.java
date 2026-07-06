package main.java.cz.pavelkalas.commands.auth;

import main.java.cz.pavelkalas.core.DbContext;
import main.java.cz.pavelkalas.models.User;
import main.java.cz.pavelkalas.provider.DbUserProvider;
import main.java.cz.pavelkalas.utils.MessageUtils;
import main.java.cz.pavelkalas.utils.TextUtils;
import main.java.cz.pavelkalas.utils.MessageUtils.Color;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

/**
 * Příkaz pro přihlášení hráče k serveru.
 */
public class RegisterCommand extends CommandBase {

	/**
	 * Instance databáze uživatelů.
	 */
	private final DbUserProvider userDb = DbContext.users;

	@Override
	public String getName() {
		return "register";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "register";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (args.length > 0) {
				String password = args[0].trim();
				if (password.length() >= 6 && password.length() < 128) {
					register(player, password);
				} else {
					MessageUtils.sendMessage(player, "Heslo musi byt dlouhe alespon 6 znaku a kratsi nez 128 znaku!", Color.Yellow);
				}
			} else {
				MessageUtils.sendMessage(player, "Nezadal jsi heslo.", Color.Red);
			}
		}
	}

	/**
	 * Po validní registraci hráče.
	 * 
	 * @param player   - Hráč
	 * @param password - Heslo
	 */
	private void register(EntityPlayer player, String password) {
		if (userDb.exists(player.getName())) {
			MessageUtils.sendMessage(player, "Jsi jiz zaregistrovany! Prosim pouzij /login <heslo>", Color.Red);
			return;
		}

		password = TextUtils.stringToHex(password);

		if (userDb.add(new User(player.getName(), password))) {
			MessageUtils.sendMessage(player, "Byl/a jsi uspesne registrovan/a!", Color.Green);
			User user = userDb.get(player.getName());
			if (user != null) {
				user._isLogged = true;
			}
		} else {
			MessageUtils.sendMessage(player, "Nelze te zaregistrovat!..", Color.Red);
		}
	}
}