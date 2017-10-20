/*
 * Copyright (C) 2013-2014 Phokham Nonava
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.fluxchess.pulse;

import com.fluxchess.jcpi.models.GenericChessman;

final class PieceType {

  static final int MASK = 0x7;

  static final int PAWN = 0;
  static final int KNIGHT = 1;
  static final int BISHOP = 2;
  static final int ROOK = 3;
  static final int QUEEN = 4;
  static final int KING = 5;
  static final int NOPIECETYPE = 6;

  static final int[] values = {
      PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING
  };

  // Piece values as defined by Larry Kaufman
  static final int PAWN_VALUE = 100;
  static final int KNIGHT_VALUE = 325;
  static final int BISHOP_VALUE = 325;
  static final int ROOK_VALUE = 500;
  static final int QUEEN_VALUE = 975;
  static final int KING_VALUE = 20000;

  private PieceType() {
  }

  static boolean isValid(int pieceType) {
    switch (pieceType) {
      case PAWN:
      case KNIGHT:
      case BISHOP:
      case ROOK:
      case QUEEN:
      case KING:
        return true;
      case NOPIECETYPE:
      default:
        return false;
    }
  }

  static GenericChessman toGenericChessman(int pieceType) {
    switch (pieceType) {
      case PAWN:
        return GenericChessman.PAWN;
      case KNIGHT:
        return GenericChessman.KNIGHT;
      case BISHOP:
        return GenericChessman.BISHOP;
      case ROOK:
        return GenericChessman.ROOK;
      case QUEEN:
        return GenericChessman.QUEEN;
      case KING:
        return GenericChessman.KING;
      case NOPIECETYPE:
      default:
        throw new IllegalArgumentException();
    }
  }

  static boolean isValidPromotion(int pieceType) {
    switch (pieceType) {
      case KNIGHT:
      case BISHOP:
      case ROOK:
      case QUEEN:
        return true;
      case PAWN:
      case KING:
      case NOPIECETYPE:
      default:
        return false;
    }
  }

  static boolean isSliding(int pieceType) {
    switch (pieceType) {
      case BISHOP:
      case ROOK:
      case QUEEN:
        return true;
      case PAWN:
      case KNIGHT:
      case KING:
        return false;
      case NOPIECETYPE:
      default:
        throw new IllegalArgumentException();
    }
  }

  static int getValue(int pieceType) {
    switch (pieceType) {
      case PAWN:
        return PAWN_VALUE;
      case KNIGHT:
        return KNIGHT_VALUE;
      case BISHOP:
        return BISHOP_VALUE;
      case ROOK:
        return ROOK_VALUE;
      case QUEEN:
        return QUEEN_VALUE;
      case KING:
        return KING_VALUE;
      case NOPIECETYPE:
      default:
        throw new IllegalArgumentException();
    }
  }

}
