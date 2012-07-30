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

import org.mule.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

/**
 * TODO
 */
public class Module implements Validatable
{
    private String name;
    private String namespace;
    @JsonProperty("shared-config-snippet-uri")
    private URI configUri;
    private List<Bloc> blocs = new ArrayList<Bloc>();
    @JsonProperty("shared-contents")
    private List<Content> contents = new ArrayList<Content>();
    @JsonProperty("shared-params")
    private List<Param> params = new ArrayList<Param>();

    @JsonProperty("content-types")
    private List<ContentType> contentTypes = new ArrayList<ContentType>();

    private File definitionFile;


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    public List<Bloc> getBlocs()
    {
        return blocs;
    }

    public void setBlocs(List<Bloc> blocs)
    {
        this.blocs = blocs;
    }

    public URI getConfigUri()
    {
        return configUri;
    }

    public void setConfigUri(URI configUri)
    {
        this.configUri = configUri;
    }

    public List<Content> getContents()
    {
        return contents;
    }

    public void setContents(List<Content> contents)
    {
        this.contents = contents;
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

    public List<ContentType> getContentTypes()
    {
        return contentTypes;
    }

    public void setContentTypes(List<ContentType> contentTypes)
    {
        this.contentTypes = contentTypes;
    }

    public void validate(StringBuilder errors)
    {
        if (name == null)
        {
            errors.append("A Module must have a name. Definition is: ").append(getDefinitionFile()).append("\n");
        }

        if (namespace == null)
        {
            errors.append("A Module must have an namespace. Definition is: ").append(getDefinitionFile()).append("\n");
        }

        if (blocs.size() == 0)
        {
            errors.append("A Module must have at least one Bloc. Definition is: ").append(getDefinitionFile()).append("\n");
        }

    }

    public Document loadConfig() throws IOException, DocumentException
    {
        if(getConfigUri()==null) return null;
        File f = new File(getDefinitionFile().getParentFile(), getConfigUri().toString());
        return DocumentHelper.parseText(IOUtils.toString(new FileInputStream(f)));
    }
}
