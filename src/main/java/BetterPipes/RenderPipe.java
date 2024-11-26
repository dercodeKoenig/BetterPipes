package BetterPipes;

import com.ibm.icu.impl.Pair;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
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
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import static ARLib.obj.GroupObject.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL;
import static BetterPipes.BetterPipes.MODID;
import static net.minecraft.client.renderer.RenderStateShard.*;

public class RenderPipe implements BlockEntityRenderer<EntityPipe> {
ResourceLocation tex = ResourceLocation.fromNamespaceAndPath(MODID,"textures/block/fluid_pipe1.png");

    public RenderPipe(BlockEntityRendererProvider.Context c){
        super();
    }
    float e = 0.001f;
    void renderTopConnection(EntityPipe tile, VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay){
        if(tile.connections.get(Direction.UP).isEnabled){
            v.addVertex(stack.last(),-0.25f, 0.25f, -0.25f).setNormal(0,0,-1).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.25f, -0.25f).setNormal(0,0,-1).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.5f, -0.25f).setNormal(0,0,-1).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, 0.5f, -0.25f).setNormal(0,0,-1).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),-0.25f, 0.25f, 0.25f).setNormal(0,0,1).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.25f, 0.25f).setNormal(0,0,1).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.5f, 0.25f).setNormal(0,0,1).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, 0.5f, 0.25f).setNormal(0,0,1).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),-0.25f, 0.25f, -0.25f).setNormal(-1,0,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, 0.25f, 0.25f).setNormal(-1,0,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, 0.5f, 0.25f).setNormal(-1,0,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, 0.5f, -0.25f).setNormal(-1,0,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),0.25f, 0.25f, -0.25f).setNormal(1,0,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.25f, 0.25f).setNormal(1,0,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.5f, 0.25f).setNormal(1,0,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.5f, -0.25f).setNormal(1,0,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

        }else{
            v.addVertex(stack.last(),0.25f, 0.25f, 0.25f).setNormal(0,1,0).setUv(1f,1f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, 0.25f, 0.25f).setNormal(0,1,0).setUv(0f,1f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, 0.25f, -0.25f).setNormal(0,1,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.25f, -0.25f).setNormal(0,1,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
        }
    }
    void renderBottomConnection(EntityPipe tile, VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay){
        if(tile.connections.get(Direction.DOWN).isEnabled){
            v.addVertex(stack.last(),-0.25f, -0.25f, -0.25f).setNormal(0,0,-1).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, -0.25f).setNormal(0,0,-1).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.5f, -0.25f).setNormal(0,0,-1).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.5f, -0.25f).setNormal(0,0,-1).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),-0.25f, -0.25f, 0.25f).setNormal(0,0,1).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, 0.25f).setNormal(0,0,1).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.5f, 0.25f).setNormal(0,0,1).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.5f, 0.25f).setNormal(0,0,1).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),-0.25f, -0.25f, -0.25f).setNormal(-1,0,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, 0.25f).setNormal(-1,0,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.5f, 0.25f).setNormal(-1,0,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.5f, -0.25f).setNormal(-1,0,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),0.25f, -0.25f, -0.25f).setNormal(1,0,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, 0.25f).setNormal(1,0,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.5f, 0.25f).setNormal(1,0,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.5f, -0.25f).setNormal(1,0,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

        }else{
            v.addVertex(stack.last(),0.25f, -0.25f, 0.25f).setNormal(0,-1,0).setUv(1f,1f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, 0.25f).setNormal(0,-1,0).setUv(0f,1f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, -0.25f).setNormal(0,-1,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, -0.25f).setNormal(0,-1,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
        }
    }

    void renderEastConnection(EntityPipe tile, VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay){
        if(tile.connections.get(Direction.EAST).isEnabled){
            v.addVertex(stack.last(),0.25f, 0.25f, -0.25f).setNormal(0,0,-1).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, -0.25f).setNormal(0,0,-1).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.5f, -0.25f, -0.25f).setNormal(0,0,-1).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.5f, 0.25f, -0.25f).setNormal(0,0,-1).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),0.25f, 0.25f, 0.25f).setNormal(0,0,1).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, 0.25f).setNormal(0,0,1).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.5f, -0.25f, 0.25f).setNormal(0,0,1).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.5f, 0.25f, 0.25f).setNormal(0,0,1).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),0.25f, 0.25f, -0.25f).setNormal(0,1,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.25f, 0.25f).setNormal(0,1,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.5f, 0.25f, 0.25f).setNormal(0,1,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.5f, 0.25f, -0.25f).setNormal(0,1,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),0.25f, -0.25f, -0.25f).setNormal(0,-1,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, 0.25f).setNormal(0,-1,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.5f, -0.25f, 0.25f).setNormal(0,-1,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.5f, -0.25f, -0.25f).setNormal(0,-1,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

        }else{
            v.addVertex(stack.last(),0.25f, 0.25f, 0.25f).setNormal(1,0,0).setUv(1f,1f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, 0.25f).setNormal(1,0,0).setUv(0f,1f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, -0.25f).setNormal(1,0,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.25f, -0.25f).setNormal(1,0,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
        }
    }

    void renderWestConnection(EntityPipe tile, VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay){
        if(tile.connections.get(Direction.WEST).isEnabled){
            v.addVertex(stack.last(),-0.25f, 0.25f, -0.25f).setNormal(0,0,-1).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, -0.25f).setNormal(0,0,-1).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.5f, -0.25f, -0.25f).setNormal(0,0,-1).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.5f, 0.25f, -0.25f).setNormal(0,0,-1).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),-0.25f, 0.25f, 0.25f).setNormal(0,0,1).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, 0.25f).setNormal(0,0,1).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.5f, -0.25f, 0.25f).setNormal(0,0,1).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.5f, 0.25f, 0.25f).setNormal(0,0,1).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),-0.25f, 0.25f, -0.25f).setNormal(0,1,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, 0.25f, 0.25f).setNormal(0,1,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.5f, 0.25f, 0.25f).setNormal(0,1,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.5f, 0.25f, -0.25f).setNormal(0,1,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),-0.25f, -0.25f, -0.25f).setNormal(0,-1,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, 0.25f).setNormal(0,-1,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.5f, -0.25f, 0.25f).setNormal(0,-1,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.5f, -0.25f, -0.25f).setNormal(0,-1,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

        }else{
            v.addVertex(stack.last(),-0.25f, 0.25f, 0.25f).setNormal(-1,0,0).setUv(1f,1f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, 0.25f).setNormal(-1,0,0).setUv(0f,1f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, -0.25f).setNormal(-1,0,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, 0.25f, -0.25f).setNormal(-1,0,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
        }
    }
    void renderSouthConnection(EntityPipe tile, VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay){
        if(tile.connections.get(Direction.SOUTH).isEnabled){
            v.addVertex(stack.last(),-0.25f, 0.25f, 0.25f).setNormal(-1,0,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, 0.25f).setNormal(-1,0,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, 0.5f).setNormal(-1,0,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, 0.25f, 0.5f).setNormal(-1,0,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),0.25f, 0.25f, 0.25f).setNormal(1,0,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, 0.25f).setNormal(1,0,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, 0.5f).setNormal(1,0,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.25f, 0.5f).setNormal(1,0,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),-0.25f, 0.25f, 0.25f).setNormal(0,1,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.25f, 0.25f).setNormal(0,1,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.25f, 0.5f).setNormal(0,1,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, 0.25f, 0.5f).setNormal(0,1,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),-0.25f, -0.25f, 0.25f).setNormal(0,-1,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, 0.25f).setNormal(0,-1,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, 0.5f).setNormal(0,-1,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, 0.5f).setNormal(0,-1,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

        }else{
            v.addVertex(stack.last(),-0.25f, 0.25f, 0.25f).setNormal(0,0,1).setUv(1f,1f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.25f, 0.25f).setNormal(0,0,1).setUv(0f,1f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, 0.25f).setNormal(0,0,1).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, 0.25f).setNormal(0,0,1).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
        }
    }
    void renderNorthConnection(EntityPipe tile, VertexConsumer v, PoseStack stack, int packedLight, int packedOverlay){
        if(tile.connections.get(Direction.NORTH).isEnabled){
            v.addVertex(stack.last(),-0.25f, 0.25f, -0.25f).setNormal(-1,0,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, -0.25f).setNormal(-1,0,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, -0.5f).setNormal(-1,0,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, 0.25f, -0.5f).setNormal(-1,0,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),0.25f, 0.25f, -0.25f).setNormal(1,0,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, -0.25f).setNormal(1,0,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, -0.5f).setNormal(1,0,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.25f, -0.5f).setNormal(1,0,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),-0.25f, 0.25f, -0.25f).setNormal(0,1,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.25f, -0.25f).setNormal(0,1,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.25f, -0.5f).setNormal(0,1,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, 0.25f, -0.5f).setNormal(0,1,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

            v.addVertex(stack.last(),-0.25f, -0.25f, -0.25f).setNormal(0,-1,0).setUv(1f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, -0.25f).setNormal(0,-1,0).setUv(0f,0f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, -0.5f).setNormal(0,-1,0).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, -0.5f).setNormal(0,-1,0).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);

        }else{
            v.addVertex(stack.last(),-0.25f, 0.25f, -0.25f).setNormal(0,0,-1).setUv(1f,1f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, 0.25f, -0.25f).setNormal(0,0,-1).setUv(0f,1f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),0.25f, -0.25f, -0.25f).setNormal(0,0,-1).setUv(0f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
            v.addVertex(stack.last(),-0.25f, -0.25f, -0.25f).setNormal(0,0,-1).setUv(1f,0.5f).setOverlay(packedOverlay).setColor(0xFFFFFFFF).setLight(packedLight);
        }
    }

    void renderHorizontalFluidStill(
            double x0,double x1,double z0,double z1,double y0,double y1,
            TextureAtlasSprite fluidStill,
            int color,
            MultiBufferSource source, PoseStack stack, int packedLight, int packedOverlay,
            float y0BottomOffsetNorth,
            float y0BottomOffsetSouth,
            float y0BottomOffsetEast,
            float y0BottomOffsetWest
    ) {
        float x0f = (float) x0;
        float x1f = (float) x1;
        float y0f = (float) y0;
        float y1f = (float) y1;
        float z0f = (float) z0;
        float z1f = (float) z1;

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
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setOutputState(ITEM_ENTITY_TARGET)
                        .setTextureState(new TextureStateShard(fluidStill.atlasLocation(), false, true))
                        .createCompositeState(false)
        );
        VertexConsumer v = source.getBuffer(r);
        //render top face
        v.addVertex(stack.last(), x0f, y1f, z0f).setNormal(0, 1, 0).setUv(fluidStill.getU0(), fluidStill.getV0()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y1f, z1f).setNormal(0, 1, 0).setUv(fluidStill.getU0(), fluidStill.getV1()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y1f, z1f).setNormal(0, 1, 0).setUv(fluidStill.getU1(), fluidStill.getV1()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y1f, z0f).setNormal(0, 1, 0).setUv(fluidStill.getU1(), fluidStill.getV0()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

        //render bottom face
        v.addVertex(stack.last(), x1f, y0f, z0f).setNormal(0, -1, 0).setUv(fluidStill.getU1(), fluidStill.getV0()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x1f, y0f, z1f).setNormal(0, -1, 0).setUv(fluidStill.getU1(), fluidStill.getV1()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y0f, z1f).setNormal(0, -1, 0).setUv(fluidStill.getU0(), fluidStill.getV1()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        v.addVertex(stack.last(), x0f, y0f, z0f).setNormal(0, -1, 0).setUv(fluidStill.getU0(), fluidStill.getV0()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

        // Render east face (x+ side)
        if (y1 - y0BottomOffsetEast > e) {
            v.addVertex(stack.last(), x1f, y0BottomOffsetEast, z0f).setNormal(1, 0, 0).setUv(fluidStill.getU0(), fluidStill.getV1()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y1f, z0f).setNormal(1, 0, 0).setUv(fluidStill.getU0(), fluidStill.getV0()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y1f, z1f).setNormal(1, 0, 0).setUv(fluidStill.getU1(), fluidStill.getV0()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y0BottomOffsetEast, z1f).setNormal(1, 0, 0).setUv(fluidStill.getU1(), fluidStill.getV1()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
        // Render west face (x- side)
        if (y1 - y0BottomOffsetWest > e) {
            v.addVertex(stack.last(), x0f, y0BottomOffsetWest, z1f).setNormal(-1, 0, 0).setUv(fluidStill.getU1(), fluidStill.getV1()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y1f, z1f).setNormal(-1, 0, 0).setUv(fluidStill.getU1(), fluidStill.getV0()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y1f, z0f).setNormal(-1, 0, 0).setUv(fluidStill.getU0(), fluidStill.getV0()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y0BottomOffsetWest, z0f).setNormal(-1, 0, 0).setUv(fluidStill.getU0(), fluidStill.getV1()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
        // Render south face (z+ side)#
        if (y1 - y0BottomOffsetSouth > e) {
            v.addVertex(stack.last(), x1f, y0BottomOffsetSouth, z1f).setNormal(0, 0, 1).setUv(fluidStill.getU1(), fluidStill.getV1()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y1f, z1f).setNormal(0, 0, 1).setUv(fluidStill.getU1(), fluidStill.getV0()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y1f, z1f).setNormal(0, 0, 1).setUv(fluidStill.getU0(), fluidStill.getV0()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y0BottomOffsetSouth, z1f).setNormal(0, 0, 1).setUv(fluidStill.getU0(), fluidStill.getV1()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
        // Render north face (z- side)
        if (y1 - y0BottomOffsetNorth > e) {
            v.addVertex(stack.last(), x0f, y0BottomOffsetNorth, z0f).setNormal(0, 0, -1).setUv(fluidStill.getU0(), fluidStill.getV1()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x0f, y1f, z0f).setNormal(0, 0, -1).setUv(fluidStill.getU0(), fluidStill.getV0()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y1f, z0f).setNormal(0, 0, -1).setUv(fluidStill.getU1(), fluidStill.getV0()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), x1f, y0BottomOffsetNorth, z0f).setNormal(0, 0, -1).setUv(fluidStill.getU1(), fluidStill.getV1()).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
        }
    }
    void renderHorizontalFluidFlowing(
            float x0, float x1, float z0, float z1, float y0, float y1,
            TextureAtlasSprite fluidFlowing,
            int color, Direction flowDirection,
            MultiBufferSource source, PoseStack stack,
            int packedLight, int packedOverlay,
            float y0BottomOffsetNorth,
            float y0BottomOffsetSouth,
            float y0BottomOffsetEast,
            float y0BottomOffsetWest
    ) {

        RenderType r = RenderType.create("",
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
                        .setTextureState(new TextureStateShard(fluidFlowing.atlasLocation(), false, true))
                        .createCompositeState(false)
        );

        VertexConsumer v = source.getBuffer(r);

        float u0 = fluidFlowing.getU0();
        float u1 = fluidFlowing.getU1();
        float v0 = fluidFlowing.getV0();
        float v1 = fluidFlowing.getV1();


        // y1=y0+0.25f;
        //System.out.println(y1);

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
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z0).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z1).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render west face (x- side)
            if (y1 - y0BottomOffsetWest >e) {
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z1).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z0).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render south face (z+ side)
            if (y1 - y0BottomOffsetSouth >e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render north face (z- side)
            if (y1 - y0BottomOffsetNorth >e) {
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
            if (y1 - y0BottomOffsetEast >e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z0).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z1).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render west face (x- side)
            if (y1 - y0BottomOffsetWest >e) {
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z1).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z0).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render south face (z+ side)
            if (y1 - y0BottomOffsetSouth >e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render north face (z- side)
            if (y1 - y0BottomOffsetNorth >e) {
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
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render east face (x+ side)
            if (y1 - y0BottomOffsetEast >e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z0).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z1).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render west face (x- side)
            if (y1 - y0BottomOffsetWest >e) {
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z1).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z0).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render south face (z+ side)
            if (y1 - y0BottomOffsetSouth >e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render north face (z- side)
            if (y1 - y0BottomOffsetNorth >e) {
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
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            v.addVertex(stack.last(), (float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);

            // Render east face (x+ side)
            if (y1 - y0BottomOffsetEast >e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z0).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetEast, (float) z1).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render west face (x- side)
            if (y1 - y0BottomOffsetWest >e) {
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z1).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetWest, (float) z0).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render south face (z+ side)
            if (y1 - y0BottomOffsetSouth >e) {
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
            // Render north face (z- side)
            if (y1 - y0BottomOffsetNorth >e) {
                v.addVertex(stack.last(), (float) x0, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
                v.addVertex(stack.last(), (float) x1, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setOverlay(packedOverlay).setLight(packedLight);
            }
        }
    }



    void renderFluids(EntityPipe tile, MultiBufferSource source, PoseStack stack, int packedLight, int packedOverlay) {
        if(!tile.mainTank.isEmpty()) {
            IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(tile.mainTank.getFluid().getFluid());
            int color = extensions.getTintColor();
            ResourceLocation fluidtextureStill = extensions.getStillTexture();
            TextureAtlasSprite spriteStill = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidtextureStill);
            ResourceLocation fluidtextureFlowing = extensions.getFlowingTexture();
            TextureAtlasSprite spriteFlowing = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidtextureFlowing);


            if (
                    !tile.connections.get(Direction.NORTH).isEnabled &&
                            !tile.connections.get(Direction.SOUTH).isEnabled &&
                            !tile.connections.get(Direction.WEST).isEnabled &&
                            !tile.connections.get(Direction.EAST).isEnabled
            ) {
                // vertical render
            } else {
                // horizontal render
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
                Direction outFow = null;
                Direction inFlow = null;
                int numOutputs = 0;
                int numInputs = 0;
                PipeConnection conn;
                conn = tile.connections.get(Direction.NORTH);
                if (conn.isEnabled) {
                    y0BottomOffsetNorth = y0 - 2 * e + 0.5f * (float) conn.tank.getFluidAmount() / conn.tank.getCapacity();
                    if (conn.outputsToOutside) {
                        numOutputs++;
                        outFow = Direction.NORTH;
                    }
                    if (conn.outputsToInside) {
                        numInputs++;
                        inFlow = Direction.NORTH;
                    }
                }
                conn = tile.connections.get(Direction.SOUTH);
                if (conn.isEnabled) {
                    y0BottomOffsetSouth = y0 - 2 * e + 0.5f * (float) conn.tank.getFluidAmount() / conn.tank.getCapacity();
                    if (conn.outputsToOutside) {
                        numOutputs++;
                        outFow = Direction.SOUTH;
                    }
                    if (conn.outputsToInside) {
                        numInputs++;
                        inFlow = Direction.SOUTH;
                    }
                }
                conn = tile.connections.get(Direction.EAST);
                if (conn.isEnabled) {
                    y0BottomOffsetEast = y0 - 2 * e + 0.5f * (float) conn.tank.getFluidAmount() / conn.tank.getCapacity();
                    if (conn.outputsToOutside) {
                        numOutputs++;
                        outFow = Direction.EAST;
                    }
                    if (conn.outputsToInside) {
                        numInputs++;
                        inFlow = Direction.EAST;
                    }
                }
                conn = tile.connections.get(Direction.WEST);
                if (conn.isEnabled) {
                    y0BottomOffsetWest = y0 - 2 * e + 0.5f * (float) conn.tank.getFluidAmount() / conn.tank.getCapacity();
                    if (conn.outputsToOutside) {
                        numOutputs++;
                        outFow = Direction.WEST;
                    }
                    if (conn.outputsToInside) {
                        numInputs++;
                        inFlow = Direction.WEST;
                    }
                }
                if (numOutputs == 1) {
                    // exactly one output is found, great! use this as flow direction
                    renderHorizontalFluidFlowing(x0, x1, z0, z1, y0, y1, spriteFlowing, color, outFow, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);

                } else if (numInputs == 1) {
                    // exactly one input is found, great! use this as flow direction
                    renderHorizontalFluidFlowing(x0, x1, z0, z1, y0, y1, spriteFlowing, color, inFlow, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);

                } else {
                    // do not use flowing animation
                    renderHorizontalFluidStill(x0, x1, z0, z1, y0, y1, spriteStill, color, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                }

            }
        }
        if (tile.connections.get(Direction.UP).isEnabled) {

        }
        if (tile.connections.get(Direction.DOWN).isEnabled) {

        }
        if (tile.connections.get(Direction.WEST).isEnabled) {
            PipeConnection conn = tile.connections.get(Direction.WEST);
            int fluidInTank = conn.tank.getFluidAmount();
            if (fluidInTank > 0) {
                int max = conn.tank.getCapacity();
                float relativeFill = (float) fluidInTank / max;
                IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(conn.getFluidInTank(0).getFluid());
                int color = extensions.getTintColor();
                ResourceLocation fluidtextureStill = extensions.getStillTexture();
                TextureAtlasSprite spriteStill = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidtextureStill);
                ResourceLocation fluidtextureFlowing = extensions.getFlowingTexture();
                TextureAtlasSprite spriteFlowing = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidtextureFlowing);

                float x0 = -0.5f;
                float x1 = -0.25f;
                float z0 = -0.25f + e;
                float z1 = 0.25f - e;
                float y0 = -0.25f + e;
                float y1 = -0.25f - e + 0.5f * relativeFill;
                float y0BottomOffsetNorth = y0;
                float y0BottomOffsetSouth = y0;
                float y0BottomOffsetWest = y0;
                float y0BottomOffsetEast =y0-2*e+ 0.5f*(float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();
                if (conn.neighborFluidHandler instanceof PipeConnection p)
                    y0BottomOffsetWest = y0-2*e+0.5f*(float) p.tank.getFluidAmount() / p.tank.getCapacity();

                if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                    renderHorizontalFluidFlowing(x0, x1, z0, z1, y0, y1, spriteFlowing, color, Direction.EAST, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                    renderHorizontalFluidFlowing(x0, x1, z0, z1, y0, y1, spriteFlowing, color, Direction.WEST, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                } else {
                    renderHorizontalFluidStill(x0, x1, z0, z1, y0, y1, spriteStill, color, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                }
            }
        }
        if (tile.connections.get(Direction.EAST).isEnabled) {
            PipeConnection conn = tile.connections.get(Direction.EAST);
            int fluidInTank = conn.tank.getFluidAmount();
            if (fluidInTank > 0) {
                int max = conn.tank.getCapacity();
                float relativeFill = (float) fluidInTank / max;
                IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(conn.getFluidInTank(0).getFluid());
                int color = extensions.getTintColor();
                ResourceLocation fluidtextureStill = extensions.getStillTexture();
                TextureAtlasSprite spriteStill = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidtextureStill);
                ResourceLocation fluidtextureFlowing = extensions.getFlowingTexture();
                TextureAtlasSprite spriteFlowing = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidtextureFlowing);

                float x0 = 0.25f;
                float x1 = 0.5f;
                float z0 = -0.25f + e;
                float z1 = 0.25f - e;
                float y0 = -0.25f + e;
                float y1 = -0.25f - e + 0.5f * relativeFill;
                float y0BottomOffsetNorth = y0;
                float y0BottomOffsetSouth = y0;
                float y0BottomOffsetWest = y0-2*e+0.5f*(float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();;
                float y0BottomOffsetEast = y0;
                if (conn.neighborFluidHandler instanceof PipeConnection p)
                    y0BottomOffsetEast = y0-2*e+0.5f*(float) p.tank.getFluidAmount() / p.tank.getCapacity();

                if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                    renderHorizontalFluidFlowing(x0, x1, z0, z1, y0, y1, spriteFlowing, color, Direction.WEST, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                    renderHorizontalFluidFlowing(x0, x1, z0, z1, y0, y1, spriteFlowing, color, Direction.EAST, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                } else {
                    renderHorizontalFluidStill(x0, x1, z0, z1, y0, y1, spriteStill, color, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                }
            }
        }
        if (tile.connections.get(Direction.SOUTH).isEnabled) {
            PipeConnection conn = tile.connections.get(Direction.SOUTH);
            int fluidInTank = conn.tank.getFluidAmount();
            if (fluidInTank > 0) {
                int max = conn.tank.getCapacity();
                float relativeFill = (float) fluidInTank / max;
                IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(conn.getFluidInTank(0).getFluid());
                int color = extensions.getTintColor();
                ResourceLocation fluidtextureStill = extensions.getStillTexture();
                TextureAtlasSprite spriteStill = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidtextureStill);
                ResourceLocation fluidtextureFlowing = extensions.getFlowingTexture();
                TextureAtlasSprite spriteFlowing = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidtextureFlowing);

                float x0 = -0.25f+e;
                float x1 = 0.25f-e;
                float z0 = 0.25f;
                float z1 = 0.5f;
                float y0 = -0.25f + e;
                float y1 = -0.25f - e + 0.5f * relativeFill;
                float y0BottomOffsetNorth = y0-2*e+0.5f*(float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();
                float y0BottomOffsetSouth = y0;
                float y0BottomOffsetWest = y0;
                float y0BottomOffsetEast = y0;
                if (conn.neighborFluidHandler instanceof PipeConnection p)
                    y0BottomOffsetSouth = y0-2*e+0.5f*(float) p.tank.getFluidAmount() / p.tank.getCapacity();

                if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                    renderHorizontalFluidFlowing(x0, x1, z0, z1, y0, y1, spriteFlowing, color, Direction.NORTH, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                    renderHorizontalFluidFlowing(x0, x1, z0, z1, y0, y1, spriteFlowing, color, Direction.SOUTH, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                } else {
                    renderHorizontalFluidStill(x0, x1, z0, z1, y0, y1, spriteStill, color, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                }
            }
        }
        if (tile.connections.get(Direction.NORTH).isEnabled) {
            PipeConnection conn = tile.connections.get(Direction.NORTH);
            int fluidInTank = conn.tank.getFluidAmount();
            if (fluidInTank > 0) {
                int max = conn.tank.getCapacity();
                float relativeFill = (float) fluidInTank / max;
                IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(conn.getFluidInTank(0).getFluid());
                int color = extensions.getTintColor();
                ResourceLocation fluidtextureStill = extensions.getStillTexture();
                TextureAtlasSprite spriteStill = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidtextureStill);
                ResourceLocation fluidtextureFlowing = extensions.getFlowingTexture();
                TextureAtlasSprite spriteFlowing = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidtextureFlowing);

                float x0 = -0.25f+e;
                float x1 = 0.25f-e;
                float z0 = -0.5f;
                float z1 = -0.25f;
                float y0 = -0.25f + e;
                float y1 = -0.25f - e + 0.5f * relativeFill;
                float y0BottomOffsetNorth = y0;
                float y0BottomOffsetSouth = y0-2*e+0.5f*(float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();
                float y0BottomOffsetWest = y0;;
                float y0BottomOffsetEast = y0;
                if (conn.neighborFluidHandler instanceof PipeConnection p)
                    y0BottomOffsetNorth = y0-2*e+0.5f*(float) p.tank.getFluidAmount() / p.tank.getCapacity();

                if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                    renderHorizontalFluidFlowing(x0, x1, z0, z1, y0, y1, spriteFlowing, color, Direction.SOUTH, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                    renderHorizontalFluidFlowing(x0, x1, z0, z1, y0, y1, spriteFlowing, color, Direction.NORTH, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                } else {
                    renderHorizontalFluidStill(x0, x1, z0, z1, y0, y1, spriteStill, color, source, stack, packedLight, packedOverlay,
                            y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                }
            }
        }
    }
    @Override
    public void render(EntityPipe tile, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        stack.translate(0.5f,0.5f,0.5f);
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
                        .setTextureState(new TextureStateShard(tex,false,true))
                        .createCompositeState(false)
        );

        VertexConsumer v = bufferSource.getBuffer(r);
        renderTopConnection(tile, v,stack,packedLight,packedOverlay);
        renderBottomConnection(tile, v,stack,packedLight,packedOverlay);
        renderNorthConnection(tile, v,stack,packedLight,packedOverlay);
        renderSouthConnection(tile, v,stack,packedLight,packedOverlay);
        renderEastConnection(tile, v,stack,packedLight,packedOverlay);
        renderWestConnection(tile, v,stack,packedLight,packedOverlay);

        // if it has only y connections, render vertical fluid
        renderFluids(tile,bufferSource,stack,packedLight,packedOverlay);


    }

}