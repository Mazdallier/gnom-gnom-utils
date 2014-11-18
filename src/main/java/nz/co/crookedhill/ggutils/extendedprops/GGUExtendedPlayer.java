package nz.co.crookedhill.ggutils.extendedprops;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import nz.co.crookedhill.ggutils.GGUtils;
import nz.co.crookedhill.ggutils.network.GGUSyncPlayerPropsPacket;
import nz.co.crookedhill.ggutils.proxy.CommonProxy;

public class GGUExtendedPlayer implements IExtendedEntityProperties {

	public static final String GGU_EXT_PLAYER = "gguProps";
	public final EntityPlayer player;
	public int currMana;
	public ItemStack[] inventory2 = {null, null, null, null, null, null,null, null, null};

	/**
	 * Constructor - make sure to init all variables.
	 * 
	 * @param player
	 */
	public GGUExtendedPlayer(EntityPlayer player){
		this.player = player;
		this.currMana = 50;
	}


	/*===============================================================================
	 * 
	 * NBT SAVE/LOAD
	 * 
	 *===============================================================================
	 */

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = new NBTTagCompound();
		try {
			properties.setByteArray("Inventory2", serialize(inventory2));
			properties.setInteger("mana", this.currMana);
		} catch (IOException e) {
			e.printStackTrace();
		}
		compound.setTag(GGU_EXT_PLAYER, properties);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(GGU_EXT_PLAYER);
		try {
			this.inventory2 = deserialize(properties.getByteArray("inventory2"));
			this.currMana = properties.getInteger("mana");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*===============================================================================
	 * 
	 * Registering and syncing Extended Properties
	 * 
	 *===============================================================================
	 */	
	/**
	 * Used to register these extended properties for the player during EntityConstructing event
	 * This method is for convenience only; it will make your code look nicer
	 */
	public static final void register(EntityPlayer player){
		player.registerExtendedProperties(GGUExtendedPlayer.GGU_EXT_PLAYER, new GGUExtendedPlayer(player));

	}

	/**
	 * Returns ExtendedPlayer properties for player
	 * This method is for convenience only; it will make your code look nicer
	 */
	public static final GGUExtendedPlayer get(EntityPlayer player){
		return (GGUExtendedPlayer) player.getExtendedProperties(GGU_EXT_PLAYER);
	}

	public void syncAll(){
		GGUtils.network.sendTo(new GGUSyncPlayerPropsPacket(player), (EntityPlayerMP) player);
	}

	/**
	 * Needed for initialization of the extended properties.
	 *
	 * @param entity
	 * @param world
	 */
	@Override
	public void init(Entity entity, World world) {}

	/**
	 * Used for saving NBT tags using the player username.
	 * 
	 * @param player
	 * @return String
	 */
	private static final String getSaveKey(EntityPlayer player) {
		// no longer a username field, so use the command sender name instead:
		return player.getCommandSenderName() + ":" + GGU_EXT_PLAYER;
	}

	/**
	 * Server/Client packet synchronization and NBT uploading.
	 * 
	 * @param player
	 */
	public static final void loadProxyData(EntityPlayer player) {
		GGUExtendedPlayer playerData = GGUExtendedPlayer.get(player);
		NBTTagCompound savedData = CommonProxy.getEntityData(getSaveKey(player));
		if (savedData != null) { playerData.loadNBTData(savedData); }
		GGUtils.network.sendTo(new GGUSyncPlayerPropsPacket(player), (EntityPlayerMP) player);
	}
	public static final void saveProxyData(EntityPlayer player) {
		NBTTagCompound savedData = new NBTTagCompound();
		GGUExtendedPlayer.get(player).saveNBTData(savedData);
		CommonProxy.storeEntityData(getSaveKey(player), savedData);
	}
	
	
	public byte[] serialize(ItemStack[] otherInventory) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(otherInventory);
		return b.toByteArray();
	}

	public ItemStack[] deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return (ItemStack[])o.readObject();
	}
}

