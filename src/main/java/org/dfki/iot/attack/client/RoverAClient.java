package org.dfki.iot.attack.client;

import static org.opcfoundation.ua.utils.EndpointUtil.selectByMessageSecurityMode;
import static org.opcfoundation.ua.utils.EndpointUtil.selectByProtocol;
import static org.opcfoundation.ua.utils.EndpointUtil.selectBySecurityPolicy;
import static org.opcfoundation.ua.utils.EndpointUtil.sortBySecurityLevel;

import java.net.InetAddress;

import org.dfki.iot.attack.model.RoverAModel;
import org.dfki.iot.attack.util.ExampleKeys;
import org.dfki.iot.attack.util.IPMACUtil;
import org.dfki.iot.attack.util.JSONUtil;
import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.TestStackRequest;
import org.opcfoundation.ua.core.TestStackResponse;
import org.opcfoundation.ua.transport.ServiceChannel;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoverAClient {

	// Create Logger
	private static final Logger myLogger = LoggerFactory.getLogger(RoverAClient.class);

	public static void main(String[] args) throws Exception {

		if (args.length == 0) {
			myLogger.error("Usage: RoverAClient [ Please provide the Application Name]");
			return;
		}
		String applicationName = args[0];

		////////////// CLIENT //////////////
		// Load Client's Application Instance Certificate from file
		KeyPair myClientApplicationInstanceCertificate = ExampleKeys.getCert(applicationName);
		// Create Client
		Client myClient = Client.createClientApplication(myClientApplicationInstanceCertificate);
		KeyPair myHttpsCertificate = ExampleKeys.getHttpsCert(applicationName);
		myClient.getApplication().getHttpsSettings().setKeyPair(myHttpsCertificate);
		//////////////////////////////////////

		////////// DISCOVER ENDPOINT /////////
		// Discover server's endpoints, and choose one
		String publicHostname = InetAddress.getLocalHost().getHostName();

		// String url = "opc.tcp://" + publicHostname + ":8666/" +
		// applicationName; // ServerExample1

		String url = "opc.tcp://192.168.0.101:8666/" + applicationName;
		myLogger.info("RoverAClient: Connecting to \"" + url + "\" .. ");

		// "https://"+publicHostname+":8443/UAExample"; // ServerExample1

		EndpointDescription[] endpoints = myClient.discoverEndpoints(url);

		// Filter out all but opc.tcp protocol endpoints
		if (url.startsWith("opc.tcp")) {
			endpoints = selectByProtocol(endpoints, "opc.tcp");
			// Filter out all but Signed & Encrypted endpoints
			endpoints = selectByMessageSecurityMode(endpoints, MessageSecurityMode.SignAndEncrypt);
			// Filter out all but Basic128 cryption endpoints
			endpoints = selectBySecurityPolicy(endpoints, SecurityPolicy.BASIC128RSA15);
			// Sort endpoints by security level. The lowest level at the
			// beginning, the highest at the end of the array
			endpoints = sortBySecurityLevel(endpoints);
		} else
			endpoints = selectByProtocol(endpoints, "https");

		// Choose one endpoint
		EndpointDescription endpoint = endpoints[endpoints.length - 1];

		RoverAModel roverAmodel = RoverAModel.getRandomRoverClientA();
		roverAmodel.setRequesterIPAddress(IPMACUtil.getCurrentMachineIpAddress());
		roverAmodel.setRequesterMACAddress(IPMACUtil.getCurrentMachineMACAddress());

		// Create Channel
		ServiceChannel myChannel = myClient.createServiceChannel(endpoint);

		// Create Test Request
		TestStackRequest req = new TestStackRequest(null, null, null, new Variant(JSONUtil.getJSONString(roverAmodel)));
		myLogger.info("REQUEST: " + req);

		// Invoke service
		TestStackResponse res = myChannel.TestStack(req);
		myLogger.info("RESPONSE: " + res);

		// Close channel
		myChannel.closeAsync();
		System.exit(0);
	}

}
