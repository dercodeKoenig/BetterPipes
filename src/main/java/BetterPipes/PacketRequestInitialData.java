package BetterPipes;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;


public class PacketRequestInitialData implements CustomPacketPayload {

    public interface clientOnload{
        void clientOnload(ServerPlayer player);
    }


    public static final ResourceLocation ID = new ResourceLocation("betterpipes", "packet_request_initial_data");


    public PacketRequestInitialData(ResourceLocation dimension, BlockPos pos) {
        this.pos = pos;
        this.dimension =dimension;
    }

    ResourceLocation dimension;
    BlockPos pos;

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(dimension);
        buf.writeBlockPos(pos);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketRequestInitialData read(FriendlyByteBuf buf) {
        return new PacketRequestInitialData(buf.readResourceLocation(),buf.readBlockPos());
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            Level l = ServerLifecycleHooks.getCurrentServer().getLevel(ResourceKey.create(Registries.DIMENSION, dimension));
            BlockEntity be = l.getBlockEntity(pos);
            if (be instanceof clientOnload c) {
                c.clientOnload((ServerPlayer) ctx.player().get());
            }
        });
    }
}

