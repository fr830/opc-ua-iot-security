package org.dfki.iot.attack.client;

import static org.opcfoundation.ua.utils.EndpointUtil.selectByMessageSecurityMode;
import static org.opcfoundation.ua.utils.EndpointUtil.selectByProtocol;
import static org.opcfoundation.ua.utils.EndpointUtil.selectBySecurityPolicy;
import static org.opcfoundation.ua.utils.EndpointUtil.sortBySecurityLevel;

import org.dfki.iot.attack.model.RoverAModel;
import org.dfki.iot.attack.util.ExampleKeys;
import org.dfki.iot.attack.util.GenericUtil;
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

	private static final Logger myLogger = LoggerFactory.getLogger(RoverAClient.class);

	public static void main(String[] args) throws Exception {

		String applicationName = "RoverA";
		String protocolType = null, ipAddress = null, endpointUrl = null;

		String isLocalEnvironment = GenericUtil.readPropertyConfigFile("isLocalEnvironment");

		if ("true".equals(isLocalEnvironment)) {

			protocolType = GenericUtil.readPropertyConfigFile("protocolType");
			ipAddress = GenericUtil.getCurrentMachineIpAddress();

		} else {

			if (args.length == 0 || args.length == 1) {
				myLogger.error(
						"Usage: RoverAClient [ Please provide the Application protocol type ( opc.tcp / https ) and Ip Address ( 10.24.0.101 ) ]");
				return;
			}

			protocolType = args[0];
			ipAddress = args[1];
		}
		////////////// CLIENT //////////////
		// Load Client's Application Instance Certificate from file
		KeyPair myClientApplicationInstanceCertificate = ExampleKeys.getCert(applicationName);
		// Create Client
		Client myClient = Client.createClientApplication(myClientApplicationInstanceCertificate);
		KeyPair myHttpsCertificate = ExampleKeys.getHttpsCert(applicationName);
		myClient.getApplication().getHttpsSettings().setKeyPair(myHttpsCertificate);
		//////////////////////////////////////

		if (("opc.tcp").equalsIgnoreCase(protocolType)) {

			endpointUrl = protocolType + "://" + ipAddress + ":8666/" + applicationName;
		} else {
			endpointUrl = protocolType + "://" + ipAddress + ":8443/" + applicationName;
		}

		myLogger.info("RoverAClient: Connecting to \"" + endpointUrl + "\" .. ");

		EndpointDescription[] endpoints = myClient.discoverEndpoints(endpointUrl);

		// Filter out all but opc.tcp protocol endpoints
		if (("opc.tcp").equalsIgnoreCase(protocolType)) {
			endpoints = selectByProtocol(endpoints, "opc.tcp");
			// Filter out all but Signed & Encrypted endpoints
			endpoints = selectByMessageSecurityMode(endpoints, MessageSecurityMode.SignAndEncrypt);
			// Filter out all but Basic128 cryption endpoints
			endpoints = selectBySecurityPolicy(endpoints, SecurityPolicy.BASIC128RSA15);
			// Sort endpoints by security level. The lowest level at the
			// beginning, the highest at the end of the array
			endpoints = sortBySecurityLevel(endpoints);
		} else {
			endpoints = selectByProtocol(endpoints, "https");
		}

		// Choose one endpoint
		EndpointDescription endpoint = endpoints[0];

		endpoint.setEndpointUrl(endpointUrl);

		RoverAModel roverAmodel = RoverAModel.getRandomRoverClientA();
		roverAmodel.setRequesterIPAddress(GenericUtil.getCurrentMachineIpAddress());
		roverAmodel.setRequesterMACAddress(GenericUtil.getCurrentMachineMACAddress());

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
