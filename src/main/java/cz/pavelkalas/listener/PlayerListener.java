package main.java.cz.pavelkalas.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import main.java.cz.pavelkalas.core.DbContext;
import main.java.cz.pavelkalas.models.User;
import main.java.cz.pavelkalas.utils.MessageUtils;
import main.java.cz.pavelkalas.utils.MessageUtils.Color;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

/**
 * Třída pro listování eventů hráče.
 */
public class PlayerListener {

	/**
	 * Pozice hráče po připojení.
	 */
	private final Map<UUID, Vec3d> frozenPositions = new HashMap<>();

	/**
	 * Upozornění pro přihlášení/registraci
	 */
	private final Map<UUID, Integer> loginWarn = new HashMap<>();

	/**
	 * Při každém ticku se hráč teleportuje na původní lokace které se uložily při
	 * připojení k serveru, jen v případě, že je odhlášený nebo není registrovaný.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END)
			return;
		if (event.player.world.isRemote)
			return;

		EntityPlayerMP player = (EntityPlayerMP) event.player;
		User user = DbContext.users.get(player.getName());

		if (user != null && user._isLogged)
			return;

		Vec3d pos = frozenPositions.get(player.getUniqueID());
		if (pos == null)
			return;

		player.setPositionAndUpdate(pos.x, pos.y, pos.z);

		player.motionX = 0;
		player.motionY = 0;
		player.motionZ = 0;
	}

	/**
	 * Při připojení k serveru se uloží zpráva do MAP a neustále se vypisuje, dokud
	 * se hráč nepřipojí.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onLoginTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END)
			return;
		if (event.player.world.isRemote)
			return;

		EntityPlayerMP player = (EntityPlayerMP) event.player;
		User user = DbContext.users.get(player.getName());

		if (user != null && user._isLogged)
			return;

		UUID id = player.getUniqueID();
		int t = loginWarn.getOrDefault(id, 0);

		if (t % 200 == 0) {
			MessageUtils.sendMessage(player, (user == null ? "Registruj se pomoci /register <heslo>" : "Prihlas se pomoci /login <heslo>"), Color.Yellow);
		}

		loginWarn.put(id, t + 1);
	}

	/**
	 * Zablokuje jakékoliv útoky na jiné entity pokud hráč není přihlášen nebo
	 * registrován.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onAttack(AttackEntityEvent event) {
		User user = DbContext.users.get(event.getEntity().getName());
		if (user == null || !user._isLogged) {
			event.setCanceled(true);
		}
	}

	/**
	 * Zablokuje ničení bloků pokud hráč není přihlášen nebo registrován.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onBreak(BlockEvent.BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		User user = DbContext.users.get(player.getName());

		if (user == null || !user._isLogged) {
			event.setCanceled(true);
		}
	}

	/**
	 * Zablokuje pokládání bloků pokud hráč není přihlášen nebo registrován
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onPlace(BlockEvent.PlaceEvent event) {
		EntityPlayer player = event.getPlayer();
		User user = DbContext.users.get(player.getName());

		if (user == null || !user._isLogged) {
			event.setCanceled(true);
		}
	}

	/**
	 * Při připojení hráče k serveru se do frozenLocations uloží výchozí lokace kde
	 * se připojil.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		if (!(event.player instanceof EntityPlayerMP))
			return;

		EntityPlayerMP player = (EntityPlayerMP) event.player;

		frozenPositions.put(player.getUniqueID(), new Vec3d(player.posX, player.posY, player.posZ));

		MessageUtils.sendMessage(player, "Pripojil se hrac '" + player.getName() + "' na server.", Color.White);
	}

	/**
	 * Při odpojení hráče se po sobě uklidí z obou MAP hráč a nastaví se, že je
	 * odhlášený.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event) {
		frozenPositions.remove(event.player.getUniqueID());
		loginWarn.remove(event.player.getUniqueID());

		User user = DbContext.users.get(event.player.getName());
		if (user != null) {
			user._isLogged = false;
		}
	}

	/**
	 * Zablokuje, aby jakákoliv entita útočila na hráče pokud hráč není přihlášen
	 * nebo registrován.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerDamage(LivingHurtEvent event) {
		if (!(event.getEntity() instanceof EntityPlayerMP)) {
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
		User user = DbContext.users.get(player.getName());

		if (user == null || !user._isLogged) {
			event.setCanceled(true);
		}
	}
}
