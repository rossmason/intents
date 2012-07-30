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
import org.mule.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

/**
 * TODO
 */
public class Bloc implements Validatable
{
    private String name;
    private String description;
    private String intent;
    private List<Param> params = new ArrayList<Param>();
    private List<Content> contents = new ArrayList<Content>();

    @JsonProperty("input-types")
    private List<String> inputTypes = new ArrayList<String>();

    @JsonProperty("output-types")
    private List<String> outputTypes = new ArrayList<String>();

    private File definitionFile;

    private Module module;

    @JsonProperty("snippet-uri")
    private String snippetUri;

    public void validate(StringBuilder errors)
    {
        if (name == null)
        {
            errors.append("A Bloc must have a name. Definition is: ").append(getDefinitionFile()).append("\n");
        }

        if (intent == null)
        {
            errors.append("A Bloc must have an intent. Definition is: ").append(getDefinitionFile()).append("\n");
        }


        if (inputTypes.size() == 0 && !intent.equals("subscribe"))
        {
            errors.append("A Bloc must have at least one input data type. Definition is: ").append(getDefinitionFile()).append("\n");
        }

        if (outputTypes.size() == 0)
        {
            errors.append("A Bloc must have at least one output data type. Definition is: ").append(getDefinitionFile()).append("\n");
        }

        for (Param param : params)
        {
            if(param.getType()==null) {
                errors.append("A Param must have a type defined. Param is: ").append(param.getName()).append(" Definition is: ").append(getDefinitionFile()).append("\n");
            }
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

    public Module getModule()
    {
        return module;
    }

    public void setModule(Module module)
    {
        this.module = module;
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

    public String getSnippetUri()
    {
        return snippetUri;
    }

    public void setSnippetUri(String snippetUri)
    {
        this.snippetUri = snippetUri;
    }

    public Document loadSnippet() throws IOException, DocumentException
    {
        File f = new File(module.getDefinitionFile().getParentFile(), getSnippetUri());
        return DocumentHelper.parseText(IOUtils.toString(new FileInputStream(f)));
    }
}
