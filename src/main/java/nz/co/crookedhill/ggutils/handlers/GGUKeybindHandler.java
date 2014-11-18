package nz.co.crookedhill.ggutils.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import nz.co.crookedhill.ggutils.GGUtils;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class GGUKeybindHandler {
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

		if(FMLClientHandler.instance().getClient().inGameHasFocus){

			if(GGUtils.arseTardis.isPressed()) {
				ItemStack[] inventory = player.inventory.mainInventory;
				player.inventory.mainInventory = this.swapInventories(inventory);
			}
		}
	}
}
