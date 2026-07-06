package main.java.cz.pavelkalas.commands.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import main.java.cz.pavelkalas.core.DbContext;
import main.java.cz.pavelkalas.models.User;
import main.java.cz.pavelkalas.provider.DbUserProvider;
import main.java.cz.pavelkalas.utils.MessageUtils;
import main.java.cz.pavelkalas.utils.MessageUtils.Color;
import main.java.cz.pavelkalas.utils.PlayerUtils;
import main.java.cz.pavelkalas.utils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

/**
 * Příkaz pro přihlášení hráče k serveru.
 */
public class LoginCommand extends CommandBase {

	/**
	 * Instance databáze uživatelů.
	 */
	private final DbUserProvider userDb = DbContext.users;

	/**
	 * Mapa pokusů o přihlášení.
	 */
	private final Map<UUID, Integer> loginAttempts = new HashMap<>();

	/**
	 * Definice maximálních pokusů o přihlášení před vyhozením.
	 */
	private final int MAX_LOGIN_ATTEMPTS = 3;

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

		if (user != null && user._isLogged) {
			MessageUtils.sendMessage(player, "Jsi jiz prihlasen!", Color.Yellow);
			return;
		}

		if (user != null) {
			if (user.passwordMatches(password)) {
				user._isLogged = true;
				MessageUtils.sendMessage(player, "Uspesne prihlasen!", Color.Green);
				if (loginAttempts.containsKey(player.getUniqueID())) {
					loginAttempts.remove(player.getUniqueID());
				}
			} else {
				MessageUtils.sendMessage(player, "Spatne heslo!", Color.Red);
				if (loginAttempts.containsKey(player.getUniqueID())) {
					int value = loginAttempts.get(player.getUniqueID()) - 1;

					if (value <= 1) {
						PlayerUtils.kickPlayer(player, "Prilis mnoho pokusu o prihlaseni!");
						loginAttempts.remove(player.getUniqueID());
						return;
					}

					loginAttempts.put(player.getUniqueID(), value);
				} else {
					loginAttempts.put(player.getUniqueID(), MAX_LOGIN_ATTEMPTS);
				}
			}
		} else {
			MessageUtils.sendMessage(player, "Nejsi zaregistrovan! Pouzij /register [heslo]", Color.Yellow);
		}
	}
}
