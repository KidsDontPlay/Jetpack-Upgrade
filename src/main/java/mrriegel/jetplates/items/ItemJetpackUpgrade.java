package mrriegel.jetplates.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import mrriegel.jetplates.proxy.ClientProxy;
import mrriegel.limelib.item.CommonSubtypeItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemJetpackUpgrade extends CommonSubtypeItem {
	public ItemJetpackUpgrade() {
		super("jetplate", 3);
		this.setCreativeTab(CreativeTabs.COMBAT);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.values()[stack.getItemDamage()];
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Combine this with a chestplate.");
		tooltip.add("Press " + Keyboard.getKeyName(ClientProxy.gui.getKeyCode()) + " while wearing the upgraded chestplate to open the GUI.");
	}
}
