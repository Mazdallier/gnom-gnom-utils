package nz.co.crookedhill.ggutils.extendedprops;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.Constants;
import nz.co.crookedhill.ggutils.GGUtils;
import nz.co.crookedhill.ggutils.network.GGUSyncPlayerPropsPacket;
import nz.co.crookedhill.ggutils.proxy.CommonProxy;

public class GGUExtendedPlayer implements IExtendedEntityProperties 
{
	private final String tagName = "messInventory";
	public static final String GGU_EXT_PLAYER = "gguProps";
	private final EntityPlayer player;
	
	private int lastRow;
	private int numberOfEnderLimbs;
	private ItemStack[] messInventory;
	
	/**
	 * Constructor - make sure to init all variables.
	 * 
	 * @param player
	 */
	public GGUExtendedPlayer(EntityPlayer player)
	{
		this.player = player;
		this.numberOfEnderLimbs = 0;
		this.lastRow = 1;
	}


	/*===============================================================================
	 * 
	 * NBT SAVE/LOAD
	 * 
	 *===============================================================================
	 */

	@Override
	public void saveNBTData(NBTTagCompound compound) 
	{
		NBTTagCompound properties = new NBTTagCompound();
		properties.setInteger("enderLimbs", this.numberOfEnderLimbs);
		properties.setInteger("rowNumber", this.lastRow);		
		compound.setTag(GGU_EXT_PLAYER, properties);
		
		this.messInventory = new ItemStack[this.numberOfEnderLimbs];
		
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < this.messInventory.length; ++i) {
			if (this.messInventory[i] != null) {
				NBTTagCompound item = new NBTTagCompound();
				item.setByte("Slot", (byte) i);
				this.messInventory[i].writeToNBT(item);
				items.appendTag(item);
			}
		}
		compound.setTag(this.tagName, items);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) 
	{
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(GGU_EXT_PLAYER);
		this.numberOfEnderLimbs = properties.getInteger("enderLimbs");
		this.lastRow = properties.getInteger("rowNumber");
		
		this.messInventory = new ItemStack[this.numberOfEnderLimbs];
		
		NBTTagList items = compound.getTagList(this.tagName, Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < items.tagCount(); ++i) {
			NBTTagCompound item = items.getCompoundTagAt(i);
			byte slot = item.getByte("Slot");
			if (slot >= 0 && slot < this.messInventory.length) {
				this.messInventory[i] = ItemStack.loadItemStackFromNBT(item);
			}
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
	public static final void register(EntityPlayer player)
	{
		player.registerExtendedProperties(GGUExtendedPlayer.GGU_EXT_PLAYER, new GGUExtendedPlayer(player));
	}

	/**
	 * Returns ExtendedPlayer properties for player
	 * This method is for convenience only; it will make your code look nicer
	 */
	public static final GGUExtendedPlayer get(EntityPlayer player)
	{
		return (GGUExtendedPlayer) player.getExtendedProperties(GGU_EXT_PLAYER);
	}

	public void syncAll()
	{
		GGUtils.network.sendTo(new GGUSyncPlayerPropsPacket(this.player), (EntityPlayerMP) this.player);
	}

	public void syncInventory()
	{
		GGUtils.network.sendTo(new GGUSyncPlayerPropsPacket(this.player), (EntityPlayerMP) this.player);
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
	private static final String getSaveKey(EntityPlayer player) 
	{
		return player.getCommandSenderName() + ":" + GGU_EXT_PLAYER;
	}

	/**
	 * Server/Client packet synchronization and NBT uploading.
	 * 
	 * @param player
	 */
	public static final void loadProxyData(EntityPlayer player) 
	{
		GGUExtendedPlayer playerData = GGUExtendedPlayer.get(player);
		NBTTagCompound savedData = CommonProxy.getEntityData(getSaveKey(player));
		if (savedData != null) { playerData.loadNBTData(savedData); }
		GGUtils.network.sendTo(new GGUSyncPlayerPropsPacket(player), (EntityPlayerMP) player);
	}
	
	public static final void saveProxyData(EntityPlayer player) 
	{
		NBTTagCompound savedData = new NBTTagCompound();
		GGUExtendedPlayer.get(player).saveNBTData(savedData);
		CommonProxy.storeEntityData(getSaveKey(player), savedData);
	}
	
	/*===============================================================================
	 * 
	 * GETTERS AND SETTERS
	 * 
	 *===============================================================================
	 */	
	
	public void setNumberofLimbs(int number)
	{
		this.numberOfEnderLimbs = number;
		this.syncAll();
	}
	
	public int getNumberOfLimbs()
	{
		return this.numberOfEnderLimbs;
	}

	//Get the last row
	public int getLastRow()
	{
		return this.lastRow;
	}
	
	//set the last row
	public void setLastRow(int newLastRow)
	{
		this.lastRow = newLastRow;
		this.syncAll();
	}
	
	//get the itemstacks
	public ItemStack[] getInventory()
	{
		return messInventory;
	}
	
	//sets the itemstacks
	public void setInventory(ItemStack[] inventory)
	{
		this.messInventory = inventory.clone();
		this.syncInventory();
	}
}

