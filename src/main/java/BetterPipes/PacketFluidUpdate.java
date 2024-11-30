

package BetterPipes;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.nio.charset.Charset;

public class PacketFluidUpdate implements CustomPacketPayload {


    public static final ResourceLocation ID = new ResourceLocation("betterpipes", "packet_fluid_amount_update");


    public PacketFluidUpdate(BlockPos pos, int direction, Fluid fluid, long time) {
        this.pos = pos;
        this.direction = direction;
        this.fluid = fluid;
        this.time = time;
    }

    Fluid fluid;
    long time;
    BlockPos pos;
    int direction;

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(direction);
buf.writeResourceLocation(BuiltInRegistries.FLUID.getKey(fluid));
        buf.writeLong(time);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketFluidUpdate read(FriendlyByteBuf buf) {
        return new PacketFluidUpdate(buf.readBlockPos(), buf.readInt(), BuiltInRegistries.FLUID.get(buf.readResourceLocation()), buf.readLong());
    }

    public void handle(PlayPayloadContext ctx) {
        Level world = Minecraft.getInstance().level;
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof EntityPipe pipe) {
            if (direction == -1) {
                pipe.setFluidInTank(fluid, time);
            } else {
                pipe.connections.get(Direction.values()[direction]).setFluidInTank(fluid, time);
            }
        }
    }
}

