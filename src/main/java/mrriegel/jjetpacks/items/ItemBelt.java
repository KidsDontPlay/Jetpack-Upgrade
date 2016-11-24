package mrriegel.jjetpacks.items;

import mrriegel.jjetpacks.CreativeTab;
import mrriegel.limelib.item.CommonItem;

public class ItemBelt extends CommonItem {
	public ItemBelt() {
		super("belt");
		this.setCreativeTab(CreativeTab.tab1);
		this.setMaxStackSize(1);
	}
}
