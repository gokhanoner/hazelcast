/*
 * Copyright (c) 2008-2018, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.query.impl;

import com.hazelcast.nio.serialization.Data;
import com.hazelcast.query.impl.collections.ReadOnlyMapDelegate;
import com.hazelcast.util.function.Supplier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LazyDuplicateDetectingMultiResult extends LazyMultiResultSet<QueryableEntry> {

    private final List<Supplier<Map<Data, QueryableEntry>>> resultSuppliers
            = new ArrayList<Supplier<Map<Data, QueryableEntry>>>();
    private int estimatedSize;

    @Override
    public void addResultSetSupplier(Supplier<Map<Data, QueryableEntry>> resultSupplier, int resultSize) {
        resultSuppliers.add(resultSupplier);
        estimatedSize += resultSize;
    }

    @Nonnull
    @Override
    protected Set<QueryableEntry> initialize() {
        if (resultSuppliers.isEmpty()) {
            return Collections.emptySet();
        }
        //Since duplicate detection required, we're paying copy cost again
        //TODO : check what can be done to prevent second copy cost
        Map<Data, QueryableEntry> results = new HashMap<Data, QueryableEntry>();
        for (Supplier<Map<Data, QueryableEntry>> resultSupplier : resultSuppliers) {
            Map<Data, QueryableEntry> result = resultSupplier.get();
            results.putAll(result);
        }
        return new ReadOnlyMapDelegate(results);
    }

    @Override
    public int estimatedSize() {
        return estimatedSize;
    }
}