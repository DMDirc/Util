/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dmdirc.util.validators;

import java.util.List;

/**
 * Validator checking the list is not empty.
 *
 * @param <T> List type
 */
public class ListNotEmptyValidator<T> implements Validator<List<T>> {

    /**
     * Serialisation version.
     */
    private static final long serialVersionUID = 1;

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidationResponse validate(final List<T> object) {
        if (object == null || object.isEmpty()) {
            return new ValidationResponse("List cannot be an empty.");
        } else {
            return new ValidationResponse();
        }
    }

}
