/*
 * Copyright (c) 2006-2017 DMDirc Developers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
