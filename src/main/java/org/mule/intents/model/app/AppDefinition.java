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
import org.mule.intents.model.Trigger;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * TODO
 */
public class AppDefinition
{
    private String name;

    @JsonProperty("app-version")
    private String appVersion;

    @JsonProperty("platform-version")
    private String platformVersion;

    private List<ActionConfig> actions = new ArrayList<ActionConfig>();

    private TriggerConfig trigger;

    public AppDefinition()
    {
    }

    public AppDefinition(String name, List<ActionConfig> templates)
    {
        this.name = name;
        this.actions = templates;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<ActionConfig> getActions()
    {
        return actions;
    }

    public void setActions(List<ActionConfig> actions)
    {
        this.actions = actions;
    }

    public String getAppVersion()
    {
        return appVersion;
    }

    public void setAppVersion(String appVersion)
    {
        this.appVersion = appVersion;
    }

    public String getPlatformVersion()
    {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion)
    {
        this.platformVersion = platformVersion;
    }

    public TriggerConfig getTrigger()
    {
        return trigger;
    }

    public void setTrigger(TriggerConfig trigger)
    {
        this.trigger = trigger;
    }

    public void validate(Registry registry, StringBuilder errors)
    {
        List<Action> actionDefs = new ArrayList<Action>();

        if (getAppVersion() == null)
        {

            errors.append("Badly formed app, version is null: ").append(getName());
        }

        if (getPlatformVersion() == null)
        {
            errors.append("Badly formed app, platform version is null: ").append(getName());
        }

        if (trigger == null)
        {
            errors.append("An App definition must start with a Trigger intent");
            return;
        }

        //Process trigger params
        trigger.checkAndSetParams(registry, errors);

        for (ActionConfig actionConfig : getActions())
        {
            Action action = registry.getAction(actionConfig.getName());
            if (action == null)
            {
                throw new IllegalArgumentException("Action not registered: " + actionConfig.getName());
            }

            if (action instanceof Trigger)
            {
                throw new IllegalStateException("An App definition cannot have a Trigger intent anywhere else but the beginning");
            }

            actionDefs.add(action);

            //Validate params and set defaults
            actionConfig.checkAndSetParams(registry, errors);
        }
    }
}
