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

package nz.co.crookedhill.ggutils.handlers;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOre;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * if the player has a tool inhand that is enchanted
 * when a block is broken, cancel the block break.
 * get a list of what the block drops.
 * check what the ore smelts into and replace it in the
 * list of droped items with what it smelts into
 * then set the block to air and spawn an itemstack, with a stacksize of
 * normal drop+(1 to i) (i being the level of the enchantment)
 * 
 * this class needs to be cleaned up wil, very hackey :)
 *
 */

public class GGUEnchantmentHandler 
{
	@SubscribeEvent
	public void onBlockBreak(BreakEvent event) 
	{
		if(!event.world.isRemote) {
			if(event.block instanceof BlockOre || event.block instanceof BlockLog) 
			{
				if(event.getPlayer().getHeldItem().getItem().getToolClasses(event.getPlayer().getHeldItem()).contains(event.block.getHarvestTool(event.blockMetadata)) &&
						event.getPlayer().getHeldItem().getItem().getHarvestLevel(event.getPlayer().getHeldItem(), event.block.getHarvestTool(event.blockMetadata)) >= event.block.getHarvestLevel(event.blockMetadata)) {

					if(event.getPlayer().getHeldItem() != null 
							&& event.getPlayer().getHeldItem().getEnchantmentTagList() != null) 
					{
						int enchantmentLevel = 0;
						Random rand = new Random();
						for(int i = 0; i < event.getPlayer().getHeldItem().getEnchantmentTagList().tagCount(); i++)
						{
							if(event.getPlayer().getHeldItem().getEnchantmentTagList().getStringTagAt(i).equals("{lvl:3s,id:103s,}"))
								enchantmentLevel = 3;
							if(event.getPlayer().getHeldItem().getEnchantmentTagList().getStringTagAt(i).equals("{lvl:2s,id:103s,}"))
								enchantmentLevel = 2;
							if(event.getPlayer().getHeldItem().getEnchantmentTagList().getStringTagAt(i).equals("{lvl:1s,id:103s,}"))
								enchantmentLevel = 1;
						}
						event.setCanceled(true);
						FurnaceRecipes recipes = FurnaceRecipes.smelting();
						ArrayList<ItemStack> items = event.block.getDrops(event.world, event.x, event.y, event.z, 0, 3);
						for(int i = 0; i < items.size(); i++)
						{
							if(recipes.getSmeltingResult(items.get(i)) != null) 
							{
								items.set(i, recipes.getSmeltingResult(items.get(i)));
								int dropCount = items.get(i).stackSize + Math.abs(rand.nextInt(enchantmentLevel));
								items.get(i).stackSize = dropCount;
							}
						}
						event.world.setBlockToAir(event.x, event.y, event.z);
						for(int j = 0; j < items.size(); j++)
						{
							event.world.spawnEntityInWorld(new EntityItem(event.world, (float)event.x, (float)event.y, (float)event.z, items.get(j)));
						}

					}
				}
			}
		}
	}
}
