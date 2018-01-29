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
package org.onosproject.incubator.net.l2monitoring.soam.loss;

import java.time.Instant;

import org.onosproject.incubator.net.l2monitoring.soam.SoamId;

/**
 * Object for representing Loss Measurement Stats History.
 * Extends {@link org.onosproject.incubator.net.l2monitoring.soam.loss.LossMeasurementStat}
 */
public interface LossMeasurementStatHistory extends LossMeasurementStat {
    /**
     * The identifier of the historic measurement.
     * @return The id
     */
    SoamId historyStatsId();

    /**
     * The time that the historic Measurement Interval ended.
     * @return A java Instant
     */
    Instant endTime();

    /**
     * Builder for {@link org.onosproject.incubator.net.l2monitoring.soam.loss.LossMeasurementStatHistory}.
     */
    public interface LmStatHistoryBuilder extends LmStatBuilder {
        LossMeasurementStatHistory build();
    }
}
