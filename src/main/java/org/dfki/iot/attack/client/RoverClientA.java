package org.dfki.iot.attack.client;

import static org.opcfoundation.ua.utils.EndpointUtil.selectByMessageSecurityMode;
import static org.opcfoundation.ua.utils.EndpointUtil.selectByProtocol;
import static org.opcfoundation.ua.utils.EndpointUtil.selectBySecurityPolicy;
import static org.opcfoundation.ua.utils.EndpointUtil.sortBySecurityLevel;

import java.net.InetAddress;

import org.dfki.iot.attack.util.ExampleKeys;
import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.TestStackRequest;
import org.opcfoundation.ua.core.TestStackResponse;
import org.opcfoundation.ua.transport.ServiceChannel;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityPolicy;

public class RoverClientA {


	public static void main(String[] args) throws Exception {
		
		//////////////  CLIENT  //////////////
		// Load Client's Application Instance Certificate from file
		KeyPair myClientApplicationInstanceCertificate = ExampleKeys.getCert("TEST1");
		// Create Client
		Client myClient = Client.createClientApplication( myClientApplicationInstanceCertificate );
		KeyPair myHttpsCertificate = ExampleKeys.getHttpsCert("TEST1"); 
		myClient.getApplication().getHttpsSettings().setKeyPair(myHttpsCertificate);		
		//////////////////////////////////////		
		
		
		////////// DISCOVER ENDPOINT /////////
		// Discover server's endpoints, and choose one
		String publicHostname = InetAddress.getLocalHost().getHostName();	
		//String url = "opc.tcp://"+publicHostname+":8666/UAExample"; // ServerExample1
				//"https://"+publicHostname+":8443/UAExample"; // ServerExample1
				//"opc.tcp://"+publicHostname+":51210/"; // :51210=Sample Server
				//"https://"+publicHostname+":51212/SampleServer/"; // :51212=Sample Server
				//"opc.tcp://"+publicHostname+":62541/"; // :62541=DataAccess Server
		
		String url = "opc.tcp://131.246.177.129:8666/UAExample";
		
		EndpointDescription[] endpoints = myClient.discoverEndpoints(url);
		// Filter out all but opc.tcp protocol endpoints
		if (url.startsWith("opc.tcp")) {
			endpoints = selectByProtocol(endpoints, "opc.tcp");
			// Filter out all but Signed & Encrypted endpoints
			endpoints = selectByMessageSecurityMode(endpoints,
					MessageSecurityMode.SignAndEncrypt);
			// Filter out all but Basic128 cryption endpoints
			endpoints = selectBySecurityPolicy(endpoints,
					SecurityPolicy.BASIC128RSA15);
			// Sort endpoints by security level. The lowest level at the
			// beginning, the highest at the end of the array
			endpoints = sortBySecurityLevel(endpoints);
		} else
			endpoints = selectByProtocol(endpoints, "https");
			
		// Choose one endpoint
		EndpointDescription endpoint = endpoints[endpoints.length-1]; 
		//////////////////////////////////////		
				
		////////////  TEST-STACK  ////////////
		// Create Channel
		ServiceChannel myChannel = myClient.createServiceChannel( endpoint );
		// Create Test Request		
		TestStackRequest req = new TestStackRequest(null, null, null, new Variant("testing 12333333333333333333333333333333333333333333333333333333333333"));
		System.out.println("REQUEST: "+req);		
		// Invoke service
		TestStackResponse res = myChannel.TestStack(req);		
		// Print result
		System.out.println("RESPONSE: "+res);		
		//////////////////////////////////////		
		
		
		/////////////  SHUTDOWN  /////////////
		// Close channel
		myChannel.closeAsync();
		//////////////////////////////////////		
		System.exit(0);
	}
	


}
