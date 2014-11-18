package nz.co.crookedhill.ggutils.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import nz.co.crookedhill.ggutils.extendedprops.GGUExtendedPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class GGUInventorySwitchHandler implements IMessageHandler<GGUInventorySwitchPacket, IMessage>
{

	@Override
	public IMessage onMessage(GGUInventorySwitchPacket message, MessageContext ctx) {
		ItemStack[] inventory = ctx.getServerHandler().playerEntity.inventory.mainInventory;
		ctx.getServerHandler().playerEntity.inventory.mainInventory = this.swapInventories(inventory, ctx.getServerHandler().playerEntity);
		System.out.println("packet recieved");
		return null;
	}
	private ItemStack[] swapInventories(ItemStack[] inventory, EntityPlayer player) {
		List<ItemStack> currHotbar = new ArrayList<ItemStack>();
		ItemStack[] nbtHotbar = GGUExtendedPlayer.get(player).inventory2;
		/* set the current hotbar to a buffer variable */
		for(int i = 0; i < 9; i++)
			currHotbar.add(inventory[i]);
		/* set the inventories hotbar to the nbt hotbar */
		for(int i = 0; i < 9; i++)
			inventory[i] = nbtHotbar[i];
		/* set the nbt hotbar to the buffer hotbar */
		for(int i = 0; i < 9; i++)
			nbtHotbar[i] = currHotbar.get(i);
		return inventory;
	}

}
