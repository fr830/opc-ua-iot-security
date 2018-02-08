package org.dfki.iot.attack.client;

import static org.opcfoundation.ua.utils.EndpointUtil.selectByMessageSecurityMode;
import static org.opcfoundation.ua.utils.EndpointUtil.selectByProtocol;
import static org.opcfoundation.ua.utils.EndpointUtil.selectBySecurityPolicy;
import static org.opcfoundation.ua.utils.EndpointUtil.sortBySecurityLevel;

import org.dfki.iot.attack.util.EventLogUtil;
import org.dfki.iot.attack.util.ExampleKeys;
import org.dfki.iot.attack.util.GenericUtil;
import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.application.SessionChannel;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.RequestHeader;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.WriteRequest;
import org.opcfoundation.ua.core.WriteResponse;
import org.opcfoundation.ua.core.WriteValue;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoverAClient1 {

	private static final Logger myLogger = LoggerFactory.getLogger(RoverAClient1.class);
	private static final RequestHeader RequestHeader = null;

	public static void main(String[] args) throws Exception {

		// String applicationName = "/RoverA";131.246.169.225
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
		////////////// CLIENT //////////////
		// Load Client's Application Instance Certificate from file
		KeyPair myClientApplicationInstanceCertificate = ExampleKeys.getCert(applicationName);
		// Create Client
		Client myClient = Client.createClientApplication(myClientApplicationInstanceCertificate);
		KeyPair myHttpsCertificate = ExampleKeys.getHttpsCert(applicationName);
		myClient.getApplication().getHttpsSettings().setKeyPair(myHttpsCertificate);
		//////////////////////////////////////

		ipAddress ="85.214.53.244"; 
		
		if (("opc.tcp").equalsIgnoreCase(protocolType)) {

			endpointUrl = protocolType + "://" + ipAddress + ":8666/" + applicationName;
		} else {
			endpointUrl = protocolType + "://" + ipAddress + ":8443/" + applicationName;
		}

		myLogger.info("RoverAClient: Connecting to \"" + endpointUrl + "\" .. ");

		/*String uri = "opc.tcp://localhost:62541/";
		uri = "opc.tcp://192.168.0.105:8666/TimeSyncServer";
		// Discover server applications
		ApplicationDescription[] servers = myClient.discoverApplications( uri );*/
		
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
		
/////////  DISCOVER SERVERS  /////////
		
		
		// Choose one application
	//	ApplicationDescription server = servers[0];
		// Connect the application

		// Choose one endpoint
	EndpointDescription endpoint = endpoints[0];

		endpoint.setEndpointUrl(endpointUrl);

	

		// Create Channel
		SessionChannel mySession = myClient.createSessionChannel(endpoint);

	//	mySession.activate("user1", "p4ssword");
	//	mySession.activate("user", "password");
		EventLogUtil.writeToClientEventLog("ClientId2",
				mySession.getSession().getAuthenticationToken().toString());
		
		mySession.activate();
		
		/*CallMethodRequest[] MethodsToCall =null;
		Variant[] InputArguments = null;
		MethodsToCall[0].setInputArguments(InputArguments );;
		
		CallRequest req = new CallRequest(RequestHeader, MethodsToCall );
		CallResponse call = mySession.Call(req);*/
	
	/*	
		CallRequest callRequest = new CallRequest();
		CallMethodRequest methodRequest = new CallMethodRequest();
		CallMethodRequest methodRequest2 = new CallMethodRequest();
		
		Variant variant1 = new Variant(new Integer []{7,8,9});
		Variant variant2 = new Variant(new Integer []{43,5,6});
		methodRequest2.setInputArguments(new Variant[]{variant1,variant2});
		callRequest.setMethodsToCall( new CallMethodRequest[] {methodRequest,methodRequest2} );
		NodeId MethodId = new NodeId(2, "ListSolvers");
		methodRequest.setMethodId(MethodId );
		methodRequest2.setMethodId(MethodId);
		CallResponse res = mySession.Call( callRequest );
		System.out.println( res );

		// Read a variable (Works with NanoServer example!)
		ReadResponse res5 = mySession.Read(null, null, TimestampsToReturn.Neither,
				new ReadValueId(Identifiers.Server_NamespaceArray, Attributes.Value, null, null));

		myLogger.info("RESPONSE: " + res5);*/

		// Close channel
		NodeId nodeSearch=Identifiers.Server_ServerStatus ;
		
		RequestHeader readHeader=new RequestHeader();
		
	/*	readHeader.setAuditEntryId(""+mySession.getSession().getAuthenticationToken()+DateTime.currentTime()); 
		//Before
		ReadResponse res4 = mySession.Read(readHeader, null, TimestampsToReturn.Neither,
				new ReadValueId(nodeSearch, Attributes.Description, null, null));
		//Write
		myLogger.info("Read Response:Before Modification " + res4);
		WriteRequest req=new WriteRequest();
		org.opcfoundation.ua.core.RequestHeader readHeaderWrite =new RequestHeader();
		req.setRequestHeader(readHeaderWrite );
		
		readHeaderWrite.setAuditEntryId(""+mySession.getSession().getAuthenticationToken()+DateTime.currentTime()); 
		WriteValue value=new WriteValue(nodeSearch,Attributes.Description,  "", new DataValue(
                new Variant(new LocalizedText("This Node has corrupted use different Node to retrieve the ServerTime", LocalizedText.NO_LOCALE)),
                StatusCode.GOOD, null, null));
		WriteValue[] NodesToWrite={value};
		req.setNodesToWrite(NodesToWrite);
		WriteResponse write = mySession.Write(req);
		
		myLogger.info("WriteResponse: After Attribute Write " + write);*/
		
		readHeader.setAuditEntryId(""+mySession.getSession().getAuthenticationToken()+DateTime.currentTime()); 
		//readHeader.setAuditEntryId(""+mySession.getSession().getAuthenticationToken()+DateTime.currentTime());
		ReadResponse res5 = mySession.Read(readHeader, null, TimestampsToReturn.Neither,
				new ReadValueId(nodeSearch, Attributes.Description, null, null));
		myLogger.info("Read Response : Read After Write Attribute " + res5);
		
		
		mySession.closeAsync();
		System.exit(0);

		
		
		

	}

}
