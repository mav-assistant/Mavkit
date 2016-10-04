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

public class Panic extends Error {
	private static final long serialVersionUID = -8494908034465131450L;

	private final String key;
	private final Object[] args;
	
	public Panic(String key) {
		this.key = key;
		this.args = new Object[0];
	}
	
	public Panic(String key, Object... args) {
		this.key = key;
		this.args = args;
	}
	
	@Override
	public String getMessage() {
		return I18n.getDefault(key, args);
	}
	
	@Override
	public String getLocalizedMessage() {
		return I18n.get(key, args);
	}

}
