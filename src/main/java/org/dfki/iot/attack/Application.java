package org.dfki.iot.attack;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.dfki.iot.attack.server.RoverAServer;
import org.dfki.iot.attack.util.ExcelUtil;
import org.dfki.iot.attack.util.IPMACUtil;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ResponseHeader;
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
							throws ServiceFaultException, UnknownHostException, SocketException {
						// TestStack echo
						ResponseHeader responseHeader = new ResponseHeader();
						String[] StringTable = new String[2];
						StringTable[0] = IPMACUtil.getCurrentMachineIpAddress();
						StringTable[1] = IPMACUtil.getCurrentMachineMACAddress();
						responseHeader.setStringTable(StringTable);

						ExcelUtil.auditRequest("RoverA", req.getRequest().getInput().getValue().toString());

						req.sendResponse(new TestStackResponse(responseHeader, req.getRequest().getInput()));

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
