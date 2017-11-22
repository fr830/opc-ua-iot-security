package org.dfki.iot.attack.server;

import java.net.SocketException;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.dfki.iot.attack.util.ExampleKeys;
import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.application.Server;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.TestStackRequest;
import org.opcfoundation.ua.core.TestStackResponse;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;
import org.opcfoundation.ua.transport.security.CertificateValidator;
import org.opcfoundation.ua.transport.security.HttpsSecurityPolicy;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.utils.EndpointUtil;

public class RoverServerA extends Server {

	public RoverServerA(Application application) throws ServiceResultException, SocketException {
		super(application);
		addServiceHandler(this);

		// Add Client Application Instance Certificate validator - Accept them
		// all (for now)
		application.getOpctcpSettings().setCertificateValidator(CertificateValidator.ALLOW_ALL);
		application.getHttpsSettings().setCertificateValidator(CertificateValidator.ALLOW_ALL);

		// The HTTPS SecurityPolicies are defined separate from the endpoint
		// securities
		application.getHttpsSettings().setHttpsSecurityPolicies(HttpsSecurityPolicy.ALL);

		// Peer verifier
		application.getHttpsSettings().setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		// Load Servers's Application Instance Certificate...
		KeyPair myServerApplicationInstanceCertificate = ExampleKeys.getCert("TEST1");
		application.addApplicationInstanceCertificate(myServerApplicationInstanceCertificate);
		// ...and HTTPS certificate
		KeyPair myHttpsCertificate = ExampleKeys.getHttpsCert("TEST1");
		application.getHttpsSettings().setKeyPair(myHttpsCertificate);

		// Add User Token Policies
		addUserTokenPolicy(UserTokenPolicy.ANONYMOUS);
		addUserTokenPolicy(UserTokenPolicy.SECURE_USERNAME_PASSWORD);

		// Create an endpoint for each network interface
		String hostname = EndpointUtil.getHostname();
		String bindAddress, endpointAddress;
		for (String addr : EndpointUtil.getInetAddressNames()) {
			bindAddress = "https://" + addr + ":8443/UAExample";
			endpointAddress = "https://" + hostname + ":8443/UAExample";
			System.out.println(endpointAddress + " bound at " + bindAddress);
			// The HTTPS ports are using NONE OPC security
			bind(bindAddress, endpointAddress, SecurityMode.NONE);

			bindAddress = "opc.tcp://" + addr + ":8666/UAExample";
			endpointAddress = "opc.tcp://" + hostname + ":8666/UAExample";
			System.out.println(endpointAddress + " bound at " + bindAddress);
			bind(bindAddress, endpointAddress, SecurityMode.ALL);
		}

		//////////////////////////////////////
	}

	public static void main(String[] args) throws Exception {
		////////////// SERVER //////////////
		// Create UA Server Application
		// Create UA Service Server
		Application myServerApplication = new Application();
		RoverServerA myServer = new RoverServerA(myServerApplication);

		// Add a service to the server - TestStack echo
		myServer.addServiceHandler(

				// An example service handler. This handler sends echo responses
				new Object() {
					@SuppressWarnings("unused")
					public void onTestStack(EndpointServiceRequest<TestStackRequest, TestStackResponse> req)
							throws ServiceFaultException {
						// TestStack echo
						req.sendResponse(new TestStackResponse(null, req.getRequest().getInput()));
					}
				}

		);

		// myServer.addServiceHandler(new MyNodeManagementServiceHandler());
		// myServer.addServiceHandler(new MyAttributeServiceHandler());

		//////////////////////////////////////
		// Press enter to shutdown
		System.out.println("Press enter to shutdown");
		System.in.read();
		//////////////////////////////////////

		///////////// SHUTDOWN /////////////
		// Close the server by unbinding all endpoints
		myServer.getApplication().close();
		//////////////////////////////////////

	}

}
