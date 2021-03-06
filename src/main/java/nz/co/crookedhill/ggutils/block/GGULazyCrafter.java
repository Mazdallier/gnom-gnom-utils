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

package nz.co.crookedhill.ggutils.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import nz.co.crookedhill.ggutils.achievements.GGUAchievements;
import nz.co.crookedhill.ggutils.util.GGURecipeFilter;

public class GGULazyCrafter extends Block
{

	protected GGULazyCrafter(Material material) 
	{
		super(material);
		this.setBlockName("lazyCrafter");
		this.setHardness(0.5f);
		this.setStepSound(Block.soundTypeWood);
		//side textures will the crafting table textures, top will be 
		//an edited one with an enderpearl crammed in.

		//when an item is crafted, ender particles will be spawned around
		//the block, preferably ontop where the enderpearl is.
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float float1, float float2, float float3) 
	{
		player.addStat(GGUAchievements.usedlazyCrafter, 1);
		//open the gui.
		//get the players inventory.
		//display the inventory and the items avalable 
		//to craft with current items.
		if(world.isRemote)
		{
			IInventory inventory = player.inventory;
			List invItems = new ArrayList();
			//36-39=armour
			for(int i = 0; i < 35; i++) 
			{
				ItemStack items = inventory.getStackInSlot(i);
				if(items == null)
					continue;
				invItems.add(items);
			}
			List recipes = GGURecipeFilter.filter(invItems);
			for(int i = 0; i < recipes.size(); i++) 
			{
				if(recipes.get(i)instanceof ShapedRecipes) 
				{
					//System.out.println(player.getDisplayName()+" can craft a shaped "+((ShapedRecipes)recipes.get(i)).getRecipeOutput().getDisplayName());
				}
				else if(recipes.get(i)instanceof ShapelessRecipes) 
				{
					//System.out.println(player.getDisplayName()+" can craft a shapeless "+((ShapelessRecipes)recipes.get(i)).getRecipeOutput().getDisplayName());
				}

			}

			return true;
		}
		return false;
	}

}
