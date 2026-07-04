package cz.pavelkalas.core;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ServerMain.MODID, name = ServerMain.NAME, version = ServerMain.VERSION, acceptableRemoteVersions = "*")
public class ServerMain {
	public static final String MODID = "omninexus";
	public static final String NAME = "OmniNexus";
	public static final String VERSION = "1.0";

	private static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
	}

	/**
	 * Vstupní bod módu.
	 */
	@EventHandler
	public void init(FMLInitializationEvent event) {
	}
}
