/*
 * Copyright (c) 2014, William <w.cameron@crookedhill.co.nz>
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package nz.co.crookedhill.ggutils.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nz.co.crookedhill.ggutils.GGUtils;
import nz.co.crookedhill.ggutils.extendedprops.GGUExtendedPlayer;

public class GGUWoodenGear extends Item
{
	public GGUWoodenGear() 
	{
		this.setMaxStackSize(64);
		this.setTextureName(GGUtils.MODID + ":woodenGear_texture");
	}
	
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int sumin, float f, float f2, float f3) {
		GGUExtendedPlayer.get(player).currMana++;
		System.out.println(GGUExtendedPlayer.get(player).currMana);
		return true;
		
	}
}
