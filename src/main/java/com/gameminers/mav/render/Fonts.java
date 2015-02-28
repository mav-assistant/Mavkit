/*
 * This file is part of Mav.
 *
 * Mav is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Mav is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with Mav. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gameminers.mav.render;

import java.awt.Font;
import java.io.InputStream;

import org.newdawn.slick.TrueTypeFont;

import com.gameminers.mav.Dialogs;

public class Fonts {

	private static Font lightFont;
	private static Font baseFont;
	
	private static Font[] lightAWT;
	private static Font[] baseAWT;
	
	public static TrueTypeFont[] light;
	public static TrueTypeFont[] base;
	
	public static float[] sizes = {
		14,
		24,
		36,
		48
	};
	
	public static void loadFonts() {
		try {
			
			InputStream baseIn = ClassLoader.getSystemResourceAsStream("resources/fonts/OpenSans-Regular.ttf");
			InputStream lightIn = ClassLoader.getSystemResourceAsStream("resources/fonts/OpenSans-Light.ttf");

			baseFont = Font.createFont(Font.TRUETYPE_FONT, baseIn);
			lightFont = Font.createFont(Font.TRUETYPE_FONT, lightIn);

			baseIn.close();
			lightIn.close();
			
			lightAWT = new Font[sizes.length];
			baseAWT = new Font[sizes.length];
			
			base = new TrueTypeFont[sizes.length];
			light = new TrueTypeFont[sizes.length];
			
			for (int i = 0; i < sizes.length; i++) {
				lightAWT[i] = lightFont.deriveFont(sizes[i]);
				baseAWT[i] = baseFont.deriveFont(sizes[i]);
				
				light[i] = new TrueTypeFont(lightAWT[i], true);
				base[i] = new TrueTypeFont(baseAWT[i], true);
			}
		} catch (Throwable t) {
			Dialogs.showErrorDialog(null, "An error occurred while initializing assets. Mav will now exit.", t);
		}
	}

}
