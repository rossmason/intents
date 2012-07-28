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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

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
            if(args.length ==0) {
                throw new IllegalArgumentException("you need to pass in the the application file to load");
            }
            File appFile = new File(args[0]);

            BlocRegistry registry = new BlocRegistry(new File("src/main/resources/blocs"));
            if(!appFile.exists()) {
                throw new FileNotFoundException("Cannot find the app definition file at: " + appFile.getAbsolutePath());
            }
            fis = new FileInputStream(appFile);
            AppBuilder builder = new AppBuilder(fis, registry);

            String rawConfig = builder.getRawConfig();

            //Embed Mule
            DefaultMuleContextFactory muleContextFactory = new DefaultMuleContextFactory();
            ConfigResource resource = new ConfigResource(builder.getApp().getName(), new ByteArrayInputStream(rawConfig.getBytes()));

            SpringXmlConfigurationBuilder configBuilder = new SpringXmlConfigurationBuilder(new ConfigResource[]{resource});
            Properties params = builder.getAllParams();
            //TODO where should this param live
            params.setProperty("http.port", "8081");
            MuleContext muleContext = muleContextFactory.createMuleContext(configBuilder, params);
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

}
