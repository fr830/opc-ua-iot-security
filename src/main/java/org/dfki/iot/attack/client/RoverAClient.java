package org.dfki.iot.attack.client;

import static org.opcfoundation.ua.utils.EndpointUtil.selectByMessageSecurityMode;
import static org.opcfoundation.ua.utils.EndpointUtil.selectByProtocol;
import static org.opcfoundation.ua.utils.EndpointUtil.selectBySecurityPolicy;
import static org.opcfoundation.ua.utils.EndpointUtil.sortBySecurityLevel;

import javax.swing.plaf.basic.BasicDesktopIconUI;

import org.dfki.iot.attack.model.RoverAModel;
import org.dfki.iot.attack.util.EventLogUtil;
import org.dfki.iot.attack.util.ExampleKeys;
import org.dfki.iot.attack.util.ExcelUtil;
import org.dfki.iot.attack.util.GenericUtil;
import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.application.SessionChannel;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.ActivateSessionResponse;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddNodesRequest;
import org.opcfoundation.ua.core.AddNodesResponse;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseRequest;
import org.opcfoundation.ua.core.CallMethodRequest;
import org.opcfoundation.ua.core.CallRequest;
import org.opcfoundation.ua.core.CallResponse;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.RequestHeader;
import org.opcfoundation.ua.core.SetMonitoringModeRequest;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.UserNameIdentityToken;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoverAClient {

	private static final Logger myLogger = LoggerFactory.getLogger(RoverAClient.class);
	private static final RequestHeader RequestHeader = null;

	public static void main(String[] args) throws Exception {

		// String applicationName = "/RoverA";
		String applicationName = "TimeSyncServer";
		String protocolType = null, ipAddress = null, endpointUrl = null;

		String isLocalEnvironment = GenericUtil.readClientPropertyConfigFile("isLocalEnvironment");

		if ("true".equals(isLocalEnvironment)) {

			protocolType = GenericUtil.readClientPropertyConfigFile("protocolType");
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
		//protocolType=""
		////////////// CLIENT //////////////
		// Load Client's Application Instance Certificate from file
		KeyPair myClientApplicationInstanceCertificate = ExampleKeys.getCert(applicationName);
		// Create Client
		Client myClient = Client.createClientApplication(myClientApplicationInstanceCertificate);
		KeyPair myHttpsCertificate = ExampleKeys.getHttpsCert(applicationName);
		myClient.getApplication().getHttpsSettings().setKeyPair(myHttpsCertificate);
		//////////////////////////////////////
		//192.168.0.105:8443
		if (("opc.tcp").equalsIgnoreCase(protocolType)) {

			endpointUrl = protocolType + "://" + "131.246.169.225" + ":8666/" + applicationName;
		} else {
			endpointUrl = protocolType + "://" + "131.246.169.225" + ":8443/" + applicationName;
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
     
		//myClient.set
		// Choose one endpoint
		EndpointDescription endpoint = endpoints[0];

		endpoint.setEndpointUrl(endpointUrl);

	//	roverAmodel.setRequesterIPAddress(GenericUtil.getCurrentMachineIpAddress());
	//	roverAmodel.setRequesterMACAddress(GenericUtil.getCurrentMachineMACAddress());
		
		// Create Channel
		SessionChannel mySession = myClient.createSessionChannel(endpoint);
		EventLogUtil.writeToClientEventLog("ClientId1",
				mySession.getSession().getAuthenticationToken().toString());
//mySession.getSession().getDiagnosticsInfo().get
		/*SetMonitoringModeRequest reqAudit=new SetMonitoringModeRequest();
		org.opcfoundation.ua.core.RequestHeader requestHead=new RequestHeader();
		String AuditEntryId="Client1234!@##";
		requestHead.setAuditEntryId(AuditEntryId);
		reqAudit.setRequestHeader(requestHead);
		mySession.SetMonitoringMode(reqAudit);*/
	//	mySession.activate("user", "password");
	//	 mySession.activate("username", "password");
		
		mySession.activate();
		AddNodesRequest req =new AddNodesRequest();
		org.opcfoundation.ua.core.RequestHeader requsetHeader =new RequestHeader();
		requsetHeader.setAuditEntryId(""+mySession.getSession().getAuthenticationToken()+DateTime.currentTime()); 
		req.setRequestHeader(requsetHeader );
		UnsignedInteger unsignedInteger=new UnsignedInteger(567);
		NodeId nodeId =new NodeId(0, "second Node");
		AddNodesItem addNodesItem=new AddNodesItem();
		QualifiedName BrowseName =new QualifiedName("NamespaceArray");
		addNodesItem.setBrowseName(BrowseName );
		ExpandedNodeId ParentNodeId =new ExpandedNodeId(new UnsignedInteger(1), "http://opcfoundation.org/UA/", unsignedInteger);
		addNodesItem.setParentNodeId(ParentNodeId );
		ExpandedNodeId typeId =new ExpandedNodeId("http://opcfoundation.org/UA/",unsignedInteger);
		addNodesItem.setRequestedNewNodeId(typeId);
		addNodesItem.setTypeDefinition(typeId);
		addNodesItem.setNodeClass(NodeClass.Object);
		addNodesItem.setReferenceTypeId(nodeId);
		ExtensionObject NodeAttributes =new ExtensionObject(typeId);
		addNodesItem.setNodeAttributes(NodeAttributes );
		req.setNodesToAdd(new AddNodesItem []{addNodesItem});
		AddNodesResponse addNodes = mySession.AddNodes(req);
		myLogger.info("RESPONSE: ADD NODE " + addNodes);
		/*CallMethodRequest[] MethodsToCall =null;
		Variant[] InputArguments = null;
		MethodsToCall[0].setInputArguments(InputArguments );;
		
		CallRequest req = new CallRequest(RequestHeader, MethodsToCall );
		CallResponse call = mySession.Call(req);*/
	
		
		
		//Method Node Call
	/*	CallRequest callRequest = new CallRequest();
		CallMethodRequest methodRequest = new CallMethodRequest();
		CallMethodRequest methodRequest2 = new CallMethodRequest();
		
		Variant variant1 = new Variant(new Integer []{1,2,3});
		Variant variant2 = new Variant(new Integer []{4,5,6});
		methodRequest2.setInputArguments(new Variant[]{variant1,variant2});
		callRequest.setMethodsToCall( new CallMethodRequest[] {methodRequest,methodRequest2} );
		NodeId MethodId = new NodeId(2, "ListSolvers");
		methodRequest.setMethodId(MethodId );
		methodRequest2.setMethodId(MethodId);
		CallResponse res = mySession.Call( callRequest );
		System.out.println( res );*/
		/*BrowseRequest brreq =new BrowseRequest();
		
		brreq.setNodesToBrowse(new BrowseDescription[]{new BrowseDescription(123, , , IncludeSubtypes, NodeClassMask, ResultMask)});
		mySession.Browse(brreq );*/
		
		//Read Node Call
		
		NodeId nodeSearch= new NodeId(0, 567);
		RequestHeader readHeader=new RequestHeader();
		
		readHeader.setAuditEntryId(""+mySession.getSession().getAuthenticationToken()+DateTime.currentTime());
		ReadResponse res5 = mySession.Read(readHeader, null, TimestampsToReturn.Neither,
				new ReadValueId(nodeSearch, Attributes.Value, null, null));

		myLogger.info("RESPONSE: " + res5);
		

		// Close channel
		mySession.closeAsync();
		System.exit(0);

		/***
		 * ServiceChannel myChannel = myClient.createServiceChannel(endpoint);
		 * 
		 * // Create Test Request TestStackRequest req = new
		 * TestStackRequest(null, null, null, new
		 * Variant(JSONUtil.getJSONString(roverAmodel)));
		 * myLogger.info("REQUEST: " + req);
		 * 
		 * // Invoke service TestStackResponse res = myChannel.TestStack(req);
		 * myLogger.info("RESPONSE: " + res);
		 ***/

	}

}
