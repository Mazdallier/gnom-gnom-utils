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

package nz.co.crookedhill.ggutils.network;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import nz.co.crookedhill.ggutils.block.GGUBlockSortivator;
import nz.co.crookedhill.ggutils.util.GGUSort;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public abstract class GGUAbstracthandler implements IMessageHandler<GGUAbstractPacket, IMessage>
{
	
	/**
	 * an example of sending a message is:
	 * (what is passed to the packet)
	 * String message = ""+(char)x+(char)y+(char)z;
	 * (what apears in the main class)
	 * GGUtils.network.sendToServer(new GGUSortPacket(message));
	 */
	 
  /**
   * what happens when one side of the network recieves the packet 
   * associated with the extended class.
   */
	@Override
	public IMessage onMessage(GGUSortPacket message, MessageContext ctx);
	
}
