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

import org.mule.intents.model.app.ParamConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class Action
{
    private String name;
    private String description;
    private List<ParamConfig> params = new ArrayList<ParamConfig>();
    private Template template;

    public String getName()
    {
        return name;
    }

    public String getQualifiedName()
    {
        return template.getModule().getNamespace() + ":" + name;
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

    public List<ParamConfig> getParams()
    {
        return params;
    }

    public void setParams(List<ParamConfig> params)
    {
        this.params = params;
    }

    public Template getTemplate()
    {
        return template;
    }

    void setTemplate(Template template)
    {
        this.template = template;
    }
}
