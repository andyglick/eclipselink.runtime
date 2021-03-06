/*******************************************************************************
 * Copyright (c) 1998, 2015 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     11/05/2014-2.6 Tomas Kraus
 *       - 449818: Initial API and implementation.
 ******************************************************************************/
package org.eclipse.persistence.testing.models.jpa22.advanced.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts {@link String}s stored in database to not contain {@code ' '} characters. Any occurrence of {@code ' '}
 * character is replaced by {@code '+'} character.
 * @author Tomas Kraus
 */
@Converter(autoApply=false)
public class CompetitionConverter implements AttributeConverter<String, String> {

    /**
     * Entity to database conversion.
     * @param attribute  Entity attribute value to be converted.
     * @return Value to be stored in the database column.
     */
    @Override
    public String convertToDatabaseColumn(final String attribute) {
        if (attribute != null) {
            return attribute.replace(' ', '+');
        } else {
            return null;
        }
    }

    /**
     * Database to entity conversion.
     * @param dbData Database column value to be converted.
     * @return Value to be stored in entity.
     */
    @Override
    public String convertToEntityAttribute(final String dbData) {
        if (dbData != null) {
            return dbData.replace('+', ' ');
        } else {
            return null;
        }
    }

}
