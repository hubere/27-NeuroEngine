/*
 * Copyright (C) 2013-2014 Phokham Nonava
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.fluxchess.pulse;

import java.io.StringBufferInputStream;

final class UCIEmulator {

	void run() {

		StringBufferInputStream s = new StringBufferInputStream("ABCD");
		System.setIn(s);

		s = new StringBufferInputStream("ABCD");

		s = new StringBufferInputStream("ABCD");
		System.setIn(s);

		// uci
		// uciok
		// ucinewgame
		// position startpos moves e2e4 e7e5
		// go infinite

	}

}
