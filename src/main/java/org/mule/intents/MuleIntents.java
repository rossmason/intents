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

import org.mule.api.MuleContext;
import org.mule.config.ConfigResource;
import org.mule.config.spring.SpringXmlConfigurationBuilder;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * TODO
 */
public class MuleIntents
{
    public static final void main(String[] args)
    {
        FileInputStream fis = null;
        try
        {
            File appFile;
            //appFile = new File(args[0]);
            appFile = new File("src/main/resources/apps/gmailtobox.json");
            BlocRegistry registry = new BlocRegistry(new File("src/main/resources/blocs"));
            if(!appFile.exists()) {
                throw new FileNotFoundException("Cannot find the app definition file at: " + appFile.getAbsolutePath());
            }
            fis = new FileInputStream(appFile);
            AppBuilder builder = new AppBuilder(fis, registry);

            String rawConfig = builder.getRawConfig();
            rawConfig = removeDocNamespace(rawConfig);

            //Embed Mule
            DefaultMuleContextFactory muleContextFactory = new DefaultMuleContextFactory();
            ConfigResource resource = new ConfigResource(builder.getApp().getName(), new ByteArrayInputStream(rawConfig.getBytes()));
            SpringXmlConfigurationBuilder configBuilder = new SpringXmlConfigurationBuilder(new ConfigResource[]{resource});
            MuleContext muleContext = muleContextFactory.createMuleContext(configBuilder);
            muleContext.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
        finally {
            IOUtils.closeQuietly(fis);
        }
    }
    
    protected static String removeDocNamespace(String config) throws IOException
    {
        ByteArrayInputStream bios = new ByteArrayInputStream(config.getBytes());
        Source xmlInput = new StreamSource(bios);
        Source xsl = new StreamSource(new File("src/main/resources/remove-doc-namespace.xsl"));
        StringWriter string = new StringWriter();
        Result xmlOutput = new StreamResult(string);

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer(xsl);
            transformer.transform(xmlInput, xmlOutput);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        System.err.println(string.toString());
        return string.toString();
    }
}
