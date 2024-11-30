package BetterPipes;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import static BetterPipes.Registry.*;
import static BetterPipes.RenderPipe.PIPE_FLUID_SHADER_SHARD;
import static BetterPipes.RenderPipe.POSITION_COLOR_TEXTURE_NORMAL;

@Mod("betterpipes")
public class BetterPipes {

    public BetterPipes(IEventBus modEventBus, ModContainer modContaine) throws IOException {
        NeoForge.EVENT_BUS.register(EntityPipe.class);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::loadComplete);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::RegisterCapabilities);
        modEventBus.addListener(this::registerEntityRenderers);
        modEventBus.addListener(this::registerNetworkStuff);
        modEventBus.addListener(this::onShaderRegistry);
        Registry.register(modEventBus);


        Path configDir = Paths.get(FMLPaths.CONFIGDIR.get().toString()); // Replace with your directory path
        String filename = "Better_Pipes.txt";

        ConfigManager configManager = new ConfigManager(configDir, filename);

        EntityPipe.MAX_OUTPUT_RATE = configManager.getInt("MAIN_MAX_OUTPUT_RATE", 0);
        EntityPipe.REQUIRED_FILL_FOR_MAX_OUTPUT = configManager.getInt("MAIN_REQUIRED_FILL_FOR_MAX_OUTPUT", 0);
        EntityPipe.MAIN_CAPACITY = configManager.getInt("MAIN_CAPACITY", 0);
        EntityPipe.CONNECTION_MAX_OUTPUT_RATE = configManager.getInt("CONNECTION_MAX_OUTPUT_RATE", 0);
        EntityPipe.CONNECTION_REQUIRED_FILL_FOR_MAX_OUTPUT = configManager.getInt("CONNECTION_REQUIRED_FILL_FOR_MAX_OUTPUT", 0);
        EntityPipe.CONNECTION_CAPACITY = configManager.getInt("CONNECTION_CAPACITY", 0);
        EntityPipe.STATE_UPDATE_TICKS = configManager.getInt("Z_STATE_UPDATE_TICKS", 0);
        EntityPipe.FORCE_OUTPUT_AFTER_TICKS = configManager.getInt("Z_FORCE_OUTPUT_AFTER_TICKS", 0);


    }

    public void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(PIPE.get(), RenderType.cutout());
    }

    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ENTITY_PIPE.get(), RenderPipe::new);
    }

    public void registerNetworkStuff(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        PacketBlockEntity.register(registrar);
        PacketFluidUpdate.register(registrar);
        PacketFluidAmountUpdate.register(registrar);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            e.accept(PIPE.get());
        }
    }

    private void RegisterCapabilities(RegisterCapabilitiesEvent e) {
        e.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ENTITY_PIPE.get(), (tile, side) -> tile.getFluidHandler(side));
    }

    private void loadComplete(FMLLoadCompleteEvent e) {

    }

    public void onShaderRegistry(RegisterShadersEvent event) {
        try {
            ResourceLocation shaderLocation = ResourceLocation.fromNamespaceAndPath("betterpipes", "pipe_fluid_shader");
            event.registerShader(new ShaderInstance(event.getResourceProvider(), shaderLocation, POSITION_COLOR_TEXTURE_NORMAL), loadedShader ->
                    PIPE_FLUID_SHADER = loadedShader
            );
        } catch (IOException e) {
            throw (new RuntimeException(e));
        }
    }
}