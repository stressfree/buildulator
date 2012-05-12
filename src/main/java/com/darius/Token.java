/*
 * Copyright 2002 by Darius Bacon <darius@wry.me>.
 *
 * Permission is granted to anyone to use this software for any
 * purpose on any computer system, and to redistribute it freely,
 * subject to the following restrictions:
 *
 * 1. The author is not responsible for the consequences of use of
 *    this software, no matter how awful, even if they arise from
 *    defects in it.
 *
 * 2. The origin of this software must not be misrepresented, either
 *    by explicit claim or by omission.
 *
 * 3. Altered versions must be plainly marked as such, and must not
 *    be misrepresented as being the original software.
 */
package com.darius;

/**
 * The Class Token.
 */
class Token {

    /** The Constant TT_ERROR. */
    public static final int TT_ERROR = -1;

    /** The Constant TT_EOF. */
    public static final int TT_EOF = -2;

    /** The Constant TT_NUMBER. */
    public static final int TT_NUMBER = -3;

    /** The Constant TT_WORD. */
    public static final int TT_WORD = -4;

    /** The Constant TT_LE. */
    public static final int TT_LE = -5;

    /** The Constant TT_NE. */
    public static final int TT_NE = -6;

    /** The Constant TT_GE. */
    public static final int TT_GE = -7;

    /**
     * Instantiates a new token.
     *
     * @param ttype the ttype
     * @param nval the nval
     * @param input the input
     * @param start the start
     * @param end the end
     */
    public Token(int ttype, double nval, String input, int start, int end) {
        this.ttype = ttype;
        this.sval = input.substring(start, end);
        this.nval = nval;
        this.location = start;

        int count = 0;
        for (int i = start - 1; 0 <= i; --i) {
            if (!Character.isWhitespace(input.charAt(i)))
                break;
            ++count;
        }
        this.leadingWhitespace = count;

        count = 0;
        for (int i = end; i < input.length(); ++i) {
            if (!Character.isWhitespace(input.charAt(i)))
                break;
            ++count;
        }
        this.trailingWhitespace = count;
    }

    /**
     * Instantiates a new token.
     *
     * @param ttype the ttype
     * @param nval the nval
     * @param sval the sval
     * @param token the token
     */
    Token(int ttype, double nval, String sval, Token token) {
        this.ttype = ttype;
        this.sval = sval;
        this.nval = nval;
        this.location = token.location;
        this.leadingWhitespace = token.leadingWhitespace;
        this.trailingWhitespace = token.trailingWhitespace;
    }

    /** The ttype. */
    public final int ttype;

    /** The sval. */
    public final String sval;

    /** The nval. */
    public final double nval;

    /** The location. */
    public final int location;

    /** The trailing whitespace. */
    public final int leadingWhitespace, trailingWhitespace;
}
