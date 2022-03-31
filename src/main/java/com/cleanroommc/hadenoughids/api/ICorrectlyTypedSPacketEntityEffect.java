package com.cleanroommc.hadenoughids.api;

import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class is injected into {@link net.minecraft.network.play.server.SPacketEntityEffect}
 * via {@link com.cleanroommc.hadenoughids.core.visitors.SPacketEntityEffectVisitor}
 *
 * Cast this interface to the packet to get effectId and amplifier values as integers.
 *
 * Currently used in {@link net.minecraft.client.network.NetHandlerPlayClient#handleEntityEffect(SPacketEntityEffect)}
 */
@SuppressWarnings("unused")
public interface ICorrectlyTypedSPacketEntityEffect {

    @SideOnly(Side.CLIENT)
    int cleanroom$getEffectId();

    @SideOnly(Side.CLIENT)
    int cleanroom$getAmplifier();

}
