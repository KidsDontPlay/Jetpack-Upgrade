package mrriegel.jjetpacks;

import mrriegel.jjetpacks.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class CreativeTab {
	public static CreativeTabs tab1 = new CreativeTabs(JJetpacks.MODID) {
		@Override
		public Item getTabIconItem() {
			if (ModItems.icon == null)
				return Items.BLAZE_POWDER;
			return ModItems.icon;
		}

		@Override
		public String getTranslatedTabLabel() {
			return JJetpacks.MODNAME;
		}
	};
}
