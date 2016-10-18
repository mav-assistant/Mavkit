/*
 * This file is part of Mavkit.
 *
 * Mavkit is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Mavkit is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mavkit. If not, see <http://www.gnu.org/licenses/>.
 */

package com.unascribed.mavkit.internal.canvas;

import static org.lwjgl.nanovg.NanoVG.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.unascribed.mavkit.Mavkit;
import com.unascribed.mavkit.render.Canvas;

/**
 * Implementation of Canvas on top of NanoVG.
 */
public abstract class NanoVGCanvas implements Canvas {
	private static final Logger log = LoggerFactory.getLogger("NanoVGCanvas");
	public class NanoVGFont implements Font {
		private final int handle;
		private final String name;
		
		private final FloatBuffer lineHeight = BufferUtils.createFloatBuffer(1);
		private final FloatBuffer ascenderHeight = BufferUtils.createFloatBuffer(1);
		private final FloatBuffer descenderHeight = BufferUtils.createFloatBuffer(1);
		
		private final FloatBuffer measure = BufferUtils.createFloatBuffer(4);
		
		public NanoVGFont(@NonNull String name, int handle) {
			this.name = name;
			this.handle = handle;
		}
		
		@Override
		public void dispose() {
			log.warn("Attempted to dispose NanoVGFont");
		}

		@Override
		@NonNull
		public String getName() {
			return name;
		}

		@Override
		public float getLineHeight() {
			return lineHeight.get(0);
		}

		@Override
		public float getAscenderHeight() {
			return ascenderHeight.get(0);
		}

		@Override
		public float getDescenderHeight() {
			return descenderHeight.get(0);
		}

		@Override
		public float measureWidth(@NonNull String string) {
			nvgTextBounds(ctx, 0, 0, string, 0, measure);
			return Math.abs(measure.get(2)-measure.get(0));
		}

		@Override
		public float measureHeight(@NonNull String string, float breakWidth) {
			nvgTextBoxBounds(ctx, 0, 0, breakWidth, string, 0, measure);
			return Math.abs(measure.get(3)-measure.get(1));
		}
		boolean isOwner(@Nullable NanoVGCanvas canvas) {
			return NanoVGCanvas.this == canvas;
		}

	}
	public class NanoVGImage implements Image {
		private final int handle;
		
		private final IntBuffer w = BufferUtils.createIntBuffer(1);
		private final IntBuffer h = BufferUtils.createIntBuffer(1);
		
		public NanoVGImage(int handle) {
			this.handle = handle;
			nvgImageSize(ctx, handle, w, h);
		}
		
		@Override
		public void dispose() {
			nvgDeleteImage(ctx, handle);
		}

		@Override
		public int getWidth() {
			return w.get(0);
		}

		@Override
		public int getHeight() {
			return h.get(0);
		}
		
		boolean isOwner(@Nullable NanoVGCanvas canvas) {
			return NanoVGCanvas.this == canvas;
		}

	}
	public class NanoVGColor implements Color {
		private final NVGColor wrapped;
		public NanoVGColor(@NonNull NVGColor color) {
			this.wrapped = color;
		}
		@Override
		public float getRed() {
			return wrapped.r();
		}
		@Override
		public float getGreen() {
			return wrapped.g();
		}
		@Override
		public float getBlue() {
			return wrapped.b();
		}
		@Override
		public float getAlpha() {
			return wrapped.a();
		}
		
		@Override
		public void setRed(float red) {
			wrapped.r(red);
		}
		@Override
		public void setGreen(float green) {
			wrapped.g(green);
		}
		@Override
		public void setBlue(float blue) {
			wrapped.b(blue);
		}
		@Override
		public void setAlpha(float alpha) {
			wrapped.a(alpha);
		}
		
		@Override
		public void set(@NonNull Color c) {
			if (c instanceof NanoVGColor) {
				wrapped.set(((NanoVGColor)c).wrapped);
			} else {
				setRed(c.getRed());
				setGreen(c.getGreen());
				setBlue(c.getBlue());
				setAlpha(c.getAlpha());
			}
		}
		@Override
		public void dispose() {
			wrapped.free();			
		}
		
		boolean isOwner(@Nullable NanoVGCanvas canvas) {
			return NanoVGCanvas.this == canvas;
		}
	}
	public class NanoVGPaint implements Paint {
		private final NVGPaint wrapped;
		public NanoVGPaint(NVGPaint paint) {
			this.wrapped = paint;
		}
		
		@Override
		public Color getInnerColor() {
			return new NanoVGColor(wrapped.innerColor());
		}
		
		@Override
		public Color getOuterColor() {
			return new NanoVGColor(wrapped.outerColor());
		}

		@Override
		public void dispose() {
			wrapped.free();
		}
		
		boolean isOwner(NanoVGCanvas canvas) {
			return NanoVGCanvas.this == canvas;
		}
		
	}
	
	
	private final Mavkit mav;
	private final long ctx;
	private final Map<Integer, NanoVGFont> fonts = Maps.newHashMap();
	public NanoVGCanvas(@NonNull Mavkit mav, long ctx) {
		this.mav = mav;
		this.ctx = ctx;
	}
	
	private NVGColor unwrap(@Nullable Color color) {
		if (color == null) throw new IllegalArgumentException("Cannot use a null color");
		if (!(color instanceof NanoVGColor)) {
			return nvgRGBAf(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), NVGColor.create());
		}
		return ((NanoVGColor)color).wrapped;
	}
	private NVGPaint unwrap(@Nullable Paint paint) {
		if (paint == null) throw new IllegalArgumentException("Cannot use a null paint");
		if (!(paint instanceof NanoVGPaint) || !((NanoVGPaint)paint).isOwner(this)) {
			throw new IllegalArgumentException("Cannot use a Paint from another Canvas");
		}
		return ((NanoVGPaint)paint).wrapped;
	}
	private int unwrap(@Nullable Image image) {
		if (image == null) throw new IllegalArgumentException("Cannot use a null image");
		if (!(image instanceof NanoVGImage) || !((NanoVGImage)image).isOwner(this)) {
			throw new IllegalArgumentException("Cannot use an Image from another Canvas");
		}
		return ((NanoVGImage)image).handle;
	}
	private int unwrap(@Nullable Font font) {
		if (font == null) throw new IllegalArgumentException("Cannot use a null font");
		if (!(font instanceof NanoVGFont) || !((NanoVGFont)font).isOwner(this)) {
			throw new IllegalArgumentException("Cannot use a Font from another Canvas");
		}
		return ((NanoVGFont)font).handle;
	}
	
	private int convert(ImageMode... modes) {
		int i = 0;
		for (ImageMode im : modes) {
			switch (im) {
				case FLIP_Y:
					i |= NVG_IMAGE_FLIPY;
					break;
				case GENERATE_MIPMAPS:
					i |= NVG_IMAGE_GENERATE_MIPMAPS;
					break;
				case PREMULTIPLIED:
					i |= NVG_IMAGE_PREMULTIPLIED;
					break;
				case REPEAT_X:
					i |= NVG_IMAGE_REPEATX;
					break;
				case REPEAT_Y:
					i |= NVG_IMAGE_REPEATY;
					break;
				default:
					break;
			}
		}
		return i;
	}
	
	private int convert(@NonNull HorizontalAlign horz, @NonNull VerticalAlign vert) {
		int i = 0;
		switch (horz) {
			case CENTER:
				i |= NVG_ALIGN_CENTER;
				break;
			case LEFT:
				i |= NVG_ALIGN_LEFT;
				break;
			case RIGHT:
				i |= NVG_ALIGN_RIGHT;
				break;
			default:
				break;
		}
		switch (vert) {
			case BASELINE:
				i |= NVG_ALIGN_BASELINE;
				break;
			case BOTTOM:
				i |= NVG_ALIGN_BOTTOM;
				break;
			case MIDDLE:
				i |= NVG_ALIGN_MIDDLE;
				break;
			case TOP:
				i |= NVG_ALIGN_TOP;
				break;
			default:
				break;
		}
		return i;
	}
	
	@NonNull
	private ByteBuffer load(@NonNull String resource) {
		try {
			ByteBuffer buffer;
	
			Path path = Paths.get(resource);
			if (Files.isReadable(path)) {
				try (SeekableByteChannel fc = Files.newByteChannel(path)) {
					buffer = BufferUtils.createByteBuffer((int)fc.size() + 1);
					while (fc.read(buffer) != -1);
				}
			} else {
				InputStream source = ClassLoader.getSystemResourceAsStream(resource);
				if (source != null) {
					try (
						InputStream src = source;
						ReadableByteChannel rbc = Channels.newChannel(src)
					) {
						buffer = BufferUtils.createByteBuffer(4096);
		
						while (true) {
							int bytes = rbc.read(buffer);
							if (bytes == -1)
								break;
							if (buffer.remaining() == 0)
								buffer = resizeBuffer(buffer, buffer.capacity() * 2);
						}
					}
				} else {
					throw new RuntimeException("Could not find resource with path "+resource);
				}
			}
	
			buffer.flip();
			return buffer;
		} catch (IOException e) {
			throw new RuntimeException("Failed to load resource "+resource, e);
		}
	}
	
	@NonNull
	private ByteBuffer resizeBuffer(@NonNull ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
}

	@Override
	public void beginFrame() {
		nvgBeginFrame(ctx, mav.getDisplay().getWidth(), mav.getDisplay().getHeight(), mav.getDisplay().getPixelRatio());
	}
	
	@Override
	public void endFrame() {
		nvgEndFrame(ctx);
	}
	
	
	@Override
	public void cancelFrame() {
		nvgCancelFrame(ctx);
	}

	@Override
	public Restorer save() {
		nvgSave(ctx);
		return this::restore;
	}

	@Override
	public void restore() {
		nvgRestore(ctx);
	}

	@Override
	public void reset() {
		nvgReset(ctx);
	}

	@Override
	public void strokeStyle(@NonNull Color color) {
		nvgStrokeColor(ctx, unwrap(color));
	}

	@Override
	public void strokeStyle(@NonNull Paint paint) {
		nvgStrokePaint(ctx, unwrap(paint));
	}

	@Override
	public void fillStyle(@NonNull Color color) {
		nvgFillColor(ctx, unwrap(color));
	}

	@Override
	public void fillStyle(@NonNull Paint paint) {
		nvgFillPaint(ctx, unwrap(paint));
	}

	@Override
	public void strokeMiterLimit(float limit) {
		nvgMiterLimit(ctx, limit);
	}

	@Override
	public void strokeWidth(float size) {
		nvgStrokeWidth(ctx, size);
	}

	@Override
	public void strokeCap(@NonNull StrokeCap cap) {
		int nvg;
		switch (cap) {
			case BUTT:
				nvg = NVG_BUTT;
				break;
			case ROUND:
				nvg = NVG_ROUND;
				break;
			case SQUARE:
				nvg = NVG_SQUARE;
				break;
			default:
				return;
		}
		nvgLineCap(ctx, nvg);
	}

	@Override
	public void strokeJoin(@NonNull StrokeJoin join) {
		int nvg;
		switch (join) {
			case BEVEL:
				nvg = NVG_BEVEL;
				break;
			case MITER:
				nvg = NVG_MITER;
				break;
			case ROUND:
				nvg = NVG_ROUND;
				break;
			default:
				return;
		}
		nvgLineJoin(ctx, nvg);
	}

	@Override
	public void alpha(float alpha) {
		nvgGlobalAlpha(ctx, alpha);
	}

	@Override
	public void resetTransform() {
		nvgResetTransform(ctx);
	}

	@Override
	public void translate(float x, float y) {
		nvgTranslate(ctx, x, y);
	}

	@Override
	public void rotate(float angle) {
		nvgRotate(ctx, angle);
	}

	@Override
	public void skew(float angleX, float angleY) {
		if (angleX != 0) nvgSkewX(ctx, angleX);
		if (angleY != 0) nvgSkewY(ctx, angleY);
	}

	@Override
	public void scale(float x, float y) {
		nvgScale(ctx, x, y);
	}

	@Override
	@Nullable
	public Image createImage(@NonNull BufferedImage img, ImageMode... modes) {
		int nvg = convert(modes);
		int w = img.getWidth();
		int h = img.getHeight();
		int[] rgb = new int[w*h];
		img.getRGB(0, 0, w, h, rgb, 0, w);
		ByteBuffer data = BufferUtils.createByteBuffer(w*h*4);
		data.asIntBuffer().put(rgb);
		data.flip();
		int handle = nvgCreateImageRGBA(ctx, w, h, nvg, data);
		if (handle == -1) return null;
		return new NanoVGImage(handle);
	}

	@Override
	@Nullable
	public Image loadImage(@NonNull File file, ImageMode... modes) {
		int nvg = convert(modes);
		ByteBuffer data = load(file.getAbsolutePath());
		int handle = nvgCreateImageMem(ctx, nvg, data);
		if (handle == -1) return null;
		return new NanoVGImage(handle);
	}

	@Override
	@Nullable
	public Image loadImage(@NonNull String path, ImageMode... modes) {
		int nvg = convert(modes);
		ByteBuffer data = load(path);
		int handle = nvgCreateImageMem(ctx, nvg, data);
		if (handle == -1) return null;
		return new NanoVGImage(handle);
	}

	@Override
	@Nullable
	public Paint createLinearGradient(float startX, float startY, float endX,
			float endY, Color start, Color end) {
		NVGPaint paint = nvgLinearGradient(ctx, startX, startY, endX, endY, unwrap(start), unwrap(end), NVGPaint.create());
		return new NanoVGPaint(paint);
	}

	@Override
	@Nullable
	public Paint createBoxGradient(float x, float y, float width, float height,
			float radius, float feather, Color inner, Color outer) {
		NVGPaint paint = nvgBoxGradient(ctx, x, y, width, height, radius, feather, unwrap(inner), unwrap(outer), NVGPaint.create());
		return new NanoVGPaint(paint);
	}

	@Override
	@Nullable
	public Paint createRadialGradient(float centerX, float centerY,
			float innerRadius, float outerRadius, @NonNull Color inner, @NonNull Color outer) {
		NVGPaint paint = nvgRadialGradient(ctx, centerX, centerY, innerRadius, outerRadius, unwrap(inner), unwrap(outer), NVGPaint.create());
		return new NanoVGPaint(paint);
	}

	@Override
	@Nullable
	public Paint createPattern(float x, float y, float width, float height,
			float angle, @NonNull Image image, float alpha) {
		NVGPaint paint = NVGPaint.create();
		paint = nvgImagePattern(ctx, x, y, width, height, angle, unwrap(image), alpha, paint);
		return new NanoVGPaint(paint);
	}

	@Override
	public void scissor(float x, float y, float width, float height) {
		nvgScissor(ctx, x, y, width, height);
	}

	@Override
	public void intersectScissor(float x, float y, float width, float height) {
		nvgIntersectScissor(ctx, x, y, width, height);
	}

	@Override
	public void resetScissor() {
		nvgResetScissor(ctx);
	}

	@Override
	public void beginPath() {
		nvgBeginPath(ctx);
	}

	@Override
	public void moveTo(float x, float y) {
		nvgMoveTo(ctx, x, y);
	}

	@Override
	public void lineTo(float x, float y) {
		nvgLineTo(ctx, x, y);
	}

	@Override
	public void bezierTo(float control1x, float control1y, float control2x,
			float control2y, float x, float y) {
		nvgBezierTo(ctx, control1x, control1y, control2x, control2y, x, y);
	}

	@Override
	public void quadTo(float controlX, float controlY, float x, float y) {
		nvgQuadTo(ctx, controlX, controlY, x, y);
	}

	@Override
	public void arcTo(float control1x, float control1y, float control2x,
			float control2y, float radius) {
		nvgArcTo(ctx, control1x, control1y, control2x, control2y, radius);
	}

	@Override
	public void closePath() {
		nvgClosePath(ctx);
	}

	@Override
	public void pathWinding(@NonNull Winding winding) {
		int nvg;
		switch (winding) {
			case CCW:
				nvg = NVG_CCW;
				break;
			case CW:
				nvg = NVG_CW;
				break;
			default:
				return;
		}
		nvgPathWinding(ctx, nvg);
	}

	@Override
	public void arc(float centerX, float centerY, float radius,
			float angleStart, float angleEnd, @NonNull Winding winding) {
		int nvg;
		switch (winding) {
			case CCW:
				nvg = NVG_CCW;
				break;
			case CW:
				nvg = NVG_CW;
				break;
			default:
				return;
		}
		nvgArc(ctx, centerX, centerY, radius, angleStart, angleEnd, nvg);
	}

	@Override
	public void rect(float x, float y, float width, float height) {
		nvgRect(ctx, x, y, width, height);
	}

	@Override
	public void roundedRect(float x, float y, float width, float height,
			float radius) {
		nvgRoundedRect(ctx, x, y, width, height, radius);
	}

	@Override
	public void ellipse(float centerX, float centerY, float radiusX,
			float radiusY) {
		nvgEllipse(ctx, centerX, centerY, radiusX, radiusY);
	}

	@Override
	public void circle(float centerX, float centerY, float radius) {
		nvgCircle(ctx, centerX, centerY, radius);
	}

	@Override
	public void fill() {
		nvgFill(ctx);
	}

	@Override
	public void stroke() {
		nvgStroke(ctx);
	}

	@Override
	@Nullable
	public Font loadFont(@NonNull String name, @NonNull String path) {
		ByteBuffer data = load(path);
		int handle = nvgCreateFontMem(ctx, name, data, 0);
		if (handle == -1) return null;
		NanoVGFont font = new NanoVGFont(name, handle);
		fonts.put(handle, font);
		return font;
	}

	@Override
	@Nullable
	public Font loadFont(@NonNull String name, @NonNull File file) {
		ByteBuffer data = load(file.getAbsolutePath());
		int handle = nvgCreateFontMem(ctx, name, data, 0);
		if (handle == -1) return null;
		NanoVGFont font = new NanoVGFont(name, handle);
		fonts.put(handle, font);
		return font;
	}

	@Override
	@Nullable
	public Font findFont(@NonNull String name) {
		int font = nvgFindFont(ctx, name);
		if (font == -1) {
			return null;
		} else {
			return fonts.get(font);
		}
	}

	@Override
	public void textSize(float size) {
		nvgFontSize(ctx, size);
	}

	@Override
	public void textBlur(float blur) {
		nvgFontBlur(ctx, blur);
	}

	@Override
	public void textLetterSpacing(float spacing) {
		nvgTextLetterSpacing(ctx, spacing);
	}

	@Override
	public void textLineHeight(float lineHeight) {
		nvgTextLineHeight(ctx, lineHeight);
	}

	@Override
	public void textAlign(@NonNull HorizontalAlign horizontal, @NonNull VerticalAlign vertical) {
		int nvg = convert(horizontal, vertical);
		nvgTextAlign(ctx, nvg);
	}

	@Override
	public void fontFace(@NonNull Font font) {
		nvgFontFaceId(ctx, unwrap(font));
	}

	@Override
	public void drawText(float x, float y, @NonNull String string) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer utf8 = stack.UTF8(string);
			nvgText(ctx, x, y, utf8, 0);
		}
	}

	@Override
	public void drawTextBox(float x, float y, float breakWidth, @NonNull String string) {
		nvgTextBox(ctx, x, y, breakWidth, string, 0);
	}

	@Override
	public Color colorFromPackedRGB(int rgb) {
		return colorFromRGB(((rgb >> 16)&0xFF)/255f, ((rgb >> 8)&0xFF)/255f, (rgb&0xFF)/255f);
	}

	@Override
	public Color colorFromPackedARGB(int argb) {
		return colorFromRGBA(((argb >> 16)&0xFF)/255f, ((argb >> 8)&0xFF)/255f, (argb&0xFF)/255f, ((argb >> 24)&0xFF)/255f);
	}

	@Override
	public Color colorFromRGB(float r, float g, float b) {
		NVGColor color = nvgRGBf(r, g, b, NVGColor.create());
		return new NanoVGColor(color);
	}

	@Override
	public Color colorFromRGBA(float r, float g, float b, float a) {
		NVGColor color = nvgRGBAf(r, g, b, a, NVGColor.create());
		return new NanoVGColor(color);
	}

	@Override
	public Color colorFromHSL(float h, float s, float l) {
		NVGColor color = nvgHSL(h, s, l, NVGColor.create());
		return new NanoVGColor(color);
	}

	@Override
	public Color colorFromHSLA(float h, float s, float l, float a) {
		NVGColor color = nvgHSLA(h, s, l, (byte)(a*255), NVGColor.create());
		return new NanoVGColor(color);
	}

	@Override
	@NonNull
	public Color lerp(@NonNull Color c0, @NonNull Color c1, float u) {
		NVGColor color = nvgLerpRGBA(unwrap(c0), unwrap(c1), u, NVGColor.create());
		return new NanoVGColor(color);
	}
	
	@Override
	public float radToDeg(float rad) {
		return nvgRadToDeg(rad);
	}
	
	@Override
	public float degToRad(float deg) {
		return nvgDegToRad(deg);
	}

}
