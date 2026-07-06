package main.java.cz.pavelkalas.commands;

import main.java.cz.pavelkalas.commands.auth.ChangePasswordCommand;
import main.java.cz.pavelkalas.commands.auth.LoginCommand;
import main.java.cz.pavelkalas.commands.auth.RegisterCommand;
import main.java.cz.pavelkalas.commands.teleport.PortCommand;
import main.java.cz.pavelkalas.commands.teleport.PortRandomCommand;
import main.java.cz.pavelkalas.commands.teleport.PortToCommand;
import main.java.cz.pavelkalas.commands.teleport.PortToMeCommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Třída pro registraci všech příkazů.
 */
public class CommandRegister {
	public CommandRegister(FMLServerStartingEvent event) {
		event.registerServerCommand(new LoginCommand());
		event.registerServerCommand(new RegisterCommand());
		event.registerServerCommand(new ChangePasswordCommand());
		event.registerServerCommand(new PortCommand());
		event.registerServerCommand(new PortToCommand());
		event.registerServerCommand(new PortToMeCommand());
		event.registerServerCommand(new PortRandomCommand());
	}
}