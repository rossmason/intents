/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.intents;

import org.mule.api.MuleMessage;
import org.mule.intents.model.Bloc;
import org.mule.intents.model.ContentType;
import org.mule.intents.model.Module;
import org.mule.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * TODO
 */
public class BlocRegistry
{
    private Map<String, Bloc> blocs = new HashMap<String, Bloc>();
    private Map<String, Module> modules = new HashMap<String, Module>();
    private Map<String, ContentType> contentTypes = new HashMap<String, ContentType>();
    private File rootDirectory;
    private StringBuilder errors = new StringBuilder();

    public BlocRegistry(File root) throws Exception
    {
        rootDirectory = root;
        addCommonDataTypes();
        ObjectMapper mapper = new ObjectMapper();
        //File root = new File("src/main/resources/blocs");
        Collection<File> files = (Collection<File>)FileUtils.listFiles(root, new String[]{"module.json"}, true);
        for (File file : files)
        {
            Module module = null;
            try
            {
                module = mapper.readValue(file, Module.class);
                module.setDefinitionFile(file);
                addModule(module);
                addContentTypes(module);
            }
            catch (IOException e)
            {
                errors.append("failed to parse Module: ").append(file).append(". Error is: ").append(e.getMessage()).append("\n");
            }
        }
        if(errors.length() > 0)
        {
            throw new IllegalArgumentException("There were errors parsing the bloc registry: \n" + errors.toString());
        }
    }

    public void addBloc(Bloc bloc)
    {
        bloc.validate(errors);
        if(blocs.get(bloc.getName())!=null)
        {
            throw new IllegalArgumentException("Bloc with name already exists: " + bloc.getName());
        }

        blocs.put(bloc.getName(), bloc);
    }

    public void addModule(Module module)
    {
        module.validate(errors);
        if(modules.get(module.getName())!=null)
        {
            throw new IllegalArgumentException("Module with name already exists: " + module.getName());
        }

        modules.put(module.getName(), module);

        for (Bloc bloc : module.getBlocs())
        {
            bloc.setModule(module);
            addBloc(bloc);
        }
    }
    
    protected void addContentTypes(Module module)
    {
        for (ContentType dataType : module.getContentTypes())
        {
            addContentType(dataType);
        }
    }

    public void addContentType(ContentType contentType)
    {
        contentTypes.put(contentType.getType(), contentType);
    }

    public Bloc getBloc(String name)
    {
        return blocs.get(name);
    }

    public Module getModule(String name)
    {
        return modules.get(name);
    }

    public List<Bloc> getBlocsThatSupportTypes(List<String> types)
    {
        List<Bloc> supportedBlocs = new ArrayList<Bloc>();
        for (Bloc bloc : blocs.values())
        {
            for (String type : types)
            {
                if(bloc.supportsInputType(type))
                {
                    supportedBlocs.add(bloc);
                }
            }
        }
        return supportedBlocs;
    }

    public File getRootDirectory()
    {
        return rootDirectory;
    }

    public Map<String, ContentType> getContentTypes()
    {
        return contentTypes;
    }
    
    protected void addCommonDataTypes() throws URISyntaxException
    {
        addContentType(new ContentType("application/mule.message",
                "Based content type for org.mule.api.MuleMessage, typically a content hit will be added to the end i.e. 'application/mule-message+email'",
                new URI(MuleMessage.class.getName()), "class"));
    }
}
