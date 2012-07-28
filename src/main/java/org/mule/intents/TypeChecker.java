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

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.routing.filters.WildcardFilter;

import javax.activation.MimeType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TODO
 */
public class TypeChecker implements Callable, Initialisable
{
    /**
     * logger used by this class
     */
    protected static final Log logger = LogFactory.getLog(TypeChecker.class);

    private String types;
    private String bloc;
    private WildcardFilter filter;


    public String getTypes()
    {
        return types;
    }

    public void setTypes(String types)
    {
        this.types = types;
    }

    public String getBloc()
    {
        return bloc;
    }

    public void setBloc(String bloc)
    {
        this.bloc = bloc;
    }

    public void initialise() throws InitialisationException
    {
        filter = new WildcardFilter(getTypes());
    }

    public Object onCall(MuleEventContext eventContext) throws Exception
    {
        //Check outbound first since this would override the inbound Content-type in the flow
        String contentType = (String) eventContext.getMessage().getOutboundProperty(("Content-Type"));
        if (contentType == null)
        {
            contentType = (String) eventContext.getMessage().getInboundProperty(("Content-Type"));
            if (contentType == null)
            {
                logger.error("current message doesn't have a 'Content-Type' header set");
                logger.error(eventContext.getMessage().toString());
                return eventContext;
            }
        }

        MimeType mimeType = new MimeType(contentType);
        if (!filter.accept(mimeType.getPrimaryType() + "/" + mimeType.getSubType()))
        {
            throw new IllegalArgumentException("The current message content type: " + contentType + " is not compatible with return data-type for bloc: " + getBloc() + ". This Bloc output data type should be: " + getTypes());
        }

        return eventContext.getMessage();
    }
}
