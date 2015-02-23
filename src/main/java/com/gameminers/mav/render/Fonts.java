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
import com.gameminers.mav.screen.Screen;

public class Fonts {

	public static void loadFonts() {
		try {
			InputStream baseIn = ClassLoader.getSystemResourceAsStream("resources/fonts/OpenSans-Regular.ttf");
			InputStream lightIn = ClassLoader.getSystemResourceAsStream("resources/fonts/OpenSans-Light.ttf");

			Font baseFont = Font.createFont(Font.TRUETYPE_FONT, baseIn);
			Font lightFont = Font.createFont(Font.TRUETYPE_FONT, lightIn);

			baseIn.close();
			lightIn.close();
			
			Screen.baseFont = new TrueTypeFont[] {
					new TrueTypeFont(baseFont.deriveFont(14.0f), true),
					new TrueTypeFont(baseFont.deriveFont(24.0f), true),
					new TrueTypeFont(baseFont.deriveFont(36.0f), true),
					new TrueTypeFont(baseFont.deriveFont(48.0f), true),
			};
			Screen.lightFont = new TrueTypeFont[] {
					new TrueTypeFont(lightFont.deriveFont(14.0f), true),
					new TrueTypeFont(lightFont.deriveFont(24.0f), true),
					new TrueTypeFont(lightFont.deriveFont(36.0f), true),
					new TrueTypeFont(lightFont.deriveFont(48.0f), true),
			};
		} catch (Throwable t) {
			Dialogs.showErrorDialog(null, "An error occurred while initializing assets. Mav will now exit.", t);
		}
	}

}
