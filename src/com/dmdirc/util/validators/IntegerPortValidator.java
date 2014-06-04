/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dmdirc.util.validators;

/**
 * Validates a port number.
 */
public class IntegerPortValidator extends IntegerValidator {

    /**
     * The minimum port number.
     */
    private static final int MIN_PORT = 1;

    /**
     * The maximum port number.
     */
    private static final int MAX_PORT = 65535;

    /**
     * Creates a new port validator.
     */
    public IntegerPortValidator() {
        super(MIN_PORT, MAX_PORT);
    }

}
