/*
 * Copyright 2015-present Open Networking Laboratory
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

import com.google.common.collect.Lists;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.onosproject.app.ApplicationAdminService;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.core.Application;
import org.onosproject.core.ApplicationId;
import org.onosproject.incubator.net.virtual.*;
import org.onosproject.security.AppPermission;
import org.onosproject.security.SecurityAdminService;
import org.onosproject.security.SecurityUtil;

import java.security.Permission;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Dynamic interface for graint vnet policy
 */
@Command(scope = "onos", name = "grantVnetPerm",
        description = "Dynamic interface for granting vnet policy to application")
public class GrantVnetPermCommand extends AbstractShellCommand {

    @Argument(index = 0, name = "name", description = "Application name",
            required = true, multiValued = false)
    String name = null;

    @Argument(index = 1, name = "action", description = "Action",
            required = true, multiValued = false)
    String action = null;

    @Argument(index = 2, name = "vnet", description = "Virtual Network ID",
            required = true, multiValued = false)
    String id = null;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    @Override
    protected void execute() {
        ApplicationAdminService applicationAdminService = get(ApplicationAdminService.class);
        ApplicationId appId = applicationAdminService.getId(name);
        if (appId == null) {
            print("No such application: %s", name);
            return;
        }
        Application app = applicationAdminService.getApplication(appId);
        SecurityAdminService smService = SecurityUtil.getSecurityService();
        if (smService == null) {
            print("Security Mode is disabled");
            return;
        }

        VirtualNetworkAdminService service = get(VirtualNetworkAdminService.class);

        if (id == null) {
            print("Please specify virtual network ID");
            return;
        } else {
            if(smService.isSecured(appId) == true) {
                VirtualNetworkService vnetService =get(VirtualNetworkService.class);

                VirtualNetwork vnet = null;

                vnet = vnetService.getVirtualNetwork(NetworkId.networkId(Long.valueOf(id).longValue()));
                if (vnet == null) {
                    print("Specified virtual network id is nonexistent");
                    return;
                }
                else {
                    Set<org.onosproject.security.Permission> permissionSet = new HashSet<org.onosproject.security.Permission>();
                    if(action.equals("READ")) {
                        permissionSet.add(new org.onosproject.security.Permission(AppPermission.class.getName(), "VNET_READ" + "_" + id));
                    }
                    else if(action.equals("WRITE")) {
                        permissionSet.add(new org.onosproject.security.Permission(AppPermission.class.getName(), "VNET_WRITE" + "_" + id));
                    }
                    else if(action.equals("EVENT")) {
                        permissionSet.add(new org.onosproject.security.Permission(AppPermission.class.getName(), "VNET_EVENT" + "_" + id));
                    }
                    else {
                        print("Unknown action");
                        return;
                    }

                    smService.updatePolicy(app.id(), permissionSet);
                    print("\nPermissions granted: ");

                    printMap(smService.getPrintableGrantedPermissions(app.id()));
                }
            }
            else {
                print("Not secured application: %s", name);
                return;
            }
        }
    }


    /**
     * TYPES.
     * 0 - APP_PERM
     * 1 - ADMIN SERVICE
     * 2 - NB_SERVICE
     * 3 - SB_SERVICE
     * 4 - CLI_SERVICE
     * 5 - ETC_SERVICE
     * 6 - CRITICAL PERMISSIONS
     * 7 - ETC
     **/
    private void printMap(Map<Integer, List<Permission>> assortedMap) {

        for (Permission perm: assortedMap.get(0)) { // APP PERM
            if (perm.getName().contains("WRITE")) {
                printYellow("\t[APP PERMISSION] " + perm.getName());
            } else {
                printGreen("\t[APP PERMISSION] " + perm.getName());
            }
        }

        for (Permission perm: assortedMap.get(4)) {
            printGreen("\t[CLI SERVICE] " + perm.getName() + "(" + perm.getActions() + ")");
        }

        for (Permission perm: assortedMap.get(5)) {
            printYellow("\t[Other SERVICE] " + perm.getName() + "(" + perm.getActions() + ")");
        }

        for (Permission perm: assortedMap.get(7)) {
            printYellow("\t[Other] " + perm.getClass().getSimpleName() +
                    " " + perm.getName() + " (" + perm.getActions() + ")");
        }

        for (Permission perm: assortedMap.get(1)) { // ADMIN SERVICES
            printRed("\t[NB-ADMIN SERVICE] " + perm.getName() + "(" + perm.getActions() + ")");
        }

        for (Permission perm: assortedMap.get(3)) { // ADMIN SERVICES
            printRed("\t[SB SERVICE] " + perm.getName() + "(" + perm.getActions() + ")");
        }

        for (Permission perm: assortedMap.get(6)) { // CRITICAL SERVICES
            printRed("\t[CRITICAL PERMISSION] " + perm.getClass().getSimpleName() +
                    " " + perm.getName() + " (" + perm.getActions() + ")");
        }
    }

    private void printRed(String format, Object... args) {
        print(ANSI_RED + String.format(format, args) + ANSI_RESET);
    }

    private void printYellow(String format, Object... args) {
        print(ANSI_YELLOW + String.format(format, args) + ANSI_RESET);
    }

    private void printGreen(String format, Object... args) {
        print(ANSI_GREEN + String.format(format, args) + ANSI_RESET);
    }
}

