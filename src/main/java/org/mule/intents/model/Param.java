/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.intents.model;

import org.mule.intents.model.fields.FieldType;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * TODO
 */
public class Param
{
    private String name;
    private String description;
    private String type;
    private FieldType fieldType;
    private boolean optional = false;
    @JsonProperty("default")
    private String defaultValue;

    public Param()
    {
    }

    public Param(String name, String description, boolean optional, String defaultValue)
    {
        this.name = name;
        this.description = description;
        this.optional = optional;
        this.defaultValue = defaultValue;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isOptional()
    {
        return optional;
    }

    public void setOptional(boolean optional)
    {
        this.optional = optional;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public void setType(String type)
    {
        this.type = type;
        fieldType = FieldType.get(type);
    }

    public boolean isValid(String value) {
        return fieldType.isValid(value);
    }

    public String getType()
    {
        return type;
    }

    public Object validate(String value, StringBuilder errors)
    {
        if (value == null)
        {
            if (isOptional())
            {
                return getDefaultValue();
            }
            else
            {
                errors.append("Value is null but is not optional: ").append(getName()).append("\n");
            }
        }
        return value;
    }
}
