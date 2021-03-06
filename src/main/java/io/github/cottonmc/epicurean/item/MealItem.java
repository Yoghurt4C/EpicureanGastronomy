package io.github.cottonmc.epicurean.item;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import io.github.cottonmc.epicurean.meal.FlavorGroup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class MealItem extends FoodItem {

	public MealItem(int hunger, float saturation, boolean wolfFood) {
		super(hunger, saturation, wolfFood, new Item.Settings().itemGroup(EpicureanGastronomy.EPICUREAN_GROUP));
	}

	@Override
	protected void onConsumed(ItemStack stack, World world, PlayerEntity player) {
		super.onConsumed(stack, world, player);
		for (StatusEffectInstance effect : getMealEffects(stack)) {
			player.addPotionEffect(effect);
		}
		if (!stack.hasTag()) return;
		if (stack.getTag().containsKey("FlavorProfile")) {
			CompoundTag profile = stack.getTag().getCompound("FlavorProfile");
			int addHunger = 0;
			float addSaturation = 0;
			if (profile.containsKey("Hunger")) addHunger = profile.getInt("Hunger");
			if (profile.containsKey("Saturation")) addSaturation = profile.getFloat("Saturation");
			player.getHungerManager().add(addHunger, addSaturation);
		}
	}

	public static List<StatusEffectInstance> getMealEffects(ItemStack stack) {
		if (!stack.hasTag() || stack.getTag().containsKey("Potion")) return Collections.singletonList(new StatusEffectInstance(StatusEffects.SATURATION, 200));
		return PotionUtil.getPotionEffects(stack);
	}

	@Override
	public void buildTooltip(ItemStack stack, World world, List<TextComponent> tooltip, TooltipContext ctx) {
		super.buildTooltip(stack, world, tooltip, ctx);
		PotionUtil.buildTooltip(stack, tooltip, 1.0F);
		if (!stack.hasTag()) return;
		if (stack.getTag().containsKey("FlavorProfile")) {
			if (Screen.isShiftPressed()) {
				CompoundTag profile = stack.getTag().getCompound("FlavorProfile");
				tooltip.add(new TranslatableTextComponent("tooltip.epicurean.meal." + profile.getString("ProminentFlavor").toLowerCase()).applyFormat(TextFormat.GRAY));
				int hunger = 0;
				float saturation = 0;
				if (profile.containsKey("Hunger")) hunger = profile.getInt("Hunger");
				if (profile.containsKey("Saturation")) saturation = profile.getFloat("Saturation");
				float percentage = Math.round(saturation*100.0);
				TranslatableTextComponent restores = new TranslatableTextComponent("tooltip.epicurean.meal.restores");
				TranslatableTextComponent restHunger = new TranslatableTextComponent("tooltip.epicurean.meal.hunger");
				TranslatableTextComponent restSaturation = new TranslatableTextComponent("tooltip.epicurean.meal.saturation");
				TextComponent restoration;
				if (EpicureanGastronomy.config.useSaturationOnly) {
					restoration = new StringTextComponent(restores.getText()
							+ percentage + restSaturation.getText())
							.applyFormat(TextFormat.GRAY);
				} else {
					restoration = new StringTextComponent(restores.getText()
							+ hunger + restHunger.getText()
							+ percentage + restSaturation.getText())
							.applyFormat(TextFormat.GRAY);
				}
				if (hunger != 0 && saturation != 0) tooltip.add(restoration);
				if (profile.containsKey("Seasonings")) {
					tooltip.add(new TranslatableTextComponent("tooltip.epicurean.meal.seasonings").applyFormat(TextFormat.GRAY));
					CompoundTag seasonings = profile.getCompound("Seasonings");
					for (String key : seasonings.getKeys()) {
						String name = Registry.ITEM.get(new Identifier(key)).getTranslationKey();
						int amount = seasonings.getInt(key);
						TranslatableTextComponent translation = new TranslatableTextComponent(name);
						tooltip.add(new StringTextComponent(amount + " x " + translation.getText()).applyFormat(TextFormat.GRAY));
					}
				}

			} else {
				tooltip.add(new TranslatableTextComponent("tooltip.epicurean.readmore").applyFormat(TextFormat.GREEN));
			}
		}

	}
}
