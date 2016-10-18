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

package com.unascribed.mavkit;

import java.awt.image.BufferedImage;
import java.io.File;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.unascribed.mavkit.render.Canvas;

public class NoOpCanvas implements Canvas {

	public class DummyColor implements Color {
		@Override public void dispose() {}
		@Override public float getRed() { return 1; }
		@Override public float getGreen() { return 0; }
		@Override public float getBlue() { return 0; }
		@Override public float getAlpha() { return 1; }
		@Override public void setRed(float value) {}
		@Override public void setGreen(float value) {}
		@Override public void setBlue(float value) {}
		@Override public void setAlpha(float value) {}
		@Override public void set(Color c) {}
	}

	@Override public void beginFrame() {}
	@Override public void endFrame() {}
	@Override public void cancelFrame() {}
	@Override public @NonNull Restorer save() { return () -> {}; }
	@Override public void restore() {}
	@Override public void reset() {}
	@Override public void strokeStyle(@NonNull Color color) {}
	@Override public void strokeStyle(@NonNull Paint paint) {}
	@Override public void fillStyle(@NonNull Color color) {}
	@Override public void fillStyle(@NonNull Paint paint) {}
	@Override public void strokeMiterLimit(float limit) {}
	@Override public void strokeWidth(float size) {}
	@Override public void strokeCap(@NonNull StrokeCap cap) {}
	@Override public void strokeJoin(@NonNull StrokeJoin join) {}
	@Override public void alpha(float alpha) {}
	@Override public void resetTransform() {}
	@Override public void translate(float x, float y) {}
	@Override public void rotate(float angle) {}
	@Override public void skew(float angleX, float angleY) {}
	@Override public void scale(float x, float y) {}
	@Override public @Nullable Image createImage(BufferedImage img, ImageMode... modes) { return null; }
	@Override public @Nullable Image loadImage(File file, ImageMode... modes) { return null; }
	@Override public @Nullable Image loadImage(String path, ImageMode... modes) { return null; }
	@Override public @Nullable Paint createLinearGradient(float startX, float startY, float endX, float endY, Color start, Color end) { return null; }
	@Override public @Nullable Paint createBoxGradient(float x, float y, float width, float height, float radius, float feather, Color inner, Color outer) { return null; }
	@Override public @Nullable Paint createRadialGradient(float centerX, float centerY, float innerRadius, float outerRadius, Color inner, Color outer) { return null; }
	@Override public @Nullable Paint createPattern(float x, float y, float width, float height, float angle, Image image, float alpha) { return null; }
	@Override public void scissor(float x, float y, float width, float height) {}
	@Override public void intersectScissor(float x, float y, float width, float height) {}
	@Override public void resetScissor() {}
	@Override public void beginPath() {}
	@Override public void moveTo(float x, float y) {}
	@Override public void lineTo(float x, float y) {}
	@Override public void bezierTo(float control1x, float control1y, float control2x, float control2y, float x, float y) {}
	@Override public void quadTo(float controlX, float controlY, float x, float y) {}
	@Override public void arcTo(float control1x, float control1y, float control2x, float control2y, float radius) { }
	@Override public void closePath() {}
	@Override public void pathWinding(@NonNull Winding winding) {}
	@Override public void arc(float centerX, float centerY, float radius, float angleStart, float angleEnd, @NonNull Winding winding) {}
	@Override public void rect(float x, float y, float width, float height) {}
	@Override public void roundedRect(float x, float y, float width, float height, float radius) {}
	@Override public void ellipse(float centerX, float centerY, float radiusX, float radiusY) {}
	@Override public void circle(float centerX, float centerY, float radius) {}
	@Override public void fill() {}
	@Override public void stroke() {}
	@Override public @Nullable Font loadFont(String name, String path) { return null; }
	@Override public @Nullable Font loadFont(String name, File file) { return null; }
	@Override public @Nullable Font findFont(String name) { return null; }
	@Override public void textSize(float size) {}
	@Override public void textBlur(float blur) {}
	@Override public void textLetterSpacing(float spacing) {}
	@Override public void textLineHeight(float lineHeight) {}
	@Override public void textAlign(@NonNull HorizontalAlign horizontal, @NonNull VerticalAlign vertical) {}
	@Override public void fontFace(@NonNull Font font) {}
	@Override public void drawText(float x, float y, @NonNull String string) {}
	@Override public void drawTextBox(float x, float y, float breakWidth, @NonNull String string) {}
	@Override public @NonNull Color colorFromPackedRGB(int rgb) { return new DummyColor(); }
	@Override public @NonNull Color colorFromPackedARGB(int argb) { return new DummyColor(); }
	@Override public @NonNull Color colorFromRGB(float r, float g, float b) { return new DummyColor(); }
	@Override public @NonNull Color colorFromRGBA(float r, float g, float b, float a) { return new DummyColor(); }
	@Override public @NonNull Color colorFromHSL(float h, float s, float l) { return new DummyColor(); }
	@Override public @NonNull Color colorFromHSLA(float h, float s, float l, float a) { return new DummyColor(); }
	@Override public @NonNull Color lerp(@NonNull Color c0, @NonNull Color c1, float u) { return new DummyColor(); }

}
