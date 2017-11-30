package org.dfki.iot.attack;

import java.io.IOException;

import org.dfki.iot.attack.server.RoverAServer;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.TestStackRequest;
import org.opcfoundation.ua.core.TestStackResponse;
import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

	// Create Logger
	private static final Logger myLogger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws ServiceResultException, IOException {

		// Create UA Server Application
		// Create UA Service Server
		org.opcfoundation.ua.application.Application myServerApplication = new org.opcfoundation.ua.application.Application();
		RoverAServer myServer = new RoverAServer(myServerApplication, "RoverA");

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

		myLogger.info("Press enter to shutdown");
		System.in.read();

		// Close the server by unbinding all endpoints
		myServer.getApplication().close();

	}

}
