package BetterPipes;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static BetterPipes.Registry.PIPE;
import static net.minecraft.client.renderer.RenderStateShard.*;

public class RenderPipe implements BlockEntityRenderer<EntityPipe> {

    public static VertexFormat POSITION_COLOR_TEXTURE_NORMAL = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("Color", VertexFormatElement.COLOR)
            .add("UV0", VertexFormatElement.UV0)
            .add("UV1", VertexFormatElement.UV1)
            .add("UV2", VertexFormatElement.UV2)
            .add("Normal", VertexFormatElement.NORMAL)
            .build();

    static float e = 0.001f;
    static float wMin = 0.02f;
    static float wMax = 0.25f - e;

    public RenderPipe(BlockEntityRendererProvider.Context c) {
        super();
    }


    public static void renderFluidCubeStill(
            float x0, float x1, float z0, float z1, float y0, float y1,
            float u0, float u1, float v0, float v1,
            int color,
            VertexConsumer v
    ) {


        //render up face
        v.addVertex(x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);

        //render bottom face
        v.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);

        // Render east face (x+ side)
        v.addVertex((float) x1, (float) y0, (float) z0).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x1, (float) y0, (float) z1).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);

        // Render west face (x- side)
        v.addVertex((float) x0, (float) y0, (float) z1).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x0, (float) y0, z0).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);

        // Render south face (z+ side)
        v.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);

        // Render north face (z- side)
        v.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);

    }

    public static void renderFluidCubeFacebyDirection(
            float x0, float x1, float z0, float z1, float y0, float y1,
            float u0, float u1, float v0, float v1,
            Direction d,
            int color,
            VertexConsumer v
    ) {

        if (d == Direction.UP) {
            //render up face
            v.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        }
        if (d == Direction.DOWN) {
            //render bottom face
            v.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        }

        if (d == Direction.EAST) {
            // Render east face (x+ side)
            v.addVertex((float) x1, (float) y0, (float) z0).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y0, (float) z1).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
        }
        if (d == Direction.WEST) {
            // Render west face (x- side)
            v.addVertex((float) x0, (float) y0, (float) z1).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y0, (float) z0).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        }
        if (d == Direction.SOUTH) {
            // Render south face (z+ side)
            v.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        }
        if (d == Direction.NORTH) {
            // Render north face (z- side)
            v.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        }
    }

    public static void renderVerticalFluidStill(
            float x0f, float x1f, float z0f, float z1f, float y0f, float y1f,
            float u0, float u1, float v0, float v1,
            int color,
            VertexConsumer v
    ) {

        // Render east face (x+ side)
        v.addVertex(x1f, y0f, z0f).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x1f, y1f, z0f).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x1f, y1f, z1f).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x1f, y0f, z1f).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);

        // Render west face (x- side)
        v.addVertex(x0f, y0f, z1f).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x0f, y1f, z1f).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x0f, y1f, z0f).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x0f, y0f, z0f).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);

        // Render south face (z+ side)#
        v.addVertex(x1f, y0f, z1f).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x1f, y1f, z1f).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x0f, y1f, z1f).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x0f, y0f, z1f).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);

        // Render north face (z- side)
        v.addVertex(x0f, y0f, z0f).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x0f, y1f, z0f).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x1f, y1f, z0f).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x1f, y0f, z0f).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
    }

    public static void renderHorizontalFluidStill(
            float x0f, float x1f, float z0f, float z1f, float y0f, float y1f,
            float u0, float u1, float v0, float v1,
            int color,
            VertexConsumer v,
            float y0BottomOffsetNorth,
            float y0BottomOffsetSouth,
            float y0BottomOffsetEast,
            float y0BottomOffsetWest
    ) {


        y1f = Math.max(y1f, y0f + 5 * e);


        //render top face
        v.addVertex(x0f, y1f, z0f).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x0f, y1f, z1f).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x1f, y1f, z1f).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x1f, y1f, z0f).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);

        //render bottom face
        v.addVertex(x1f, y0f, z0f).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x1f, y0f, z1f).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x0f, y0f, z1f).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x0f, y0f, z0f).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);

        // Render east face (x+ side)
        if (y1f - y0BottomOffsetEast > e) {
            v.addVertex(x1f, y0BottomOffsetEast, z0f).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x1f, y1f, z0f).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x1f, y1f, z1f).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x1f, y0BottomOffsetEast, z1f).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        }
        // Render west face (x- side)
        if (y1f - y0BottomOffsetWest > e) {
            v.addVertex(x0f, y0BottomOffsetWest, z1f).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x0f, y1f, z1f).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x0f, y1f, z0f).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x0f, y0BottomOffsetWest, z0f).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        }
        // Render south face (z+ side)#
        if (y1f - y0BottomOffsetSouth > e) {
            v.addVertex(x1f, y0BottomOffsetSouth, z1f).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x1f, y1f, z1f).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x0f, y1f, z1f).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x0f, y0BottomOffsetSouth, z1f).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        }
        // Render north face (z- side)
        if (y1f - y0BottomOffsetNorth > e) {
            v.addVertex(x0f, y0BottomOffsetNorth, z0f).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x0f, y1f, z0f).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x1f, y1f, z0f).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x1f, y0BottomOffsetNorth, z0f).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
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
        vx2.addVertex( x0, y1, z0).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, z0).setNormal(0, 1, 0).setUv(uh0, v0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, zh0).setNormal(0, 1, 0).setUv(uh0, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x0, y1, zh0).setNormal(0, 1, 0).setUv(u0, vh0).setColor(color).setLight(0).setOverlay(0);

// Quad 2: Top
        vx2.addVertex( xh0, y1, z0).setNormal(0, 1, 0).setUv(uh0, v0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh1, y1, z0).setNormal(0, 1, 0).setUv(uh1, v0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh1, y1, zh0).setNormal(0, 1, 0).setUv(uh1, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, zh0).setNormal(0, 1, 0).setUv(uh0, vh0).setColor(color).setLight(0).setOverlay(0);

// Quad 3: Top-right
        vx2.addVertex( xh1, y1, z0).setNormal(0, 1, 0).setUv(uh1, v0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x1, y1, z0).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x1, y1, zh0).setNormal(0, 1, 0).setUv(u1, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh1, y1, zh0).setNormal(0, 1, 0).setUv(uh1, vh0).setColor(color).setLight(0).setOverlay(0);

// Quad 4: Right
        vx2.addVertex( xh1, y1, zh0).setNormal(0, 1, 0).setUv(uh1, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x1, y1, zh0).setNormal(0, 1, 0).setUv(u1, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x1, y1, zh1).setNormal(0, 1, 0).setUv(u1, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh1, y1, zh1).setNormal(0, 1, 0).setUv(uh1, vh1).setColor(color).setLight(0).setOverlay(0);

// Quad 5: Bottom-right
        vx2.addVertex( xh1, y1, zh1).setNormal(0, 1, 0).setUv(uh1, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x1, y1, zh1).setNormal(0, 1, 0).setUv(u1, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x1, y1, z1).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh1, y1, z1).setNormal(0, 1, 0).setUv(uh1, v1).setColor(color).setLight(0).setOverlay(0);

// Quad 6: Bottom
        vx2.addVertex( xh0, y1, zh1).setNormal(0, 1, 0).setUv(uh0, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh1, y1, zh1).setNormal(0, 1, 0).setUv(uh1, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh1, y1, z1).setNormal(0, 1, 0).setUv(uh1, v1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, z1).setNormal(0, 1, 0).setUv(uh0, v1).setColor(color).setLight(0).setOverlay(0);

// Quad 7: Bottom-left
        vx2.addVertex( x0, y1, zh1).setNormal(0, 1, 0).setUv(u0, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, zh1).setNormal(0, 1, 0).setUv(uh0, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, z1).setNormal(0, 1, 0).setUv(uh0, v1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x0, y1, z1).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);

// Quad 8: Left
        vx2.addVertex( x0, y1, zh0).setNormal(0, 1, 0).setUv(u0, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, zh0).setNormal(0, 1, 0).setUv(uh0, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, zh1).setNormal(0, 1, 0).setUv(uh0, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x0, y1, zh1).setNormal(0, 1, 0).setUv(u0, vh1).setColor(color).setLight(0).setOverlay(0);

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
        vx2.addVertex( x0, y1, zh0).setNormal(0, 1, 0).setUv(u0, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, zh0).setNormal(0, 1, 0).setUv(uh0, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, z0).setNormal(0, 1, 0).setUv(uh0, v0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x0, y1, z0).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);

// Quad 2: Top
        vx2.addVertex( xh0, y1, zh0).setNormal(0, 1, 0).setUv(uh0, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh1, y1, zh0).setNormal(0, 1, 0).setUv(uh1, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh1, y1, z0).setNormal(0, 1, 0).setUv(uh1, v0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, z0).setNormal(0, 1, 0).setUv(uh0, v0).setColor(color).setLight(0).setOverlay(0);

// Quad 3: Top-right
        vx2.addVertex( xh1, y1, zh0).setNormal(0, 1, 0).setUv(uh1, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x1, y1, zh0).setNormal(0, 1, 0).setUv(u1, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x1, y1, z0).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh1, y1, z0).setNormal(0, 1, 0).setUv(uh1, v0).setColor(color).setLight(0).setOverlay(0);

// Quad 4: Right
        vx2.addVertex( xh1, y1, zh1).setNormal(0, 1, 0).setUv(uh1, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x1, y1, zh1).setNormal(0, 1, 0).setUv(u1, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x1, y1, zh0).setNormal(0, 1, 0).setUv(u1, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh1, y1, zh0).setNormal(0, 1, 0).setUv(uh1, vh0).setColor(color).setLight(0).setOverlay(0);

// Quad 5: Bottom-right
        vx2.addVertex( xh1, y1, z1).setNormal(0, 1, 0).setUv(uh1, v1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x1, y1, z1).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x1, y1, zh1).setNormal(0, 1, 0).setUv(u1, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh1, y1, zh1).setNormal(0, 1, 0).setUv(uh1, vh1).setColor(color).setLight(0).setOverlay(0);

// Quad 6: Bottom
        vx2.addVertex( xh0, y1, z1).setNormal(0, 1, 0).setUv(uh0, v1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh1, y1, z1).setNormal(0, 1, 0).setUv(uh1, v1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh1, y1, zh1).setNormal(0, 1, 0).setUv(uh1, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, zh1).setNormal(0, 1, 0).setUv(uh0, vh1).setColor(color).setLight(0).setOverlay(0);

// Quad 7: Bottom-left
        vx2.addVertex( x0, y1, z1).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, z1).setNormal(0, 1, 0).setUv(uh0, v1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, zh1).setNormal(0, 1, 0).setUv(uh0, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x0, y1, zh1).setNormal(0, 1, 0).setUv(u0, vh1).setColor(color).setLight(0).setOverlay(0);

// Quad 8: Left
        vx2.addVertex( x0, y1, zh1).setNormal(0, 1, 0).setUv(u0, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, zh1).setNormal(0, 1, 0).setUv(uh0, vh1).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( xh0, y1, zh0).setNormal(0, 1, 0).setUv(uh0, vh0).setColor(color).setLight(0).setOverlay(0);
        vx2.addVertex( x0, y1, zh0).setNormal(0, 1, 0).setUv(u0, vh0).setColor(color).setLight(0).setOverlay(0);

    }
    void renderVerticalFluidCutOutTopDownFace(
            TextureAtlasSprite fluidStill, MultiBufferSource source, int color, int light, int overlay,
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


    public static void renderHorizontalFluidFlowing(
            float x0, float x1, float z0, float z1, float y0, float y1,
            float u0, float u1, float v0, float v1,
            int color, Direction flowDirection,
            VertexConsumer v,
            float y0BottomOffsetNorth,
            float y0BottomOffsetSouth,
            float y0BottomOffsetEast,
            float y0BottomOffsetWest
    ) {
        y1 = Math.max(y1, y0 + 5 * e);

        if (flowDirection == Direction.NORTH) {
            //render top face
            v.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);

            //render bottom face
            v.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);

            // Render east face (x+ side)
            if (y1 - y0BottomOffsetEast > e) {
                v.addVertex((float) x1, (float) y0BottomOffsetEast, (float) z0).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y0BottomOffsetEast, (float) z1).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            }
            // Render west face (x- side)
            if (y1 - y0BottomOffsetWest > e) {
                v.addVertex((float) x0, (float) y0BottomOffsetWest, (float) z1).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y0BottomOffsetWest, (float) z0).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            }
            // Render south face (z+ side)
            if (y1 - y0BottomOffsetSouth > e) {
                v.addVertex((float) x1, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            }
            // Render north face (z- side)
            if (y1 - y0BottomOffsetNorth > e) {
                v.addVertex((float) x0, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            }
        }

        if (flowDirection == Direction.SOUTH) {
            //render top face
            v.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);

            //render bottom face
            v.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);

            // Render east face (x+ side)
            if (y1 - y0BottomOffsetEast > e) {
                v.addVertex((float) x1, (float) y0BottomOffsetEast, (float) z0).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y0BottomOffsetEast, (float) z1).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            }
            // Render west face (x- side)
            if (y1 - y0BottomOffsetWest > e) {
                v.addVertex((float) x0, (float) y0BottomOffsetWest, (float) z1).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y0BottomOffsetWest, (float) z0).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            }
            // Render south face (z+ side)
            if (y1 - y0BottomOffsetSouth > e) {
                v.addVertex((float) x1, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            }
            // Render north face (z- side)
            if (y1 - y0BottomOffsetNorth > e) {
                v.addVertex((float) x0, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            }
        }
        if (flowDirection == Direction.EAST) {
            //render top face
            v.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);

            //render bottom face
            v.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);

            // Render east face (x+ side)
            if (y1 - y0BottomOffsetEast > e) {
                v.addVertex((float) x1, (float) y0BottomOffsetEast, (float) z0).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y0BottomOffsetEast, (float) z1).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            }
            // Render west face (x- side)
            if (y1 - y0BottomOffsetWest > e) {
                v.addVertex((float) x0, (float) y0BottomOffsetWest, (float) z1).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y0BottomOffsetWest, (float) z0).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            }
            // Render south face (z+ side)
            if (y1 - y0BottomOffsetSouth > e) {
                v.addVertex((float) x1, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            }
            // Render north face (z- side)
            if (y1 - y0BottomOffsetNorth > e) {
                v.addVertex((float) x0, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            }
        }
        if (flowDirection == Direction.WEST) {
            //render top face
            v.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);

            //render bottom face
            v.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);

            // Render east face (x+ side)
            if (y1 - y0BottomOffsetEast > e) {
                v.addVertex((float) x1, (float) y0BottomOffsetEast, (float) z0).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y0BottomOffsetEast, (float) z1).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            }
            // Render west face (x- side)
            if (y1 - y0BottomOffsetWest > e) {
                v.addVertex((float) x0, (float) y0BottomOffsetWest, (float) z1).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y0BottomOffsetWest, (float) z0).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            }
            // Render south face (z+ side)
            if (y1 - y0BottomOffsetSouth > e) {
                v.addVertex((float) x1, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y0BottomOffsetSouth, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            }
            // Render north face (z- side)
            if (y1 - y0BottomOffsetNorth > e) {
                v.addVertex((float) x0, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
                v.addVertex((float) x1, (float) y0BottomOffsetNorth, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            }
        }
    }


    public static void renderHorizontalFluidStillCentered(
            float x0f, float x1f, float z0f, float z1f, float y0f, float y1f,
            float u0, float u1, float v0, float v1,
            int color,
            VertexConsumer v,
            Direction direction
    ) {

        //render top face
        v.addVertex(x0f, y1f, z0f).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x0f, y1f, z1f).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x1f, y1f, z1f).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x1f, y1f, z0f).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);

        //render bottom face
        v.addVertex(x1f, y0f, z0f).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x1f, y0f, z1f).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x0f, y0f, z1f).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        v.addVertex(x0f, y0f, z0f).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);

        if (direction != Direction.EAST && direction != Direction.WEST) {
            // Render east face (x+ side)
            v.addVertex(x1f, y0f, z0f).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x1f, y1f, z0f).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x1f, y1f, z1f).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x1f, y0f, z1f).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);

            // Render west face (x- side)
            v.addVertex(x0f, y0f, z1f).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x0f, y1f, z1f).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x0f, y1f, z0f).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x0f, y0f, z0f).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        }
        if (direction != Direction.NORTH && direction != Direction.SOUTH) {
            // Render south face (z+ side)#
            v.addVertex(x1f, y0f, z1f).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x1f, y1f, z1f).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x0f, y1f, z1f).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x0f, y0f, z1f).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);

            // Render north face (z- side)
            v.addVertex(x0f, y0f, z0f).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x0f, y1f, z0f).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x1f, y1f, z0f).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            v.addVertex(x1f, y0f, z0f).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        }
    }

    public static void renderFluidFlowingCentered(
            float x0, float x1, float z0, float z1, float y0, float y1,
            float u0, float u1, float v0, float v1,
            int color, Direction flowDirection,
            VertexConsumer vx1
    ) {

        if (flowDirection == Direction.NORTH) {
            // Render east face (x+ side)
            vx1.addVertex((float) x1, (float) y0, (float) z0).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y0, (float) z1).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);

            // Render west face (x- side)
            vx1.addVertex((float) x0, (float) y0, (float) z1).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z0).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);

            // Render up  face (y+ side)
            vx1.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);

            // Render down face (y- side)
            vx1.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        }

        if (flowDirection == Direction.SOUTH) {
            // Render east face (x+ side)
            vx1.addVertex((float) x1, (float) y0, (float) z0).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y0, (float) z1).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);

            // Render west face (x- side)
            vx1.addVertex((float) x0, (float) y0, (float) z1).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z0).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);

            // Render up  face (y+ side)
            vx1.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);

            // Render down face (y- side)
            vx1.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        }

        if (flowDirection == Direction.EAST) {
            // Render south face (z+ side)
            vx1.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);

            // Render north face (z- side)
            vx1.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);

            // Render up  face (y+ side)
            vx1.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);

            // Render down face (y- side)
            vx1.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
        }

        if (flowDirection == Direction.WEST) {
            // Render south face (z+ side)
            vx1.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);

            // Render north face (z- side)
            vx1.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);

            // Render up  face (y+ side)
            vx1.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);

            // Render down face (y- side)
            vx1.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, -1, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, -1, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
        }

        if (flowDirection == Direction.UP) {
            // Render east face (x+ side)
            vx1.addVertex((float) x1, (float) y0, (float) z0).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y0, (float) z1).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);

            // Render west face (x- side)
            vx1.addVertex((float) x0, (float) y0, (float) z1).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z0).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);

            // Render south face (z+ side)
            vx1.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);

            // Render north face (z- side)
            vx1.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
        }

        if (flowDirection == Direction.DOWN) {
            // Render east face (x+ side)
            vx1.addVertex((float) x1, (float) y0, (float) z0).setNormal(1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z0).setNormal(1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z1).setNormal(1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y0, (float) z1).setNormal(1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);

            // Render west face (x- side)
            vx1.addVertex((float) x0, (float) y0, (float) z1).setNormal(-1, 0, 0).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z1).setNormal(-1, 0, 0).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z0).setNormal(-1, 0, 0).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z0).setNormal(-1, 0, 0).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);

            // Render south face (z+ side)
            vx1.addVertex((float) x1, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z1).setNormal(0, 0, 1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y0, (float) z1).setNormal(0, 0, 1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);

            // Render north face (z- side)
            vx1.addVertex((float) x0, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u1, v1).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x0, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u1, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y1, (float) z0).setNormal(0, 0, -1).setUv(u0, v0).setColor(color).setLight(0).setOverlay(0);
            vx1.addVertex((float) x1, (float) y0, (float) z0).setNormal(0, 0, -1).setUv(u0, v1).setColor(color).setLight(0).setOverlay(0);
        }
    }

    public static boolean shouldRenderCentered(Fluid f) {
        //return true;
        return f.getFluidType().isLighterThanAir();
    }

    public static void renderFluids(EntityPipe tile, VertexConsumer v) {
        BlockState tileState = tile.getLevel().getBlockState(tile.getBlockPos());
        if (!(tileState.getBlock() instanceof BlockPipe)) return;
        // render center
        if (!tile.tank.isEmpty()) {

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
                    !tile.connections.get(Direction.NORTH).isEnabled(tileState) &&
                            !tile.connections.get(Direction.SOUTH).isEnabled(tileState) &&
                            !tile.connections.get(Direction.WEST).isEnabled(tileState) &&
                            !tile.connections.get(Direction.EAST).isEnabled(tileState)
            ) {
                // is vertical render because no horizontal connections are found

                float relativeFill = (float) tile.tank.getFluidAmount() / tile.tank.getCapacity();


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
                            color, outFlow, v);

                } else if (numInputs == 1) {
                    // exactly one input is found, great! use this as flow direction
                    renderFluidFlowingCentered(
                            x0, x1, z0, z1, y0, y1,
                            u0f, u1f, v0f, v1f,
                            color, inFlow, v);
                } else {
                    // do not use flowing animation
                    renderVerticalFluidStill(
                            x0, x1, z0, z1, y0, y1,
                            u0s, u1s, v0s, v1s,
                            color, v);
                }
            } else {
                if (!shouldRenderCentered(tile.tank.getFluid().getFluid())) {
                    // is horizontal render
                    float x0 = -0.25f + e;
                    float x1 = 0.25f - e;
                    float z0 = -0.25f + e;
                    float z1 = 0.25f - e;
                    float y0 = -0.25f + e;
                    float y1 = y0 - 2 * e + 0.5f * (float) tile.tank.getFluidAmount() / tile.tank.getCapacity();
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
                    if (conn.isEnabled(tileState)) {
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
                    if (conn.isEnabled(tileState)) {
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
                    if (conn.isEnabled(tileState)) {
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
                    if (conn.isEnabled(tileState)) {
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
                                color, outFlow, v,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);

                    } else if (numInputs == 1) {
                        // exactly one input is found, great! use this as flow direction
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, inFlow, v,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);

                    } else {
                        // do not use flowing animation
                        renderHorizontalFluidStill(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, v,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    }

                    if (tile.connections.get(Direction.UP).isEnabled(tileState)) {
                        float relativeFill = (float) tile.tank.getFluidAmount() / tile.tank.getCapacity();

                        float actualW = wMin + (wMax - wMin) * relativeFill;
                        y0 = -0.25f - e + 0.5f * (float) tile.tank.getFluidAmount() / tile.tank.getCapacity();
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
                                    color, Direction.UP, v);
                        }
                        if (conn.outputsToInside) {
                            //add render down animation
                            renderFluidFlowingCentered(
                                    x0, x1, z0, z1, y0, y1,
                                    u0f, u1f, v0f, v1f,
                                    color, Direction.DOWN, v);
                        }
                    }
                } else {
                    // it is a centered horizontal render
                    Direction outFlow = null;
                    Direction inFlow = null;
                    int numOutputs = 0;
                    int numInputs = 0;

                    float relativeFill = (float) tile.tank.getFluidAmount() / tile.tank.getCapacity();

                    float actualW = wMin + (wMax - wMin) * relativeFill;
                    for (Direction d : Direction.values()) {
                        PipeConnection conn = tile.connections.get(d);
                        if (conn.isEnabled(tileState)) {
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
                                        color, d.getOpposite(), v);
                            } else if (conn.getsInputFromInside) {
                                numOutputs++;
                                outFlow = d;
                                renderFluidFlowingCentered(
                                        x0, x1, z0, z1, y0, y1,
                                        u0f, u1f, v0f, v1f
                                        , color, d, v);
                            } else {
                                renderVerticalFluidStill(
                                        x0, x1, z0, z1, y0, y1,
                                        u0s, u1s, v0s, v1s,
                                        color, v);
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
                                color, outFlow, v);

                        if (!tile.connections.get(outFlow.getOpposite()).isEnabled(tileState) || tile.connections.get(outFlow.getOpposite()).tank.isEmpty()) {
                            renderFluidCubeFacebyDirection(
                                    x0, x1, z0, z1, y0, y1,
                                    u0s, u1s, v0s, v1s,
                                    outFlow.getOpposite(), color, v);
                        }


                    } else if (numInputs == 1) {
                        // exactly one input is found, great! use this as flow direction
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, inFlow, v);

                        if (!tile.connections.get(inFlow.getOpposite()).isEnabled(tileState) || tile.connections.get(inFlow.getOpposite()).tank.isEmpty()) {
                            renderFluidCubeFacebyDirection(
                                    x0, x1, z0, z1, y0, y1,
                                    u0s, u1s, v0s, v1s,
                                    inFlow.getOpposite(), color, v);
                        }

                    } else {
                        // do not use flowing animation
                        renderFluidCubeStill(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, v);
                    }
                }
            }
        }

        //render connections
        if (tile.connections.get(Direction.UP).isEnabled(tileState)) {
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
                            color, Direction.DOWN, v);
                } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                    renderFluidFlowingCentered(
                            x0, x1, z0, z1, y0, y1,
                            u0f, u1f, v0f, v1f,
                            color, Direction.UP, v);
                } else {
                    renderVerticalFluidStill(
                            x0, x1, z0, z1, y0, y1,
                            u0s, u1s, v0s, v1s,
                            color, v);
                }
/*
                float relativeFillAbove = (float) tile.connections.get(Direction.UP).neighborFluidHandler().getFluidInTank(0).getAmount() / tile.connections.get(Direction.UP).neighborFluidHandler().getTankCapacity(0);
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
        if (tile.connections.get(Direction.DOWN).isEnabled(tileState)) {
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
                            color, Direction.UP, v);
                } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                    renderFluidFlowingCentered(
                            x0, x1, z0, z1, y0, y1,
                            u0f, u1f, v0f, v1f,
                            color, Direction.DOWN, v);
                } else {
                    renderVerticalFluidStill(
                            x0, x1, z0, z1, y0, y1,
                            u0s, u1s, v0s, v1s,
                            color, v);
                }
                /*
                float relativeFillAbove = (float) tile.mainTank.getFluidAmount() / tile.mainTank.getCapacity();
                float actualWAbove = wMin + (wMax - wMin) * relativeFillAbove;
                float xh2 = -actualWAbove;
                float xh3 = actualWAbove;
                float zh2 = -actualWAbove;
                float zh3 = actualWAbove;
                boolean renderAbove = actualWAbove < actualW;

                float relativeFillBelow = (float) tile.connections.get(Direction.DOWN).neighborFluidHandler().getFluidInTank(0).getAmount() / tile.connections.get(Direction.DOWN).neighborFluidHandler().getTankCapacity(0);
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
        if (tile.connections.get(Direction.WEST).isEnabled(tileState)) {
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
                    float y0BottomOffsetEast = y0 - 2 * e + 0.5f * (float) tile.tank.getFluidAmount() / tile.tank.getCapacity();
                    if (conn.neighborFluidHandler() instanceof PipeConnection p)
                        y0BottomOffsetWest = y0 - 2 * e + 0.5f * (float) p.tank.getFluidAmount() / p.tank.getCapacity();

                    if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.EAST, v,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.WEST, v,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else {
                        renderHorizontalFluidStill(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, v,
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
                                color, Direction.EAST, v);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.WEST, v);
                    } else {
                        renderHorizontalFluidStillCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, v, Direction.WEST);
                    }
                }
            }
        }
        if (tile.connections.get(Direction.EAST).isEnabled(tileState)) {
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
                    float y0BottomOffsetWest = y0 - 2 * e + 0.5f * (float) tile.tank.getFluidAmount() / tile.tank.getCapacity();
                    float y0BottomOffsetEast = y0;
                    if (conn.neighborFluidHandler() instanceof PipeConnection p)
                        y0BottomOffsetEast = y0 - 2 * e + 0.5f * (float) p.tank.getFluidAmount() / p.tank.getCapacity();

                    if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.WEST, v,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.EAST, v,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else {
                        renderHorizontalFluidStill(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, v,
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
                                color, Direction.WEST, v);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.EAST, v);
                    } else {
                        renderHorizontalFluidStillCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, v, Direction.EAST);
                    }
                }
            }
        }
        if (tile.connections.get(Direction.SOUTH).isEnabled(tileState)) {
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
                    float y0BottomOffsetNorth = y0 - 2 * e + 0.5f * (float) tile.tank.getFluidAmount() / tile.tank.getCapacity();
                    float y0BottomOffsetSouth = y0;
                    float y0BottomOffsetWest = y0;
                    float y0BottomOffsetEast = y0;
                    if (conn.neighborFluidHandler() instanceof PipeConnection p)
                        y0BottomOffsetSouth = y0 - 2 * e + 0.5f * (float) p.tank.getFluidAmount() / p.tank.getCapacity();

                    if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.NORTH, v,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.SOUTH, v,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else {
                        renderHorizontalFluidStill(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, v,
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
                                color, Direction.NORTH, v);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.SOUTH, v);
                    } else {
                        renderHorizontalFluidStillCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, v, Direction.SOUTH);
                    }
                }
            }
        }
        if (tile.connections.get(Direction.NORTH).isEnabled(tileState)) {
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
                    float y0BottomOffsetSouth = y0 - 2 * e + 0.5f * (float) tile.tank.getFluidAmount() / tile.tank.getCapacity();
                    float y0BottomOffsetWest = y0;
                    ;
                    float y0BottomOffsetEast = y0;
                    if (conn.neighborFluidHandler() instanceof PipeConnection p)
                        y0BottomOffsetNorth = y0 - 2 * e + 0.5f * (float) p.tank.getFluidAmount() / p.tank.getCapacity();

                    if (conn.getsInputFromOutside && !conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.SOUTH, v,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderHorizontalFluidFlowing(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.NORTH, v,
                                y0BottomOffsetNorth, y0BottomOffsetSouth, y0BottomOffsetEast, y0BottomOffsetWest);
                    } else {
                        renderHorizontalFluidStill(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, v,
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
                                color, Direction.SOUTH, v);
                    } else if (!conn.getsInputFromOutside && conn.getsInputFromInside) {
                        renderFluidFlowingCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0f, u1f, v0f, v1f,
                                color, Direction.NORTH, v);
                    } else {
                        renderHorizontalFluidStillCentered(
                                x0, x1, z0, z1, y0, y1,
                                u0s, u1s, v0s, v1s,
                                color, v, Direction.NORTH);
                    }
                }
            }
        }
    }
    @Override
    public void render(EntityPipe tile, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {



        if (tile.mesh != null || tile.requiresMeshUpdate) {

            tile.vertexBuffer.bind();

            RENDERTYPE_ENTITY_SOLID_SHADER.setupRenderState();
            LIGHTMAP.setupRenderState();
            LEQUAL_DEPTH_TEST.setupRenderState();
            CULL.setupRenderState();
            COLOR_DEPTH_WRITE.setupRenderState();
            TRANSLUCENT_TRANSPARENCY.setupRenderState();
            BLOCK_SHEET_MIPPED.setupRenderState();

            if (tile.requiresMeshUpdate) {
                tile.requiresMeshUpdate = false;
                BufferBuilder bufferBuilder = new BufferBuilder(tile.myByteBuffer, VertexFormat.Mode.QUADS, RenderPipe.POSITION_COLOR_TEXTURE_NORMAL);
                RenderPipe.renderFluids(tile, bufferBuilder);
                tile.mesh = bufferBuilder.build();
                if (tile.mesh != null)
                    tile.vertexBuffer.upload(tile.mesh);
            }

            if(tile.mesh != null) {
                ShaderInstance shader = RenderSystem.getShader();
                Matrix4f wtfAmIdoing = new Matrix4f(RenderSystem.getModelViewMatrix());
                Matrix4f wtfAmIdoing2 = wtfAmIdoing.mul(stack.last().pose());
                wtfAmIdoing2 = wtfAmIdoing2.translate(0.5f, 0.5f, 0.5f);
                shader.setDefaultUniforms(VertexFormat.Mode.QUADS, wtfAmIdoing2, RenderSystem.getProjectionMatrix(), Minecraft.getInstance().getWindow());
                shader.apply();
                tile.vertexBuffer.draw();
                shader.clear();
            }

            VertexBuffer.unbind();

            RENDERTYPE_ENTITY_SOLID_SHADER.clearRenderState();
            LIGHTMAP.clearRenderState();
            LEQUAL_DEPTH_TEST.clearRenderState();
            CULL.clearRenderState();
            COLOR_DEPTH_WRITE.clearRenderState();
            TRANSLUCENT_TRANSPARENCY.clearRenderState();
            BLOCK_SHEET_MIPPED.clearRenderState();

        }
    }
}