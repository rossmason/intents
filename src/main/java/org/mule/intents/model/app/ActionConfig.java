/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.intents.model.app;

import org.mule.intents.Registry;
import org.mule.intents.model.Action;
import org.mule.intents.model.Param;

import java.util.Properties;

/**
 * TODO
 */
public class ActionConfig
{
    private String name;
    private Properties params = new Properties();

    public ActionConfig()
    {
    }

    public ActionConfig(String name, Properties params)
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

    public void checkAndSetParams(Registry reg, StringBuilder errors)
    {
        Action action = lookupAction(reg);
        // Make sure any Action params with default values are added here
        for (ParamConfig config : action.getParams())
        {
            if(!params.containsKey(config.getName()))
            {
                if(config.getValue()!=null)
                {
                    params.setProperty(config.getName(), config.getValue());
                }
            }
        }
        for (Param param : action.getTemplate().getParams())
        {
            String value = params.getProperty(param.getName());
            if(value==null)
            {
                if(param.isOptional()) {
                    value = param.getDefaultValue();
                    if(value!=null) {
                        params.put(param.getName(), value);
                    }
                } else if (param.getDefaultValue()!=null) {
                    params.put(param.getName(), param.getDefaultValue());
                } else {
                    errors.append("Param is missing but is not optional: ").append(param.getName()).append("\n");
                }
            }

            if (!param.isValid(value)) {
                errors.append("Value: ").append(value).append(" for Param: ").append(param.getName()).append(" is not valid.  Type is: ").append(param.getType()).append("\n");
            }
        }
    }

    protected Action lookupAction(Registry reg) {
        return reg.getAction(getName());
    }
}
