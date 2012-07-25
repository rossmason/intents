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

import org.mule.intents.model.AppDefinition;
import org.mule.intents.model.Bloc;
import org.mule.intents.model.BlocConfig;
import org.mule.intents.model.Snippet;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * TODO
 */
public class AppBuilder
{
    public static final Namespace docNS = new Namespace("doc", "http://www.mulesoft.org/schema/mule/documentation");
    public static final Namespace muleNS = new Namespace("", "http://www.mulesoft.org/schema/mule/core");
    public static final Namespace xsiNS = new Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

    public static final QName xsiQN = new QName("schemaLocation", xsiNS);

    private Document config;
    private String rawConfig;
    private BlocRegistry registry;
    private AppDefinition app;
    private Element flow;
    private Map<String,String> schemaLocations = new HashMap<String,String>();

    public AppBuilder(InputStream appDef, BlocRegistry registry) throws IOException, DocumentException
    {
        this.registry = registry;
        ObjectMapper mapper = new ObjectMapper();
        //This is a safe operation meaning that it is validated against the registry when it
        //is created, no need to perform any extra checks
        app = mapper.readValue(appDef, AppDefinition.class);
        //this could be better isolated, but its a spike
        app.validate(registry);
        List<Bloc> blocs = new ArrayList<Bloc>();

        //Lookup Bloc elements for this app
        for (BlocConfig blocConfig : app.getBlocs())
        {
            blocs.add(registry.getBloc(blocConfig.getName()));
        }

        //Create the Mule config with the mule root element and NS
        config = createMuleConfig();

        for (Bloc bloc : blocs)
        {
            for (Snippet snippet : bloc.getSnippets())
            {
                if (bloc.getIntent().equals("subscribe") )
                {
                    addTrigger(snippet);
                }
                else
                {
                    addAction(snippet);
                }
            }
        }

        rawConfig = convertConfigToString();
        System.out.println(rawConfig);
        System.out.println(getAllParams().toString());
    }

    public Document getConfig()
    {
        return config;
    }

    public String getRawConfig()
    {
        return rawConfig;
    }

    public AppDefinition getApp()
    {
        return app;
    }

    public void addTrigger(Snippet intent) throws IOException, DocumentException
    {
        Document snippet = intent.loadSnippet();
        addNamespaces(snippet);
        for (Object o : snippet.getRootElement().content())
        {
            if (o instanceof Element)
            {
                Element e = (Element) ((Element) o).detach();
                config.getRootElement().add(e);
                if (e.attribute("name").getValue().equals("main"))
                {
                    flow = e;
                }
            }
        }
    }

    public void addAction(Snippet intent) throws IOException, DocumentException
    {
        Document snippet = intent.loadSnippet();
        addNamespaces(snippet);
        addSchemaLocations(snippet);
        for (Object o : snippet.getRootElement().content())
        {
            if (o instanceof Element)
            {
                Element e = (Element) ((Element) o).detach();
                config.getRootElement().add(e);
                if (e.getQName().getName().equals("sub-flow"))
                {
                    Element element = flow.addElement("flow-ref");
                    element.addAttribute("name", e.attribute("name").getValue());
                    element.addAttribute(new QName("name", docNS), intent.getParent().getName());
                }
            }
        }
    }

    public Properties getAllParams() throws IOException
    {
        Properties params = new Properties();
        for (BlocConfig blocConfig : app.getBlocs())
        {
            params.putAll(blocConfig.getParams());
        }

        return params;
    }

    protected void addNamespaces(Document snippet)
    {
        List<Namespace> ns = snippet.getRootElement().declaredNamespaces();
        for (Namespace n : ns)
        {
            if(!n.getURI().equals("http://www.mulesoft.org/schema/mule/snippet") &&
                !n.getURI().equals("http://www.mulesoft.org/schema/mule/documentation")) {
                config.getRootElement().add(n.detach());
            }
        }
    }


    
    protected void addSchemaLocations(Document snippet)
    {
        Attribute att = snippet.getRootElement().attribute(xsiQN);
        if(att==null) return;
        
        Attribute existing = config.getRootElement().attribute(xsiQN);
        if(existing==null) {
            config.getRootElement().addAttribute(xsiQN, "");
            existing = config.getRootElement().attribute(xsiQN);
        }
        
        //save schemaLocations
        for (StringTokenizer tokenizer = new StringTokenizer(att.getValue(), " "); tokenizer.hasMoreTokens(); )
        {
            String x = tokenizer.nextToken();
            String y = tokenizer.nextToken();
            //Skip the snippet namespace
            if(!x.equals("http://www.mulesoft.org/schema/mule/snippet") ||
               !x.equals("http://www.mulesoft.org/schema/mule/documentation")){
                schemaLocations.put(x,y);
            }
        }

        StringBuilder buf = new StringBuilder();
        for (String s : schemaLocations.keySet())
        {
            buf.append(s).append(" ").append(schemaLocations.get(s)).append("\n");
        }
                
        existing.setValue(buf.toString());

        System.out.println("xsi:schemaLocation = " + existing.getValue());
    }

    protected Document createMuleConfig() {
        DocumentFactory factory = DocumentFactory.getInstance();
        Element root = factory.createElement(new QName("mule", muleNS));
        root.addAttribute("version", app.getPlatformVersion());
        return DocumentFactory.getInstance().createDocument(root);
    }

    protected String convertConfigToString() throws IOException
    {
        OutputFormat format = OutputFormat.createPrettyPrint();
        StringWriter string = new StringWriter();
        XMLWriter writer = new XMLWriter(string, format);
        writer.write(getConfig());
        return string.toString();
    }
}