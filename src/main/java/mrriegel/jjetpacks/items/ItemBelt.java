package mrriegel.jjetpacks.items;

import mrriegel.jjetpacks.CreativeTab;
import net.minecraft.item.Item;

public class ItemBelt extends Item {
	public ItemBelt() {
		super();
		this.setCreativeTab(CreativeTab.tab1);
		this.setMaxStackSize(1);
		this.setRegistryName("belt");
		this.setUnlocalizedName(getRegistryName().toString());
	}
}
