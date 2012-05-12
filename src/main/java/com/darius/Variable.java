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

import java.util.Hashtable;

/**
 * A variable is a simple expression with a name (like "x") and a settable value.
 */
public class Variable extends Expr {

    /** The variables. */
    private static Hashtable<String, Variable> variables =
            new Hashtable<String, Variable>();

    /**
     * Return a unique variable named `name'. There can be only one variable
     * with the same name returned by this method; that is, make(s1) == make(s2)
     * if and only if s1.equals(s2).
     *
     * @param name
     *            the variable's name
     * @return the variable; create it initialized to 0 if it doesn't yet exist
     */
    static public synchronized Variable make(String name) {
        Variable result = (Variable) variables.get(name);
        if (result == null) {
            variables.put(name, result = new Variable(name));
        }
        return result;
    }

    /** The name. */
    private String name;

    /** The val. */
    private double val;

    /**
     * Create a new variable, with initial value 0.
     *
     * @param name
     *            the variable's name
     */
    public Variable(String name) {
        this.name = name;
        val = 0;
    }

    /**
     * Return the name.
     *
     * @return the string
     */
    public String toString() {
        return name;
    }

    /**
     * Get the value.
     *
     * @return the current value
     */
    public double value() {
        return val;
    }

    /**
     * Set the value.
     *
     * @param value
     *            the new value
     */
    public void setValue(double value) {
        val = value;
    }
}
