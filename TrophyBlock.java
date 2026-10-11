package com.trophymod;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemActionResult;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TrophyBlock extends Block {
    public static final MapCodec<TrophyBlock> CODEC = createCodec(TrophyBlock::new);
    public static final BooleanProperty GLASS = BooleanProperty.of("glass");

    private static final VoxelShape OPEN_SHAPE = Block.createCuboidShape(3, 0, 3, 13, 14, 13);
    private static final VoxelShape CASE_SHAPE = Block.createCuboidShape(2, 0, 2, 14, 15, 14);

    public TrophyBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(GLASS, false));
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(GLASS);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        return state.get(GLASS) ? CASE_SHAPE : OPEN_SHAPE;
    }

    // Click derecho con el "Cristal para trofeos" en la mano: coloca la vitrina.
    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos,
                                             PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.isOf(TrophyMod.TROPHY_GLASS) && !state.get(GLASS)) {
            if (!world.isClient) {
                world.setBlockState(pos, state.with(GLASS, true), Block.NOTIFY_ALL);
                world.playSound(null, pos, SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                if (!player.isCreative()) stack.decrement(1);
            }
            return ItemActionResult.success(world.isClient);
        }
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
    }

    // Agacharse + click derecho con la mano vacía: quita el cristal y lo devuelve.
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (state.get(GLASS) && player.isSneaking()) {
            if (!world.isClient) {
                world.setBlockState(pos, state.with(GLASS, false), Block.NOTIFY_ALL);
                world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 0.6f, 1.2f);
                if (!player.isCreative()) {
                    player.getInventory().offerOrDrop(new ItemStack(TrophyMod.TROPHY_GLASS));
                }
            }
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    // Si rompes el trofeo con cristal, el cristal también se te devuelve.
    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && !player.isCreative() && state.get(GLASS)) {
            Block.dropStack(world, pos, new ItemStack(TrophyMod.TROPHY_GLASS));
        }
        return super.onBreak(world, pos, state, player);
    }
}
