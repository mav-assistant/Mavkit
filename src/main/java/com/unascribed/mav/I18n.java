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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import com.google.common.base.Throwables;

public final class I18n {
	private static final String BUNDLE_NAME = "com.unascribed.mav.lang";

	private static final ResourceBundle LOCAL = ResourceBundle.getBundle(BUNDLE_NAME);
	private static final ResourceBundle DEFAULT = ResourceBundle.getBundle(BUNDLE_NAME, Locale.ROOT);

	private I18n() {}

	public static String get(String key) {
		try {
			return LOCAL.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public static String get(String key, Object arg) {
		FormattingTuple tuple = MessageFormatter.format(get(key), arg);
		return tuple.getMessage()+(tuple.getThrowable() != null ? "\n"+Throwables.getStackTraceAsString(tuple.getThrowable()) : "");
	}
	
	public static String get(String key, Object arg1, Object arg2) {
		FormattingTuple tuple = MessageFormatter.format(get(key), arg1, arg2);
		return tuple.getMessage()+(tuple.getThrowable() != null ? "\n"+Throwables.getStackTraceAsString(tuple.getThrowable()) : "");
	}
	
	public static String get(String key, Object... args) {
		FormattingTuple tuple = MessageFormatter.arrayFormat(get(key), args);
		return tuple.getMessage()+(tuple.getThrowable() != null ? "\n"+Throwables.getStackTraceAsString(tuple.getThrowable()) : "");
	}
	
	public static String getDefault(String key) {
		try {
			return DEFAULT.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public static String getDefault(String key, Object arg) {
		FormattingTuple tuple = MessageFormatter.format(getDefault(key), arg);
		return tuple.getMessage()+(tuple.getThrowable() != null ? "\n"+Throwables.getStackTraceAsString(tuple.getThrowable()) : "");
	}
	
	public static String getDefault(String key, Object arg1, Object arg2) {
		FormattingTuple tuple = MessageFormatter.format(getDefault(key), arg1, arg2);
		return tuple.getMessage()+(tuple.getThrowable() != null ? "\n"+Throwables.getStackTraceAsString(tuple.getThrowable()) : "");
	}
	
	public static String getDefault(String key, Object... args) {
		FormattingTuple tuple = MessageFormatter.arrayFormat(getDefault(key), args);
		return tuple.getMessage()+(tuple.getThrowable() != null ? "\n"+Throwables.getStackTraceAsString(tuple.getThrowable()) : "");
	}
}
