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

import java.io.File;
import java.util.List;

/**
 * TODO
 */
public class Bloc
{
    private String name;
    private String description;
    private String intent;
    private List<Param> params;
    private List<Snippet> snippets;
    private File root;

    public String getName()
    {
        return name;
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

    public String getIntent()
    {
        return intent;
    }

    public void setIntent(String intent)
    {
        this.intent = intent;
    }

    public List<Param> getParams()
    {
        return params;
    }

    public void setParams(List<Param> params)
    {
        this.params = params;
    }

    public List<Snippet> getSnippets()
    {
        return snippets;
    }

    public void setSnippets(List<Snippet> snippets)
    {
        this.snippets = snippets;
        for (Snippet snippet : snippets)
        {
            snippet.setParent(this);
        }
    }

    public File getRoot()
    {
        return root;
    }

    public void setRoot(File root)
    {
        this.root = root;
    }
}
