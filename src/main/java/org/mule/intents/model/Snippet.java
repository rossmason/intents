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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.codehaus.jackson.annotate.JsonProperty;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

/**
 * TODO
 */
public class Snippet
{
    private String name;
    private String description;
    private String role;
    private Bloc parent;
    
    @JsonProperty("snippet-uri")
    private String snippetUri;

    @JsonProperty("input-types")
    private List<String> inputTypes = new ArrayList<String>();

    @JsonProperty("output-type")
    private String outputType;

    public Snippet()
    {
    }

    public Snippet(String name, String description, String role, String snippetUri)
    {
        this.name = name;
        this.description = description;
        this.role = role;
        this.snippetUri = snippetUri;
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

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
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
        File f = new File(parent.getRoot(), getSnippetUri());
        return DocumentHelper.parseText(IOUtils.toString(removeDocNamespace(f)));
    }

    //TODO figure out to remove this junk
    protected InputStream removeDocNamespace(File config) throws IOException
    {
        Source xmlInput = new StreamSource(config);
        Source xsl = new StreamSource(new File("src/main/resources/remove-doc-namespace.xsl"));
        StringWriter string = new StringWriter();
        Result xmlOutput = new StreamResult(string);

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer(xsl);
            transformer.transform(xmlInput, xmlOutput);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(string.toString().getBytes());
    }

    public List<String> getInputTypes()
    {
        return inputTypes;
    }

    public void setInputTypes(List<String> inputTypes)
    {
        this.inputTypes = inputTypes;
    }

    public String getOutputType()
    {
        return outputType;
    }

    public void setOutputType(String outputType)
    {
        this.outputType = outputType;
    }

    public Bloc getParent()
    {
        return parent;
    }

    public void setParent(Bloc parent)
    {
        this.parent = parent;
    }
}
