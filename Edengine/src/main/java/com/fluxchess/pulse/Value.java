/*
 * Copyright (C) 2013-2014 Phokham Nonava
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.fluxchess.pulse;

public class Value {

  public static final int INFINITE = 200000;
  public static final  int CHECKMATE = 100000;
  public static final int CHECKMATE_THRESHOLD = CHECKMATE - Depth.MAX_PLY;
  public static final int DRAW = 0;
  public static final int NOVALUE = 300000;

  private Value() {
  }

}
