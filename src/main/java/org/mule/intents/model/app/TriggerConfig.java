/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.intents.model.app;

import org.mule.intents.Registry;
import org.mule.intents.model.Action;

/**
 * TODO
 */
public class TriggerConfig extends ActionConfig
{
    protected Action lookupAction(Registry reg) {
        return reg.getTrigger(getName());
    }
}
