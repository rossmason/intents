/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.intents.model.fields;

/**
 * TODO
 */
public final class Text extends FieldType
{
    private String type;

    public Text(String type)
    {
        super(type);
    }

    @Override
    protected void parseType(String type)
    {
        this.type = type;
    }

    @Override
    public boolean isValid(String value)
    {
        //no op
        return true;
    }
}
