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
package com.gameminers.mav;

public class Strings {
	/**
	 * Compares two strings, trimming either to length if necessary, and returns how alike they are from 0 to 1.0.<br/>
	 * The length of the second string is seen as the "master" length, and 1.0 will only be returned if s1 matches s2 perfectly up to it's
	 * length.<br/>
	 * Examples<br/>
	 * 
	 * <pre>
	 * similarity("abcde", "abc") -> 1.0
	 * similarity("abc", "abcde") ->  0.6
	 * similarity("foo", "bar") -> 0.0
	 * similarity("hamburger", "ham") -> 1.0
	 * similarity("ham", "hamburger") -> 0.33333333
	 * similarity("bar", "baz") -> 0.66666667
	 * </pre>
	 * 
	 * @author Aesen Vismea
	 * @param s1
	 *            The first string in the comparison operation
	 * @param s2
	 *            The second, "master" string, in the comparison
	 * @return A double from 0.0 to 1.0 denoting how similar the two strings are
	 */
	public static double similarity(String s1, String s2) {
		if (s1 == null && s2 != null) return 0.0;
		if (s1.equals(s2)) return 1.0;
		if (s1.isEmpty()) return 0.0;
		final int max = s2.length();
		if (s1.length() > s2.length()) {
			s1 = s1.substring(0, s2.length() - 1);
		}
		int matched = 0;
		for (int i = 0; i < s2.length(); i++) {
			if (s1.length() <= i) {
				break;
			}
			if (s1.charAt(i) == s2.charAt(i)) {
				matched++;
			}
		}
		return (double) matched / (double) max;
	}
}
