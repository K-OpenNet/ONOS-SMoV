/*

 * Copyright 2014 Open Networking Laboratory

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

package org.onosproject.ddos12;



import com.google.common.collect.Lists;

import org.apache.felix.scr.annotations.Activate;

import org.apache.felix.scr.annotations.Component;

import org.apache.felix.scr.annotations.Deactivate;

import org.apache.felix.scr.annotations.Reference;

import org.apache.felix.scr.annotations.ReferenceCardinality;

import org.onosproject.core.ApplicationId;

import org.onosproject.core.CoreService;

import org.onosproject.net.Host;

import org.onosproject.net.host.HostEvent;

import org.onosproject.net.host.HostListener;

import org.onosproject.net.host.HostService;

import org.onosproject.net.intent.HostToHostIntent;

import org.onosproject.net.intent.IntentService;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;



import java.util.List;



/**

 * Skeletal ONOS application component.

 */

@Component(immediate = true)

public class h2hintent {



    private final Logger log = LoggerFactory.getLogger(getClass());

    

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)

    protected IntentService intentService;



    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)

    protected HostService hostService;



    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)

    private CoreService coreService;



    private InternalHostListener hostListener = new InternalHostListener();

    

    private List<Host> hosts = Lists.newArrayList();

    private ApplicationId appId;

    

    @Activate

    protected void activate() {

        appId = coreService.registerApplication("org.onosproject.ddos12");

        hostService.addListener(hostListener);

        log.info("Started11111111111111111111111111111111111111111111111111111111111111111111111112222222222222222222222222222222222222222222222222222222222222222222222223333333333333333333333333333333333333333333333333333333333333333333333333333444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444");

    }



    @Deactivate

    protected void deactivate() {

        hostService.removeListener(hostListener);

        log.info("Stopped");

    }
    public void dothis(){
        System.out.println("yes started");
     }
    public void hostprint(){
        System.out.println(hosts);
    }

    private class InternalHostListener implements HostListener {

        @Override

        public void event(HostEvent hostEvent) {

            switch (hostEvent.type()) {



                case HOST_ADDED:

                    addConnectivity(hostEvent.subject());

                    hosts.add(hostEvent.subject());

                    break;

                case HOST_REMOVED:

                    break;

                case HOST_UPDATED:

                    break;

                case HOST_MOVED:

                    break;

            }

        }

    }



    private void addConnectivity(Host host) {

        for (Host dst : hosts) {

            HostToHostIntent intent = HostToHostIntent.builder().appId(appId).one(host.id()).two(dst.id()).build();

            intentService.submit(intent);

        }

    }

}