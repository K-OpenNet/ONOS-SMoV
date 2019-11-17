/*
 * Copyright 2018-present Open Networking Foundation
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

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.onosproject.net.packet.PacketProcessor;
import org.onosproject.net.packet.PacketService;
import org.onosproject.net.packet.PacketContext;
import org.onosproject.net.packet.InboundPacket;
import org.onlab.packet.Ethernet;
import org.onlab.packet.ICMP;
import org.onlab.packet.ICMP6;
import org.onlab.packet.IPv4;
import org.onlab.packet.IPv6;
import org.onlab.packet.Ip4Prefix;
import org.onlab.packet.Ip6Prefix;
import org.onlab.packet.MacAddress;
import org.onlab.packet.TCP;
import org.onlab.packet.TpPort;
import org.onlab.packet.UDP;
import org.onosproject.net.flow.DefaultTrafficSelector;
import org.onosproject.net.flow.DefaultTrafficTreatment;
import org.onosproject.net.flow.TrafficSelector;
import org.onosproject.net.flow.TrafficTreatment;
import org.onosproject.net.flow.FlowEntry;
import org.onosproject.net.flow.FlowRule;
import org.onosproject.net.flow.DefaultFlowRule;
import org.onosproject.net.flow.FlowRuleService;
import org.onosproject.net.flow.criteria.Criterion;
import org.onosproject.net.flow.criteria.EthCriterion;
import org.onosproject.net.flow.instructions.Instruction;
import org.onosproject.net.flow.instructions.Instructions;
import org.onosproject.net.DeviceId;
import org.onosproject.net.PortNumber;
import org.onosproject.core.CoreService;
import java.lang.*;
import java.io.*;
import java.util.*;

@Component(immediate = true)
@Service(value = HostAccessControl.class)
public class HostAccessControl {

    private final Logger log = LoggerFactory.getLogger(getClass());

	// nessary services
	@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
	protected PacketService packetService;

	@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
	protected CoreService coreService;

	@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
	protected FlowRuleService flowRuleService;

	private HACPacketProcessor processor = new HACPacketProcessor();

    @Activate
    protected void activate() {
		packetService.addProcessor(processor, PacketProcessor.director(2));
		coreService.registerApplication("org.onosproject.hostAC");

        log.info("Started");
    }

    @Deactivate
    protected void deactivate() {
        log.info("Stopped");
    }

	private class HACPacketProcessor implements PacketProcessor {

		@Override
		public void process(PacketContext context) {

			// check packets
			InboundPacket pkt = context.inPacket();
			Ethernet ethPkt = pkt.parsed();
			TrafficSelector.Builder selectorBuilder = DefaultTrafficSelector.builder();

			if (ethPkt == null) {
				return;
			}

			if (ethPkt.getEtherType() == Ethernet.TYPE_ARP) {
				return;
			}

			if (ethPkt.getEtherType() == Ethernet.TYPE_IPV4) {
				IPv4 ipv4Packet = (IPv4) ethPkt.getPayload();
				byte ipv4Protocol = ipv4Packet.getProtocol();
				Ip4Prefix matchIp4SrcPrefix = 
					Ip4Prefix.valueOf(ipv4Packet.getSourceAddress(),
							Ip4Prefix.MAX_MASK_LENGTH);
				Ip4Prefix matchIp4DstPrefix =
					Ip4Prefix.valueOf(ipv4Packet.getDestinationAddress(),
							Ip4Prefix.MAX_MASK_LENGTH);

				int portNumber = 0;
				String protocolType = "";

				if (matchIp4SrcPrefix.toString().equals("0.0.0.0/32")) {
					return;
				}
				
				if (matchIp4DstPrefix.toString().equals("255.255.255.255/32")) {
					return;
				}

				selectorBuilder.matchEthType(Ethernet.TYPE_IPV4)
					.matchIPSrc(matchIp4SrcPrefix)
					.matchIPDst(matchIp4DstPrefix);

				// check proto type
				if (ipv4Protocol == IPv4.PROTOCOL_TCP) {
					TCP tcpPacket = (TCP) ipv4Packet.getPayload();
					selectorBuilder.matchIPProtocol(ipv4Protocol)
						//.matchTcpSrc(TpPort.tpPort(tcpPacket.getSourcePort()))
						.matchTcpDst(TpPort.tpPort(tcpPacket.getDestinationPort()));
					portNumber = tcpPacket.getDestinationPort();
					protocolType = "TCP";
				} else if (ipv4Protocol == IPv4.PROTOCOL_UDP) {
					UDP udpPacket = (UDP) ipv4Packet.getPayload();
					selectorBuilder.matchIPProtocol(ipv4Protocol)
						//.matchUdpSrc(TpPort.tpPort(udpPacket.getSourcePort()))
						.matchUdpDst(TpPort.tpPort(udpPacket.getDestinationPort()));
					portNumber = udpPacket.getDestinationPort();
					protocolType = "UDP";
				} else if (ipv4Protocol == IPv4.PROTOCOL_ICMP) {
					ICMP icmpPacket = (ICMP) ipv4Packet.getPayload();
					selectorBuilder.matchIPProtocol(ipv4Protocol);
					protocolType = "ICMP";
						//.matchIcmpType(icmpPacket.getIcmpType())
						//.matchIcmpCode(icmpPacket.getIcmpCode());
				} else {
					return;
				}
				
				TrafficTreatment treatment = DefaultTrafficTreatment.builder().build();

				DeviceId deviceId = context.inPacket().receivedFrom().deviceId();

				// install AC rules
				FlowRule flowRule = DefaultFlowRule.builder()
					.forDevice(deviceId)
					.withCookie(30000)
					.makeTemporary(30)
					.withPriority(30000)
					.withSelector(selectorBuilder.build())
					.withTreatment(treatment)
					.build();

				boolean ruleApply = true;
				HostACStore hostACStore = new HostACStore();
				for (int i = 0; i < hostACStore.getSize(); i++) {
					HostACRule tempRule = hostACStore.getRule(i);
					if (!matchIp4SrcPrefix.toString().equals(tempRule.getSrcIP().toString())) {
						continue;
					}

					if (!matchIp4DstPrefix.toString().equals(tempRule.getDstIP().toString())) {
						continue;
					}

					if (!protocolType.equals(tempRule.getProtocolType())) {
						continue;
					}

					if (portNumber != 0) {
						if (portNumber != tempRule.getDstPort()) {
							continue;
						}
					}

					ruleApply = false;
					break;
				}

				if (ruleApply) {
					flowRuleService.applyFlowRules(flowRule);
				}

				return;
			}
		}
	}
}
