package BetterPipes;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import static ARLib.obj.GroupObject.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL;
import static BetterPipes.BetterPipes.MODID;
import static net.minecraft.client.renderer.RenderStateShard.*;

public class RenderPipe implements BlockEntityRenderer<EntityPipe> {
ResourceLocation tex = ResourceLocation.fromNamespaceAndPath(MODID,"textures/block/fluid_pipe1.png");

    public RenderPipe(BlockEntityRendererProvider.Context c){
        super();
    }


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

    @Override
    public void render(EntityPipe tile, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        stack.translate(0.5f,0.5f,0.5f);
        RenderType r = RenderType.create("renderer_235646whatever",
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

    }

}