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


import java.net.MalformedURLException;
import java.net.URL;

/**
 * TODO
 */
public class Url extends FieldType
{
    public Url(String type)
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
        try
        {
            new URL(value);
        }
        catch (MalformedURLException e)
        {
            return false;
        }

        return true;
    }
}
