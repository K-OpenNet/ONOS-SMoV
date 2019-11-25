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

package org.onosproject.ddos12;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.onosproject.cli.AbstractShellCommand;

import org.onlab.packet.IpPrefix;
import org.onosproject.net.PortNumber;
import java.lang.*;
import java.io.*;
import java.util.List;
import java.util.Map;

import org.onosproject.net.Host;
import org.onosproject.net.host.HostService;


/**
 * Application security DDos defense commands.
 */
@Command(scope = "onos", name = "defense",
		description = "DDos defense Interface")
public class ddos12Command extends AbstractShellCommand {

	@Argument(index = 0, name = "type", description = "COM1 / COM2 / COM3",
			required = true, multiValued = false)
	String type = null;
/*
	@Argument(index = 1, name = "srcIP", description = "Source IP Address",
			required = false, multiValued = false)
	String srcIP = null;

	@Argument(index = 2, name = "dstIP", description = "Traget Destination IP Address",
			required = false, multiValued = false)
	String dstIP = null;
	
	@Argument(index = 3, name = "protocolType", description = "Target Protocol Type",
			required = false, multiValued = false)
	String protocolType = null;

	@Argument(index = 4, name = "dstPort", description = "Target Destination Port Number",
			required = false, multiValued = false)
	String dstPort = null;*/
	
	@Override
 

    
	protected void execute() {
		IpPrefix sIP;
		IpPrefix dIP;
		int dPort = 0;
	
		if (!(type.equals("COM1") || type.equals("COM2") || type.equals("COM3"))) {
			print("Type is ADD, REMOVE, or GET");
			return;
		}
   try{
			// call ADD and REMOVE


      
			switch (type) {
				case "COM1":
          h2hintent h2h = new h2hintent();
          h2h.hostprint();
         
        /*
          
          Host srcHost;
          HostService hostservice = new HostService;
          int i;
          i = hostservice.getHostCount();
					System.out.println("COM1: " + i);*/
					break;
				case "COM2":
					System.out.println("COM2");
					break;
        case "COM3":
          System.out.println("COM3");
          break;
				default:
					print("error");
		}
   }catch (Exception e) {
			print("Address must take form \"x.x.x.x/y\"");
		}
	}
}
