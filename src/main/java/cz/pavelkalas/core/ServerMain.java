package main.java.cz.pavelkalas.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import main.java.cz.pavelkalas.commands.auth.ChangePasswordCommand;
import main.java.cz.pavelkalas.commands.auth.LoginCommand;
import main.java.cz.pavelkalas.commands.auth.RegisterCommand;
import main.java.cz.pavelkalas.provider.DbUserProvider.User;
import main.java.cz.pavelkalas.utils.MessageUtils;
import main.java.cz.pavelkalas.utils.MessageUtils.Color;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.BlockEvent;

@Mod(modid = ServerMain.MODID, name = ServerMain.NAME, version = ServerMain.VERSION, acceptableRemoteVersions = "*")
public class ServerMain {

	public static final String MODID = "omninexus";
	public static final String NAME = "OmniNexus";
	public static final String VERSION = "1.0";

	private final Map<UUID, Vec3d> frozenPositions = new HashMap<>();
	private final Map<UUID, Integer> loginWarn = new HashMap<>();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new LoginCommand());
		event.registerServerCommand(new RegisterCommand());
		event.registerServerCommand(new ChangePasswordCommand());
	}

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
			MessageUtils.sendMessage(player,
					(user == null ? "Registruj se pomoci /register <heslo>" : "Prihlas se pomoci /login <heslo>"),
					Color.Yellow);
		}

		loginWarn.put(id, t + 1);
	}

	@SubscribeEvent
	public void onAttack(AttackEntityEvent event) {
		User user = DbContext.users.get(event.getEntity().getName());
		if (user == null || !user._isLogged) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onBreak(BlockEvent.BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		User user = DbContext.users.get(player.getName());

		if (user == null || !user._isLogged) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onPlace(BlockEvent.PlaceEvent event) {
		EntityPlayer player = event.getPlayer();
		User user = DbContext.users.get(player.getName());

		if (user == null || !user._isLogged) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		if (!(event.player instanceof EntityPlayerMP))
			return;

		EntityPlayerMP player = (EntityPlayerMP) event.player;

		frozenPositions.put(player.getUniqueID(), new Vec3d(player.posX, player.posY, player.posZ));

		MessageUtils.sendMessage(player, "Pripojil se hrac '" + player.getName() + "' na server.", Color.White);
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event) {
		frozenPositions.remove(event.player.getUniqueID());
		loginWarn.remove(event.player.getUniqueID());

		User user = DbContext.users.get(event.player.getName());
		if (user != null) {
			user._isLogged = false;
		}
	}
}