/*
 * Copyright (c) 2006-2014 DMDirc Developers
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Allows multiple validators to be chained together.
 *
 * @param <A> The type of class that this chain validates
 */
public class ValidatorChain<A> implements Validator<A> {

    /**
     * A list of validators to use.
     */
    private final List<Validator<A>> validatorList;

    /**
     * Creates a new validator chain containing the specified validators.
     *
     * @param validators The validators to be used in this chain.
     * @deprecated Use a builder or pass a list to avoid unchecked warnings.
     */
    @SuppressWarnings({"unchecked", "varargs"})
    @Deprecated
    public ValidatorChain(final Validator<A>... validators) {
        validatorList = Arrays.asList(validators);
    }

    /**
     * Creates a new validator chain containing the specified validators.
     *
     * @param validators The validators to be used in this chain.
     */
    public ValidatorChain(final List<Validator<A>> validators) {
        validatorList = Collections.unmodifiableList(validators);
    }

    @Override
    public ValidationResponse validate(final A object) {
        for (Validator<A> validator : validatorList) {
            final ValidationResponse res = validator.validate(object);

            if (res.isFailure()) {
                return res;
            }
        }

        return new ValidationResponse();
    }

    /**
     * Creates a builder which can be used to construct a new
     * {@link ValidatorChain}.
     *
     * @param <T> The type of validator that will be in the chain.
     * @return A builder to use to make a validator.
     */
    public static <T> ValidatorChainBuilder<T> builder() {
        return new ValidatorChainBuilder<>();
    }
}