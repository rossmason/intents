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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * TODO
 */
public class Options extends FieldType
{
    private List<String> options;

    public Options(String type)
    {
        super(type);
    }

    @Override
    protected void parseType(String type)
    {
        options = new ArrayList<String>();
        int x = type.indexOf("(");
        int y = type.indexOf(")");
        if(x < 0 || y < 0) {
            throw new IllegalArgumentException("option-list type is malformed: " + type);
        }

        for (StringTokenizer tokenizer = new StringTokenizer(type.substring(x+1, y), ","); tokenizer.hasMoreTokens(); )
        {
            options.add(tokenizer.nextToken().trim());
        }

    }

    @Override
    public boolean isValid(String value)
    {
        return options.contains(value);
    }
}
