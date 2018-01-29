/*
 * Copyright 2017-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.store.primitives.impl;

import org.onosproject.store.primitives.DistributedPrimitiveCreator;
import org.onosproject.store.service.AsyncAtomicIdGenerator;
import org.onosproject.store.service.AtomicIdGeneratorBuilder;

/**
 * Default implementation of AtomicIdGeneratorBuilder.
 */
public class DefaultAtomicIdGeneratorBuilder extends AtomicIdGeneratorBuilder {

    private final DistributedPrimitiveCreator primitiveCreator;

    public DefaultAtomicIdGeneratorBuilder(DistributedPrimitiveCreator primitiveCreator) {
        this.primitiveCreator = primitiveCreator;
    }

    @Override
    public AsyncAtomicIdGenerator build() {
        return primitiveCreator.newAsyncIdGenerator(name());
    }
}
