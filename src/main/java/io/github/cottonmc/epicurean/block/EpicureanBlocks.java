package io.github.cottonmc.epicurean.block;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import io.github.cottonmc.epicurean.item.EpicureanItems;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EpicureanBlocks {
	public static final Block COOKING_TABLE = register("cooking_table", new CookingTableBlock());

	public static final Identifier COOKING_CONTAINER = new Identifier(EpicureanGastronomy.MOD_ID, "cooking");

	public static Block register(String name, Block block) {
		Registry.register(Registry.BLOCK, new Identifier(EpicureanGastronomy.MOD_ID, name), block);
		BlockItem item = new BlockItem(block, new Item.Settings().itemGroup(EpicureanGastronomy.EPICUREAN_GROUP));
		EpicureanItems.register(name, item);
		return block;
	}

	public static void init() {
		ContainerProviderRegistry.INSTANCE.registerFactory(COOKING_CONTAINER, (syncId, id, player, buf) -> new CookingTableContainer(syncId, player.inventory));
	}
}