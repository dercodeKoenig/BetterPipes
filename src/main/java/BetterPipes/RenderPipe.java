package BetterPipes;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import static BetterPipes.BetterPipes.MODID;
import static net.minecraft.client.renderer.RenderStateShard.*;

public class RenderPipe implements BlockEntityRenderer<EntityPipe> {
    ResourceLocation tex = ResourceLocation.fromNamespaceAndPath(MODID, "textures/block/fluid_pipe1.png");

    public static VertexFormat POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("Color", VertexFormatElement.COLOR)
            .add("UV0", VertexFormatElement.UV0)
            .add("UV1", VertexFormatElement.UV1)
            .add("UV2", VertexFormatElement.UV2)
            .add("Normal", VertexFormatElement.NORMAL)
            .build();

    public static fluidRenderData makeFluidRenderType(Fluid f, String sx) {
        if (f == Fluids.EMPTY) f = Fluids.WATER;
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(f);
        int color = extensions.getTintColor();
        ResourceLocation fluidtextureStill = extensions.getStillTexture();
        TextureAtlasSprite spriteStill = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidtextureStill);
        ResourceLocation fluidtextureFlowing = extensions.getFlowingTexture();
        TextureAtlasSprite spriteFlowing = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidtextureFlowing);

        fluidRenderData d = new fluidRenderData();
        d.spriteFLowing = spriteFlowing;
        d.spriteStill = spriteStill;
        d.color = color;
        d.renderTypeStill = RenderType.create("fluidStillPipeRenderer_" + fluidtextureStill.getPath() + "_" + sx,
                POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.Mode.QUADS,
                RenderType.TRANSIENT_BUFFER_SIZE,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setOverlayState(OVERLAY)
                        .setLightmapState(LIGHTMAP)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setOutputState(ITEM_ENTITY_TARGET)
                        .setTextureState(new TextureStateShard(spriteStill.atlasLocation(), false, true))
                        .createCompositeState(false)
        );
        d.renderTypeFlowing = RenderType.create("fluidFlowingPipeRenderer_" + fluidtextureFlowing.getPath() + "_" + sx,
                POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.Mode.QUADS,
                RenderType.TRANSIENT_BUFFER_SIZE,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setOverlayState(OVERLAY)
                        .setLightmapState(LIGHTMAP)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setOutputState(ITEM_ENTITY_TARGET)
                        .setTextureState(new TextureStateShard(spriteFlowing.atlasLocation(), false, true))
                        .createCompositeState(false)
        );
        return d;
    }

    static float e = 0.002f;
    static float wMin = 0.02f;
    static float wMax = 0.25f - e;

    public RenderPipe(BlockEntityRendererProvider.Context c) {
        super();
    }

    void renderTopConnection(EntityPipe tile, VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay) {
        if (tile.connections.get(Direction.UP).isEnabled) {
            float v0 = 0.25f;
            float v1 = 0.5f;
            if (tile.connections.get(Direction.UP).isExtraction) {
                v0 = 0f;
                v1 = 0.25f;
            }

            v.addVertex(stack.last(), -0.25f, 0.25f, -0.25f).setNormal(0, 0, -1).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.25f, -0.25f).setNormal(0, 0, -1).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.5f, -0.25f).setNormal(0, 0, -1).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, 0.5f, -0.25f).setNormal(0, 0, -1).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), -0.25f, 0.25f, 0.25f).setNormal(0, 0, 1).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.25f, 0.25f).setNormal(0, 0, 1).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.5f, 0.25f).setNormal(0, 0, 1).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, 0.5f, 0.25f).setNormal(0, 0, 1).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), -0.25f, 0.25f, -0.25f).setNormal(-1, 0, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, 0.25f, 0.25f).setNormal(-1, 0, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, 0.5f, 0.25f).setNormal(-1, 0, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, 0.5f, -0.25f).setNormal(-1, 0, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), 0.25f, 0.25f, -0.25f).setNormal(1, 0, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.25f, 0.25f).setNormal(1, 0, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.5f, 0.25f).setNormal(1, 0, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.5f, -0.25f).setNormal(1, 0, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

        } else {
            float u0 = 0f;
            float u1 = 0.5f;
            float v0 = 0.5f;
            float v1 = 1f;
            if (tile.isExtractionMode && !tile.isExtractionActive) {
                u0 = 0.5f;
                u1 = 1f;
                v0 = 0f;
                v1 = 0.5f;
            }
            if (tile.isExtractionMode && tile.isExtractionActive) {
                u0 = 0.5f;
                u1 = 1f;
                v0 = 0.5f;
                v1 = 1f;
            }

            v.addVertex(stack.last(), 0.25f, 0.25f, 0.25f).setNormal(0, 1, 0).setUv(u1, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, 0.25f, 0.25f).setNormal(0, 1, 0).setUv(u0, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, 0.25f, -0.25f).setNormal(0, 1, 0).setUv(u0, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.25f, -0.25f).setNormal(0, 1, 0).setUv(u1, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
        }
    }

    void renderBottomConnection(EntityPipe tile, VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay) {
        if (tile.connections.get(Direction.DOWN).isEnabled) {
            float v0 = 0.25f;
            float v1 = 0.5f;
            if (tile.connections.get(Direction.DOWN).isExtraction) {
                v0 = 0f;
                v1 = 0.25f;
            }
            v.addVertex(stack.last(), -0.25f, -0.25f, -0.25f).setNormal(0, 0, -1).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, -0.25f).setNormal(0, 0, -1).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.5f, -0.25f).setNormal(0, 0, -1).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.5f, -0.25f).setNormal(0, 0, -1).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), -0.25f, -0.25f, 0.25f).setNormal(0, 0, 1).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, 0.25f).setNormal(0, 0, 1).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.5f, 0.25f).setNormal(0, 0, 1).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.5f, 0.25f).setNormal(0, 0, 1).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), -0.25f, -0.25f, -0.25f).setNormal(-1, 0, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, 0.25f).setNormal(-1, 0, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.5f, 0.25f).setNormal(-1, 0, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.5f, -0.25f).setNormal(-1, 0, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), 0.25f, -0.25f, -0.25f).setNormal(1, 0, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, 0.25f).setNormal(1, 0, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.5f, 0.25f).setNormal(1, 0, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.5f, -0.25f).setNormal(1, 0, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

        } else {
            float u0 = 0f;
            float u1 = 0.5f;
            float v0 = 0.5f;
            float v1 = 1f;
            if (tile.isExtractionMode && !tile.isExtractionActive) {
                u0 = 0.5f;
                u1 = 1f;
                v0 = 0f;
                v1 = 0.5f;
            }
            if (tile.isExtractionMode && tile.isExtractionActive) {
                u0 = 0.5f;
                u1 = 1f;
                v0 = 0.5f;
                v1 = 1f;
            }
            v.addVertex(stack.last(), 0.25f, -0.25f, 0.25f).setNormal(0, -1, 0).setUv(u1, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, 0.25f).setNormal(0, -1, 0).setUv(u0, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, -0.25f).setNormal(0, -1, 0).setUv(u0, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, -0.25f).setNormal(0, -1, 0).setUv(u1, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
        }
    }

    void renderEastConnection(EntityPipe tile, VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay) {
        if (tile.connections.get(Direction.EAST).isEnabled) {
            float v0 = 0.25f;
            float v1 = 0.5f;
            if (tile.connections.get(Direction.EAST).isExtraction) {
                v0 = 0f;
                v1 = 0.25f;
            }
            v.addVertex(stack.last(), 0.25f, 0.25f, -0.25f).setNormal(0, 0, -1).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, -0.25f).setNormal(0, 0, -1).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.5f, -0.25f, -0.25f).setNormal(0, 0, -1).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.5f, 0.25f, -0.25f).setNormal(0, 0, -1).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), 0.25f, 0.25f, 0.25f).setNormal(0, 0, 1).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, 0.25f).setNormal(0, 0, 1).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.5f, -0.25f, 0.25f).setNormal(0, 0, 1).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.5f, 0.25f, 0.25f).setNormal(0, 0, 1).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), 0.25f, 0.25f, -0.25f).setNormal(0, 1, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.25f, 0.25f).setNormal(0, 1, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.5f, 0.25f, 0.25f).setNormal(0, 1, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.5f, 0.25f, -0.25f).setNormal(0, 1, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), 0.25f, -0.25f, -0.25f).setNormal(0, -1, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, 0.25f).setNormal(0, -1, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.5f, -0.25f, 0.25f).setNormal(0, -1, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.5f, -0.25f, -0.25f).setNormal(0, -1, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

        } else {
            float u0 = 0f;
            float u1 = 0.5f;
            float v0 = 0.5f;
            float v1 = 1f;
            if (tile.isExtractionMode && !tile.isExtractionActive) {
                u0 = 0.5f;
                u1 = 1f;
                v0 = 0f;
                v1 = 0.5f;
            }
            if (tile.isExtractionMode && tile.isExtractionActive) {
                u0 = 0.5f;
                u1 = 1f;
                v0 = 0.5f;
                v1 = 1f;
            }
            v.addVertex(stack.last(), 0.25f, 0.25f, 0.25f).setNormal(1, 0, 0).setUv(u1, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, 0.25f).setNormal(1, 0, 0).setUv(u0, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, -0.25f).setNormal(1, 0, 0).setUv(u0, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.25f, -0.25f).setNormal(1, 0, 0).setUv(u1, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
        }
    }

    void renderWestConnection(EntityPipe tile, VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay) {
        if (tile.connections.get(Direction.WEST).isEnabled) {
            float v0 = 0.25f;
            float v1 = 0.5f;
            if (tile.connections.get(Direction.WEST).isExtraction) {
                v0 = 0f;
                v1 = 0.25f;
            }
            v.addVertex(stack.last(), -0.25f, 0.25f, -0.25f).setNormal(0, 0, -1).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, -0.25f).setNormal(0, 0, -1).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.5f, -0.25f, -0.25f).setNormal(0, 0, -1).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.5f, 0.25f, -0.25f).setNormal(0, 0, -1).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), -0.25f, 0.25f, 0.25f).setNormal(0, 0, 1).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, 0.25f).setNormal(0, 0, 1).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.5f, -0.25f, 0.25f).setNormal(0, 0, 1).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.5f, 0.25f, 0.25f).setNormal(0, 0, 1).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), -0.25f, 0.25f, -0.25f).setNormal(0, 1, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, 0.25f, 0.25f).setNormal(0, 1, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.5f, 0.25f, 0.25f).setNormal(0, 1, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.5f, 0.25f, -0.25f).setNormal(0, 1, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), -0.25f, -0.25f, -0.25f).setNormal(0, -1, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, 0.25f).setNormal(0, -1, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.5f, -0.25f, 0.25f).setNormal(0, -1, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.5f, -0.25f, -0.25f).setNormal(0, -1, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

        } else {
            float u0 = 0f;
            float u1 = 0.5f;
            float v0 = 0.5f;
            float v1 = 1f;
            if (tile.isExtractionMode && !tile.isExtractionActive) {
                u0 = 0.5f;
                u1 = 1f;
                v0 = 0f;
                v1 = 0.5f;
            }
            if (tile.isExtractionMode && tile.isExtractionActive) {
                u0 = 0.5f;
                u1 = 1f;
                v0 = 0.5f;
                v1 = 1f;
            }
            v.addVertex(stack.last(), -0.25f, 0.25f, 0.25f).setNormal(-1, 0, 0).setUv(u1, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, 0.25f).setNormal(-1, 0, 0).setUv(u0, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, -0.25f).setNormal(-1, 0, 0).setUv(u0, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, 0.25f, -0.25f).setNormal(-1, 0, 0).setUv(u1, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
        }
    }

    void renderSouthConnection(EntityPipe tile, VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay) {
        if (tile.connections.get(Direction.SOUTH).isEnabled) {
            float v0 = 0.25f;
            float v1 = 0.5f;
            if (tile.connections.get(Direction.SOUTH).isExtraction) {
                v0 = 0f;
                v1 = 0.25f;
            }
            v.addVertex(stack.last(), -0.25f, 0.25f, 0.25f).setNormal(-1, 0, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, 0.25f).setNormal(-1, 0, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, 0.5f).setNormal(-1, 0, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, 0.25f, 0.5f).setNormal(-1, 0, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), 0.25f, 0.25f, 0.25f).setNormal(1, 0, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, 0.25f).setNormal(1, 0, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, 0.5f).setNormal(1, 0, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.25f, 0.5f).setNormal(1, 0, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), -0.25f, 0.25f, 0.25f).setNormal(0, 1, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.25f, 0.25f).setNormal(0, 1, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.25f, 0.5f).setNormal(0, 1, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, 0.25f, 0.5f).setNormal(0, 1, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), -0.25f, -0.25f, 0.25f).setNormal(0, -1, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, 0.25f).setNormal(0, -1, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, 0.5f).setNormal(0, -1, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, 0.5f).setNormal(0, -1, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

        } else {
            float u0 = 0f;
            float u1 = 0.5f;
            float v0 = 0.5f;
            float v1 = 1f;
            if (tile.isExtractionMode && !tile.isExtractionActive) {
                u0 = 0.5f;
                u1 = 1f;
                v0 = 0f;
                v1 = 0.5f;
            }
            if (tile.isExtractionMode && tile.isExtractionActive) {
                u0 = 0.5f;
                u1 = 1f;
                v0 = 0.5f;
                v1 = 1f;
            }
            v.addVertex(stack.last(), -0.25f, 0.25f, 0.25f).setNormal(0, 0, 1).setUv(u1, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.25f, 0.25f).setNormal(0, 0, 1).setUv(u0, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, 0.25f).setNormal(0, 0, 1).setUv(u0, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, 0.25f).setNormal(0, 0, 1).setUv(u1, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
        }
    }

    void renderNorthConnection(EntityPipe tile, VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay) {
        if (tile.connections.get(Direction.NORTH).isEnabled) {
            float v0 = 0.25f;
            float v1 = 0.5f;
            if (tile.connections.get(Direction.NORTH).isExtraction) {
                v0 = 0f;
                v1 = 0.25f;
            }
            v.addVertex(stack.last(), -0.25f, 0.25f, -0.25f).setNormal(-1, 0, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, -0.25f).setNormal(-1, 0, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, -0.5f).setNormal(-1, 0, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, 0.25f, -0.5f).setNormal(-1, 0, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), 0.25f, 0.25f, -0.25f).setNormal(1, 0, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, -0.25f).setNormal(1, 0, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, -0.5f).setNormal(1, 0, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.25f, -0.5f).setNormal(1, 0, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), -0.25f, 0.25f, -0.25f).setNormal(0, 1, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.25f, -0.25f).setNormal(0, 1, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.25f, -0.5f).setNormal(0, 1, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, 0.25f, -0.5f).setNormal(0, 1, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(), -0.25f, -0.25f, -0.25f).setNormal(0, -1, 0).setUv(0.5f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, -0.25f).setNormal(0, -1, 0).setUv(0f, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, -0.5f).setNormal(0, -1, 0).setUv(0f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, -0.5f).setNormal(0, -1, 0).setUv(0.5f, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

        } else {
            float u0 = 0f;
            float u1 = 0.5f;
            float v0 = 0.5f;
            float v1 = 1f;
            if (tile.isExtractionMode && !tile.isExtractionActive) {
                u0 = 0.5f;
                u1 = 1f;
                v0 = 0f;
                v1 = 0.5f;
            }
            if (tile.isExtractionMode && tile.isExtractionActive) {
                u0 = 0.5f;
                u1 = 1f;
                v0 = 0.5f;
                v1 = 1f;
            }
            v.addVertex(stack.last(), -0.25f, 0.25f, -0.25f).setNormal(0, 0, -1).setUv(u1, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, 0.25f, -0.25f).setNormal(0, 0, -1).setUv(u0, v1).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), 0.25f, -0.25f, -0.25f).setNormal(0, 0, -1).setUv(u0, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(), -0.25f, -0.25f, -0.25f).setNormal(0, 0, -1).setUv(u1, v0).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
        }
    }

    void renderFluidCubeStill(
            float x0, float x1, float z0, float z1, float y0, float y1,
            float u0, float u1, float v0, float v1,
            int color,
            VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay
    ) {


        //render up face
        v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

        //render bottom face
        v.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

        // Render east face (x+ side)
        v.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

        // Render west face (x- side)
        v.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

        // Render south face (z+ side)
        v.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

        // Render north face (z- side)
        v.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

    }

    void renderFluidCubeFacebyDirection(
            float x0, float x1, float z0, float z1, float y0, float y1,
            float u0, float u1, float v0, float v1,
            Direction d,
            int color,
            VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay
    ) {

        if (d == Direction.UP) {
            //render up face
            v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
        if (d == Direction.DOWN) {
            //render bottom face
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }

        if (d == Direction.EAST) {
            // Render east face (x+ side)
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
        if (d == Direction.WEST) {
            // Render west face (x- side)
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
        if (d == Direction.SOUTH) {
            // Render south face (z+ side)
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
        if (d == Direction.NORTH) {
            // Render north face (z- side)
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
    }

    void renderVerticalFluidStill(
            float x0f, float x1f, float z0f, float z1f, float y0f, float y1f,
            float u0, float u1, float v0, float v1,
            int color,
            VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay
    ) {

        // Render east face (x+ side)
        v.addVertex(stack.last(), x1f, y0f, z0f).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y1f, z0f).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y1f, z1f).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y0f, z1f).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

        // Render west face (x- side)
        v.addVertex(stack.last(), x0f, y0f, z1f).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y1f, z1f).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y1f, z0f).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y0f, z0f).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

        // Render south face (z+ side)#
        v.addVertex(stack.last(), x1f, y0f, z1f).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y1f, z1f).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y1f, z1f).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y0f, z1f).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

        // Render north face (z- side)
        v.addVertex(stack.last(), x0f, y0f, z0f).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y1f, z0f).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y1f, z0f).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y0f, z0f).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
    }

    void renderHorizontalFluidStill(
            float x0f, float x1f, float z0f, float z1f, float y0f, float y1f,
            float u0, float u1, float v0, float v1,
            int color,
            VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay,
            float y0BottomOffsetNorth,
            float y0BottomOffsetSouth,
            float y0BottomOffsetEast,
            float y0BottomOffsetWest
    ) {


        y1f = Math.max(y1f, y0f + 5 * e);


        //render top face
        v.addVertex(stack.last(), x0f, y1f, z0f).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y1f, z1f).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y1f, z1f).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y1f, z0f).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

        //render bottom face
        v.addVertex(stack.last(), x1f, y0f, z0f).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y0f, z1f).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y0f, z1f).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y0f, z0f).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

        // Render east face (x+ side)
        if (y1f - y0BottomOffsetEast > e) {
            v.addVertex(stack.last(), x1f, y0BottomOffsetEast, z0f).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y1f, z0f).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y1f, z1f).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y0BottomOffsetEast, z1f).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
        // Render west face (x- side)
        if (y1f - y0BottomOffsetWest > e) {
            v.addVertex(stack.last(), x0f, y0BottomOffsetWest, z1f).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y1f, z1f).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y1f, z0f).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y0BottomOffsetWest, z0f).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
        // Render south face (z+ side)#
        if (y1f - y0BottomOffsetSouth > e) {
            v.addVertex(stack.last(), x1f, y0BottomOffsetSouth, z1f).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y1f, z1f).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y1f, z1f).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y0BottomOffsetSouth, z1f).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
        // Render north face (z- side)
        if (y1f - y0BottomOffsetNorth > e) {
            v.addVertex(stack.last(), x0f, y0BottomOffsetNorth, z0f).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y1f, z0f).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y1f, z0f).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y0BottomOffsetNorth, z0f).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
    }
    /*
    // Helper to interpolate UV coordinates
    float interpolate(float outerStart, float outerEnd, float innerStart, float innerEnd, float value) {
        return ((value - outerStart) / (outerEnd - outerStart)) * (innerEnd - innerStart) + innerStart;
    }
    void renderHorizontalFaceCutOut(
            float x0,float x1,float z0,float z1,float y1,
            float xh0, float xh1, float zh0, float zh1,
            float u0,float u1,float v0,float v1,
            PoseStack stack, VertexConsumer vx2,
            int color, int packedLight, int packedOverlay){

// Interpolated UVs for the hole
        float uh0 = interpolate(x0, x1, u0, u1, xh0);
        float uh1 = interpolate(x0, x1, u0, u1, xh1);
        float vh0 = interpolate(z0, z1, v0, v1, zh0);
        float vh1 = interpolate(z0, z1, v0, v1, zh1);

// Render the 8 surrounding quads
// Quad 1: Top-left
        vx2.addVertex(stack.last(), x0, y1, z0).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, z0).setNormal(0, 1, 0).setUv(uh0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, zh0).setNormal(0, 1, 0).setUv(uh0, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x0, y1, zh0).setNormal(0, 1, 0).setUv(u0, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

// Quad 2: Top
        vx2.addVertex(stack.last(), xh0, y1, z0).setNormal(0, 1, 0).setUv(uh0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh1, y1, z0).setNormal(0, 1, 0).setUv(uh1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh1, y1, zh0).setNormal(0, 1, 0).setUv(uh1, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, zh0).setNormal(0, 1, 0).setUv(uh0, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

// Quad 3: Top-right
        vx2.addVertex(stack.last(), xh1, y1, z0).setNormal(0, 1, 0).setUv(uh1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x1, y1, z0).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x1, y1, zh0).setNormal(0, 1, 0).setUv(u1, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh1, y1, zh0).setNormal(0, 1, 0).setUv(uh1, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

// Quad 4: Right
        vx2.addVertex(stack.last(), xh1, y1, zh0).setNormal(0, 1, 0).setUv(uh1, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x1, y1, zh0).setNormal(0, 1, 0).setUv(u1, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x1, y1, zh1).setNormal(0, 1, 0).setUv(u1, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh1, y1, zh1).setNormal(0, 1, 0).setUv(uh1, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

// Quad 5: Bottom-right
        vx2.addVertex(stack.last(), xh1, y1, zh1).setNormal(0, 1, 0).setUv(uh1, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x1, y1, zh1).setNormal(0, 1, 0).setUv(u1, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x1, y1, z1).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh1, y1, z1).setNormal(0, 1, 0).setUv(uh1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

// Quad 6: Bottom
        vx2.addVertex(stack.last(), xh0, y1, zh1).setNormal(0, 1, 0).setUv(uh0, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh1, y1, zh1).setNormal(0, 1, 0).setUv(uh1, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh1, y1, z1).setNormal(0, 1, 0).setUv(uh1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, z1).setNormal(0, 1, 0).setUv(uh0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

// Quad 7: Bottom-left
        vx2.addVertex(stack.last(), x0, y1, zh1).setNormal(0, 1, 0).setUv(u0, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, zh1).setNormal(0, 1, 0).setUv(uh0, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, z1).setNormal(0, 1, 0).setUv(uh0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x0, y1, z1).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

// Quad 8: Left
        vx2.addVertex(stack.last(), x0, y1, zh0).setNormal(0, 1, 0).setUv(u0, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, zh0).setNormal(0, 1, 0).setUv(uh0, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, zh1).setNormal(0, 1, 0).setUv(uh0, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x0, y1, zh1).setNormal(0, 1, 0).setUv(u0, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

    }
    void renderHorizontalFaceCutOutReverse(
            float x0,float x1,float z0,float z1,float y1,
            float xh0, float xh1, float zh0, float zh1,
            float u0,float u1,float v0,float v1,
            PoseStack stack, VertexConsumer vx2,
            int color, int packedLight, int packedOverlay){

// Interpolated UVs for the hole
        float uh0 = interpolate(x0, x1, u0, u1, xh0);
        float uh1 = interpolate(x0, x1, u0, u1, xh1);
        float vh0 = interpolate(z0, z1, v0, v1, zh0);
        float vh1 = interpolate(z0, z1, v0, v1, zh1);

// Render the 8 surrounding quads
// Quad 1: Top-left
        vx2.addVertex(stack.last(), x0, y1, zh0).setNormal(0, 1, 0).setUv(u0, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, zh0).setNormal(0, 1, 0).setUv(uh0, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, z0).setNormal(0, 1, 0).setUv(uh0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x0, y1, z0).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

// Quad 2: Top
        vx2.addVertex(stack.last(), xh0, y1, zh0).setNormal(0, 1, 0).setUv(uh0, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh1, y1, zh0).setNormal(0, 1, 0).setUv(uh1, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh1, y1, z0).setNormal(0, 1, 0).setUv(uh1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, z0).setNormal(0, 1, 0).setUv(uh0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

// Quad 3: Top-right
        vx2.addVertex(stack.last(), xh1, y1, zh0).setNormal(0, 1, 0).setUv(uh1, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x1, y1, zh0).setNormal(0, 1, 0).setUv(u1, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x1, y1, z0).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh1, y1, z0).setNormal(0, 1, 0).setUv(uh1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

// Quad 4: Right
        vx2.addVertex(stack.last(), xh1, y1, zh1).setNormal(0, 1, 0).setUv(uh1, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x1, y1, zh1).setNormal(0, 1, 0).setUv(u1, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x1, y1, zh0).setNormal(0, 1, 0).setUv(u1, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh1, y1, zh0).setNormal(0, 1, 0).setUv(uh1, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

// Quad 5: Bottom-right
        vx2.addVertex(stack.last(), xh1, y1, z1).setNormal(0, 1, 0).setUv(uh1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x1, y1, z1).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x1, y1, zh1).setNormal(0, 1, 0).setUv(u1, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh1, y1, zh1).setNormal(0, 1, 0).setUv(uh1, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

// Quad 6: Bottom
        vx2.addVertex(stack.last(), xh0, y1, z1).setNormal(0, 1, 0).setUv(uh0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh1, y1, z1).setNormal(0, 1, 0).setUv(uh1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh1, y1, zh1).setNormal(0, 1, 0).setUv(uh1, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, zh1).setNormal(0, 1, 0).setUv(uh0, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

// Quad 7: Bottom-left
        vx2.addVertex(stack.last(), x0, y1, z1).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, z1).setNormal(0, 1, 0).setUv(uh0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, zh1).setNormal(0, 1, 0).setUv(uh0, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x0, y1, zh1).setNormal(0, 1, 0).setUv(u0, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

// Quad 8: Left
        vx2.addVertex(stack.last(), x0, y1, zh1).setNormal(0, 1, 0).setUv(u0, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, zh1).setNormal(0, 1, 0).setUv(uh0, vh1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), xh0, y1, zh0).setNormal(0, 1, 0).setUv(uh0, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        vx2.addVertex(stack.last(), x0, y1, zh0).setNormal(0, 1, 0).setUv(u0, vh0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

    }
    void renderVerticalFluidCutOutTopDownFace(
            TextureAtlasSprite fluidStill, MultiBufferSource source, PoseStack stack, int color, int light, int overlay,
            float x0,float x1,float z0,float z1,float y0,float y1,
            float xh0, float xh1, float zh0, float zh1,
            float xh2, float xh3, float zh2, float zh3,
            boolean rendery0,boolean rendery1
    ){
        RenderType r2 = RenderType.create("",
                POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.Mode.QUADS,
                32,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setOverlayState(OVERLAY)
                        .setLightmapState(LIGHTMAP)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setOutputState(ITEM_ENTITY_TARGET)
                        .setTextureState(new TextureStateShard(fluidStill.atlasLocation(), false, true))
                        .createCompositeState(false)
        );
        VertexConsumer vx2 = source.getBuffer(r2);
        if(rendery0)
            renderHorizontalFaceCutOut(x0,x1,z0,z1,y0,xh0,xh1,zh0,zh1,u0,u1,v0,v1,stack,vx2,color,light,overlay);
        if(rendery1)
            renderHorizontalFaceCutOutReverse(x0,x1,z0,z1,y1,xh2,xh3,zh2,zh3,u0,u1,v0,v1,stack,vx2,color,light,overlay);
    }
     */


    void renderHorizontalFluidFlowing(
            float x0, float x1, float z0, float z1, float y0, float y1,
            float u0, float u1, float v0, float v1,
            int color, Direction flowDirection,
            VertexConsumer v, PoseStack stack,
            int packedLight, int packedOverlay,
            float y0BottomOffsetNorth,
            float y0BottomOffsetSouth,
            float y0BottomOffsetEast,
            float y0BottomOffsetWest
    ) {
        y1 = Math.max(y1, y0 + 5 * e);

        if (flowDirection == Direction.NORTH) {
            //render top face
            v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            //render bottom face
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render east face (x+ side)
            if (y1 - y0BottomOffsetEast > e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z0).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z1).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render west face (x- side)
            if (y1 - y0BottomOffsetWest > e) {
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z1).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z0).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render south face (z+ side)
            if (y1 - y0BottomOffsetSouth > e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render north face (z- side)
            if (y1 - y0BottomOffsetNorth > e) {
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
        }

        if (flowDirection == Direction.SOUTH) {
            //render top face
            v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            //render bottom face
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render east face (x+ side)
            if (y1 - y0BottomOffsetEast > e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z0).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z1).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render west face (x- side)
            if (y1 - y0BottomOffsetWest > e) {
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z1).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z0).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render south face (z+ side)
            if (y1 - y0BottomOffsetSouth > e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render north face (z- side)
            if (y1 - y0BottomOffsetNorth > e) {
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
        }
        if (flowDirection == Direction.EAST) {
            //render top face
            v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            //render bottom face
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render east face (x+ side)
            if (y1 - y0BottomOffsetEast > e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z0).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z1).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render west face (x- side)
            if (y1 - y0BottomOffsetWest > e) {
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z1).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z0).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render south face (z+ side)
            if (y1 - y0BottomOffsetSouth > e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render north face (z- side)
            if (y1 - y0BottomOffsetNorth > e) {
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
        }
        if (flowDirection == Direction.WEST) {
            //render top face
            v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            //render bottom face
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render east face (x+ side)
            if (y1 - y0BottomOffsetEast > e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z0).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z1).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render west face (x- side)
            if (y1 - y0BottomOffsetWest > e) {
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z1).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z0).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render south face (z+ side)
            if (y1 - y0BottomOffsetSouth > e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render north face (z- side)
            if (y1 - y0BottomOffsetNorth > e) {
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
        }
    }


    void renderHorizontalFluidStillCentered(
            float x0f, float x1f, float z0f, float z1f, float y0f, float y1f,
            float u0, float u1, float v0, float v1,
            int color,
            VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay,
            Direction direction
    ) {

        //render top face
        v.addVertex(stack.last(), x0f, y1f, z0f).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y1f, z1f).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y1f, z1f).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y1f, z0f).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

        //render bottom face
        v.addVertex(stack.last(), x1f, y0f, z0f).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y0f, z1f).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y0f, z1f).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y0f, z0f).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

        if (direction != Direction.EAST && direction != Direction.WEST) {
            // Render east face (x+ side)
            v.addVertex(stack.last(), x1f, y0f, z0f).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y1f, z0f).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y1f, z1f).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y0f, z1f).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render west face (x- side)
            v.addVertex(stack.last(), x0f, y0f, z1f).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y1f, z1f).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y1f, z0f).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y0f, z0f).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
        if (direction != Direction.NORTH && direction != Direction.SOUTH) {
            // Render south face (z+ side)#
            v.addVertex(stack.last(), x1f, y0f, z1f).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y1f, z1f).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y1f, z1f).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y0f, z1f).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render north face (z- side)
            v.addVertex(stack.last(), x0f, y0f, z0f).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y1f, z0f).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y1f, z0f).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y0f, z0f).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
    }

    void renderFluidFlowingCentered(
            float x0, float x1, float z0, float z1, float y0, float y1,
            float u0, float u1, float v0, float v1,
            int color, Direction flowDirection,
            VertexConsumer vx1, PoseStack stack,
            int packedLight, int packedOverlay
    ) {

        if (flowDirection == Direction.NORTH) {
            // Render east face (x+ side)
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render west face (x- side)
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render up  face (y+ side)
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render down face (y- side)
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }

        if (flowDirection == Direction.SOUTH) {
            // Render east face (x+ side)
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render west face (x- side)
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render up  face (y+ side)
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render down face (y- side)
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }

        if (flowDirection == Direction.EAST) {
            // Render south face (z+ side)
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render north face (z- side)
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render up  face (y+ side)
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render down face (y- side)
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }

        if (flowDirection == Direction.WEST) {
            // Render south face (z+ side)
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render north face (z- side)
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render up  face (y+ side)
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render down face (y- side)
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }

        if (flowDirection == Direction.UP) {
            // Render east face (x+ side)
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render west face (x- side)
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render south face (z+ side)
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render north face (z- side)
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }

        if (flowDirection == Direction.DOWN) {
            // Render east face (x+ side)
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render west face (x- side)
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render south face (z+ side)
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render north face (z- side)
            vx1.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            vx1.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
    }

    boolean shouldRenderCentered(Fluid f) {
        return true;
        //return f.getFluidType().isLighterThanAir();
    }

    void renderFluids(EntityPipe tile, MultiBufferSource source, PoseStack stack, int packedLight, int packedOverlay) {
        // render center
        if (!tile.mainTank.isEmpty()) {

            float u0f = tile.renderData.spriteFLowing.getU0();
            float u1f = tile.renderData.spriteFLowing.getU1();
            float v0f = tile.renderData.spriteFLowing.getV0();
            float v1f = tile.renderData.spriteFLowing.getV1();

            float u0s = tile.renderData.spriteStill.getU0();
            float u1s = tile.renderData.spriteStill.getU1();
            float v0s = tile.renderData.spriteStill.getV0();
            float v1s = tile.renderData.spriteStill.getV1();

            int color = tile.renderData.color;


            if (
                    !tile.connections.get(Direction.NORTH).isEnabled &&
                            !tile.connections.get(Direction.SOUTH).isEnabled &&
                            !tile.connections.get(Direction.WEST).isEnabled &&
                            !tile.connections.get(Direction.EAST).isEnabled
            ) {
                // is vertical render because no horizontal connections are found

                float relativeFill = (float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();


                float actualW = wMin + (wMax - wMin) * relativeFill;
                float y0 = -0.25f;
                float y1 = 0.25f;
                float x0 = -actualW;
                float x1 = actualW;
                float z0 = -actualW;
                float z1 = actualW;
/*
                float relativeFillAbove = (float) tile.connections.get(Direction.UP).tank.getFluidAmount() / tile.connections.get(Direction.UP).tank.getCapacity();
                float actualWAbove = wMin + (wMax - wMin) * relativeFillAbove;
                float xh2 = -actualWAbove;
                float xh3 = actualWAbove;
                float zh2 = -actualWAbove;
                float zh3 = actualWAbove;
                boolean renderAbove = actualWAbove < actualW;

                float relativeFillBelow = (float) tile.connections.get(Direction.DOWN).tank.getFluidAmount() / tile.connections.get(Direction.DOWN).tank.getCapacity();
                float actualWBelow = wMin + (wMax - wMin) * relativeFillBelow;
                float xh0 = -actualWBelow;
                float xh1 = actualWBelow;
                float zh0 = -actualWBelow;
                float zh1 = actualWBelow;
                boolean renderBelow = actualWBelow < actualW;

                renderVerticalFluidCutOutTopDownFace(spriteStill,source,stack,color,packedLight,packedOverlay,
                        x0,x1,z0,z1,y0,y1,
                        xh0,xh1,zh0,zh1,
                        xh2,xh3,zh2,zh3,
                        renderBelow,renderAbove);
 */

                // first scan if it has only one output to use this as flow direction, second look if it has only one input to use as flow direction
                Direction outFlow = null;
                Direction inFlow = null;
                int numOutputs = 0;
                int numInputs = 0;
                PipeConnection conn;
                conn = tile.connections.get(Direction.UP);
                if (conn.getsInputFromInside) {
                    outFlow = Direction.UP;
                    numOutputs++;
                }
                if (conn.getsInputFromOutside) {
                    inFlow = Direction.UP.getOpposite();
                    numInputs++;
                }
                conn = tile.connections.get(Direction.DOWN);
                if (conn.getsInputFromInside) {
                    outFlow = Direction.DOWN;
                    numOutputs++;
                }
                if (conn.getsInputFromOutside) {
                    inFlow = Direction.DOWN.getOpposite();
                    numInputs++;
                }
                if (numOutputs == 1) {
                    // exactly one output is found, great! use this as flow direction
                    renderFluidFlowingCentered(
                            x0, x1, z0, z1, y0, y1,
                            u0f, u1f, v0f, v1f,
                            color, outFlow, source.getBuffer(tile.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);

                } else if (numInputs == 1) {
                    // exactly one input is found, great! use this as flow direction
                    renderFluidFlowingCentered(
                            x0, x1, z0, z1, y0, y1,
                            u0f, u1f, v0f, v1f,
                            color, inFlow, source.getBuffer(tile.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                } else {
                    // do not use flowing animation
                    renderVerticalFluidStill(
                            x0, x1, z0, z1, y0, y1,
                            u0s, u1s, v0s, v1s,
                            color, source.getBuffer(tile.renderData.renderTypeStill), stack, packedLight, packedOverlay);
                }
            } else {
                if (!shouldRenderCentered(tile.mainTank.getFluid().getFluid())) {
                    // is horizontal render
                    float x0 = -0.25f + e;
                    float x1 = 0.25f - e;
                    float z0 = -0.25f + e;
                    float z1 = 0.25f - e;
                    float y0 = -0.25f + e;
                    float y1 = y0 - 2 * e + 0.5f * (float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();
                    float y0BottomOffsetNorth = y0;
                    float y0BottomOffsetSouth = y0;
                    float y0BottomOffsetWest = y0;
                    float y0BottomOffsetEast = y0;

                    // first scan if it has only one output to use this as flow direction
                    Direction outFlow = null;
                    Direction inFlow = null;
                    int numOutputs = 0;
                    int numInputs = 0;
                    PipeConnection conn;
                    conn = tile.connections.get(Direction.NORTH);
                    if (conn.isEnabled) {
                        y0BottomOffsetNorth = y0 - 2 * e + 0.5f * (float) conn.tank.getFluidAmount() / conn.tank.getCapacity();
                        if (conn.getsInputFromInside) {
                            numOutputs++;
                            outFlow = Direction.NORTH;
                        }
                        if (conn.getsInputFromOutside) {
                            numInputs++;
                            inFlow = Direction.NORTH.getOpposite();
                        }
                    }
                    conn = tile.connections.get(Direction.SOUTH);
                    if (conn.isEnabled) {
                        y0BottomOffsetSouth = y0 - 2 * e + 0.5f * (float) conn.tank.getFluidAmount() / conn.tank.getCapacity();
                        if (conn.getsInputFromInside) {
                            numOutputs++;
                            outFlow = Direction.SOUTH;
                        }
                        if (conn.getsInputFromOutside) {
                            numInputs++;
                            inFlow = Direction.SOUTH.getOpposite();
                        }
                    }
                    conn = tile.connections.get(Direction.EAST);
                    if (conn.isEnabled) {
                        y0BottomOffsetEast = y0 - 2 * e + 0.5f * (float) conn.tank.getFluidAmount() / conn.tank.getCapacity();
                        if (conn.getsInputFromInside) {
                            numOutputs++;
                            outFlow = Direction.EAST;
                        }
                        if (conn.getsInputFromOutside) {
                            numInputs++;
                            inFlow = Direction.EAST.getOpposite();
                        }
                    }
                    conn = tile.connections.get(Direction.WEST);
                    if (conn.isEnabled) {
                        y0BottomOffsetWest = y0 - 2 * e + 0.5f * (float) conn.tank.getFluidAmount() / conn.tank.getCapacity();
                        if (conn.getsInputFromInside) {
                            numOutputs++;
                            outFlow = Direction.WEST;
                        }
                        if (conn.getsInputFromOutside) {
                            numInputs++;
                            inFlow = Direction.WEST.getOpposite();
                        }
                    }
                    if (numOutputs == 1) {
                        // exactly one output is found, great! use this as flow direction
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, outFlow, source.getBuffer(tile.renderData.renderTypeFlowing), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);

                    } else if (numInputs == 1) {
                        // exactly one input is found, great! use this as flow direction
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, inFlow, source.getBuffer(tile.renderData.renderTypeFlowing), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);

                    } else {
                        // do not use flowing animation
                        renderHorizontalFluidStill(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, source.getBuffer(tile.renderData.renderTypeStill), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    }

                    if (tile.connections.get(Direction.UP).isEnabled) {
                        float relativeFill = (float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();

                        float actualW = wMin + (wMax - wMin) * relativeFill;
                        y0 = -0.25f + e;
                        y1 = 0.25f;
                        x0 = -actualW;
                        x1 = actualW;
                        z0 = -actualW;
                        z1 = actualW;
/*
                    float relativeFillAbove = (float) tile.connections.get(Direction.UP).tank.getFluidAmount() / tile.connections.get(Direction.UP).tank.getCapacity();
                    float actualWAbove = wMin + (wMax - wMin) * relativeFillAbove;
                    float xh2 = -actualWAbove;
                    float xh3 = actualWAbove;
                    float zh2 = -actualWAbove;
                    float zh3 = actualWAbove;
                    boolean renderAbove = actualWAbove < actualW;
   renderVerticalFluidCutOutTopDownFace(
                            spriteStill,source,stack,color,packedLight,packedOverlay,
                            x0,x1,z0,z1,y0,y1,
                            0,0,0,0,
                            xh2,xh3,zh2,zh3,
                            false,renderAbove
                    );
 */

                        conn = tile.connections.get(Direction.UP);
                        if (conn.getsInputFromInside) {
                            //add render up animation
                            renderFluidFlowingCentered(
                                    x0, x1, z0, z1, y0, y1,
                                    u0f, u1f, v0f, v1f,
                                    color, Direction.UP, source.getBuffer(tile.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                        }
                        if (conn.outputsToInside) {
                            //add render down animation
                            renderFluidFlowingCentered(
                                    x0, x1, z0, z1, y0, y1,
                                    u0f, u1f, v0f, v1f,
                                    color, Direction.DOWN, source.getBuffer(tile.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                        }
                    }
                } else {
                    // it is a centered horizontal render
                    Direction outFlow = null;
                    Direction inFlow = null;
                    int numOutputs = 0;
                    int numInputs = 0;

                    float relativeFill = (float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();

                    float actualW = wMin + (wMax - wMin) * relativeFill;
                    for (Direction d : Direction.values()) {
                        PipeConnection conn = tile.connections.get(d);
                        if (conn.isEnabled) {
                            float y0 = -actualW;
                            float y1 = actualW;
                            float x0 = -actualW;
                            float x1 = actualW;
                            float z0 = -actualW;
                            float z1 = actualW;

                            if (d == Direction.UP) {
                                y0 = actualW;
                                y1 = 0.25f;
                            }
                            if (d == Direction.DOWN) {
                                y0 = -0.25f;
                                y1 = -actualW;
                            }
                            if (d == Direction.EAST) {
                                x0 = actualW;
                                x1 = 0.25f;
                            }
                            if (d == Direction.WEST) {
                                x0 = -0.25f;
                                x1 = -actualW;
                            }
                            if (d == Direction.SOUTH) {
                                z0 = actualW;
                                z1 = 0.25f;
                            }
                            if (d == Direction.NORTH) {
                                z0 = -0.25f;
                                z1 = -actualW;
                            }
                            if (conn.outputsToInside) {
                                numInputs++;
                                inFlow = d;
                                renderFluidFlowingCentered(
                                        x0, x1, z0, z1, y0, y1,
                                        u0f, u1f, v0f, v1f,
                                        color, d.getOpposite(), source.getBuffer(tile.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                            } else if (conn.getsInputFromInside) {
                                numOutputs++;
                                outFlow = d;
                                renderFluidFlowingCentered(
                                        x0, x1, z0, z1, y0, y1,
                                        u0f, u1f, v0f, v1f
                                        , color, d, source.getBuffer(tile.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                            } else {
                                renderVerticalFluidStill(
                                        x0, x1, z0, z1, y0, y1,
                                        u0s, u1s, v0s, v1s,
                                        color, source.getBuffer(tile.renderData.renderTypeStill), stack, packedLight, packedOverlay);
                            }
                        }
                    }

                    float y0 = -actualW;
                    float y1 = actualW;
                    float x0 = -actualW;
                    float x1 = actualW;
                    float z0 = -actualW;
                    float z1 = actualW;

                    if (numOutputs == 1) {
                        // exactly one output is found, great! use this as flow direction
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, outFlow, source.getBuffer(tile.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);

                        if (!tile.connections.get(outFlow.getOpposite()).isEnabled || tile.connections.get(outFlow.getOpposite()).tank.isEmpty()) {
                            renderFluidCubeFacebyDirection(
                                    x0, x1, z0, z1, y0, y1,
                                    u0s, u1s, v0s, v1s,
                                    outFlow.getOpposite(), color, source.getBuffer(tile.renderData.renderTypeStill), stack, packedLight, packedOverlay);
                        }


                    } else if (numInputs == 1) {
                        // exactly one input is found, great! use this as flow direction
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, inFlow, source.getBuffer(tile.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);

                        if (!tile.connections.get(inFlow.getOpposite()).isEnabled || tile.connections.get(inFlow.getOpposite()).tank.isEmpty()) {
                            renderFluidCubeFacebyDirection(
                                    x0, x1, z0, z1, y0, y1,
                                    u0s, u1s, v0s, v1s,
                                    inFlow.getOpposite(), color, source.getBuffer(tile.renderData.renderTypeStill), stack, packedLight, packedOverlay);
                        }

                    } else {
                        // do not use flowing animation
                        renderFluidCubeStill(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, source.getBuffer(tile.renderData.renderTypeStill), stack, packedLight, packedOverlay);
                    }
                }
            }
        }

        //render connections
        if (tile.connections.get(Direction.UP).isEnabled) {
            PipeConnection conn = tile.connections.get(Direction.UP);

            float u0f = conn.renderData.spriteFLowing.getU0();
            float u1f = conn.renderData.spriteFLowing.getU1();
            float v0f = conn.renderData.spriteFLowing.getV0();
            float v1f = conn.renderData.spriteFLowing.getV1();

            float u0s = tile.renderData.spriteStill.getU0();
            float u1s = tile.renderData.spriteStill.getU1();
            float v0s = tile.renderData.spriteStill.getV0();
            float v1s = tile.renderData.spriteStill.getV1();

            int color = conn.renderData.color;

            int fluidInTank = conn.tank.getFluidAmount();
            if (fluidInTank > 0) {
                int max = conn.tank.getCapacity();
                float relativeFill = (float) fluidInTank / max;

                float actualW = wMin + (wMax - wMin) * relativeFill;
                float y0 = 0.25f - e;
                float y1 = 0.5f;
                float x0 = -actualW;
                float x1 = actualW;
                float z0 = -actualW;
                float z1 = actualW;


                if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                    renderFluidFlowingCentered(
                            x0, x1, z0, z1, y0, y1,
                            u0f, u1f, v0f, v1f,
                            color, Direction.DOWN, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                    renderFluidFlowingCentered(
                            x0, x1, z0, z1, y0, y1,
                            u0f, u1f, v0f, v1f,
                            color, Direction.UP, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                } else {
                    renderVerticalFluidStill(
                            x0, x1, z0, z1, y0, y1,
                            u0s, u1s, v0s, v1s,
                            color, source.getBuffer(conn.renderData.renderTypeStill), stack, packedLight, packedOverlay);
                }
/*
                float relativeFillAbove = (float) tile.connections.get(Direction.UP).neighborFluidHandler.getFluidInTank(0).getAmount() / tile.connections.get(Direction.UP).neighborFluidHandler.getTankCapacity(0);
                float actualWAbove = wMin + (wMax - wMin) * relativeFillAbove;
                float xh2 = -actualWAbove;
                float xh3 = actualWAbove;
                float zh2 = -actualWAbove;
                float zh3 = actualWAbove;
                boolean renderAbove = actualWAbove < actualW;

                float relativeFillBelow = (float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();
                float actualWBelow = wMin + (wMax - wMin) * relativeFillBelow;
                float xh0 = -actualWBelow;
                float xh1 = actualWBelow;
                float zh0 = -actualWBelow;
                float zh1 = actualWBelow;
                boolean renderBelow = actualWBelow < actualW;

                renderVerticalFluidCutOutTopDownFace(spriteStill,source,stack,color,packedLight,packedOverlay,
                        x0,x1,z0,z1,y0,y1,
                        xh0,xh1,zh0,zh1,
                        xh2,xh3,zh2,zh3,
                        renderBelow,renderAbove);
 */
            }
        }
        if (tile.connections.get(Direction.DOWN).isEnabled) {
            PipeConnection conn = tile.connections.get(Direction.DOWN);

            float u0f = conn.renderData.spriteFLowing.getU0();
            float u1f = conn.renderData.spriteFLowing.getU1();
            float v0f = conn.renderData.spriteFLowing.getV0();
            float v1f = conn.renderData.spriteFLowing.getV1();

            float u0s = tile.renderData.spriteStill.getU0();
            float u1s = tile.renderData.spriteStill.getU1();
            float v0s = tile.renderData.spriteStill.getV0();
            float v1s = tile.renderData.spriteStill.getV1();

            int color = conn.renderData.color;

            int fluidInTank = conn.tank.getFluidAmount();
            if (fluidInTank > 0) {
                int max = conn.tank.getCapacity();
                float relativeFill = (float) fluidInTank / max;

                float actualW = wMin + (wMax - wMin) * relativeFill;
                float y0 = -0.5f;
                float y1 = -0.25f + e;
                float x0 = -actualW;
                float x1 = actualW;
                float z0 = -actualW;
                float z1 = actualW;


                if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                    renderFluidFlowingCentered(
                            x0, x1, z0, z1, y0, y1,
                            u0f, u1f, v0f, v1f,
                            color, Direction.UP, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                    renderFluidFlowingCentered(
                            x0, x1, z0, z1, y0, y1,
                            u0f, u1f, v0f, v1f,
                            color, Direction.DOWN, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                } else {
                    renderVerticalFluidStill(
                            x0, x1, z0, z1, y0, y1,
                            u0s, u1s, v0s, v1s,
                            color, source.getBuffer(conn.renderData.renderTypeStill), stack, packedLight, packedOverlay);
                }
                /*
                float relativeFillAbove = (float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();
                float actualWAbove = wMin + (wMax - wMin) * relativeFillAbove;
                float xh2 = -actualWAbove;
                float xh3 = actualWAbove;
                float zh2 = -actualWAbove;
                float zh3 = actualWAbove;
                boolean renderAbove = actualWAbove < actualW;

                float relativeFillBelow = (float) tile.connections.get(Direction.DOWN).neighborFluidHandler.getFluidInTank(0).getAmount() / tile.connections.get(Direction.DOWN).neighborFluidHandler.getTankCapacity(0);
                float actualWBelow = wMin + (wMax - wMin) * relativeFillBelow;
                float xh0 = -actualWBelow;
                float xh1 = actualWBelow;
                float zh0 = -actualWBelow;
                float zh1 = actualWBelow;
                boolean renderBelow = actualWBelow < actualW;

                renderVerticalFluidCutOutTopDownFace(spriteStill,source,stack,color,packedLight,packedOverlay,
                        x0,x1,z0,z1,y0,y1,
                        xh0,xh1,zh0,zh1,
                        xh2,xh3,zh2,zh3,
                        renderBelow,renderAbove);
                 */
            }
        }
        if (tile.connections.get(Direction.WEST).isEnabled) {
            PipeConnection conn = tile.connections.get(Direction.WEST);

            float u0f = conn.renderData.spriteFLowing.getU0();
            float u1f = conn.renderData.spriteFLowing.getU1();
            float v0f = conn.renderData.spriteFLowing.getV0();
            float v1f = conn.renderData.spriteFLowing.getV1();

            float u0s = tile.renderData.spriteStill.getU0();
            float u1s = tile.renderData.spriteStill.getU1();
            float v0s = tile.renderData.spriteStill.getV0();
            float v1s = tile.renderData.spriteStill.getV1();

            int color = conn.renderData.color;

            int fluidInTank = conn.tank.getFluidAmount();
            if (fluidInTank > 0) {
                int max = conn.tank.getCapacity();
                float relativeFill = (float) fluidInTank / max;

                if (!shouldRenderCentered(conn.tank.getFluid().getFluid())) {
                    float x0 = -0.5f;
                    float x1 = -0.25f + e;
                    float z0 = -0.25f + e;
                    float z1 = 0.25f - e;
                    float y0 = -0.25f + e;
                    float y1 = -0.25f - e + 0.5f * relativeFill;
                    float y0BottomOffsetNorth = y0;
                    float y0BottomOffsetSouth = y0;
                    float y0BottomOffsetWest = y0;
                    float y0BottomOffsetEast = y0 - 2 * e + 0.5f * (float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();
                    if (conn.neighborFluidHandler instanceof PipeConnection p)
                        y0BottomOffsetWest = y0 - 2 * e + 0.5f * (float) p.tank.getFluidAmount() / p.tank.getCapacity();

                    if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.EAST, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.WEST, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else {
                        renderHorizontalFluidStill(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, source.getBuffer(conn.renderData.renderTypeStill), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    }
                } else {

                    float actualW = wMin + (wMax - wMin) * relativeFill;
                    float y0 = -actualW;
                    float y1 = actualW;
                    float x0 = -0.5f;
                    float x1 = -0.25f + e;
                    float z0 = -actualW;
                    float z1 = actualW;

                    if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.EAST, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.WEST, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                    } else {
                        renderHorizontalFluidStillCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, source.getBuffer(conn.renderData.renderTypeStill), stack, packedLight, packedOverlay, Direction.WEST);
                    }
                }
            }
        }
        if (tile.connections.get(Direction.EAST).isEnabled) {
            PipeConnection conn = tile.connections.get(Direction.EAST);

            float u0f = conn.renderData.spriteFLowing.getU0();
            float u1f = conn.renderData.spriteFLowing.getU1();
            float v0f = conn.renderData.spriteFLowing.getV0();
            float v1f = conn.renderData.spriteFLowing.getV1();

            float u0s = tile.renderData.spriteStill.getU0();
            float u1s = tile.renderData.spriteStill.getU1();
            float v0s = tile.renderData.spriteStill.getV0();
            float v1s = tile.renderData.spriteStill.getV1();

            int color = conn.renderData.color;

            int fluidInTank = conn.tank.getFluidAmount();
            if (fluidInTank > 0) {
                int max = conn.tank.getCapacity();
                float relativeFill = (float) fluidInTank / max;

                if (!shouldRenderCentered(conn.tank.getFluid().getFluid())) {
                    float x0 = 0.25f - e;
                    float x1 = 0.5f;
                    float z0 = -0.25f + e;
                    float z1 = 0.25f - e;
                    float y0 = -0.25f + e;
                    float y1 = -0.25f - e + 0.5f * relativeFill;
                    float y0BottomOffsetNorth = y0;
                    float y0BottomOffsetSouth = y0;
                    float y0BottomOffsetWest = y0 - 2 * e + 0.5f * (float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();
                    ;
                    float y0BottomOffsetEast = y0;
                    if (conn.neighborFluidHandler instanceof PipeConnection p)
                        y0BottomOffsetEast = y0 - 2 * e + 0.5f * (float) p.tank.getFluidAmount() / p.tank.getCapacity();

                    if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.WEST, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.EAST, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else {
                        renderHorizontalFluidStill(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, source.getBuffer(conn.renderData.renderTypeStill), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    }
                } else {

                    float actualW = wMin + (wMax - wMin) * relativeFill;
                    float y0 = -actualW;
                    float y1 = actualW;
                    float x0 = 0.25f - e;
                    float x1 = 0.5f;
                    float z0 = -actualW;
                    float z1 = actualW;

                    if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.WEST, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.EAST, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                    } else {
                        renderHorizontalFluidStillCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, source.getBuffer(conn.renderData.renderTypeStill), stack, packedLight, packedOverlay, Direction.EAST);
                    }
                }
            }
        }
        if (tile.connections.get(Direction.SOUTH).isEnabled) {
            PipeConnection conn = tile.connections.get(Direction.SOUTH);

            float u0f = conn.renderData.spriteFLowing.getU0();
            float u1f = conn.renderData.spriteFLowing.getU1();
            float v0f = conn.renderData.spriteFLowing.getV0();
            float v1f = conn.renderData.spriteFLowing.getV1();

            float u0s = conn.renderData.spriteStill.getU0();
            float u1s = conn.renderData.spriteStill.getU1();
            float v0s = conn.renderData.spriteStill.getV0();
            float v1s = conn.renderData.spriteStill.getV1();

            int color = conn.renderData.color;

            int fluidInTank = conn.tank.getFluidAmount();
            if (fluidInTank > 0) {
                int max = conn.tank.getCapacity();
                float relativeFill = (float) fluidInTank / max;

                if (!shouldRenderCentered(conn.tank.getFluid().getFluid())) {
                    float x0 = -0.25f + e;
                    float x1 = 0.25f - e;
                    float z0 = 0.25f - e;
                    float z1 = 0.5f;
                    float y0 = -0.25f + e;
                    float y1 = -0.25f - e + 0.5f * relativeFill;
                    float y0BottomOffsetNorth = y0 - 2 * e + 0.5f * (float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();
                    float y0BottomOffsetSouth = y0;
                    float y0BottomOffsetWest = y0;
                    float y0BottomOffsetEast = y0;
                    if (conn.neighborFluidHandler instanceof PipeConnection p)
                        y0BottomOffsetSouth = y0 - 2 * e + 0.5f * (float) p.tank.getFluidAmount() / p.tank.getCapacity();

                    if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.NORTH, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.SOUTH, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else {
                        renderHorizontalFluidStill(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, source.getBuffer(conn.renderData.renderTypeStill), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    }
                } else {


                    float actualW = wMin + (wMax - wMin) * relativeFill;
                    float y0 = -actualW;
                    float y1 = actualW;
                    float x0 = -actualW;
                    float x1 = actualW;
                    float z0 = 0.25f - e;
                    float z1 = 0.5f;

                    if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.NORTH, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.SOUTH, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                    } else {
                        renderHorizontalFluidStillCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, source.getBuffer(conn.renderData.renderTypeStill), stack, packedLight, packedOverlay, Direction.SOUTH);
                    }
                }
            }
        }
        if (tile.connections.get(Direction.NORTH).isEnabled) {
            PipeConnection conn = tile.connections.get(Direction.NORTH);

            float u0f = conn.renderData.spriteFLowing.getU0();
            float u1f = conn.renderData.spriteFLowing.getU1();
            float v0f = conn.renderData.spriteFLowing.getV0();
            float v1f = conn.renderData.spriteFLowing.getV1();

            float u0s = conn.renderData.spriteStill.getU0();
            float u1s = conn.renderData.spriteStill.getU1();
            float v0s = conn.renderData.spriteStill.getV0();
            float v1s = conn.renderData.spriteStill.getV1();

            int color = conn.renderData.color;

            int fluidInTank = conn.tank.getFluidAmount();
            if (fluidInTank > 0) {
                int max = conn.tank.getCapacity();
                float relativeFill = (float) fluidInTank / max;

                if (!shouldRenderCentered(conn.tank.getFluid().getFluid())) {
                    float x0 = -0.25f + e;
                    float x1 = 0.25f - e;
                    float z0 = -0.5f;
                    float z1 = -0.25f + e;
                    float y0 = -0.25f + e;
                    float y1 = -0.25f - e + 0.5f * relativeFill;
                    float y0BottomOffsetNorth = y0;
                    float y0BottomOffsetSouth = y0 - 2 * e + 0.5f * (float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();
                    float y0BottomOffsetWest = y0;
                    ;
                    float y0BottomOffsetEast = y0;
                    if (conn.neighborFluidHandler instanceof PipeConnection p)
                        y0BottomOffsetNorth = y0 - 2 * e + 0.5f * (float) p.tank.getFluidAmount() / p.tank.getCapacity();

                    if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.SOUTH, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.NORTH, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else {
                        renderHorizontalFluidStill(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, source.getBuffer(conn.renderData.renderTypeStill), stack, packedLight, packedOverlay,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    }
                } else {


                    float actualW = wMin + (wMax - wMin) * relativeFill;
                    float y0 = -actualW;
                    float y1 = actualW;
                    float x0 = -actualW;
                    float x1 = actualW;
                    float z0 = -0.5f;
                    float z1 = -0.25f + e;

                    if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.SOUTH, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.NORTH, source.getBuffer(conn.renderData.renderTypeFlowing), stack, packedLight, packedOverlay);
                    } else {
                        renderHorizontalFluidStillCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, source.getBuffer(conn.renderData.renderTypeStill), stack, packedLight, packedOverlay, Direction.NORTH);
                    }
                }
            }
        }
    }

    @Override
    public void render(EntityPipe tile, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        stack.translate(0.5f, 0.5f, 0.5f);
        RenderType r = RenderType.create("",
                POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.Mode.QUADS,
                RenderType.SMALL_BUFFER_SIZE,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setOverlayState(OVERLAY)
                        .setLightmapState(LIGHTMAP)
                        .setCullState(NO_CULL)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setTextureState(new TextureStateShard(tex, false, true))
                        .createCompositeState(false)
        );

        VertexConsumer v = bufferSource.getBuffer(r);
        renderTopConnection(tile, v, stack, packedLight, packedOverlay);
        renderBottomConnection(tile, v, stack, packedLight, packedOverlay);
        renderNorthConnection(tile, v, stack, packedLight, packedOverlay);
        renderSouthConnection(tile, v, stack, packedLight, packedOverlay);
        renderEastConnection(tile, v, stack, packedLight, packedOverlay);
        renderWestConnection(tile, v, stack, packedLight, packedOverlay);

        //tile.renderData = makeFluidRenderType(tile.mainTank.getFluid().getFluid(), "");
        //for (Direction i : Direction.values()) {
        //    tile.connections.get(i).renderData = makeFluidRenderType(tile.connections.get(i).tank.getFluid().getFluid(), "");
        //}


        renderFluids(tile, bufferSource, stack, packedLight, packedOverlay);
    }


}