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

import org.mule.routing.filters.WildcardFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * TODO
 */
public class Bloc
{
    private String name;
    private String description;
    private String intent;
    private List<Param> params = new ArrayList<Param>();
    private List<Snippet> snippets = new ArrayList<Snippet>();
    private List<Content> contents = new ArrayList<Content>();

    @JsonProperty("data-types")
    private List<ContentType> dataTypes = new ArrayList<ContentType>();

    @JsonProperty("input-types")
    private List<String> inputTypes = new ArrayList<String>();

    @JsonProperty("output-types")
    private List<String> outputTypes = new ArrayList<String>();

    private File definitionFile;

    public void validate()
    {
        StringBuilder errors = new StringBuilder();
        if (name == null)
        {
            errors.append("A Bloc must have a name. Definition is: ").append(getDefinitionFile()).append("\n");
        }

        if (intent == null)
        {
            errors.append("A Bloc must have an intent. Definition is: ").append(getDefinitionFile()).append("\n");
        }

        if (snippets.size() == 0)
        {
            errors.append("A Bloc must have a snippet. Definition is: ").append(getDefinitionFile()).append("\n");
        }

        if (inputTypes.size() == 0 && !intent.equals("subscribe"))
        {
            errors.append("A Bloc must have at least one input data type. Definition is: ").append(getDefinitionFile()).append("\n");
        }

        if (outputTypes.size() == 0)
        {
            errors.append("A Bloc must have at least one output data type. Definition is: ").append(getDefinitionFile()).append("\n");
        }

        boolean hasMain = false;
        for (Snippet snippet : snippets)
        {
            if (snippet.getRole()!=null && snippet.getRole().equalsIgnoreCase("main"))
            {
                if (!hasMain)
                {
                    hasMain = true;
                }
                else
                {
                    errors.append("There can only be one snippet with the 'main' role. Definition is: ").append(getDefinitionFile()).append("\n");
                }
            }
        }

        if (!hasMain)
        {
            errors.append("A Bloc must have one snippet with the 'main' role. Definition is: ").append(getDefinitionFile()).append("\n");
        }

        if(errors.length()>0) {
            throw new IllegalArgumentException("There were issues with the following bloc definitions: \n" + errors.toString());
        }
    }

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

    public File getDefinitionFile()
    {
        return definitionFile;
    }

    public void setDefinitionFile(File definitionFile)
    {
        this.definitionFile = definitionFile;
    }

    public List<Content> getContents()
    {
        return contents;
    }

    public void setContents(List<Content> contents)
    {
        this.contents = contents;
    }

    public List<ContentType> getDataTypes()
    {
        return dataTypes;
    }

    public void setDataTypes(List<ContentType> dataTypes)
    {
        this.dataTypes = dataTypes;
    }

    public List<String> getInputTypes()
    {
        return inputTypes;
    }

    public void setInputTypes(List<String> inputTypes)
    {
        this.inputTypes = inputTypes;
    }

    public List<String> getOutputTypes()
    {
        return outputTypes;
    }

    public void setOutputTypes(List<String> outputTypes)
    {
        this.outputTypes = outputTypes;
    }

    public boolean supportsInputTypes(List<String> types)
    {
        for (String type : types)
        {
            if (supportsInputType(type))
            {
                return true;
            }
        }
        return false;
    }

    public boolean supportsOutputTypes(List<String> types)
    {
        for (String type : types)
        {
            if (supportsOutputType(type))
            {
                return true;
            }
        }
        return false;
    }

    public boolean supportsInputType(String type)
    {
        for (String inputType : inputTypes)
        {
            WildcardFilter filter = new WildcardFilter(inputType);
            if (filter.accept(type))
            {
                return true;
            }
        }
        return false;
    }

    public boolean supportsOutputType(String type)
    {
        for (String outputType : outputTypes)
        {
            WildcardFilter filter = new WildcardFilter(outputType);
            if (filter.accept(type))
            {
                return true;
            }
        }
        return false;
    }
}
