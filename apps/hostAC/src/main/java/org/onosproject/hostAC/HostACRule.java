package org.onosproject.hostAC;

import org.onlab.packet.IpPrefix;
import org.onosproject.net.PortNumber;
import java.lang.*;
import java.io.*;

public class HostACRule {
	private IpPrefix srcIP;
	private IpPrefix dstIP;
	private String protocolType;
	private int dstPort;

	public HostACRule(IpPrefix srcIP, IpPrefix dstIP, String protocolType) {
		this.srcIP = srcIP;
		this.dstIP = dstIP;
		this.protocolType = protocolType;
	}

	public HostACRule(IpPrefix srcIP, IpPrefix dstIP, String protocolType, int dstPort) {
		this.srcIP = srcIP;
		this.dstIP = dstIP;
		this.protocolType = protocolType;
		this.dstPort = dstPort;
	}

	public HostACRule(String srcIP, String dstIP, String protocolType) {
		this.srcIP = IpPrefix.valueOf(srcIP);
		this.dstIP = IpPrefix.valueOf(dstIP);
		this.protocolType = protocolType;
	}

	public HostACRule(String srcIP, String dstIP, String protocolType, int dstPort) {
		this.srcIP = IpPrefix.valueOf(srcIP);
		this.dstIP = IpPrefix.valueOf(dstIP);
		this.protocolType = protocolType;
		this.dstPort = dstPort;
	}

	// set source IP
	public void setSrcIP(IpPrefix srcIP) {
		this.srcIP = srcIP;
	}
	
	// get source IP
	public IpPrefix getSrcIP() {
		return this.srcIP;
	}

	// set destination IP
	public void setDstIP(IpPrefix dstIP) {
		this.dstIP = dstIP;
	}
	
	// get destination IP
	public IpPrefix getDstIP() {
		return this.dstIP;
	}

	// set protocol type
	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	// get protocol type
	public String getProtocolType() {
		return this.protocolType;
	}

	// set destination port
	public void setDstPort(int dstPort) {
		this.dstPort = dstPort;
	}
	
	// get destination port
	public int getDstPort() {
		return this.dstPort;
	}
}
