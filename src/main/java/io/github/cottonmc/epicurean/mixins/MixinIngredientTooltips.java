package io.github.cottonmc.epicurean.mixins;

import io.github.cottonmc.epicurean.meal.IngredientProfiles;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class MixinIngredientTooltips {

	@Inject(method = "buildTooltip", at = @At("HEAD"))
	public void addFlavorProfiles(ItemStack stack, World world, List<TextComponent> tooltips, TooltipContext ctx, CallbackInfo ci) {
		if (IngredientProfiles.MEAL_INGREDIENTS.containsKey(stack.getItem())) {
			tooltips.add(new TranslatableTextComponent("tooltip.epicurean.ingredient." + IngredientProfiles.MEAL_INGREDIENTS.get(stack.getItem()).asString()).applyFormat(TextFormat.GRAY, TextFormat.ITALIC));
		} else if (IngredientProfiles.DRESSINGS.containsKey(stack.getItem())) {
			tooltips.add(new TranslatableTextComponent("tooltip.epicurean.dressing." + IngredientProfiles.DRESSINGS.get(stack.getItem()).asString()).applyFormat(TextFormat.GRAY, TextFormat.ITALIC));
		}
	}
}
