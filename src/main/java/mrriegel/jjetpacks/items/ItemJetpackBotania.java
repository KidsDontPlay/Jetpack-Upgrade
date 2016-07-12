package mrriegel.jjetpacks.items;

import java.util.List;

import mrriegel.jjetpacks.helper.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.api.mana.IManaItem;

public class ItemJetpackBotania extends ItemJetpackBase implements IManaItem {

	@Override
	public void addMana(ItemStack arg0, int arg1) {
		NBTHelper.setInteger(arg0, "mana", Math.min(NBTHelper.getInteger(arg0, "mana") + arg1, getMaxMana(arg0)));
	}

	@Override
	public boolean canExportManaToItem(ItemStack arg0, ItemStack arg1) {
		return false;
	}

	@Override
	public boolean canExportManaToPool(ItemStack arg0, TileEntity arg1) {
		return false;
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack arg0, ItemStack arg1) {
		return true;
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack arg0, TileEntity arg1) {
		return true;
	}

	@Override
	public int getMana(ItemStack arg0) {
		return NBTHelper.getInteger(arg0, "mana");
	}

	@Override
	public int getMaxMana(ItemStack arg0) {
		switch (arg0.getItemDamage()) {
		case 0:
			return 5000;
		case 1:
			return 40000;
		case 2:
			return 320000;
		default:
			break;
		}
		return 0;
	}

	@Override
	public boolean isNoExport(ItemStack arg0) {
		return true;
	}

	@Override
	public String getName() {
		return "botaniajetpack";
	}

	@Override
	public int getNumber() {
		return 3;
	}

	@Override
	public int reduceFuel(ItemStack stack, int amount, boolean hover, boolean simulate) {
		switch (stack.getItemDamage()) {
		case 0:
			return extractMana(stack, amount, simulate);
		case 1:
			return extractMana(stack, (hover ? 1 : 8) * amount, simulate);
		case 2:
			return extractMana(stack, (hover ? 1 : 64) * amount, simulate);
		default:
			break;
		}
		return 0;
	}

	public int extractMana(ItemStack container, int maxExtract, boolean simulate) {
		if (container.getTagCompound() == null || !container.getTagCompound().hasKey("mana")) {
			return 0;
		}
		int energy = container.getTagCompound().getInteger("mana");
		int energyExtracted = Math.min(energy, maxExtract);

		if (!simulate) {
			energy -= energyExtracted;
			container.getTagCompound().setInteger("mana", energy);
		}
		return energyExtracted;
	}

	@Override
	public double getMaxVerticalSpeed(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 0.25;
		case 1:
			return 0.35;
		case 2:
			return 0.5;
		default:
			break;
		}
		return 0;
	}

	@Override
	public double getMaxHorizontalSpeed(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 0.15;
		case 1:
			return 0.35;
		case 2:
			return 0.55;
		default:
			break;
		}
		return 0;
	}

	@Override
	public double getAcceleration(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 0.1;
		case 1:
			return 0.2;
		case 2:
			return 0.3;
		default:
			break;
		}
		return 0;
	}

	@Override
	public int getDamageReduce(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 2;
		case 1:
			return 3;
		case 2:
			return 5;
		default:
			break;
		}
		return 0;
	}

	@Override
	public double getToughness(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 0;
		case 1:
			return 0;
		case 2:
			return 1;
		default:
			break;
		}
		return 0;
	}

	@Override
	public int getFuel(ItemStack stack) {
		return getMana(stack);
	}

	@Override
	public int getMaxFuel(ItemStack stack) {
		return getMaxMana(stack);
	}

	@Override
	public void setMaxFuel(ItemStack stack) {
		addMana(stack, getMaxFuel(stack));
	}

	@Override
	public double getHoverSink(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return -0.05;
		case 1:
			return -0.03;
		case 2:
			return 0;
		default:
			break;
		}
		return Double.NaN;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		// tooltip.add("Mana: " + getFuel(stack) + "/" + getMaxFuel(stack));
	}

}
