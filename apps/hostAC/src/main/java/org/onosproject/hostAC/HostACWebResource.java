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
 *
 * Originally created by Pengfei Lu, Network and Cloud Computing Laboratory, Dalian University of Technology, China
 * Advisers: Keqiu Li, Heng Qi and Haisheng Yu
 * This work is supported by the State Key Program of National Natural Science of China(Grant No. 61432002)
 * and Prospective Research Project on Future Networks in Jiangsu Future Networks Innovation Institute.
 */

package org.onosproject.hostAC;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.onlab.packet.IPv4;
import org.onlab.packet.Ip4Prefix;
import org.onlab.packet.MacAddress;
import org.onosproject.rest.AbstractWebResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.onlab.util.Tools.readTreeFromStream;

/**
 * Manage Host Access Control rules or resources.
 */
@Path("rules")
public class HostACWebResource extends AbstractWebResource {

	@GET
	public Response queryHostACRule() {
		String result = "";
		HostACStore hostACStore = new HostACStore();
		// get all rules
		for (int i = 0; i < hostACStore.getSize(); i++) {
			HostACRule hostACRule = hostACStore.getRule(i);

			result += "srcIP: " + hostACRule.getSrcIP() + ", "
				+ "dstIP: " + hostACRule.getDstIP() + ", "
				+ "type: " + hostACRule.getProtocolType();

			if (hostACRule.getDstPort() != 0) {
				result += ", dstPort: " + hostACRule.getDstPort() + " / ";
			} else {
				result += " / ";
			}
		}
		//result += "HACRule Count: " + hostACStore.getSize();
		return Response.ok(result).build();
	}
}
