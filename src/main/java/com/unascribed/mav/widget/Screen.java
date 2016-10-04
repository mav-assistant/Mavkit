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

package com.unascribed.mav.widget;

import java.util.List;

import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.checkerframework.checker.nullness.qual.NonNull;
import com.google.common.collect.Lists;
import com.unascribed.mav.render.Canvas;
import com.unascribed.mav.render.Renderable;

public abstract class Screen implements Renderable {
	private List<Widget> widgets = Lists.newArrayList();
	
	@Override
	@UIEffect
	public void render(@NonNull Canvas c, double delta) {
		c.isolate(() -> {
			for (Widget w : widgets) {
				
			}
		});
	}

}
