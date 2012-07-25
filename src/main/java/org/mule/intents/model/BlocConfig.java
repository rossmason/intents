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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * TODO
 */
public class BlocConfig
{
    private String name;
    private Properties params = new Properties();

    public BlocConfig()
    {
    }

    public BlocConfig(String name, Properties params)
    {
        this.name = name;
        this.params = params;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Properties getParams()
    {
        return params;
    }

    public void setParams(Properties params)
    {
        this.params = params;
    }
}
