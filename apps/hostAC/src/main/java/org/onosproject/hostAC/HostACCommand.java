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

package org.onosproject.hostAC;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.onosproject.cli.AbstractShellCommand;

import org.onlab.packet.IpPrefix;
import org.onosproject.net.PortNumber;
import java.lang.*;
import java.io.*;
import java.util.List;
import java.util.Map;


/**
 * Application security Host Access Control commands.
 */
@Command(scope = "onos", name = "hostac",
		description = "Host Access Control Interface")
public class HostACCommand extends AbstractShellCommand {

	@Argument(index = 0, name = "type", description = "ADD / REMOVE / GET",
			required = true, multiValued = false)
	String type = null;

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
	String dstPort = null;
	
	@Override
	protected void execute() {
		IpPrefix sIP;
		IpPrefix dIP;
		int dPort = 0;
	
		if (!(type.equals("ADD") || type.equals("REMOVE") || type.equals("GET"))) {
			print("Type is ADD, REMOVE, or GET");
			return;
		}

		if (type.equals("GET")) {
			HostACStore hostACStore = new HostACStore();
			String result = hostACStore.getRule();
			print(result);
			print("Rule Count: " + hostACStore.getSize());
			return;
		}

		if (srcIP == null) {
			print("argument srcIP is required");
			return;
		}

		if (dstIP == null) {
			print("argument dstIP is required");
			return;
		}

		if (protocolType == null) {
			print("argument protocolType is required");
			return;
		}

		if (dstPort != null) {
			if (Integer.parseInt(dstPort) < 0) {
				print("Port Number must be greater than 0");
				return;
			} else if (Integer.parseInt(dstPort) > 65535) {
				print("Port Number must be less than 65536");
				return;
			} else {
				dPort = Integer.parseInt(dstPort);
			}
		}

		if (!(protocolType.equals("TCP") || protocolType.equals("UDP") || protocolType.equals("ICMP"))) {
			print("Protocol Type is TCP, UDP or ICMP");
			return;
		}

		try {
			sIP = IpPrefix.valueOf(srcIP);
			dIP = IpPrefix.valueOf(dstIP);

			// check dstPort
			HostACRule hostACRule;
			if (dstPort == null) {
				hostACRule = new HostACRule(sIP, dIP, protocolType);
			} else {
				hostACRule = new HostACRule(sIP, dIP, protocolType, dPort);
			}

			// call ADD and REMOVE
			HostACStore hostACStore = new HostACStore();
			switch (type) {
				case "ADD":
					hostACStore.addRule(hostACRule);
					break;
				case "REMOVE":
					hostACStore.removeRule(hostACRule);
					break;
				default:
					print("error");
			}
			print("Rule Count: " + hostACStore.getSize());
		} catch (Exception e) {
			print("Address must take form \"x.x.x.x/y\"");
		}
	}
}
