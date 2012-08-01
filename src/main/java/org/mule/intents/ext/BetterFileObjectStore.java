/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.intents.ext;

import org.mule.api.store.ObjectStoreException;
import org.mule.util.store.PartitionedPersistentObjectStore;

import java.io.File;
import java.io.Serializable;

/**
 * TODO
 */
public class BetterFileObjectStore extends PartitionedPersistentObjectStore
{
    @Override
    public Serializable retrieve(Serializable key, String partitionName) throws ObjectStoreException
    {
        File file = createStoreFile(key, partitionName);
        if(file.exists()) {
        return deserialize(file);
        }
        else
        {
            return "";
        }
    }
}
