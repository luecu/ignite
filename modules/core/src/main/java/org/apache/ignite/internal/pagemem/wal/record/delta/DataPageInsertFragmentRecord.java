/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.pagemem.wal.record.delta;

import java.nio.ByteBuffer;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.internal.processors.cache.database.tree.io.DataPageIO;

/**
 * Insert fragment to data page record.
 */
public class DataPageInsertFragmentRecord extends PageDeltaRecord {
    /** Link to the last entry fragment. */
    private final long lastLink;

    /** Actual fragment data. */
    private final byte[] payload;

    /**
     * @param cacheId Cache ID.
     * @param pageId Page ID.
     * @param payload Fragment payload.
     * @param lastLink Link to the last entry fragment.
     */
    public DataPageInsertFragmentRecord(
        final int cacheId,
        final long pageId,
        final byte[] payload,
        final long lastLink
    ) {
        super(cacheId, pageId);

        this.lastLink = lastLink;
        this.payload = payload;
    }

    /** {@inheritDoc} */
    @Override public void applyDelta(ByteBuffer buf) throws IgniteCheckedException {
        DataPageIO io = DataPageIO.VERSIONS.forPage(buf);

        io.addRowFragment(buf, payload, lastLink);
    }

    /** {@inheritDoc} */
    @Override public RecordType type() {
        return RecordType.DATA_PAGE_INSERT_FRAGMENT_RECORD;
    }

    /**
     * @return Fragment payload size.
     */
    public int payloadSize() {
        return payload.length;
    }

    /**
     * @return Fragment payload.
     */
    public byte[] payload() {
        return payload;
    }

    /**
     * @return Link to the last entry fragment.
     */
    public long lastLink() {
        return lastLink;
    }
}
