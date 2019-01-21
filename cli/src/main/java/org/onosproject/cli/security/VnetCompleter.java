/*
 * Copyright 2015-present Open Networking Foundation
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

package org.onosproject.cli.security;

import org.apache.karaf.shell.console.completer.StringsCompleter;
import org.onosproject.app.ApplicationService;
import org.onosproject.cli.AbstractCompleter;
import org.onosproject.core.Application;
import org.onosproject.incubator.net.virtual.TenantId;
import org.onosproject.incubator.net.virtual.VirtualNetwork;
import org.onosproject.incubator.net.virtual.VirtualNetworkAdminService;
import org.onosproject.incubator.net.virtual.VirtualNetworkService;
import org.onosproject.security.SecurityAdminService;
import org.onosproject.security.SecurityUtil;
import org.onosproject.utils.*;

import java.util.*;

import static org.onosproject.cli.AbstractShellCommand.get;

/**
 * Application name completer for security review command.
 */
public class VnetCompleter extends AbstractCompleter {
    @Override
    public int complete(String buffer, int cursor, List<String> candidates) {
        VirtualNetworkService service = get(VirtualNetworkService.class);
        VirtualNetworkAdminService adminService = get(VirtualNetworkAdminService.class);

        List<VirtualNetwork> virtualNetworks = new ArrayList<>();

        Set<TenantId> tenantSet = adminService.getTenantIds();
        tenantSet.forEach(tenantId -> virtualNetworks.addAll(service.getVirtualNetworks(tenantId)));

        Collections.sort(virtualNetworks, Comparators.VIRTUAL_NETWORK_COMPARATOR);

        StringsCompleter delegate = new StringsCompleter();

        SortedSet<String> strings = delegate.getStrings();

	// Add VNET_ID
        while (virtualNetworks.iterator().hasNext()) {
            VirtualNetwork vnet = virtualNetworks.iterator().next();
            strings.add(vnet.id().toString());
        }

        // Now let the completer do the work for figuring out what to offer.
        return delegate.complete(buffer, cursor, candidates);
    }
}
