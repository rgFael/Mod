package com.trophymod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TrophyMod implements ModInitializer {
    public static final String MOD_ID = "trophymod";

    public static final Block TROPHY = Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, "trophy"),
            new TrophyBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .strength(1.0f)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.METAL)
                    .pistonBehavior(PistonBehavior.DESTROY)));

    public static final Item TROPHY_ITEM = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "trophy"),
            new BlockItem(TROPHY, new Item.Settings()));

    public static final Item TROPHY_GLASS = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "trophy_glass"),
            new Item(new Item.Settings()));

    public static final ItemGroup GROUP = Registry.register(Registries.ITEM_GROUP, Identifier.of(MOD_ID, "main"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(TROPHY_ITEM))
                    .displayName(Text.translatable("itemGroup." + MOD_ID))
                    .entries((ctx, entries) -> {
                        entries.add(TROPHY_ITEM);
                        entries.add(TROPHY_GLASS);
                    })
                    .build());

    @Override
    public void onInitialize() {
        // Todo se registra en los campos estáticos de arriba.
    }
}
