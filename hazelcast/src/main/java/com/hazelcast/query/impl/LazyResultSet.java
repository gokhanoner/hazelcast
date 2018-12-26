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

/**
 * Lazy ResultSet for query results. Store result suppliers.
 * Implementation must ensure that contains & iterator methods calls init method & collect supplier results.
 * Other collections contains LazyResultSet must call contains or iterator methods to initialize the lazy collection
 */
public interface LazyResultSet {

    /**
     * Iterate through result suppliers and populate the result collection.
     */
    void init();

    /**
     *
     * @return estimated size on the lazy collection without initializing any data copy
     */
    int estimatedSize();

}
