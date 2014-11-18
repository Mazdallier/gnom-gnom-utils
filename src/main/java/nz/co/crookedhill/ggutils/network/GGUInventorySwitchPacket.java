package nz.co.crookedhill.ggutils.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class GGUInventorySwitchPacket implements IMessage {
	
	int inventorySwitched;
	public GGUInventorySwitchPacket() { }
	
	public GGUInventorySwitchPacket(short inventoryNumber) {
		this.inventorySwitched = inventoryNumber;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.inventorySwitched = ByteBufUtils.readVarShort(buf);

	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarShort(buf, inventorySwitched);
	}

}
