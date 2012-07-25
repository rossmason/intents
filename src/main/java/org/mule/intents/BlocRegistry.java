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

import org.mule.intents.model.Bloc;
import org.mule.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * TODO
 */
public class BlocRegistry
{
    private Map<String, Bloc> blocs = new HashMap<String, Bloc>();
    private File rootDirectory;

    public BlocRegistry(File root) throws IOException
    {
        rootDirectory = root;
        ObjectMapper mapper = new ObjectMapper();
        //File root = new File("src/main/resources/blocs");
        Collection<File> files = (Collection<File>)FileUtils.listFiles(root, new String[]{"json"}, true);
        for (File file : files)
        {
            Bloc bloc = mapper.readValue(file, Bloc.class);
            bloc.setRoot(file.getParentFile());
            addBloc(bloc);
        }
    }

    public void addBloc(Bloc bloc)
    {
        if(blocs.get(bloc.getName())!=null)
        {
            throw new IllegalArgumentException("Bloc with name already exists: " + bloc.getName());
        }
        blocs.put(bloc.getName(), bloc);
    }

    public Bloc getBloc(String name)
    {
        return blocs.get(name);
    }

    public File getRootDirectory()
    {
        return rootDirectory;
    }
}
