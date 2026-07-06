package main.java.cz.pavelkalas.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class PlayerUtils {

	/**
	 * Kontroluje, zda je hráč stále online.
	 * 
	 * @param player - Instance hráče
	 * @return - TRUE v případě, že je hráč stále online.
	 */
	public static boolean isConnected(EntityPlayer player) {
		MinecraftServer server = player.getServer();
		return server != null && server.getPlayerList().getPlayerByUUID(player.getUniqueID()) != null;
	}

	/**
	 * Vyhodí hráče ze serveru se zprávou.
	 * 
	 * @param player  - Instance hráče
	 * @param message - Zpráva hráči
	 */
	public static void kickPlayer(EntityPlayer player, String message) {
		if (player == null || message == null) {
			throw new IllegalArgumentException("Player or message cannot be null.");
		}

		((EntityPlayerMP) player).connection.disconnect(new net.minecraft.util.text.TextComponentString(message));
	}
}
