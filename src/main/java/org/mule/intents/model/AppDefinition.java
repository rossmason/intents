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

import org.mule.intents.BlocRegistry;

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

    private List<BlocConfig> blocs = new ArrayList<BlocConfig>();

    public AppDefinition()
    {
    }

    public AppDefinition(String name, List<BlocConfig> blocs)
    {
        this.name = name;
        this.blocs = blocs;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<BlocConfig> getBlocs()
    {
        return blocs;
    }

    public void setBlocs(List<BlocConfig> blocs)
    {
        this.blocs = blocs;
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

    public void validate(BlocRegistry registry)
    {
        List<Bloc> blocDefs = new ArrayList<Bloc>();
        boolean first = true;

        if (getAppVersion()==null)
        {
            throw new IllegalArgumentException("Badly formed app, version is null: " + getName());
        }

        if (getPlatformVersion()==null)
        {
            throw new IllegalArgumentException("Badly formed app, platform version is null: " + getName());
        }

        for (BlocConfig blocConfig : getBlocs())
        {
            Bloc bloc = registry.getBloc(blocConfig.getName());
            if (bloc == null)
            {
                throw new IllegalArgumentException("Bloc not registered: " + blocConfig.getName());
            }

            if (bloc.getIntent()==null)
            {
                throw new IllegalArgumentException("Badly formed Bloc, intent is null: " + blocConfig.getName());
            }
            
            if(first) {
                first=false;
                if(!bloc.getIntent().equals("subscribe")) {
                    throw new IllegalStateException("An App definition must start with a Trigger intent");
                }
            } else {
                if(bloc.getIntent().equals("subscribe")) {
                    throw new IllegalStateException("An App definition cannot have a Trigger intent anywhere else but the beginning");
                }
            }
                
            blocDefs.add(bloc);

            for (Param param : bloc.getParams())
            {
                //Validate no optional params are missing
                if(!param.isOptional() && !blocConfig.getParams().containsKey(param.getName()))
                {
                    throw new IllegalArgumentException("Missing parameter for App: " + param.getName() + " : " + param.getDescription() + ". Bloc is: " + bloc.getName());
                } else if(param.getDefaultValue()!=null && !blocConfig.getParams().containsKey(param.getName()))
                {
                    //Add any missing params that have default values
                    blocConfig.getParams().setProperty(param.getName(), param.getDefaultValue());
                }
            }
        }
    }
}
