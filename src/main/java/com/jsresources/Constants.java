/*
 *	Constants.java
 */

/*
 * Copyright (c) 2004 by Florian Bomers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 |<---            this code is formatted to fit into 80 columns             --->|
 */

package com.jsresources;

public class Constants {
	public static final int DIR_MIC = 0;
	public static final int DIR_SPK = 1;

	public static boolean DEBUG = false;
	public static boolean VERBOSE = false;

	public static final int FORMAT_CODE_CD = 1;
	public static final int FORMAT_CODE_FM = 2;
	public static final int FORMAT_CODE_TELEPHONE = 3;
	public static final int FORMAT_CODE_GSM = 4;

	public static final String[] FORMAT_NAMES = {
			"Cell phone GSM (13.2KBit/s - Modem)",
			"Telephone ulaw (64KBit/s - ISDN)",
			"FM quality mono (352.8KBit/s - ADSL)",
			"CD quality mono (705.6KBit/s - LAN)" };

	public static final int[] FORMAT_CODES = { FORMAT_CODE_GSM,
			FORMAT_CODE_TELEPHONE, FORMAT_CODE_FM, FORMAT_CODE_CD };

	public static final int[] BUFFER_SIZE_MILLIS = { 30, 40, 50, 70, 85, 100,
			130, 150, 180, 220, 400 };
	public static final String[] BUFFER_SIZE_MILLIS_STR = { "30", "40", "50",
			"70", "85", "100", "130", "150", "180", "220", "400" };
	public static final int BUFFER_SIZE_INDEX_DEFAULT = 2;

	public static final String CONNECTION_PROPERTY = "CONNECTION";
	public static final String AUDIO_PROPERTY = "AUDIO";

	public static final int PROTOCOL_MAGIC = 0x43484154;
	public static final int PROTOCOL_VERSION = 1;
	public static final int PROTOCOL_ACK = 1001;
	public static final int PROTOCOL_ERROR = 1002;

	// Socket options
	public static final boolean TCP_NODELAY = false;
	// -1 means do not set the value
	public static final int TCP_RECEIVE_BUFFER_SIZE = 1024;
	public static final int TCP_SEND_BUFFER_SIZE = 1024;

	public static final int CONNECTION_TYPE_TCP = 1;
	public static final int CONNECTION_TYPE_UDP = 2;

	public static final int[] CONNECTION_TYPES = { CONNECTION_TYPE_TCP,
			CONNECTION_TYPE_UDP };

	public static final String[] CONNECTION_TYPE_NAMES = { "TCP (for LAN)",
			"UDP (for WAN)" };

	public static final int DEFAULT_PORT = 8765;
	public static final int DEFAULT_CONNECTION_TYPE = CONNECTION_TYPE_TCP;
	public static final int DEFAULT_FORMAT_CODE = FORMAT_CODE_TELEPHONE;

	public static void out(String s) {
		System.out.println(s);
	}
}
