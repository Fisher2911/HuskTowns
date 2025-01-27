/*
 * This file is part of HuskTowns, licensed under the Apache License 2.0.
 *
 *  Copyright (c) William278 <will27528@gmail.com>
 *  Copyright (c) contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.william278.husktowns.cache;

import net.william278.husktowns.HuskTowns;

/**
 * Legacy enumeration of cache status, identifying the current status of a cache
 *
 * @deprecated see {@link HuskTowns#isLoaded()} instead
 */
@Deprecated(since = "2.0")
public enum CacheStatus {
    /**
     * The cache has not been loaded yet
     */
    UNINITIALIZED,
    /**
     * The cache is in the process of being loaded from the database
     */
    UPDATING,
    /**
     * The cache is ready and has been loaded from the database
     */
    LOADED,
    /**
     * The cache initialization has failed and reached an error state
     */
    ERROR
}
