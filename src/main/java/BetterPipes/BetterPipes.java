package BetterPipes;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(BetterPipes.MODID)
public class BetterPipes {
    public static final String MODID = "betterpipes";

    public BetterPipes(IEventBus modEventBus, ModContainer modContaine) {
        //NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::loadComplete);
        modEventBus.addListener(this::RegisterCapabilities);
        modEventBus.addListener(this::registerEntityRenderers);

        Registry.register(modEventBus);


    }

    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        Registry.registerRenderers(event);
    }


    private void addCreative(BuildCreativeModeTabContentsEvent e) {
        Registry.addCreative(e);
    }

    private void RegisterCapabilities(RegisterCapabilitiesEvent e) {
        Registry.registerCapabilities(e);
    }

    private void loadComplete(FMLLoadCompleteEvent e) {

    }
}
