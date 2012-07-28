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

import java.net.URI;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * TODO
 */
public class ContentType
{
    @JsonProperty("type")
    private String contentType;
    private String description;
    private URI schema;
    @JsonProperty("schema-type")
    private String schemaType;

    public ContentType()
    {
    }


    public ContentType(String contentType, String description)
    {
        this.contentType = contentType;
        this.description = description;
    }

    public ContentType(String contentType, String description, URI schema, String schemaType)
    {
        this.contentType = contentType;
        this.description = description;
        this.schema = schema;
        this.schemaType = schemaType;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public URI getSchema()
    {
        return schema;
    }

    public void setSchema(URI schema)
    {
        this.schema = schema;
    }

    public String getSchemaType()
    {
        return schemaType;
    }

    public void setSchemaType(String schemaType)
    {
        this.schemaType = schemaType;
    }

    @Override
    public String toString()
    {
        return "DataType{" +
                "contentType='" + contentType + '\'' +
                ", description='" + description + '\'' +
                ", schema=" + schema +
                ", schemaType='" + schemaType + '\'' +
                '}';
    }
}
