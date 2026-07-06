package main.java.cz.pavelkalas.commands.teleport;

import main.java.cz.pavelkalas.utils.DateTypesUtil;
import main.java.cz.pavelkalas.utils.MessageUtils;
import main.java.cz.pavelkalas.utils.MessageUtils.Color;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

/**
 * Příkaz pro teleportaci na určité souřadnice (X, Y, Z) s ochranou proti
 * bugnutí bloku nebo pádu z výšky
 */
public class PortCommand extends CommandBase {

	@Override
	public String getName() {
		return "port";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "port";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayerMP)) {
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) sender;
		int[] coords = DateTypesUtil.getNumbersFromString(args);

		if (coords.length != 3) {
			MessageUtils.sendMessage(player, "Pouziti: /port [x] [y] [z]", Color.Yellow);
			return;
		}

		double x = coords[0] + 0.5D;
		int y = coords[1];
		double z = coords[2] + 0.5D;

		while (y > 0 && player.world.isAirBlock(new BlockPos(x, y - 1, z))) {
			y--;
		}

		if (y <= 0) {
			MessageUtils.sendMessage(player, "Pod zadanou pozici neni zadny pevny blok.", Color.Red);
			return;
		}

		BlockPos feet = new BlockPos(x, y, z);
		BlockPos head = feet.up();

		if (!player.world.isAirBlock(feet) || !player.world.isAirBlock(head)) {
			MessageUtils.sendMessage(player, "Na cilove pozici neni dostatek mista.", Color.Red);
			return;
		}

		AxisAlignedBB bb = player.getEntityBoundingBox().offset(x - player.posX, y - player.posY, z - player.posZ);

		if (!player.world.getCollisionBoxes(player, bb).isEmpty()) {
			MessageUtils.sendMessage(player, "Nelze se bezpecne teleportovat bez rizika bugnuti do bloku!", Color.Red);
			return;
		}

		player.setPositionAndUpdate(x, y, z);
	}
}
