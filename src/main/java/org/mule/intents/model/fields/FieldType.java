/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.intents.model.fields;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public abstract class FieldType
{
    protected String type;
    protected static List<String> types = new ArrayList<String>();

    //TODO make this a type registry
    static {
        types.add("text");
        types.add("boolean");
        types.add("password");
        types.add("options");
        types.add("phonenumber");
        types.add("email");
    }
    public FieldType(String type)
    {
        this.type = type;
        parseType(type);
    }

    protected abstract void parseType(String type);

    public abstract boolean isValid(String value);


    public static boolean isValidType(String type) {
        return types.contains(type);
    }

    public static FieldType get(String type) {

        String temp = type.toLowerCase();
        int x = type.indexOf("(") ;
        if(x > -1){
            temp = type.substring(0,x);
        }
        if(!isValidType(temp)) {
            throw new IllegalArgumentException("Invalid type: " + type + ". Possible values are: " + types);
        }
        if(temp.equals("email"))
        {
            return new Email(type);
        } else if(temp.equals("options"))
        {
            return new Options(type);
        }else if(temp.equals("phoneNumber"))
        {
            return new PhoneNumber(type);
        } else {
            return new Text(type);
        }
    }
}
