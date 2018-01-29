/*
 * Copyright 2016-present Open Networking Foundation
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
package org.onosproject.net.resource;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import org.onlab.packet.VlanId;
import org.onlab.util.Identifier;
import org.onosproject.net.DeviceId;
import org.onosproject.net.PortNumber;
import org.onosproject.net.intent.IntentId;

public class ResourceAllocationTest {

    private static final DeviceId D1 = DeviceId.deviceId("of:001");
    private static final DeviceId D2 = DeviceId.deviceId("of:002");
    private static final PortNumber P1 = PortNumber.portNumber(1);
    private static final PortNumber P2 = PortNumber.portNumber(2);
    private static final VlanId VLAN1 = VlanId.vlanId((short) 100);
    private static final VlanId VLAN2 = VlanId.vlanId((short) 200);
    private static final TestResourceConsumer RC2 = new TestResourceConsumer(2L);

    // ResourceConsumerId generated by specifying ID and class name
    private static final ResourceConsumerId RCID1 = ResourceConsumerId.of(30L, IntentId.class);

    // ResourceConsumerId generated from Identifier<Long> class
    private static final ResourceConsumerId RCID2 = ResourceConsumerId.of(RC2);

    @Test
    public void testEquals() {
        ResourceAllocation alloc1 = new ResourceAllocation(Resources.discrete(D1, P1, VLAN1).resource(), RCID1);
        ResourceAllocation sameAsAlloc1 = new ResourceAllocation(Resources.discrete(D1, P1, VLAN1).resource(), RCID1);
        ResourceAllocation alloc2 = new ResourceAllocation(Resources.discrete(D2, P2, VLAN2).resource(), RCID2);
        ResourceAllocation sameAsAlloc2 = new ResourceAllocation(Resources.discrete(D2, P2, VLAN2).resource(), RCID2);

        new EqualsTester()
                .addEqualityGroup(alloc1, sameAsAlloc1)
                .addEqualityGroup(alloc2, sameAsAlloc2)
                .testEquals();
    }

    private static class TestResourceConsumer extends Identifier<Long> implements ResourceConsumer {
        public TestResourceConsumer(long idValue) {
            super(idValue);
        }

        @Override
        public ResourceConsumerId consumerId() {
            return ResourceConsumerId.of(this);
        }
    }
}
