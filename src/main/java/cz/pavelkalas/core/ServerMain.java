package main.java.cz.pavelkalas.core;

import main.java.cz.pavelkalas.commands.CommandRegister;
import main.java.cz.pavelkalas.listener.PlayerListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Hlavní vstupní třída módu.
 */
@Mod(modid = ServerMain.MODID, name = ServerMain.NAME, version = ServerMain.VERSION, acceptableRemoteVersions = "*")
public class ServerMain {

	public static final String MODID = "omninexus";
	public static final String NAME = "OmniNexus";
	public static final String VERSION = "1.0";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// registrujeme veškeré listenery...
		MinecraftForge.EVENT_BUS.register(new PlayerListener());
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		// registrujeme veškeré příkazy...
		new CommandRegister(event);
	}
}
