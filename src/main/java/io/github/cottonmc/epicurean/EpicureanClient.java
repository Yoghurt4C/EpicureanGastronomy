package io.github.cottonmc.epicurean;

import io.github.cottonmc.epicurean.container.CookingTableScreen;
import io.github.cottonmc.epicurean.block.EpicureanBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.util.Identifier;

public class EpicureanClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientSpriteRegistryCallback.EVENT.register(((spriteAtlasTexture, registry) -> registry.register(new Identifier(EpicureanGastronomy.MOD_ID, "gui/cooking_table"))));

		ScreenProviderRegistry.INSTANCE.registerFactory(EpicureanBlocks.COOKING_CONTAINER, (syncId, identifier, player, buf) -> new CookingTableScreen(syncId, player));
	}
}
