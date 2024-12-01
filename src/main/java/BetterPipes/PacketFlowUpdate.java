package BetterPipes;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.nio.charset.Charset;

public class PacketFlowUpdate implements CustomPacketPayload {


    public static final ResourceLocation ID = new ResourceLocation("betterpipes", "packet_flow_update");


    public PacketFlowUpdate(BlockPos pos, int direction, boolean inFromOut, boolean inFromIn, boolean outToOut, boolean outToIn, long time) {
        this.pos = pos;
        this.direction = direction;
        this.inFromOut = inFromOut;
        this.inFromIn = inFromIn;
        this.outToOut = outToOut;
        this.outToIn = outToIn;
        this.time = time;
    }

    long time;
    boolean inFromOut, inFromIn, outToOut, outToIn;
    BlockPos pos;
    int direction;

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(direction);
        buf.writeBoolean(inFromOut);
        buf.writeBoolean(inFromIn);
        buf.writeBoolean(outToOut);
        buf.writeBoolean(outToIn);
        buf.writeLong(time);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketFlowUpdate read(FriendlyByteBuf buf) {
        return new PacketFlowUpdate(buf.readBlockPos(), buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readLong());
    }


    @OnlyIn(Dist.CLIENT)
    public void _handle(PlayPayloadContext ctx) {
        // use the current Dimension, the client does not need to find the dimension by String
        Level world = Minecraft.getInstance().level;
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof EntityPipe pipe) {
            pipe.connections.get(Direction.values()[direction]).setFlow(inFromIn, inFromOut, outToIn, outToOut, time);
        }
    }

    public void handle(PlayPayloadContext ctx) {
        _handle(ctx);
    }
}
