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

import java.util.Vector;

/**
 * The Class Scanner.
 */
class Scanner {

    /** The s. */
    private String s;

    /** The operator chars. */
    private String operatorChars;

    /** The tokens. */
    Vector<Token> tokens = new Vector<Token>();

    /** The index. */
    int index = -1;

    /**
     * Instantiates a new scanner.
     *
     * @param string the string
     * @param operatorChars the operator chars
     */
    public Scanner(String string, String operatorChars) {
        this.s = string;
        this.operatorChars = operatorChars;

        int i = 0;
        do {
            i = scanToken(i);
        } while (i < s.length());
    }

    /**
     * Gets the input.
     *
     * @return the input
     */
    public String getInput() {
        return s;
    }

    // The tokens may have been diddled, so this can be different from
    // getInput().
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        int whitespace = 0;
        for (int i = 0; i < tokens.size(); ++i) {
            Token t = (Token) tokens.elementAt(i);

            int spaces = (whitespace != 0 ? whitespace : t.leadingWhitespace);
            if (i == 0)
                spaces = 0;
            else if (spaces == 0
                    && !joinable((Token) tokens.elementAt(i - 1), t))
                spaces = 1;
            for (int j = spaces; 0 < j; --j)
                sb.append(" ");

            sb.append(t.sval);
            whitespace = t.trailingWhitespace;
        }
        return sb.toString();
    }

    /**
     * Joinable.
     *
     * @param s the s
     * @param t the t
     * @return true, if successful
     */
    private boolean joinable(Token s, Token t) {
        return !(isAlphanumeric(s) && isAlphanumeric(t));
    }

    /**
     * Checks if is alphanumeric.
     *
     * @param t the t
     * @return true, if is alphanumeric
     */
    private boolean isAlphanumeric(Token t) {
        return t.ttype == Token.TT_WORD || t.ttype == Token.TT_NUMBER;
    }

    /**
     * Checks if is empty.
     *
     * @return true, if is empty
     */
    public boolean isEmpty() {
        return tokens.size() == 0;
    }

    /**
     * At start.
     *
     * @return true, if successful
     */
    public boolean atStart() {
        return index <= 0;
    }

    /**
     * At end.
     *
     * @return true, if successful
     */
    public boolean atEnd() {
        return tokens.size() <= index;
    }

    /**
     * Next token.
     *
     * @return the token
     */
    public Token nextToken() {
        ++index;
        return getCurrentToken();
    }

    /**
     * Gets the current token.
     *
     * @return the current token
     */
    public Token getCurrentToken() {
        if (atEnd())
            return new Token(Token.TT_EOF, 0, s, s.length(), s.length());
        return (Token) tokens.elementAt(index);
    }

    /**
     * Scan token.
     *
     * @param i the i
     * @return the int
     */
    private int scanToken(int i) {
        while (i < s.length() && Character.isWhitespace(s.charAt(i)))
            ++i;

        if (i == s.length()) {
            return i;
        } else if (0 <= operatorChars.indexOf(s.charAt(i))) {
            if (i + 1 < s.length()) {
                String pair = s.substring(i, i + 2);
                int ttype = 0;
                if (pair.equals("<="))
                    ttype = Token.TT_LE;
                else if (pair.equals(">="))
                    ttype = Token.TT_GE;
                else if (pair.equals("<>"))
                    ttype = Token.TT_NE;
                if (0 != ttype) {
                    tokens.addElement(new Token(ttype, 0, s, i, i + 2));
                    return i + 2;
                }
            }
            tokens.addElement(new Token(s.charAt(i), 0, s, i, i + 1));
            return i + 1;
        } else if (Character.isLetter(s.charAt(i))) {
            return scanSymbol(i);
        } else if (Character.isDigit(s.charAt(i)) || '.' == s.charAt(i)) {
            return scanNumber(i);
        } else {
            tokens.addElement(makeErrorToken(i, i + 1));
            return i + 1;
        }
    }

    /**
     * Scan symbol.
     *
     * @param i the i
     * @return the int
     */
    private int scanSymbol(int i) {
        int from = i;
        while (i < s.length()
                && (Character.isLetter(s.charAt(i)) || Character.isDigit(s
                        .charAt(i))))
            ++i;
        tokens.addElement(new Token(Token.TT_WORD, 0, s, from, i));
        return i;
    }

    /**
     * Scan number.
     *
     * @param i the i
     * @return the int
     */
    private int scanNumber(int i) {
        int from = i;

        // We include letters in our purview because otherwise we'd
        // accept a word following with no intervening space.
        for (; i < s.length(); ++i)
            if ('.' != s.charAt(i) && !Character.isDigit(s.charAt(i))
                    && !Character.isLetter(s.charAt(i)))
                break;

        String text = s.substring(from, i);
        double nval;
        try {
            nval = Double.valueOf(text).doubleValue();
        } catch (NumberFormatException nfe) {
            tokens.addElement(makeErrorToken(from, i));
            return i;
        }

        tokens.addElement(new Token(Token.TT_NUMBER, nval, s, from, i));
        return i;
    }

    /**
     * Make error token.
     *
     * @param from the from
     * @param i the i
     * @return the token
     */
    private Token makeErrorToken(int from, int i) {
        return new Token(Token.TT_ERROR, 0, s, from, i);
    }
}
