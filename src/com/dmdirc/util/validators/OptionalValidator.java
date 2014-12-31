/*
 * Copyright (c) 2006-2015 DMDirc Developers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.dmdirc.util.validators;

/**
 * Validates an optional setting in tandem with another validator.
 */
public class OptionalValidator implements Validator<String> {

    /**
     * The validator to validate the optional setting.
     */
    protected final Validator<String> validator;

    /**
     * Creates a new Optional validator.
     *
     * @param validator The validator for the setting's value, if present
     */
    public OptionalValidator(final Validator<String> validator) {
        this.validator = validator;
    }

    /**
     * Gets the secondary validator.
     *
     * @return Secondary validator
     */
    public Validator<String> getValidator() {
        return validator;
    }

    @Override
    public ValidationResponse validate(final String object) {
        if (object == null) {
            return validator.validate(null);
        }

        final int colonIndex = object.indexOf(':');

        if (colonIndex == -1) {
            return validator.validate(object);
        }

        final String booleanv = object.substring(0, colonIndex);

        if ("true".equals(booleanv) || "false".equals(booleanv)) {
            return validator.validate(object.substring(colonIndex + 1));
        }

        return validator.validate(object);
    }
}
