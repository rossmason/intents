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

import org.mule.api.MuleMessage;
import org.mule.intents.model.Action;
import org.mule.intents.model.ContentType;
import org.mule.intents.model.Module;
import org.mule.intents.model.Template;
import org.mule.intents.model.Trigger;
import org.mule.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * TODO
 */
public class Registry
{
    private Map<String, Template> templates = new HashMap<String, Template>();
    private Map<String, Module> modules = new HashMap<String, Module>();
    private Map<String, Trigger> triggers = new HashMap<String, Trigger>();
    private Map<String, Action> actions = new HashMap<String, Action>();
    private Map<String, ContentType> contentTypes = new HashMap<String, ContentType>();
    private File rootDirectory;
    private StringBuilder errors = new StringBuilder();

    public Registry(File root) throws Exception
    {
        rootDirectory = root;
        addCommonDataTypes();
        ObjectMapper mapper = new ObjectMapper();
        //File root = new File("src/main/resources/templates");
        Collection<File> files = (Collection<File>)FileUtils.listFiles(root, new String[]{"module.json"}, true);
        for (File file : files)
        {
            Module module = null;
            try
            {
                module = mapper.readValue(file, Module.class);
                module.setDefinitionFile(file);
                addModule(module);
                addContentTypes(module);
            }
            catch (IOException e)
            {
                errors.append("failed to parse Module: ").append(file).append(". Error is: ").append(e.getMessage()).append("\n");
            }
        }
        if(errors.length() > 0)
        {
            throw new IllegalArgumentException("There were errors parsing the template registry: \n" + errors.toString());
        }
    }

    public void addTemplate(Template template)
    {
        template.validate(errors);
        if(templates.get(template.getName())!=null)
        {
            throw new IllegalArgumentException("Template with name already exists: " + template.getName());
        }

        templates.put(template.getName(), template);
        for (Trigger trigger : template.getTriggers())
        {
            triggers.put(trigger.getQualifiedName(), trigger);
        }
        for (Action action : template.getActions())
        {
            actions.put(action.getQualifiedName(), action);
        }
    }

    public void addModule(Module module)
    {
        module.validate(errors);
        if(modules.get(module.getName())!=null)
        {
            throw new IllegalArgumentException("Module with name already exists: " + module.getName());
        }

        modules.put(module.getName(), module);

        for (Template template : module.getTemplates())
        {
            template.setModule(module);
            addTemplate(template);
        }
    }
    
    protected void addContentTypes(Module module)
    {
        for (ContentType dataType : module.getContentTypes())
        {
            addContentType(dataType);
        }
    }

    public void addContentType(ContentType contentType)
    {
        contentTypes.put(contentType.getType(), contentType);
    }

    public Template getTemplate(String name)
    {
        return templates.get(name);
    }

    public Module getModule(String name)
    {
        return modules.get(name);
    }

    public List<Template> getTemplatesThatSupportTypes(List<String> types)
    {
        List<Template> supportedTemplates = new ArrayList<Template>();
        for (Template template : templates.values())
        {
            for (String type : types)
            {
                if(template.supportsInputType(type))
                {
                    supportedTemplates.add(template);
                }
            }
        }
        return supportedTemplates;
    }

    public File getRootDirectory()
    {
        return rootDirectory;
    }

    public Map<String, ContentType> getContentTypes()
    {
        return contentTypes;
    }
    
    protected void addCommonDataTypes() throws URISyntaxException
    {
        addContentType(new ContentType("application/vnd.mule.message",
                "Based content type for org.mule.api.MuleMessage, typically a content hit will be added to the end i.e. 'application/mule-message+email'",
                new URI(MuleMessage.class.getName()), "class"));
    }

    public Trigger getTrigger(String name)
    {
        return triggers.get(name);
    }

    public Action getAction(String name)
    {
        return actions.get(name);
    }
}
