package org.onosproject.hostAC;

import org.onosproject.net.PortNumber;
import org.onlab.packet.IpPrefix;
import java.util.*;

public class HostACStore {

	private static List<HostACRule> listRule = new ArrayList<HostACRule>();

	public HostACStore() {
	
	}

	public static void addRule(HostACRule hostACRule) {
		listRule.add(hostACRule);
	}

	public static void removeRule(HostACRule hostACRule) {
		for (int i = 0; i < listRule.size(); i++) {
			HostACRule tempRule = listRule.get(i);

			if (!tempRule.getSrcIP().toString().equals(hostACRule.getSrcIP().toString())) {
				continue;
			}

			if (!tempRule.getDstIP().toString().equals(hostACRule.getDstIP().toString())) {
				continue;
			}

			if (!tempRule.getProtocolType().toString().equals(hostACRule.getProtocolType().toString())) {
				continue;
			}

			if (tempRule.getDstPort() != hostACRule.getDstPort()) {
				continue;
			}

			listRule.remove(i);
			break;
		}
	}

	public static HostACRule getRule(int number) {
		if (listRule.size() <= number) {
			return null;
		} 
		return listRule.get(number);
	}

	public static int getSize() {
		return listRule.size();
	}

	// get rules
	public static String getRule() {
		String result = "";
		HostACStore hostACStore = new HostACStore();
		for (int i = 0; i < listRule.size(); i++) {
			HostACRule hostACRule = hostACStore.getRule(i);
			
			result += i + 1 + ". " 
				+ "srcIP: " + hostACRule.getSrcIP() + ", "
				+ "dstIP: " + hostACRule.getDstIP() + ", "
				+ "protocolType: " + hostACRule.getProtocolType();
			
			if (hostACRule.getDstPort() != 0) {
				result += ", dstPort: " + hostACRule.getDstPort() + "\n";
			} else {
				result += "\n";
			}
		}
		return result;
	}
}
