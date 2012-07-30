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
    private String type;
    private String description;
    private URI schema;
    @JsonProperty("schema-type")
    private String schemaType;

    public ContentType()
    {
    }


    public ContentType(String type, String description)
    {
        setType(type);
        this.description = description;
    }

    public ContentType(String type, String description, URI schema, String schemaType)
    {
        setType(type);
        this.description = description;
        this.schema = schema;
        this.schemaType = schemaType;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
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
        return "ContntType{" +
                "contentType='" + type + '\'' +
                ", description='" + description + '\'' +
                ", schema=" + schema +
                ", schemaType='" + schemaType + '\'' +
                '}';
    }
}
