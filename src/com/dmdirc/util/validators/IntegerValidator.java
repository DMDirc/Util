/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dmdirc.util.validators;

/**
 * Validates that a number is within certain bounds.
 */
public class IntegerValidator implements Validator<Integer> {

    /**
     * The minimum value for this number.
     */
    protected final int min;

    /**
     * The maximum value for this number.
     */
    protected final int max;

    /**
     * Creates a new numerical validator with the specified bounds.
     *
     * @param min The minimum value for the number, or -1 for unlimited.
     * @param max The maximum value for the number, or -1 for unlimited.
     */
    public IntegerValidator(final int min, final int max) {
        this.max = max == -1 ? Integer.MAX_VALUE : max;
        this.min = min == -1 ? Integer.MIN_VALUE : min;
        if (this.min > this.max) {
            throw new IllegalArgumentException("min must be less than max.");
        }
    }

    /**
     * Retrieves the maximum value that this validator will allow.
     *
     * @return This validator's maximum value
     */
    public int getMax() {
        return max;
    }

    /**
     * Retrieves the minimum value that this validator will allow.
     *
     * @return This validator's minimum value
     */
    public int getMin() {
        return min;
    }

    @Override
    public ValidationResponse validate(final Integer object) {

        if (object < min) {
            return new ValidationResponse("Must be at least " + min);
        } else if (object > max) {
            return new ValidationResponse("Must be at most " + max);
        } else {
            return new ValidationResponse();
        }
    }

}
