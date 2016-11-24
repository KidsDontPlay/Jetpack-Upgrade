package mrriegel.jjetpacks.items;

import mrriegel.jjetpacks.CreativeTab;
import mrriegel.limelib.item.CommonSubtypeItem;

public class ItemArmorUpgrade extends CommonSubtypeItem{
	public ItemArmorUpgrade() {
		super("armor",3);
		this.setCreativeTab(CreativeTab.tab1);
		this.setMaxStackSize(1);
	}
}
