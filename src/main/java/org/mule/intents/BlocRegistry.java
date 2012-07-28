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
import org.mule.util.FileUtils;

import java.io.File;
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
    private Map<String, ContentType> contentTypes = new HashMap<String, ContentType>();
    private File rootDirectory;

    public BlocRegistry(File root) throws Exception
    {
        rootDirectory = root;
        addCommonDataTypes();
        ObjectMapper mapper = new ObjectMapper();
        //File root = new File("src/main/resources/blocs");
        Collection<File> files = (Collection<File>)FileUtils.listFiles(root, new String[]{"json"}, true);
        for (File file : files)
        {
            Bloc bloc = mapper.readValue(file, Bloc.class);
            bloc.setDefinitionFile(file);
            addBloc(bloc);
            addDataTypes(bloc);
        }
    }

    public void addBloc(Bloc bloc)
    {
        bloc.validate();
        if(blocs.get(bloc.getName())!=null)
        {
            throw new IllegalArgumentException("Bloc with name already exists: " + bloc.getName());
        }

        blocs.put(bloc.getName(), bloc);
    }
    
    protected void addDataTypes(Bloc bloc)
    {
        for (ContentType dataType : bloc.getDataTypes())
        {
            addDataType(dataType);
        }
    }

    public void addDataType(ContentType dataType)
    {
        contentTypes.put(dataType.getContentType(), dataType);
    }

    public Bloc getBloc(String name)
    {
        return blocs.get(name);
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
        addDataType(new ContentType("application/mule.message",
                "Based content type for org.mule.api.MuleMessage, typically a content hit will be added to the end i.e. 'application/mule-message+email'",
                new URI(MuleMessage.class.getName()), "class"));
    }
}
