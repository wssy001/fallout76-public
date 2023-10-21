package cyou.wssy001.baseserviceprovider.config;

import org.springframework.aot.hint.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

import java.util.Collections;
import java.util.List;

@Configuration
@ImportRuntimeHints(AWTNativeConfig.class)
public class AWTNativeConfig implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection()
                .registerType(TypeReference.of("sun.java2d.marlin.DMarlinRenderingEngine"), this::buildDMarlinRenderingEngine)
        ;

        hints.jni()
                .registerType(java.lang.System.class, MemberCategory.values())

                .registerType(TypeReference.of("java.awt.AlphaComposite"), MemberCategory.values())
                .registerType(TypeReference.of("java.awt.Color"), MemberCategory.values())
                .registerType(TypeReference.of("java.awt.GraphicsEnvironment"), MemberCategory.values())
                .registerType(TypeReference.of("java.awt.geom.AffineTransform"), MemberCategory.values())
                .registerType(TypeReference.of("java.awt.geom.GeneralPath"), MemberCategory.values())
                .registerType(TypeReference.of("java.awt.geom.Path2D"), MemberCategory.values())
                .registerType(TypeReference.of("java.awt.geom.Path2D$Float"), MemberCategory.values())
                .registerType(TypeReference.of("java.awt.geom.Point2D$Float"), MemberCategory.values())
                .registerType(TypeReference.of("java.awt.geom.Rectangle2D$Float"), MemberCategory.values())
                .registerType(TypeReference.of("java.awt.image.BufferedImage"), MemberCategory.values())
                .registerType(TypeReference.of("java.awt.image.ColorModel"), MemberCategory.values())
                .registerType(TypeReference.of("java.awt.image.IndexColorModel"), MemberCategory.values())
                .registerType(TypeReference.of("java.awt.image.Raster"), MemberCategory.values())
                .registerType(TypeReference.of("java.awt.image.SampleModel"), MemberCategory.values())
                .registerType(TypeReference.of("java.awt.image.SinglePixelPackedSampleModel"), MemberCategory.values())
                .registerType(TypeReference.of("sun.awt.SunHints"), MemberCategory.values())
                .registerType(TypeReference.of("sun.awt.image.BufImgSurfaceData$ICMColorData"), MemberCategory.values())
                .registerType(TypeReference.of("sun.awt.image.ByteComponentRaster"), MemberCategory.values())
                .registerType(TypeReference.of("sun.awt.image.IntegerComponentRaster"), MemberCategory.values())
                .registerType(TypeReference.of("sun.font.CharToGlyphMapper"), MemberCategory.values())
                .registerType(TypeReference.of("sun.font.Font2D"), MemberCategory.values())
                .registerType(TypeReference.of("sun.font.FontConfigManager$FcCompFont"), MemberCategory.values())
                .registerType(TypeReference.of("sun.font.FontConfigManager$FontConfigFont"), MemberCategory.values())
                .registerType(TypeReference.of("sun.font.FontConfigManager$FontConfigInfo"), MemberCategory.values())
                .registerType(TypeReference.of("sun.font.FontStrike"), MemberCategory.values())
                .registerType(TypeReference.of("sun.font.FontUtilities"), MemberCategory.values())
                .registerType(TypeReference.of("sun.font.FreetypeFontScaler"), MemberCategory.values())
                .registerType(TypeReference.of("sun.font.GlyphLayout$GVData"), MemberCategory.values())
                .registerType(TypeReference.of("sun.font.GlyphList"), MemberCategory.values())
                .registerType(TypeReference.of("sun.font.PhysicalStrike"), MemberCategory.values())
                .registerType(TypeReference.of("sun.font.StrikeMetrics"), MemberCategory.values())
                .registerType(TypeReference.of("sun.font.TrueTypeFont"), MemberCategory.values())
                .registerType(TypeReference.of("sun.font.Type1Font"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.Disposer"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.InvalidPipeException"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.NullSurfaceData"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.SunGraphics2D"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.SurfaceData"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.Blit"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.BlitBg"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.CompositeType"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.DrawGlyphList"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.DrawGlyphListAA"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.DrawGlyphListLCD"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.DrawLine"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.DrawParallelogram"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.DrawPath"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.DrawPolygons"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.DrawRect"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.FillParallelogram"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.FillPath"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.FillRect"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.FillSpans"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.GraphicsPrimitive"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.GraphicsPrimitiveMgr"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.MaskBlit"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.MaskFill"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.ScaledBlit"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.SurfaceType"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.TransformHelper"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.loops.XORComposite"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.pipe.Region"), MemberCategory.values())
                .registerType(TypeReference.of("sun.java2d.pipe.RegionIterator"), MemberCategory.values())
                .registerType(TypeReference.of("sun.management.VMManagementImpl"), MemberCategory.values())

                .registerType(TypeReference.of("sun.awt.image.BufImgSurfaceData$ICMColorData"), this::buildICMColorData)
                .registerType(TypeReference.of("sun.font.StrikeMetrics"), this::buildStrikeMetrics)
                .registerType(TypeReference.of("sun.font.FontConfigManager$FontConfigFont"), this::buildFontConfigFont)
                .registerType(TypeReference.of("sun.java2d.loops.Blit"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.BlitBg"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.DrawGlyphList"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.DrawGlyphListAA"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.DrawGlyphListLCD"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.DrawLine"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.DrawParallelogram"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.DrawPath"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.DrawPolygons"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.DrawRect"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.FillParallelogram"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.FillPath"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.FillRect"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.FillSpans"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.MaskBlit"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.MaskFill"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.ScaledBlit"), this::buildBlit)
                .registerType(TypeReference.of("sun.java2d.loops.TransformHelper"), this::buildBlit)
                .registerType(TypeReference.of("java.awt.geom.GeneralPath"), this::buildGeneralPath)
                .registerType(TypeReference.of("java.awt.geom.Point2D$Float"), this::buildPoint2D)
                .registerType(TypeReference.of("java.awt.geom.Rectangle2D$Float"), this::buildRectangle2D)
        ;

        hints.resources()
                .registerResourceBundle("sun.awt.resources.awt")
        ;
    }

    private void buildDMarlinRenderingEngine(TypeHint.Builder builder) {
        builder.withMethod("<init>", Collections.emptyList(), ExecutableMode.INVOKE);
    }

    private void buildFontConfigFont(TypeHint.Builder builder) {
        builder.withMethod("<init>", Collections.emptyList(), ExecutableMode.INVOKE);
    }

    private void buildICMColorData(TypeHint.Builder builder) {
        builder.withMethod("<init>", List.of(TypeReference.of(long.class)), ExecutableMode.INVOKE);
    }

    private void buildStrikeMetrics(TypeHint.Builder builder) {
        builder.withMethod("<init>", List.of(TypeReference.of(float.class), TypeReference.of(float.class),
                TypeReference.of(float.class), TypeReference.of(float.class), TypeReference.of(float.class),
                TypeReference.of(float.class), TypeReference.of(float.class), TypeReference.of(float.class),
                TypeReference.of(float.class), TypeReference.of(float.class)), ExecutableMode.INVOKE);
    }

    private void buildBlit(TypeHint.Builder builder) {
        builder.withMethod("<init>", List.of(TypeReference.of(long.class), TypeReference.of("sun.java2d.loops.SurfaceType"),
                        TypeReference.of("sun.java2d.loops.CompositeType"), TypeReference.of("sun.java2d.loops.SurfaceType")),
                ExecutableMode.INVOKE);
    }

    private void buildGeneralPath(TypeHint.Builder builder) {
        builder.withMethod("<init>", List.of(TypeReference.of(int.class), TypeReference.of(byte[].class),
                                TypeReference.of(int.class), TypeReference.of(float[].class), TypeReference.of(int.class)),
                        ExecutableMode.INVOKE)
                .withMethod("<init>", Collections.emptyList(), ExecutableMode.INVOKE);
    }

    private void buildPoint2D(TypeHint.Builder builder) {
        builder.withMethod("<init>", List.of(TypeReference.of(float.class), TypeReference.of(float.class)),
                ExecutableMode.INVOKE);
    }

    private void buildRectangle2D(TypeHint.Builder builder) {
        builder.withMethod("<init>", Collections.emptyList(), ExecutableMode.INVOKE)
                .withMethod("<init>", List.of(TypeReference.of(float.class), TypeReference.of(float.class),
                        TypeReference.of(float.class), TypeReference.of(float.class)), ExecutableMode.INVOKE);
    }
}
