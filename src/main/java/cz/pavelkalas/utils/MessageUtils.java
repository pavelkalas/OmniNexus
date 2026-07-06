package main.java.cz.pavelkalas.utils;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class MessageUtils {

	/**
	 * Definice barev
	 */
	public enum Color {
		Red, Green, Blue, Yellow, White
	}

	/**
	 * Pošle jednoduchou zprávu hráči
	 * 
	 * @param player  - Instance hráče
	 * @param message - Zpráva
	 */
	public static void sendMessage(EntityPlayer player, String message) {
		player.sendMessage(new TextComponentString(message));
	}

	/**
	 * Pošle jednoduchou zprávu hráči
	 * 
	 * @param player  - Instance hráče
	 * @param message - Zpráva
	 * @param color   - Barva
	 */
	public static void sendMessage(EntityPlayer player, String message, Color color) {
		TextComponentString msg = new TextComponentString(message);
		TextFormatting col = getColor(color);
		msg.getStyle().setColor(col);
		player.sendMessage(msg);
	}

	/**
	 * Pošle jednoduchou zprávu hráči
	 * 
	 * @param sender  - Instance hráče ale v ICommandSenderu
	 * @param message - Zpráva
	 * @param color   - Barva
	 */
	public static void sendMessage(ICommandSender sender, String message, Color color) {
		EntityPlayer player = null;
		if (sender instanceof EntityPlayerMP || sender instanceof EntityPlayer) {
			player = (EntityPlayer) sender;
		}

		if (player != null) {
			TextComponentString msg = new TextComponentString(message);
			TextFormatting col = getColor(color);
			msg.getStyle().setColor(col);
			player.sendMessage(msg);
		}
	}

	/**
	 * Získá barvu
	 * 
	 * ZKRÁCENÁ VERZE PRO "TextFormatting", použijem jen "Color"
	 * 
	 * @param color - Barva
	 * @return Navrací barvu ve formátu TextFormatting
	 */
	public static TextFormatting getColor(Color color) {
		if (color == Color.Red) {
			return TextFormatting.RED;
		} else if (color == Color.Green) {
			return TextFormatting.GREEN;
		} else if (color == Color.Blue) {
			return TextFormatting.BLUE;
		} else if (color == Color.Yellow) {
			return TextFormatting.YELLOW;
		} else if (color == Color.White) {
			return TextFormatting.WHITE;
		}

		return TextFormatting.WHITE;
	}
}
