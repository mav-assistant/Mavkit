/*
 * This file is part of Mav.
 *
 * Mav is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Mav is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mav. If not, see <http://www.gnu.org/licenses/>.
 */

package com.unascribed.mav;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Represents a rendering API.
 */
public interface Canvas {

	public enum ImageMode {
		/**
		 * Generate mipmaps for this image.
		 */
		GENERATE_MIPMAPS,
		/**
		 * Repeat this image horizontally if its bounds are exceeded.
		 */
		REPEAT_X,
		/**
		 * Repeat this image vertically if its bounds are exceeded.
		 */
		REPEAT_Y,
		/**
		 * Flip this image vertically.
		 */
		FLIP_Y,
		/**
		 * Treat this image as having premultiplied alpha.
		 */
		PREMULTIPLIED
	}

	public enum CompositeOperation {
		SOURCE_OVER,
		SOURCE_IN,
		SOURCE_OUT,
		SOURCE_ATOP,
		DESTINATION_OVER,
		DESTINATION_IN,
		DESTINATION_OUT,
		DESTINATION_ATOP,
		LIGHTER,
		COPY,
		XOR
	}
	
	public enum BlendFactor {
		ZERO,
		ONE,
		SRC_COLOR,
		ONE_MINUS_SRC_COLOR,
		DST_COLOR,
		ONE_MINUS_DST_COLOR,
		SRC_ALPHA,
		ONE_MINUS_SRC_ALPHA,
		DST_ALPHA,
		ONE_MINUS_DST_ALPHA,
		SRC_ALPHA_SATURATE
	}
	
	public enum HorizontalAlign {
		/**
		 * Default, align text horizontally to the left.
		 */
		LEFT,
		/**
		 * Align text horizontally to the center.
		 */
		CENTER,
		/**
		 * Align text horizontally to the right.
		 */
		RIGHT,
	}
	public enum VerticalAlign {
		/**
		 * Align text vertically to the top.
		 */
		TOP,
		/**
		 * Align text vertically to the middle.
		 */
		MIDDLE,
		/**
		 * Align text vertically to the bottom.
		 */
		BOTTOM,
		/**
		 * Default, align text vertically to the baseline.
		 */
		BASELINE
	}


	public enum Winding {
		/**
		 * Counter-clockwise
		 */
		CCW,
		/**
		 * Clockwise
		 */
		CW
	}
	
	public class Solidity {
		/**
		 * Equivalent to Winding.CCW, but more clear in some cases.
		 */
		public static final Winding SOLID = Winding.CCW;
		/**
		 * Equivalent to Winding.CW, but more clear in some cases.
		 */
		public static final Winding HOLE = Winding.CW;
	}


	public enum StrokeJoin {
		// javadocs derived from https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D/lineJoin
		/**
		 * Corners are sharp.
		 * <p>
		 * Connected segments are joined by extending their outside edges to
		 * connect at a single point, with the effect of filling an additional
		 * lozenge-shaped area.
		 * <p>
		 * @see Canvas#strokeMiterLimit
		 */
		MITER,
		/**
		 * Corners are round.
		 * <p>
		 * Rounds off the corners of a shape by filling an additional sector of
		 * disc centered at the common endpoint of connected segments. The
		 * radius for these rounded corners is equal to the line width.
		 */
		ROUND,
		/**
		 * Corners look "cut-off".
		 * <p>
		 * Fills an additional triangular area between the common endpoint of
		 * connected segments, and the separate outside rectangular corners of
		 * each segment.
		 */
		BEVEL
	}


	public enum StrokeCap {
		/**
		 * The ends of lines are squared off at the endpoints.
		 * <p>
		 * Default.
		 */
		BUTT,
		/**
		 * The ends of lines are rounded.
		 */
		ROUND,
		/**
		 * The ends of lines are squared off, making the endpoint be within the
		 * center of the end rather than the edge.
		 */
		SQUARE
	}


	public interface Color extends Disposable {
		float getRed();
		float getGreen();
		float getBlue();
		float getAlpha();
		
		void setRed(float value);
		void setGreen(float value);
		void setBlue(float value);
		void setAlpha(float value);
		
		void set(Color c);
	}
	public interface Paint extends Disposable {
		/**
		 * @return a view of this Paint's inner color, i.e. modifications to the
		 * 		Color are reflected in this Paint.
		 */
		Color getInnerColor();
		/**
		 * @return a view of this Paint's outer color, i.e. modifications to the
		 * 		Color are reflected in this Paint.
		 */
		Color getOuterColor();
	}
	public interface Image extends Disposable {
		int getWidth();
		int getHeight();
	}
	public interface Font extends Disposable {
		String getName();
		float getLineHeight();
		float getAscenderHeight();
		float getDescenderHeight();
		float measureWidth(String string);
		float measureHeight(String string, float breakWidth);
	}
	
	/**
	 * Internal. Begin drawing a new frame.
	 */
	void beginFrame();
	/**
	 * Internal. End drawing the current frame, flushing render state.
	 */
	void endFrame();
	/**
	 * Internal. Cancel drawing the current frame.
	 */
	void cancelFrame();
	
	
	/**
	 * Pushes and saves the current render state into a state stack.
	 * <p>
	 * A matching {@link #restore} must be used to restore the state.
	 */
	void save();
	/**
	 * Pops and restores a previous render state.
	 */
	void restore();
	/**
	 * Resets the current render state to default values.
	 * <p>
	 * Does not affect the render state stack.
	 */
	void reset();
	
	/**
	 * Sets the current stroke style to a solid color.
	 * @param color The color to use
	 * @see #colorFromHSL
	 * @see #colorFromHSLA
	 * @see #colorFromPackedARGB
	 * @see #colorFromPackedRGB
	 * @see #colorFromRGB
	 * @see #colorFromRGBA
	 */
	void strokeStyle(Color color);
	/**
	 * Sets the current stroke style to a paint, such as a gradient or pattern.
	 * @param paint The paint to use
	 * @see #createBoxGradient
	 * @see #createLinearGradient
	 * @see #createRadialGradient
	 * @see #createPattern
	 */
	void strokeStyle(Paint paint);
	
	/**
	 * Sets the current fill style to a solid color;
	 * @param color The color to use
	 * @see #colorFromHSL
	 * @see #colorFromHSLA
	 * @see #colorFromPackedARGB
	 * @see #colorFromPackedRGB
	 * @see #colorFromRGB
	 * @see #colorFromRGBA
	 */
	void fillStyle(Color color);
	/**
	 * Sets the current fill style to a paint, such as a gradient or pattern.
	 * @param paint The paint to use
	 * @see #createBoxGradient
	 * @see #createLinearGradient
	 * @see #createRadialGradient
	 * @see #createPattern
	 */
	void fillStyle(Paint paint);
	
	
	/**
	 * Sets the Miter limit of the stroke.
	 * <p>
	 * The Miter limit controls when a sharp corner is beveled.
	 * @param limit The Miter limit
	 */
	void strokeMiterLimit(float limit);
	
	/**
	 * Sets the width of the stroke.
	 * @param size The size to use for the stroke
	 */
	void strokeWidth(float size);
	
	/**
	 * Sets how the end of a line in an open path is drawn.
	 * @param cap The type of cap
	 */
	void strokeCap(StrokeCap cap);
	
	/**
	 * Sets how sharp path corners are drawn.
	 * @param join The type of join
	 */
	void strokeJoin(StrokeJoin join);
	
	
	/**
	 * Sets the transparency applied to the entire canvas.
	 * @param alpha The alpha, from 0-1. 0 is fully transparent, 1 is fully
	 * 		opaque
	 */
	void alpha(float alpha);
	/**
	 * Sets the composite operation.
	 * @param operation The operation to use.
	 */
	//void composite(CompositeOperation operation);
	/**
	 * Sets the composite operation, using OpenGL-style blend parameters.
	 * @param src The source blend factor
	 * @param dest The destination blend factor
	 * @see <a href="http://www.andersriggelsen.dk/glblendfunc.php">http://www.andersriggelsen.dk/glblendfunc.php</a>
	 */
	//void composite(BlendFactor src, BlendFactor dest);
	/**
	 * Sets the composite operation, using OpenGL-style blend parameters, and
	 * setting color/alpha operations separately.
	 * @param srcColor The source blend factor, for color
	 * @param destColor The destination blend factor, for color
	 * @param srcAlpha The source blend factor, for alpha
	 * @param destAlpha The destination blend factor, for alpha
	 * @see <a href="http://www.andersriggelsen.dk/glblendfunc.php">http://www.andersriggelsen.dk/glblendfunc.php</a>
	 */
	//void composite(BlendFactor srcColor, BlendFactor destColor,
	//		BlendFactor srcAlpha, BlendFactor destAlpha);
	
	// TODO uncomment the above methods when LWJGL is updated to the new NanoVG
	
	
	/**
	 * Resets the current transform to an identity matrix.
	 */
	void resetTransform();
	/**
	 * Translates the current coordinate system.
	 * @param x The distance to translate horizontally
	 * @param y The distance to translate vertically
	 */
	void translate(float x, float y);
	/**
	 * Rotates the current coordinate system.
	 * @param angle The angle to rotate by, specified in <b>radians</b>.
	 */
	void rotate(float angle);
	/**
	 * Skews the current coordinate system.
	 * @param angleX The angle to skew by, horizontally. Specified in
	 * 		<b>radians</b>.
	 * @param angleY The angle to skew by, vertically. Specified in
	 * 		<b>radians</b>.
	 */
	void skew(float angleX, float angleY);
	/**
	 * Scales the current coordinate system.
	 * @param x The amount to scale horizontally. 1 is unchanged, 0.5 is
	 * 		half the size, 2 is twice the size.
	 * @param x The amount to scale vertically. 1 is unchanged, 0.5 is
	 * 		half the size, 2 is twice the size.
	 */
	void scale(float x, float y);
	
	
	/**
	 * Convert degrees to radians. Prefer this method to {@link Math#toRadians},
	 * as the Canvas implementation may be faster.
	 * @param deg The angle in degrees
	 * @return The angle in radians
	 */
	default float degToRad(float deg) {
		return (float)Math.toRadians(deg);
	}
	/**
	 * Convert radians to degrees. Prefer this method to {@link Math#toDegrees},
	 * as the Canvas implementation may be faster.
	 * @param rad The angle in radians
	 * @return The angle in degrees
	 */
	default float radToDeg(float rad) {
		return (float)Math.toDegrees(rad);
	}
	
	
	/**
	 * Convert the given Java AWT image into a Canvas image.
	 * @param img The BufferedImage to convert
	 * @param modes Any special modes to apply to the image
	 * @return A newly allocated Image
	 */
	Image createImage(BufferedImage img, ImageMode... modes);
	/**
	 * Load the given filename from disk into a Canvas image.
	 * <p>
	 * Supports JPG, PNG, PSD, TGA, PIC, and GIF, with <a href="https://github.com/nothings/stb/blob/master/stb_image.h#L19">some limitations</a>.
	 * Don't pass in user data, it may not load correctly. Use the more robust
	 * {@link ImageIO} from the JDK and call {@link #createImage(BufferedImage)}
	 * for user data.
	 * @param file The file to load from disk
	 * @param modes Any special modes to apply to the image
	 * @return A newly allocated Image
	 */
	Image createImage(File file, ImageMode... modes);
	/**
	 * Load the given filename from the classpath into a Canvas image.
	 * <p>
	 * Supports JPG, PNG, PSD, TGA, PIC, and GIF, with <a href="https://github.com/nothings/stb/blob/master/stb_image.h#L19">some limitations</a>.
	 * @param path The file to load off the classpath
	 * @param modes Any special modes to apply to the image
	 * @return A newly allocated Image
	 */
	Image createImage(String path, ImageMode... modes);
	
	/**
	 * Creates a linear gradient.
	 * @param startX The horizontal start coordinate
	 * @param startY The vertical start coordinate
	 * @param endX The horizontal end coordinate
	 * @param endY The vertical end coordinate
	 * @param start The start color (will become the Inner color)
	 * @param end The end color (will become the Outer color)
	 * @return A newly allocated Paint
	 */
	Paint createLinearGradient(float startX, float startY, float endX, float endY,
			Color start, Color end);
	/**
	 * Creates a box gradient, a feathered rounded rectangle. Useful for
	 * rendering drop shadows or highlights.
	 * @param x Position of the left side of the rectangle
	 * @param y Position of the top of the rectangle
	 * @param width Width of the rectangle
	 * @param height Height of the rectangle
	 * @param radius The corner radius of the rectangle
	 * @param feather How blurry to make the rectangle
	 * @param inner The inner color
	 * @param outer The outer color
	 * @return A newly allocated Paint
	 */
	Paint createBoxGradient(float x, float y, float width, float height,
			float radius, float feather, Color inner, Color outer);
	/**
	 * Creates a radial gradient.
	 * @param centerX The horizontal center
	 * @param centerY The vertical center
	 * @param innerRadius The inner radius of the gradient
	 * @param outerRadius The outer radius of the gradient
	 * @param inner The inner color
	 * @param outer The outer color
	 * @return A newly allocated Paint
	 */
	Paint createRadialGradient(float centerX, float centerY,
			float innerRadius, float outerRadius, Color inner, Color outer);
	/**
	 * Creates an image pattern.
	 * @param x Position of the left side of the rectangle
	 * @param y Position of the top of the rectangle
	 * @param width The width of one image
	 * @param height The height of one image
	 * @param angle The angle to rotate the entire pattern by, in <b>radians</b>
	 * @param image The image to use
	 * @param alpha The opacity, 1 being opaque, 0 being transparent
	 * @return A newly allocated Paint
	 * @see #createImage(BufferedImage)
	 * @see #createImage(File)
	 * @see #createImage(String)
	 */
	Paint createPattern(float x, float y, float width, float height,
			float angle, Image image, float alpha);
	
	
	/**
	 * Set the current scissor rectangle. Transformations like
	 * {@link #translate} apply.
	 * @param x Position of the left side of the rectangle
	 * @param y Position of the top of the rectangle
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 */
	void scissor(float x, float y, float width, float height);
	/**
	 * Intersects the current scissor rectangle with the specified rectangle.
	 * <p>
	 * Note: in case the rotation of the previous scissor rectangle differs from
	 * the current one, the intersection will be done between the specified
	 * rectangle and the previous scissor rectangle transformed in the current
	 * transform space. The resulting shape is always a rectangle.
	 * @param x Position of the left side of the rectangle
	 * @param y Position of the top of the rectangle
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 */
	void intersectScissor(float x, float y, float width, float height);
	/**
	 * Resets the scissor rectangle and disables scissoring.
	 */
	void resetScissor();
	
	
	/**
	 * Clear the current path and sub-paths.
	 */
	void beginPath();
	/**
	 * Starts a new sub-path with the specified point as its first point.
	 * @param x The horizontal position
	 * @param y The vertical position
	 */
	void moveTo(float x, float y);
	/**
	 * Adds a line segment from the last point in the path to the specified
	 * point.
	 * @param x The horizontal position
	 * @param y The vertical position
	 */
	void lineTo(float x, float y);
	/**
	 * Adds a cubic Bezier segment from the last point in the path via two
	 * control points to the specified point.
	 * @param control1x The first control point's horizontal position
	 * @param control1y The first control point's vertical position
	 * @param control2x The second control point's horizontal position
	 * @param control2y The second control point's vertical position
	 * @param x The horizontal position to draw to
	 * @param y The vertical position to draw to
	 */
	void bezierTo(float control1x, float control1y,
			float control2x, float control2y, float x, float y);
	/**
	 * Adds a quadratic Bezier segment from the last point in the path via a
	 * control point to the specified point.
	 * @param controlX The control point's horizontal position
	 * @param controlY The control point's vertical position
	 * @param x The horizontal position to draw to
	 * @param y The vertical position to draw to
	 */
	void quadTo(float controlX, float controlY, float x, float y);
	/**
	 * Adds an arc segment at the corner defined by the last path point, and
	 * two specified control points.
	 * @param control1x The first control point's horizontal position
	 * @param control1y The first control point's vertical position
	 * @param control2x The second control point's horizontal position
	 * @param control2y The second control point's vertical position
	 * @param radius The arc's radius
	 */
	void arcTo(float control1x, float control1y,
			float control2x, float control2y, float radius);
	/**
	 * Closes the current sub-path with a line segment.
	 */
	void closePath();
	/**
	 * Sets the current sub-path winding.
	 * @param winding The winding to use
	 * @see Solidity#SOLID
	 * @see Solidity#HOLE
	 * @see Winding#CCW
	 * @see Winding#CW
	 */
	void pathWinding(Winding winding);
	/**
	 * Creates a new circle arc shaped sub-path.
	 * @param centerX The horizontal center of the arc
	 * @param centerY The vertical center of the arc
	 * @param radius The radius of the arc
	 * @param angleStart The angle to start at, specified in <b>radians</b>
	 * @param angleEnd The angle to end at, specified in <b>radians</b>
	 * @param winding The winding to draw the arc with
	 * @see Winding#CCW
	 * @see Winding#CW
	 */
	void arc(float centerX, float centerY, float radius,
			float angleStart, float angleEnd, Winding winding);
	/**
	 * Creates a new rectangular sub-path.
	 * @param x The horizontal position of the rectangle
	 * @param y The vertical position of the rectangle
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 */
	void rect(float x, float y, float width, float height);
	/**
	 * Creates a new rounded rectangle sub-path, with all corners having the
	 * same radius.
	 * @param x The horizontal position of the rectangle
	 * @param y The vertical position of the rectangle
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 * @param radius The radius of each corner
	 */
	void roundedRect(float x, float y, float width, float height, float radius);
	/**
	 * Creates a new rounded rectangular sub-path, with each corner having its
	 * own radius.
	 * @param x The horizontal position of the rectangle
	 * @param y The vertical position of the rectangle
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 * @param radiusTopLeft The radius of the top-left corner of the rectangle
	 * @param radiusTopRight The radius of the top-right corner of the rectangle
	 * @param radiusBottomRight The radius of the bottom-right corner of the
	 * 		rectangle
	 * @param radiusBottomLeft The radius of the bottom-left corner of the
	 * 		rectangle
	 */
	//void roundedRect(float x, float y, float width, float height,
	//		float radiusTopLeft, float radiusTopRight,
	//		float radiusBottomRight, float radiusBottomLeft);
	// TODO uncomment the above method when LWJGL is updated to the new NanoVG
	/**
	 * Creates a new ellipsoid sub-path.
	 * @param centerX The horizontal position of the center
	 * @param centerY The vertical position of the center
	 * @param radiusX The horizontal radius of the ellipse
	 * @param radiusY The vertical radius of the ellipse
	 */
	void ellipse(float centerX, float centerY, float radiusX, float radiusY);
	/**
	 * Creates a new circular sub-path.
	 * @param centerX The horizontal position of the center
	 * @param centerY The vertical position of the center
	 * @param radius The radius of the circle.
	 */
	void circle(float centerX, float centerY, float radius);
	/**
	 * Fills the current path with the current fill style.
	 * @see #fillStyle(Color)
	 * @see #fillStyle(Paint)
	 */
	void fill();
	/**
	 * Strokes the current path with the current stroke style.
	 * @see #strokeStyle(Color)
	 * @see #strokeStyle(Paint)
	 */
	void stroke();
	
	
	
	/**
	 * Load a TrueType font off the classpath, with <a href="https://github.com/nothings/stb/blob/master/stb_truetype.h">some limitations</a>.
	 * @param name The name of the font family and style, such as "Roboto Light"
	 * @param path The path of the font file
	 */
	Font createFont(String name, String path);
	/**
	 * Load a TrueType font from a file, with <a href="https://github.com/nothings/stb/blob/master/stb_truetype.h">some limitations</a>.
	 * <p>
	 * Avoid using user-specified fonts, they may not render correctly.
	 * @param name The name of the font family and style, such as "Roboto Light"
	 * @param file The file to read from
	 */
	Font loadFont(String name, File file);
	
	/**
	 * Find a font by its name, such as "Roboto Light".
	 * @param name The name of the font to look for
	 * @return The Font, or null
	 */
	Font findFont(String name);
	/**
	 * Adds a fallback font.
	 * @param base The base font
	 * @param fallback The fallback font
	 */
	//void addFallbackFont(Font base, Font fallback);
	// TODO uncomment the above method when LWJGL is updated to the new NanoVG
	
	
	/**
	 * Sets the font size of the current text style.
	 * @param size The size of the text
	 */
	void textSize(float size);
	/**
	 * Sets the blur of the current text style.
	 * @param blur The amount to blur
	 */
	void textBlur(float blur);
	/**
	 * Sets the letter spacing of the current text style.
	 * @param spacing The letter spacing
	 */
	void textLetterSpacing(float spacing);
	/**
	 * Sets the proportional line height of the current text style.
	 * @param lineHeight The line height, specified as a multiple of the font
	 * 		size
	 */
	void textLineHeight(float lineHeight);
	/**
	 * Sets the alignment of the current text style. See {@link HorizontalAlign}
	 * and {@link VerticalAlign} for details.
	 * @param horizontal The horizontal alignment
	 * @param vertical The vertical alignment
	 */
	void textAlign(HorizontalAlign horizontal, VerticalAlign vertical);
	/**
	 * Sets the font face to use for text drawing.
	 * @param font The font to use
	 */
	void fontFace(Font font);
	
	/**
	 * Draws the text string at the specified location.
	 * <p>
	 * Text is not affected by {@link #scale} and {@link #rotate}. The current
	 * {@link #fillStyle} will be used. Most Canvas implementations only support
	 * solid colors for text, and won't work with Paints.
	 * @param x The horizontal position to draw the text at
	 * @param y The vertical position to draw the text at
	 * @param string The string to draw
	 */
	void drawText(float x, float y, String string);
	/**
	 * Draws the text string at the specified location, wrapped at the specified
	 * width.
	 * <p>
	 * Whitespace is stripped at the beginning of the rows, and the text is
	 * split at word boundaries or when newlines are encountered.
	 * <p>
	 * Words longer than the max width are split at the nearest character,
	 * without hyphenation.
	 * <p>
	 * Text is not affected by {@link #scale} and {@link #rotate}. The current
	 * {@link #fillStyle} will be used. Most Canvas implementations only support
	 * solid colors for text, and won't work with Paints.
	 * @param x The horizontal position to draw the text at
	 * @param y The vertical position to draw the text at
	 * @param breakWidth The width to split text at
	 * @param string The string to draw
	 */
	void drawTextBox(float x, float y, float breakWidth, String string);
	
	
	
	
	/**
	 * Create a Color from a packed RGB value.
	 * <p>
	 * Usually used for constants, like {@code 0xFF0000} for opaque red.
	 * @param rgb A packed 24-bit _RGB value, where _ is unused
	 * @return A newly allocated Color with the given value
	 */
	Color colorFromPackedRGB(int rgb);
	/**
	 * Create a Color from a packed ARGB value.
	 * <p>
	 * Usually used for constants, like {@code 0xAAFF0000} for translucent red.
	 * @param rgb A packed 32-bit ARGB value
	 * @return A newly allocated Color with the given value
	 */
	Color colorFromPackedARGB(int argb);
	
	
	/**
	 * Create a Color from individual red, green, and blue components.
	 * <p>
	 * @param r The red value, from 0-1
	 * @param g The green value, from 0-1
	 * @param b The blue value, from 0-1
	 * @return A newly allocated Color with the given value
	 */
	Color colorFromRGB(float r, float g, float b);
	/**
	 * Create a Color from individual red, green, blue, and alpha components.
	 * <p>
	 * @param r The red value, from 0-1
	 * @param g The green value, from 0-1
	 * @param b The blue value, from 0-1
	 * @param a The alpha value, from 0-1
	 * @return A newly allocated Color with the given value
	 */
	Color colorFromRGBA(float r, float g, float b, float a);
	
	
	/**
	 * Create a Color from hue, saturation, and lightness.
	 * <p>
	 * @param h The hue, from 0-1, where 0 is 0° and 1 is 360°
	 * @param s The saturation, from 0-1
	 * @param l The lightness, from 0-1, where 0 is black and 1 is white.
	 * @return A newly allocated Color with the given value
	 */
	Color colorFromHSL(float h, float s, float l);
	/**
	 * Create a Color from hue, saturation, lightness, and alpha.
	 * <p>
	 * @param h The hue, from 0-1, where 0 is 0° and 1 is 360°
	 * @param s The saturation, from 0-1
	 * @param l The lightness, from 0-1, where 0 is black and 1 is white.
	 * @param a The alpha value, from 0-1
	 * @return A newly allocated Color with the given value
	 */
	Color colorFromHSLA(float h, float s, float l, float a);
	
	
	/**
	 * Create a Color that is in-between the two passed colors.
	 * @param c0 The first color
	 * @param c1 The second color
	 * @param u How close to the second color to get. 0 is equivalent to
	 * 		{@code c0}, 0.5 is halfway between, and 1 is equivalent to
	 * 		{@code c1}.
	 * @return A newly allocated Color, even if u is 0 or 1.
	 */
	Color lerp(Color c0, Color c1, float u);
	
}
