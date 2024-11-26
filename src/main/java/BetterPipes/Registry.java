package BetterPipes;

import ARLib.blockentities.EntityFluidInputBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import static BetterPipes.BetterPipes.MODID;

public class Registry {
    public static final net.neoforged.neoforge.registries.DeferredRegister<Block> BLOCKS = net.neoforged.neoforge.registries.DeferredRegister.create(BuiltInRegistries.BLOCK, MODID);
    public static final net.neoforged.neoforge.registries.DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = net.neoforged.neoforge.registries.DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);
    public static final net.neoforged.neoforge.registries.DeferredRegister<Item> ITEMS = net.neoforged.neoforge.registries.DeferredRegister.create(BuiltInRegistries.ITEM, MODID);

    public static void registerBlockItem(String name, DeferredHolder<Block,Block> b){
        ITEMS.register(name,() -> new BlockItem(b.get(), new Item.Properties()));
    }

    public static final DeferredHolder<Block, Block> PIPE = BLOCKS.register(
            "pipe",
            () -> new BlockPipe(BlockBehaviour.Properties.of().noOcclusion().instabreak())
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EntityPipe>> ENTITY_PIPE = BLOCK_ENTITIES.register(
            "entity_pipe",
            () -> BlockEntityType.Builder.of(EntityPipe::new, PIPE.get()).build(null)
    );




    public static void register(IEventBus modBus) {
        registerBlockItem("pipe", PIPE);

        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ENTITY_PIPE.get(), RenderPipe::new);
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent e){
        if (e.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS){
            e.accept(PIPE.get());
        }
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent e) {
        e.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ENTITY_PIPE.get(), (tile, side) -> tile.getFluidHandler(side));
    }
}
