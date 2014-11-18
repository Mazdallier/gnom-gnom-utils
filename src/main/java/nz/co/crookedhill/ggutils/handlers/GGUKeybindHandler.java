package nz.co.crookedhill.ggutils.handlers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import nz.co.crookedhill.ggutils.GGUtils;
import nz.co.crookedhill.ggutils.extendedprops.GGUExtendedPlayer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class GGUKeybindHandler {
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

		if(FMLClientHandler.instance().getClient().inGameHasFocus){

			if(GGUtils.arseTardis.isPressed()) {
				System.out.println("The key was Pressed");
				ItemStack[] inventory = player.inventory.mainInventory;
				player.inventory.mainInventory = this.swapInventories(inventory, player);
			}
		}
	}
	private ItemStack[] swapInventories(ItemStack[] inventory, EntityPlayer player) {
		List<ItemStack> currHotbar = new ArrayList<ItemStack>();
		List<ItemStack> nbtHotbar = GGUExtendedPlayer.get(player).inventory2;
		/* set the current hotbar to a buffer variable */
		for(int i = 0; i < 8; i++)
			currHotbar.add(inventory[i]);
		/* set the inventories hotbar to the nbt hotbar */
		for(int i = 0; i < 8; i++)
			inventory[i] = nbtHotbar.get(i);
		/* set the nbt hotbar to the buffer hotbar */
		for(int i = 0; i < 8; i++)
			nbtHotbar.set(i, currHotbar.get(i));
		
		
		return inventory;
		
	}
}
