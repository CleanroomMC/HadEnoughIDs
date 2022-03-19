package com.cleanroommc.hadenoughids.core.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Shadow public static int getIdFromBlock(Block blockIn) { throw new AssertionError(); }
    @Shadow public static Block getBlockById(int id) { throw new AssertionError(); }

    /**
     * @author Rongmario
     * @reason Use a better format to save blocks with very high IDs in a single int
     */
    @Overwrite
    public static int getStateId(IBlockState state) {
        Block block = state.getBlock();
        return (getIdFromBlock(block) << 4) + block.getMetaFromState(state);
    }

    /**
     * @author Rongmario
     * @reason Decode using the better format
     */
    @Overwrite
    public static IBlockState getStateById(int stateId) {
        return getBlockById(stateId >> 4).getStateFromMeta(stateId & 15);
    }

}
