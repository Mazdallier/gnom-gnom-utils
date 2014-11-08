package nz.co.crookedhill.ggutils.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import nz.co.crookedhill.ggutils.GGUtils;
import nz.co.crookedhill.ggutils.util.GGUSort;

public class Sortivator extends Block {

	private IIcon[] icons = new IIcon[2];

	protected Sortivator(Material material) {
		super(material);
		this.setBlockName("sortivator");
		this.setHardness(0.5f);
		this.setStepSound(Block.soundTypeWood);
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		for(int i = 0; i < icons.length; i++) {
			icons[i] = iconRegister.registerIcon(GGUtils.MODID + ":" + "sortivator_texture"+ i);
		}
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		switch(meta) {
		case 0:
			//System.out.println("east side");
			if(side == 4) {
				return icons[1];
			}else return icons[0];
		case 1:
			//System.out.println("west side");
			if(side == 5) { //4
				return icons[1];
			}else return icons[0];
		
		case 2:
			//System.out.println("south side");
			if(side == 2) {
				return icons[1];
			}else return icons[0];
		case 3:
			//System.out.println("north side");
			if(side == 3) {
				return icons[1];
			}else return icons[0];
		case 4:
			//Facing Up!
			if(side == 0) {
				return icons[1];
			}else return icons[0];
		case 5:
			//Facing Down!
			if(side == 1) {
				return icons[1];
			}else return icons[0];
		}
		
		return null;
		
		

	}
	//function exists for testing
	/*@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float float1, float float2, float float3)
	{
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			IInventory iinventory = this.getInventory(world, x, y+1, z);

			if (iinventory != null)
			{
				GGUSort.sort(iinventory);

			}

			return true;
		}
	}*/
	//gets an inventory object from the coordernates specified and if its a double chest it will get the contents of both.
	public IInventory getInventory(World world, int x, int y, int z)
	{
		Object object = (TileEntityChest)world.getTileEntity(x, y, z);

		if (object == null)
		{
			return null;
		}
		else if (world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN))
		{
			return null;
		}
		else if (world.getBlock(x - 1, y, z) == this && (world.isSideSolid(x - 1, y + 1, z, ForgeDirection.DOWN)))
		{
			return null;
		}
		else if (world.getBlock(x + 1, y, z) == this && (world.isSideSolid(x + 1, y + 1, z, ForgeDirection.DOWN)))
		{
			return null;
		}
		else if (world.getBlock(x, y, z - 1) == this && (world.isSideSolid(x, y + 1, z - 1, ForgeDirection.DOWN)))
		{
			return null;
		}
		else if (world.getBlock(x, y, z + 1) == this && (world.isSideSolid(x, y + 1, z + 1, ForgeDirection.DOWN)))
		{
			return null;
		}
		else
		{
			if (world.getBlock(x - 1, y, z) == this)
			{
				object = new InventoryLargeChest("container.chestDouble", (TileEntityChest)world.getTileEntity(x - 1, y, z), (IInventory)object);
			}

			if (world.getBlock(x + 1, y, z) == this)
			{
				object = new InventoryLargeChest("container.chestDouble", (IInventory)object, (TileEntityChest)world.getTileEntity(x + 1, y, z));
			}

			if (world.getBlock(x, y, z - 1) == this)
			{
				object = new InventoryLargeChest("container.chestDouble", (TileEntityChest)world.getTileEntity(x, y, z - 1), (IInventory)object);
			}

			if (world.getBlock(x, y, z + 1) == this)
			{
				object = new InventoryLargeChest("container.chestDouble", (IInventory)object, (TileEntityChest)world.getTileEntity(x, y, z + 1));
			}

			return (IInventory)object;
		}
	}
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
		boolean flag = world.isBlockIndirectlyGettingPowered( x, y, z);
		if(flag) {
			int meta = world.getBlockMetadata(x, y, z);
			switch(meta) {
			case 0:
				GGUSort.sort(getInventory(world, x-1, y, z));
				break;
			case 1:
				GGUSort.sort(getInventory(world, x+1, y, z));
				break;
			case 2:
				GGUSort.sort(getInventory(world, x, y, z-1));
				break;
			case 3:
				GGUSort.sort(getInventory(world, x, y, z+1));
				break;
			case 4:
				GGUSort.sort(getInventory(world, x, y-1, z));
				break;
			case 5:
				GGUSort.sort(getInventory(world, x, y+1, z));
				break;
			}
				world.playSound(x, y, z, "ggutils:block.sortivator.sort", 1f, 1f, false);
		}
    }
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack)
    {
        int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int i1 = world.getBlockMetadata(x, y, z) & 4;
        int meta = 0;
        if (l == 0)
        {
        	meta = 2;
        }

        if (l == 1)
        {
        	meta = 1;
        }

        if (l == 2)
        {
        	meta = 3;
        }

        if (l == 3)
        {
        	meta = 0;
        }
        if(player.rotationPitch > 60) {
        	meta = 5;
        }
        if(player.rotationPitch < -60) {
        	meta = 4;
        }
        world.setBlockMetadataWithNotify(x, y, z, meta | i1, 2);
        System.out.println(meta);
    }
	

}
