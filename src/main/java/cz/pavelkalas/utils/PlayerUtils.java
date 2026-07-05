package main.java.cz.pavelkalas.utils;

import net.minecraft.entity.player.EntityPlayer;
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
}
