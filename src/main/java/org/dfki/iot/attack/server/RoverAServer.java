package org.dfki.iot.attack.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.dfki.iot.attack.model.ActiveSessionEventParam;
import org.dfki.iot.attack.model.CountryModel;
import org.dfki.iot.attack.model.CreateSessionEventParam;
import org.dfki.iot.attack.model.EventModel;
import org.dfki.iot.attack.model.EventParamModel;
import org.dfki.iot.attack.util.EventLogUtil;
import org.dfki.iot.attack.util.ExampleKeys;
import org.dfki.iot.attack.util.GenericUtil;
import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.application.Server;
import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.ActivateSessionRequest;
import org.opcfoundation.ua.core.ActivateSessionResponse;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddNodesRequest;
import org.opcfoundation.ua.core.AddNodesResponse;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.AddReferencesRequest;
import org.opcfoundation.ua.core.AddReferencesResponse;
import org.opcfoundation.ua.core.AnonymousIdentityToken;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.AttributeServiceSetHandler;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseNextRequest;
import org.opcfoundation.ua.core.BrowseNextResponse;
import org.opcfoundation.ua.core.BrowseRequest;
import org.opcfoundation.ua.core.BrowseResponse;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.CallRequest;
import org.opcfoundation.ua.core.CallResponse;
import org.opcfoundation.ua.core.CancelRequest;
import org.opcfoundation.ua.core.CancelResponse;
import org.opcfoundation.ua.core.CloseSessionRequest;
import org.opcfoundation.ua.core.CloseSessionResponse;
import org.opcfoundation.ua.core.CreateSessionRequest;
import org.opcfoundation.ua.core.CreateSessionResponse;
import org.opcfoundation.ua.core.DeleteNodesRequest;
import org.opcfoundation.ua.core.DeleteNodesResponse;
import org.opcfoundation.ua.core.DeleteReferencesRequest;
import org.opcfoundation.ua.core.DeleteReferencesResponse;
import org.opcfoundation.ua.core.EndpointConfiguration;
import org.opcfoundation.ua.core.FindServersRequest;
import org.opcfoundation.ua.core.FindServersResponse;
import org.opcfoundation.ua.core.HistoryReadRequest;
import org.opcfoundation.ua.core.HistoryReadResponse;
import org.opcfoundation.ua.core.HistoryUpdateRequest;
import org.opcfoundation.ua.core.HistoryUpdateResponse;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MethodServiceSetHandler;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.NodeManagementServiceSetHandler;
import org.opcfoundation.ua.core.QueryFirstRequest;
import org.opcfoundation.ua.core.QueryFirstResponse;
import org.opcfoundation.ua.core.QueryNextRequest;
import org.opcfoundation.ua.core.QueryNextResponse;
import org.opcfoundation.ua.core.ReadRequest;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.RegisterNodesRequest;
import org.opcfoundation.ua.core.RegisterNodesResponse;
import org.opcfoundation.ua.core.RequestHeader;
import org.opcfoundation.ua.core.ResponseHeader;
import org.opcfoundation.ua.core.ServiceFault;
import org.opcfoundation.ua.core.SessionServiceSetHandler;
import org.opcfoundation.ua.core.SignatureData;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsRequest;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsResponse;
import org.opcfoundation.ua.core.UnregisterNodesRequest;
import org.opcfoundation.ua.core.UnregisterNodesResponse;
import org.opcfoundation.ua.core.UserIdentityToken;
import org.opcfoundation.ua.core.UserNameIdentityToken;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.core.WriteRequest;
import org.opcfoundation.ua.core.WriteResponse;
import org.opcfoundation.ua.core.WriteValue;
import org.opcfoundation.ua.encoding.DecodingException;
import org.opcfoundation.ua.encoding.IEncodeable;
import org.opcfoundation.ua.transport.Endpoint;
import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;
import org.opcfoundation.ua.transport.security.BcCryptoProvider;
import org.opcfoundation.ua.transport.security.HttpsSecurityPolicy;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityAlgorithm;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.opcfoundation.ua.utils.CertificateUtils;
import org.opcfoundation.ua.utils.CryptoUtil;
import org.opcfoundation.ua.utils.EndpointUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoverAServer {

	private static final Logger logger = LoggerFactory.getLogger(RoverAServer.class);
	static Map<NodeId, Map<UnsignedInteger, DataValue>> onReadResultsMap;
	static Map<NodeId, Class<?>> datatypeMap;

	// Make ArrayList for authentication tokens
	static ArrayList<NodeId> validAuthenticationTokens = new ArrayList<NodeId>();
	static ArrayList<NodeId> sessions = new ArrayList<NodeId>();
	static Map<NodeId, Long> timeoutPeriods = new HashMap<NodeId, Long>();
	// The list of authorized users. Currently all the valid users except
	// ANONYMOUS users are authorized.
	static Map<NodeId, IEncodeable> userAuthorization = new HashMap<NodeId, IEncodeable>();
	static ContinuationPoint continuationPoint;

	static RoverAServerExample roverServer;

	// To store all the country wise ip address count
	static HashMap<String, CountryModel> countryChartMap = new HashMap<String, CountryModel>();

	/**
	 * Class to represent ContinuationPoint. This Server supports one
	 * continuation point at a time.
	 */
	static class ContinuationPoint {
		private UnsignedInteger requestedMaxReferencesPerNode;

		private ReferenceDescription[] referenceDescriptions;

		private NodeId authenticationToken;

		private ByteString currentContinuationPoint;

		public ContinuationPoint(UnsignedInteger requestedMaxReferencesPerNode,
				ReferenceDescription[] referenceDescriptions, NodeId authenticationToken,
				ByteString currentContinuationPoint) {
			this.requestedMaxReferencesPerNode = requestedMaxReferencesPerNode;
			this.referenceDescriptions = referenceDescriptions;
			this.authenticationToken = authenticationToken;
			this.currentContinuationPoint = currentContinuationPoint;
		}

		/**
		 * @return the authenticationToken
		 */
		public NodeId getAuthenticationToken() {
			return authenticationToken;
		}

		/**
		 * @return the currentContinuationPoint
		 */
		public ByteString getCurrentContinuationPoint() {
			return currentContinuationPoint;
		}

		/**
		 * @return those references that belong to next BrowseNext response
		 * @param continuationPointRequested
		 *            identify current continuation points
		 */
		public BrowseResult getNextReferencesDescriptions(ByteString continuationPointRequested) {
			// ByteString continuationPointRequested may be used to identify
			// different continuation points
			ArrayList<ReferenceDescription> referenceDescriptionsToReturn = new ArrayList<ReferenceDescription>();
			ArrayList<ReferenceDescription> originalReferenceDescriptions = new ArrayList<ReferenceDescription>(
					Arrays.asList(referenceDescriptions));

			int length = Math.min(continuationPoint.getRequestedMaxReferencesPerNode().intValue(),
					referenceDescriptions.length);
			// return only certain amount of references
			referenceDescriptionsToReturn.addAll(originalReferenceDescriptions.subList(0, length));
			// Remove these references from this ContinuationPoint
			originalReferenceDescriptions.subList(0, length).clear();
			referenceDescriptions = originalReferenceDescriptions
					.toArray(new ReferenceDescription[originalReferenceDescriptions.size()]);
			// return referenceDescriptionsToReturn;
			if (referenceDescriptions.length > 0) {
				this.currentContinuationPoint = ByteString
						.valueOf(new byte[] { (byte) (continuationPointRequested.getValue()[0] + (byte) 1) });
				return new BrowseResult(StatusCode.GOOD, currentContinuationPoint, referenceDescriptionsToReturn
						.toArray(new ReferenceDescription[referenceDescriptionsToReturn.size()]));
			}
			// if no references are left, then do not return continuationPoint
			// anymore
			return new BrowseResult(StatusCode.GOOD, null, referenceDescriptionsToReturn
					.toArray(new ReferenceDescription[referenceDescriptionsToReturn.size()]));
		}

		public ReferenceDescription[] getReferenceDescriptions() {
			return this.referenceDescriptions;
		}

		public UnsignedInteger getRequestedMaxReferencesPerNode() {
			return this.requestedMaxReferencesPerNode;
		}

		/**
		 * @param authenticationToken
		 *            the authenticationToken to set
		 */
		public void setAuthenticationToken(NodeId authenticationToken) {
			this.authenticationToken = authenticationToken;
		}

		/**
		 * @param currentContinuationPoint
		 *            the currentContinuationPoint to set
		 */
		public void setCurrentContinuationPoint(ByteString currentContinuationPoint) {
			this.currentContinuationPoint = currentContinuationPoint;
		}

		public void setReferenceDescriptions(ReferenceDescription[] referenceDescriptions) {
			this.referenceDescriptions = referenceDescriptions;
		}

		public void setRequestedMaxReferencesPerNode(UnsignedInteger requestedMaxReferencesPerNode) {
			this.requestedMaxReferencesPerNode = requestedMaxReferencesPerNode;
		}
	}

	/**
	 * This service handler contains only one method: onFindServers
	 */
	static class FindServersServiceHandler {

		/**
		 * FindServers Service
		 * 
		 * @param msgExchange
		 *            EndpointServiceRequest
		 * @throws ServiceFaultException
		 */
		public void onFindServers(EndpointServiceRequest<FindServersRequest, FindServersResponse> msgExchange)
				throws ServiceFaultException {
			FindServersRequest request = msgExchange.getRequest();

			ApplicationDescription[] servers = new ApplicationDescription[1];

			Application application = roverServer.getApplication();
			String applicationUri = application.getApplicationUri();
			String productUri = application.getProductUri();
			ApplicationDescription applicationDescription = application.getApplicationDescription();
			LocalizedText applicationName = applicationDescription.getApplicationName();
			ApplicationType applicationType = applicationDescription.getApplicationType();
			String gatewayServerUri = null;
			String discoveryProfileUri = null;
			String[] discoveryUrls = applicationDescription.getDiscoveryUrls();
			if (discoveryUrls == null) {
				// Specify default URLs for the DiscoveryServer if
				// getDiscoveryUrls() returned null.
				Endpoint[] discoveryEndpoints = roverServer.getEndpoints();
				discoveryUrls = new String[discoveryEndpoints.length];
				for (int i = 0; i < discoveryEndpoints.length; i++) {
					discoveryUrls[i] = discoveryEndpoints[i].getEndpointUrl();
				}
			}
			servers[0] = new ApplicationDescription(applicationUri, productUri, applicationName, applicationType,
					gatewayServerUri, discoveryProfileUri, discoveryUrls);

			ResponseHeader header = new ResponseHeader(DateTime.currentTime(),
					request.getRequestHeader().getRequestHandle(), StatusCode.GOOD, null, null, null);

			FindServersResponse response = new FindServersResponse(header, servers);

			if (GenericUtil.enableAuditing("onFindServers")) {

				EventParamModel eventParamModel = new EventParamModel();
				eventParamModel.setAuthenToken(request.getRequestHeader().getAuthenticationToken().toString());
				eventParamModel.setAuditId(request.getRequestHeader().getAuditEntryId());
				eventParamModel.setSessionId(null);

				InetSocketAddress proxyAddress = (InetSocketAddress) msgExchange.getChannel().getConnection()
						.getRemoteAddress();
				eventParamModel.setIpAddress(proxyAddress.getAddress().getHostAddress());
				eventParamModel.setPort(proxyAddress.getPort());

				EventModel eventModel = new EventModel("onFindServers", eventParamModel);
				EventLogUtil.writeToServerEventLog(eventModel);

			}

			msgExchange.sendResponse(response);
		}
	}

	static class RoverNodemanagementServiceHandler implements NodeManagementServiceSetHandler {

		public void onAddNodes(EndpointServiceRequest<AddNodesRequest, AddNodesResponse> msgExchange)
				throws ServiceFaultException {

			AddNodesRequest request = msgExchange.getRequest();
			AddNodesItem[] nodesToAdd = request.getNodesToAdd();
			AddNodesResponse response = new AddNodesResponse();
			AddNodesResult[] addNodesResult = new AddNodesResult[msgExchange.getRequest().getNodesToAdd().length];
			IEncodeable iEncodeable = userAuthorization
					.get(msgExchange.getRequest().getRequestHeader().getAuthenticationToken());
			String authorisation = GenericUtil.readServerPropertyConfigFile("authorisation");
			if ("true".equalsIgnoreCase(authorisation)) {
				if (!(iEncodeable instanceof AnonymousIdentityToken)) {
					addNodeProperties(nodesToAdd, response, addNodesResult);
				} else {
					addNodesResult[0] = new AddNodesResult();
					addNodesResult[0].setStatusCode(new StatusCode(StatusCodes.Bad_UserAccessDenied));
					response.setResults(addNodesResult);
				}
			} else {
				addNodeProperties(nodesToAdd, response, addNodesResult);
			}

			if (GenericUtil.enableAuditing("onAddNodes")) {
				EventParamModel eventParamModel = new EventParamModel();
				eventParamModel.setAuthenToken(request.getRequestHeader().getAuthenticationToken().toString());
				eventParamModel.setAuditId(request.getRequestHeader().getAuditEntryId());
				eventParamModel.setSessionId(null);

				InetSocketAddress proxyAddress = (InetSocketAddress) msgExchange.getChannel().getConnection()
						.getRemoteAddress();
				eventParamModel.setIpAddress(proxyAddress.getAddress().getHostAddress());
				eventParamModel.setPort(proxyAddress.getPort());

				EventModel eventModel = new EventModel("onAddNodes", eventParamModel);
				EventLogUtil.writeToServerEventLog(eventModel);
			}

			msgExchange.sendResponse(response);

		}

		private void addNodeProperties(AddNodesItem[] nodesToAdd, AddNodesResponse response,
				AddNodesResult[] addNodesResult) {
			// if(!("Anonymous").equalsIgnoreCase(userNameIdentityToken.getPolicyId())){
			int i = 0;
			for (AddNodesItem addNodesItem : nodesToAdd) {
				AddNodesResult result = addNodesResult[i] = new AddNodesResult();
				NodeId id = new NodeId(addNodesItem.getReferenceTypeId().getNamespaceIndex(),
						(UnsignedInteger) addNodesItem.getTypeDefinition().getValue());
				final DateTime serverTimeStamp = DateTime.currentTime();
				// final String applicationURI =
				// application.getApplicationUri();
				onReadResultsMap.put(id, new HashMap<UnsignedInteger, DataValue>() {
					/**
					* 
					*/
					private static final long serialVersionUID = 1L;

					{
						put(Attributes.NodeId, new DataValue(new Variant(Identifiers.Server_NamespaceArray),
								StatusCode.GOOD, null, serverTimeStamp));
						put(Attributes.NodeClass,
								new DataValue(new Variant(NodeClass.Variable), StatusCode.GOOD, null, serverTimeStamp));
						put(Attributes.BrowseName, new DataValue(new Variant(new QualifiedName("NamespaceArray")),
								StatusCode.GOOD, null, serverTimeStamp));
						put(Attributes.DisplayName,
								new DataValue(new Variant(new LocalizedText("NamespaceArray", LocalizedText.NO_LOCALE)),
										StatusCode.GOOD, null, serverTimeStamp));
						put(Attributes.Description,
								new DataValue(
										new Variant(new LocalizedText("The list of namespace URIs used by the server.",
												LocalizedText.NO_LOCALE)),
										StatusCode.GOOD, null, serverTimeStamp));
						put(Attributes.WriteMask, new DataValue(new Variant(new UnsignedInteger(0)), StatusCode.GOOD,
								null, serverTimeStamp));
						put(Attributes.UserWriteMask, new DataValue(new Variant(new UnsignedInteger(0)),
								StatusCode.GOOD, null, serverTimeStamp));
						put(Attributes.Value, new DataValue(new Variant(
								new String[] { "http://opcfoundation.org/UA/", "http://opcfoundation.org/UA/" }),
								StatusCode.GOOD, null, serverTimeStamp));
					}
				});

				response.setResults(new AddNodesResult[] { new AddNodesResult(StatusCode.GOOD, id) });
				i++;
				result.setStatusCode(StatusCode.GOOD);
			}
		}

		public void onAddReferences(EndpointServiceRequest<AddReferencesRequest, AddReferencesResponse> msgExchange)
				throws ServiceFaultException {
			throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NotImplemented));

		}

		public void onDeleteNodes(EndpointServiceRequest<DeleteNodesRequest, DeleteNodesResponse> msgExchange)
				throws ServiceFaultException {
			throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NotImplemented));

		}

		public void onDeleteReferences(
				EndpointServiceRequest<DeleteReferencesRequest, DeleteReferencesResponse> msgExchange)
				throws ServiceFaultException {
			throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NotImplemented));

		}

		public void onBrowse(EndpointServiceRequest<BrowseRequest, BrowseResponse> msgExchange)
				throws ServiceFaultException {
			throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NotImplemented));

		}

		public void onBrowseNext(EndpointServiceRequest<BrowseNextRequest, BrowseNextResponse> msgExchange)
				throws ServiceFaultException {
			throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NotImplemented));

		}

		public void onTranslateBrowsePathsToNodeIds(
				EndpointServiceRequest<TranslateBrowsePathsToNodeIdsRequest, TranslateBrowsePathsToNodeIdsResponse> msgExchange)
				throws ServiceFaultException {
			throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NotImplemented));

		}

		public void onRegisterNodes(EndpointServiceRequest<RegisterNodesRequest, RegisterNodesResponse> msgExchange)
				throws ServiceFaultException {
			throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NotImplemented));

		}

		public void onUnregisterNodes(
				EndpointServiceRequest<UnregisterNodesRequest, UnregisterNodesResponse> msgExchange)
				throws ServiceFaultException {
			throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NotImplemented));

		}

		public void onQueryFirst(EndpointServiceRequest<QueryFirstRequest, QueryFirstResponse> msgExchange)
				throws ServiceFaultException {
			throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NotImplemented));

		}

		public void onQueryNext(EndpointServiceRequest<QueryNextRequest, QueryNextResponse> msgExchange)
				throws ServiceFaultException {
			throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NotImplemented));

		}

	}

	static class RoverAttributeServiceHandler implements AttributeServiceSetHandler {

		public void onHistoryRead(EndpointServiceRequest<HistoryReadRequest, HistoryReadResponse> msgExchange)
				throws ServiceFaultException {
			throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NotImplemented));
		}

		public void onHistoryUpdate(EndpointServiceRequest<HistoryUpdateRequest, HistoryUpdateResponse> msgExchange)
				throws ServiceFaultException {
			throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NotImplemented));
		}

		/**
		 * Handle read request.
		 */

		public void onRead(EndpointServiceRequest<ReadRequest, ReadResponse> msgExchange) throws ServiceFaultException {
			ReadRequest request = msgExchange.getRequest();
			ReadValueId[] nodesToRead = request.getNodesToRead();

			DataValue[] results = null;
			ReadResponse response = null;
			ResponseHeader responseHeader = checkRequestHeader(request.getRequestHeader());

			if (responseHeader.getServiceResult().isGood()) {

				if (request.getMaxAge() < 0) {
					responseHeader = new ResponseHeader(DateTime.currentTime(),
							request.getRequestHeader().getRequestHandle(),
							new StatusCode(StatusCodes.Bad_MaxAgeInvalid), null, null, null);
				} else if (request.getTimestampsToReturn() == null) {
					responseHeader = new ResponseHeader(DateTime.currentTime(),
							request.getRequestHeader().getRequestHandle(),
							new StatusCode(StatusCodes.Bad_TimestampsToReturnInvalid), null, null, null);
				} else if (nodesToRead != null) {
					// Do actual handling of NodesToRead

					results = new DataValue[nodesToRead.length];
					DateTime serverTimestamp = DateTime.currentTime();
					for (int i = 0; i < nodesToRead.length; i++) {
						results[i] = null;
						Map<UnsignedInteger, DataValue> attributeMap = onReadResultsMap.get(nodesToRead[i].getNodeId());

						if (attributeMap != null) {

							if (attributeMap.containsKey(nodesToRead[i].getAttributeId())) {
								results[i] = (DataValue) attributeMap.get(nodesToRead[i].getAttributeId()).clone();

								if (results[i] == null) {
									results[i] = new DataValue(new StatusCode(StatusCodes.Bad_AttributeIdInvalid));
								} else if (new UnsignedInteger(13).equals(nodesToRead[i].getAttributeId())) {
									// check maxAge
									DateTime currentTimestamp = results[i].getServerTimestamp();
									DateTime currentTime = DateTime.fromMillis(System.currentTimeMillis());
									long age = currentTime.getTimeInMillis() - currentTimestamp.getTimeInMillis();
									long maxAge = request.getMaxAge().longValue();
									long diff = maxAge - age;
									// If the server does not have a value that
									// is within the maximum age, it shall
									// attempt to read a new value from the data
									// source.
									// If maxAge is set to 0, the server shall
									// attempt to read a new value from the data
									// source.
									if (diff <= 0) {
										// read new value, simulated here by
										// refreshing timestamp
										results[i].setServerTimestamp(serverTimestamp);
									}
									/*
									 * This could also be checked here but it is
									 * now ignored: If maxAge is set to the max
									 * Int32 value or greater, the server shall
									 * attempt to get a cached value.
									 */

									if (request.getTimestampsToReturn() != null) {
										// check TimestampsToReturn
										switch (request.getTimestampsToReturn()) {
										case Source:
											results[i].setSourceTimestamp(serverTimestamp);
											results[i].setServerTimestamp(null);
											break;
										case Both:
											results[i].setSourceTimestamp(serverTimestamp);
											break;
										case Neither:
											results[i].setServerTimestamp(null);
											break;
										default:
											// case Server
											break;
										}
									}

								}
							} else {
								results[i] = new DataValue(new StatusCode(StatusCodes.Bad_AttributeIdInvalid));
							}
						} else {
							results[i] = new DataValue(new StatusCode(StatusCodes.Bad_NodeIdUnknown));
						}
					}
				} else {
					// NodesToRead is empty
					responseHeader = new ResponseHeader(DateTime.currentTime(),
							request.getRequestHeader().getRequestHandle(), new StatusCode(StatusCodes.Bad_NothingToDo),
							null, null, null);
				}
			}

			// TODO: map the required fields of request data.
			/*
			 * ExcelUtil.auditRequest("RoverA",
			 * req.getRequest().getInput().getValue().toString());
			 */

			response = new ReadResponse(responseHeader, results, null);

			if (GenericUtil.enableAuditing("onRead")) {
				EventParamModel eventParamModel = new EventParamModel();
				eventParamModel.setAuthenToken(
						msgExchange.getRequest().getRequestHeader().getAuthenticationToken().toString());
				eventParamModel.setAuditId(msgExchange.getRequest().getRequestHeader().getAuditEntryId());
				eventParamModel.setSessionId(null);

				/*
				 * String text =
				 * JSONUtil.getJSONString(req.getChannel().getConnection().
				 * getRemoteAddress());
				 * 
				 * RemoteAddressModel remoteAddressModel = (RemoteAddressModel)
				 * JSONUtil.getObjFromJSONString(text, new
				 * RemoteAddressModel());
				 * 
				 * eventParamModel.setRemoteAddress(remoteAddressModel);
				 */

				InetSocketAddress proxyAddress = (InetSocketAddress) msgExchange.getChannel().getConnection()
						.getRemoteAddress();
				eventParamModel.setIpAddress(proxyAddress.getAddress().getHostAddress());
				eventParamModel.setPort(proxyAddress.getPort());

				EventModel eventModel = new EventModel("onRead", eventParamModel);
				EventLogUtil.writeToServerEventLog(eventModel);

			}
			msgExchange.sendResponse(response);
		}

		/**
		 * Handle write request.
		 */

		public void onWrite(EndpointServiceRequest<WriteRequest, WriteResponse> msgExchange)
				throws ServiceFaultException {

			WriteRequest request = msgExchange.getRequest();
			WriteValue[] nodesToWrite = request.getNodesToWrite();

			StatusCode[] results = null;
			StatusCode serviceResultCode = null;
			IEncodeable iEncodeable = userAuthorization
					.get(msgExchange.getRequest().getRequestHeader().getAuthenticationToken());

			String authorisation = GenericUtil.readServerPropertyConfigFile("authorisation");
			if ("true".equalsIgnoreCase(authorisation)) {

				if (!(iEncodeable instanceof AnonymousIdentityToken)) {

					if (nodesToWrite != null) {
						// check here that Bad_TooManyOperations should not be
						// set. No
						// limit for operations in this implementation.
						// Now set service result to GOOD always if nodesToWrite
						// is not
						// null.
						serviceResultCode = StatusCode.GOOD;

						results = new StatusCode[nodesToWrite.length];
						for (int i = 0; i < nodesToWrite.length; i++) {
							// Get all attributes of the specified node
							Map<UnsignedInteger, DataValue> attributeMap = onReadResultsMap
									.get(nodesToWrite[i].getNodeId());

							if (attributeMap != null) {
								if (attributeMap.containsKey(nodesToWrite[i].getAttributeId())) {

									if (new UnsignedInteger(13).equals(nodesToWrite[i].getAttributeId())) {
										// Write value attribute
										// Check data type using nodes DataType
										// attribute
										// Validation is done with datatypeMap
										// to enable
										// easy modification of valid data types
										NodeId datatype = (NodeId) attributeMap.get(Attributes.DataType).getValue()
												.getValue();
										if (datatype == null) {
											// Error: Current node does not have
											// data
											// type specified
											results[i] = new StatusCode(StatusCodes.Bad_TypeMismatch);
										} else {
											// Data type is defined for current
											// node
											// Get java class corresponding to
											// this OPC
											// UA data type
											Class<?> targetDataType = datatypeMap.get(datatype);
											if (targetDataType == null) {
												// No java data type found for
												// this ua
												// type
												results[i] = new StatusCode(StatusCodes.Bad_TypeMismatch);
											} else {
												// Compare data type of value
												// attribute
												// and value from write request
												if (targetDataType.isAssignableFrom(
														nodesToWrite[i].getValue().getValue().getValue().getClass())) {
													attributeMap.get(nodesToWrite[i].getAttributeId())
															.setValue(nodesToWrite[i].getValue().getValue());
													results[i] = StatusCode.GOOD;
												} else {
													// values do not match
													results[i] = new StatusCode(StatusCodes.Bad_TypeMismatch);
												}
											}
										}
									} else {
										// Write no other attribute than value.
										// Correct data type should also be
										// checked
										// here.
										attributeMap.get(nodesToWrite[i].getAttributeId())
												.setValue(nodesToWrite[i].getValue().getValue());
									}
								} else {
									results[i] = new StatusCode(StatusCodes.Bad_AttributeIdInvalid);
								}
							} else {
								results[i] = new StatusCode(StatusCodes.Bad_NodeIdInvalid);
							}
						}
					} else {
						// Empty nodesToWrite array
						serviceResultCode = new StatusCode(StatusCodes.Bad_NothingToDo);
					}

				} else {
					results = new StatusCode[1];
					results[0] = new StatusCode(StatusCodes.Bad_UserAccessDenied);

				}
			} else {

				if (nodesToWrite != null) {
					// check here that Bad_TooManyOperations should not be set.
					// No
					// limit for operations in this implementation.
					// Now set service result to GOOD always if nodesToWrite is
					// not
					// null.
					serviceResultCode = StatusCode.GOOD;

					results = new StatusCode[nodesToWrite.length];
					for (int i = 0; i < nodesToWrite.length; i++) {
						// Get all attributes of the specified node
						Map<UnsignedInteger, DataValue> attributeMap = onReadResultsMap
								.get(nodesToWrite[i].getNodeId());

						if (attributeMap != null) {
							if (attributeMap.containsKey(nodesToWrite[i].getAttributeId())) {

								if (new UnsignedInteger(13).equals(nodesToWrite[i].getAttributeId())) {
									// Write value attribute
									// Check data type using nodes DataType
									// attribute
									// Validation is done with datatypeMap to
									// enable
									// easy modification of valid data types
									NodeId datatype = (NodeId) attributeMap.get(Attributes.DataType).getValue()
											.getValue();
									if (datatype == null) {
										// Error: Current node does not have
										// data
										// type specified
										results[i] = new StatusCode(StatusCodes.Bad_TypeMismatch);
									} else {
										// Data type is defined for current node
										// Get java class corresponding to this
										// OPC
										// UA data type
										Class<?> targetDataType = datatypeMap.get(datatype);
										if (targetDataType == null) {
											// No java data type found for this
											// ua
											// type
											results[i] = new StatusCode(StatusCodes.Bad_TypeMismatch);
										} else {
											// Compare data type of value
											// attribute
											// and value from write request
											if (targetDataType.isAssignableFrom(
													nodesToWrite[i].getValue().getValue().getValue().getClass())) {
												attributeMap.get(nodesToWrite[i].getAttributeId())
														.setValue(nodesToWrite[i].getValue().getValue());
												results[i] = StatusCode.GOOD;
											} else {
												// values do not match
												results[i] = new StatusCode(StatusCodes.Bad_TypeMismatch);
											}
										}
									}
								} else {
									// Write no other attribute than value.
									// Correct data type should also be checked
									// here.
									attributeMap.get(nodesToWrite[i].getAttributeId())
											.setValue(nodesToWrite[i].getValue().getValue());
								}
							} else {
								results[i] = new StatusCode(StatusCodes.Bad_AttributeIdInvalid);
							}
						} else {
							results[i] = new StatusCode(StatusCodes.Bad_NodeIdInvalid);
						}
					}
				} else {
					// Empty nodesToWrite array
					serviceResultCode = new StatusCode(StatusCodes.Bad_NothingToDo);
				}

			}
			WriteResponse response = new WriteResponse(null, results, null);
			// Set response header to pass ctt check_responseHeader_error.js
			ResponseHeader h = new ResponseHeader(DateTime.currentTime(), request.getRequestHeader().getRequestHandle(),
					serviceResultCode, null, null, null);
			response.setResponseHeader(h);

			if (GenericUtil.enableAuditing("onWrite")) {
				EventParamModel eventParamModel = new EventParamModel();
				eventParamModel.setAuthenToken(
						msgExchange.getRequest().getRequestHeader().getAuthenticationToken().toString());
				eventParamModel.setAuditId(msgExchange.getRequest().getRequestHeader().getAuditEntryId());
				eventParamModel.setSessionId(null);

				InetSocketAddress proxyAddress = (InetSocketAddress) msgExchange.getChannel().getConnection()
						.getRemoteAddress();
				eventParamModel.setIpAddress(proxyAddress.getAddress().getHostAddress());
				eventParamModel.setPort(proxyAddress.getPort());

				EventModel eventModel = new EventModel("onWrite", eventParamModel);
				EventLogUtil.writeToServerEventLog(eventModel);
			}

			msgExchange.sendResponse(response);
		}

	}

	static class RoverAServerExample extends Server implements SessionServiceSetHandler, MethodServiceSetHandler {
		// public static final NodeId LIST_SOLVERS = new NodeId(2,
		// "ListSolvers");

		@SuppressWarnings("serial")
		public RoverAServerExample(Application application, String applicationName) throws Exception {
			super(application);
			addServiceHandler(this);
			// The HTTPS SecurityPolicies are defined separate from the endpoint
			// securities
			application.getHttpsSettings().setHttpsSecurityPolicies(HttpsSecurityPolicy.ALL);

			// Peer verifier
			application.getHttpsSettings().setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			// Load Servers's Application Instance Certificate...
			KeyPair myServerApplicationInstanceCertificate = ExampleKeys.getCert(applicationName);
			application.addApplicationInstanceCertificate(myServerApplicationInstanceCertificate);
			// ...and HTTPS certificate
			KeyPair myHttpsCertificate = ExampleKeys.getHttpsCert(applicationName);
			application.getHttpsSettings().setKeyPair(myHttpsCertificate);

			// Add User Token Policies
			String setanonymous = GenericUtil.readServerPropertyConfigFile("ANONYMOUS");
			if ("true".equalsIgnoreCase(setanonymous)) {
				// Add User Token Policies
				addUserTokenPolicy(UserTokenPolicy.ANONYMOUS);
			}
			addUserTokenPolicy(UserTokenPolicy.SECURE_USERNAME_PASSWORD);
			// addUserTokenPolicy(UserTokenPolicy.);

			// Create an endpoint for each network interface
			String hostname = EndpointUtil.getHostname();
			String bindAddress, endpointAddress;
			for (String addr : EndpointUtil.getInetAddressNames()) {
				bindAddress = "https://" + addr + ":8443/" + applicationName;
				endpointAddress = "https://" + hostname + ":8443/" + applicationName;
				logger.info("{} bound at {}", endpointAddress, bindAddress);
				// The HTTPS ports are using NONE OPC security
				bind(bindAddress, endpointAddress, SecurityMode.NONE);

				bindAddress = "opc.tcp://" + addr + ":8666/" + applicationName;
				endpointAddress = "opc.tcp://" + hostname + ":8666/" + applicationName;
				logger.info("{} bound at {}", endpointAddress, bindAddress);
				bind(bindAddress, endpointAddress, SecurityMode.ALL_101);
			}

			// Set continuationPoint to null at start-up
			continuationPoint = null;

			// *******************************************************************************
			// Put all read datavalues in one HashMap for better readability and
			// performance
			// *******************************************************************************
			final DateTime serverTimeStamp = DateTime.currentTime();
			onReadResultsMap = new HashMap<NodeId, Map<UnsignedInteger, DataValue>>();

			final String applicationURI = application.getApplicationUri();

			onReadResultsMap.put(Identifiers.Server_ServerStatus_CurrentTime,
					new HashMap<UnsignedInteger, DataValue>() {
						{
							put(Attributes.Value, new DataValue(new Variant(serverTimeStamp), StatusCode.GOOD, null,
									serverTimeStamp));
						}
					});
			onReadResultsMap.put(Identifiers.Server_ServerCapabilities_LocaleIdArray,
					new HashMap<UnsignedInteger, DataValue>() {
						{
							put(Attributes.Value,
									new DataValue(new Variant(new String[1]), StatusCode.GOOD, null, serverTimeStamp));
						}
					});

			onReadResultsMap.put(Identifiers.Server_ServerStatus, new HashMap<UnsignedInteger, DataValue>() {
				{
					put(Attributes.NodeId, new DataValue(new Variant(Identifiers.Server_ServerStatus), StatusCode.GOOD,
							null, serverTimeStamp));
					put(Attributes.NodeClass,
							new DataValue(new Variant(NodeClass.Variable), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.BrowseName, new DataValue(new Variant(new QualifiedName("ServerStatus")),
							StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.DisplayName,
							new DataValue(new Variant(new LocalizedText("ServerStatus", LocalizedText.NO_LOCALE)),
									StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.Description,
							new DataValue(new Variant(
									new LocalizedText("The current status of the server.", LocalizedText.NO_LOCALE)),
									StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.WriteMask,
							new DataValue(new Variant(new UnsignedInteger(0)), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.UserWriteMask,
							new DataValue(new Variant(new UnsignedInteger(0)), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.Value, new DataValue(null, StatusCode.GOOD, null, serverTimeStamp));
				}
			});

			onReadResultsMap.put(Identifiers.Server_ServerStatus_State, new HashMap<UnsignedInteger, DataValue>() {
				{
					put(Attributes.NodeId, new DataValue(new Variant(Identifiers.Server_ServerStatus_State),
							StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.NodeClass,
							new DataValue(new Variant(NodeClass.Variable), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.BrowseName, new DataValue(new Variant(new QualifiedName("State")), StatusCode.GOOD,
							null, serverTimeStamp));
					put(Attributes.DisplayName,
							new DataValue(new Variant(new LocalizedText("State", LocalizedText.NO_LOCALE)),
									StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.Description,
							new DataValue(new Variant(new LocalizedText("", LocalizedText.NO_LOCALE)), StatusCode.GOOD,
									null, serverTimeStamp));
					put(Attributes.WriteMask,
							new DataValue(new Variant(new UnsignedInteger(0)), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.UserWriteMask,
							new DataValue(new Variant(new UnsignedInteger(0)), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.Value,
							new DataValue(new Variant(new UnsignedInteger(0)), StatusCode.GOOD, null, serverTimeStamp));
				}
			});
			onReadResultsMap.put(Identifiers.Server_NamespaceArray, new HashMap<UnsignedInteger, DataValue>() {
				{
					put(Attributes.NodeId, new DataValue(new Variant(Identifiers.Server_NamespaceArray),
							StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.NodeClass,
							new DataValue(new Variant(NodeClass.Variable), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.BrowseName, new DataValue(new Variant(new QualifiedName("NamespaceArray")),
							StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.DisplayName,
							new DataValue(new Variant(new LocalizedText("NamespaceArray", LocalizedText.NO_LOCALE)),
									StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.Description,
							new DataValue(
									new Variant(new LocalizedText("The list of namespace URIs used by the server.",
											LocalizedText.NO_LOCALE)),
									StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.WriteMask,
							new DataValue(new Variant(new UnsignedInteger(0)), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.UserWriteMask,
							new DataValue(new Variant(new UnsignedInteger(0)), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.Value,
							new DataValue(new Variant(new String[] { "http://opcfoundation.org/UA/", applicationURI }),
									StatusCode.GOOD, null, serverTimeStamp));
				}
			});

			onReadResultsMap.put(Identifiers.Server_ServerArray, new HashMap<UnsignedInteger, DataValue>() {
				{
					put(Attributes.NodeId, new DataValue(new Variant(Identifiers.Server_ServerArray), StatusCode.GOOD,
							null, serverTimeStamp));
					put(Attributes.NodeClass,
							new DataValue(new Variant(NodeClass.Variable), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.BrowseName, new DataValue(new Variant(new QualifiedName("ServerArray")),
							StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.DisplayName,
							new DataValue(new Variant(new LocalizedText("ServerArray", LocalizedText.NO_LOCALE)),
									StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.Description, new DataValue(new Variant(
							new LocalizedText("The list of server URIs used by the server.", LocalizedText.NO_LOCALE)),
							StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.WriteMask,
							new DataValue(new Variant(new UnsignedInteger(0)), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.UserWriteMask,
							new DataValue(new Variant(new UnsignedInteger(0)), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.Value, new DataValue(new Variant(new String[] { applicationURI }), StatusCode.GOOD,
							serverTimeStamp, serverTimeStamp));
				}
			});
			onReadResultsMap.put(Identifiers.Server_ServerStatus_BuildInfo_ProductName,
					new HashMap<UnsignedInteger, DataValue>() {
						{
							put(Attributes.Value, new DataValue(new Variant("SampleNanoServer"), StatusCode.GOOD, null,
									serverTimeStamp));
						}
					});
			onReadResultsMap.put(Identifiers.Server_ServerStatus_BuildInfo_ManufacturerName,
					new HashMap<UnsignedInteger, DataValue>() {
						{
							put(Attributes.Value, new DataValue(null, StatusCode.GOOD, null, serverTimeStamp));
						}
					});
			onReadResultsMap.put(Identifiers.Server_ServerStatus_BuildInfo_SoftwareVersion,
					new HashMap<UnsignedInteger, DataValue>() {
						{
							put(Attributes.Value, new DataValue(null, StatusCode.GOOD, null, serverTimeStamp));
						}
					});
			onReadResultsMap.put(Identifiers.Server_ServerStatus_BuildInfo_BuildDate,
					new HashMap<UnsignedInteger, DataValue>() {
						{
							put(Attributes.Value,
									new DataValue(new Variant(DateTime.parseDateTime("2014-12-30T00:00:00Z")),
											StatusCode.GOOD, null, serverTimeStamp));
						}
					});

			onReadResultsMap.put(Identifiers.Server_ServerStatus_StartTime, new HashMap<UnsignedInteger, DataValue>() {
				{
					put(Attributes.Value,
							new DataValue(new Variant(serverTimeStamp), StatusCode.GOOD, null, serverTimeStamp));
				}
			});

			onReadResultsMap.put(Identifiers.Server_ServerStatus_SecondsTillShutdown,
					new HashMap<UnsignedInteger, DataValue>() {
						{
							put(Attributes.Value, new DataValue(null, StatusCode.GOOD, null, serverTimeStamp));
						}
					});

			onReadResultsMap.put(Identifiers.Server_ServerStatus_ShutdownReason,
					new HashMap<UnsignedInteger, DataValue>() {
						{
							put(Attributes.Value, new DataValue(null, StatusCode.GOOD, null, serverTimeStamp));
						}
					});

			onReadResultsMap.put(Identifiers.Server_ServerStatus_BuildInfo, new HashMap<UnsignedInteger, DataValue>() {
				{
					put(Attributes.Value, new DataValue(null, StatusCode.GOOD, null, serverTimeStamp));
				}
			});

			onReadResultsMap.put(Identifiers.Server_ServerCapabilities, new HashMap<UnsignedInteger, DataValue>() {
				{
					put(Attributes.NodeId, new DataValue(new Variant(Identifiers.Server_ServerCapabilities),
							StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.NodeClass,
							new DataValue(new Variant(NodeClass.Object), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.BrowseName, new DataValue(new Variant(new QualifiedName("ServerCapabilities")),
							StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.DisplayName,
							new DataValue(new Variant(new LocalizedText("ServerCapabilities", LocalizedText.NO_LOCALE)),
									StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.Description,
							new DataValue(
									new Variant(new LocalizedText("Describes the capabilities supported by the server.",
											LocalizedText.NO_LOCALE)),
									StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.WriteMask,
							new DataValue(new Variant(new UnsignedInteger(0)), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.UserWriteMask,
							new DataValue(new Variant(new UnsignedInteger(0)), StatusCode.GOOD, null, serverTimeStamp));
					put(Attributes.EventNotifier,
							new DataValue(new Variant(new Byte((byte) 0)), StatusCode.GOOD, null, serverTimeStamp));
				}
			});

			onReadResultsMap.put(Identifiers.Server_ServerCapabilities_MaxBrowseContinuationPoints,
					new HashMap<UnsignedInteger, DataValue>() {
						{
							put(Attributes.NodeId,
									new DataValue(
											new Variant(
													Identifiers.Server_ServerCapabilities_MaxBrowseContinuationPoints),
											StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.NodeClass, new DataValue(new Variant(NodeClass.Variable), StatusCode.GOOD,
									null, serverTimeStamp));
							put(Attributes.BrowseName,
									new DataValue(new Variant(new QualifiedName("MaxBrowseContinuationPoints")),
											StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.DisplayName,
									new DataValue(new Variant(
											new LocalizedText("MaxBrowseContinuationPoints", LocalizedText.NO_LOCALE)),
											StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.Description,
									new DataValue(
											new Variant(new LocalizedText(
													"The maximum number of continuation points for Browse operations per session.",
													LocalizedText.NO_LOCALE)),
											StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.WriteMask, new DataValue(new Variant(new UnsignedInteger(0)),
									StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.UserWriteMask, new DataValue(new Variant(new UnsignedInteger(0)),
									StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.Value, new DataValue(new Variant(new UnsignedInteger(1)), StatusCode.GOOD,
									null, serverTimeStamp));
							put(Attributes.DataType, new DataValue(new Variant(Identifiers.UInt16), StatusCode.GOOD,
									null, serverTimeStamp));
							put(Attributes.ValueRank,
									new DataValue(new Variant(-2), StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.ArrayDimensions,
									new DataValue(null, StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.AccessLevel, new DataValue(new Variant(AccessLevel.CurrentRead),
									StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.UserAccessLevel, new DataValue(new Variant(AccessLevel.CurrentRead),
									StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.MinimumSamplingInterval,
									new DataValue(new Variant(0.0), StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.Historizing,
									new DataValue(new Variant(false), StatusCode.GOOD, null, serverTimeStamp));
						}
					});

			onReadResultsMap.put(Identifiers.Server_ServerDiagnostics_EnabledFlag,
					new HashMap<UnsignedInteger, DataValue>() {
						{
							put(Attributes.NodeId,
									new DataValue(new Variant(Identifiers.Server_ServerDiagnostics_EnabledFlag),
											StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.NodeClass, new DataValue(new Variant(NodeClass.Variable), StatusCode.GOOD,
									null, serverTimeStamp));
							put(Attributes.BrowseName, new DataValue(new Variant(new QualifiedName("EnabledFlag")),
									StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.DisplayName,
									new DataValue(
											new Variant(new LocalizedText("EnabledFlag", LocalizedText.NO_LOCALE)),
											StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.Description,
									new DataValue(
											new Variant(
													new LocalizedText("If TRUE the diagnostics collection is enabled.",
															LocalizedText.NO_LOCALE)),
											StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.WriteMask, new DataValue(new Variant(new UnsignedInteger(0)),
									StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.UserWriteMask, new DataValue(new Variant(new UnsignedInteger(0)),
									StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.Value,
									new DataValue(new Variant(false), StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.DataType, new DataValue(new Variant(Identifiers.Boolean), StatusCode.GOOD,
									null, serverTimeStamp));
							put(Attributes.ValueRank,
									new DataValue(new Variant(-2), StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.ArrayDimensions,
									new DataValue(null, StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.AccessLevel, new DataValue(new Variant(AccessLevel.CurrentRead),
									StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.UserAccessLevel, new DataValue(new Variant(AccessLevel.CurrentRead),
									StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.MinimumSamplingInterval,
									new DataValue(new Variant(0.0), StatusCode.GOOD, null, serverTimeStamp));
							put(Attributes.Historizing,
									new DataValue(new Variant(false), StatusCode.GOOD, null, serverTimeStamp));
						}
					});

			// *******************************************************************************
			// Put all data type mappings in one HashMap for better readability
			// and performance
			// Only boolean supported at the moment
			// *******************************************************************************
			datatypeMap = new HashMap<NodeId, Class<?>>() {
				{
					put(Identifiers.Boolean, java.lang.Boolean.class);
				}
			};
			//////////////////////////////////////
		}

		public void onActivateSession(
				EndpointServiceRequest<ActivateSessionRequest, ActivateSessionResponse> msgExchange)
				throws ServiceFaultException {

			ActivateSessionRequest request = msgExchange.getRequest();

			StatusCode statusCode = null;
			ActivateSessionResponse response = new ActivateSessionResponse();
			ActiveSessionEventParam activeSessionEventParam = new ActiveSessionEventParam();
			activeSessionEventParam
					.setAuthenToken(request.getRequestHeader().getAuthenticationToken().getValue().toString());
			// activeSessionEventParam.setSessionId(request.getRequestHeader().get);
			RequestHeader requestHeader = request.getRequestHeader();
			NodeId authenticationToken = requestHeader.getAuthenticationToken();

			if (!sessions.contains(authenticationToken)) {
				// This session is not valid
				statusCode = new StatusCode(StatusCodes.Bad_SessionClosed);
			}
			if (statusCode == null) {
				final ExtensionObject encodedToken = request.getUserIdentityToken();
				UserIdentityToken token = null;
				if (encodedToken != null) {
					try {
						token = encodedToken.decode(getEncoderContext());
						if (token.getTypeId()
								.equals(new ExpandedNodeId(Identifiers.X509IdentityToken_Encoding_DefaultBinary)))
							statusCode = new StatusCode(StatusCodes.Bad_IdentityTokenInvalid);
					} catch (DecodingException e) {
						statusCode = new StatusCode(StatusCodes.Bad_IdentityTokenInvalid);
					}
				}

				if (timeoutPeriods != null && authenticationToken != null
						&& timeoutPeriods.get(authenticationToken) != null) {

					Long timeToTimeout = timeoutPeriods.get(authenticationToken);

					Long now = System.currentTimeMillis();

					if (timeToTimeout < now) {
						statusCode = new StatusCode(StatusCodes.Bad_SessionClosed);
						validAuthenticationTokens.remove(authenticationToken);
						timeoutPeriods.remove(authenticationToken);

					}
				} else {
					statusCode = new StatusCode(StatusCodes.Bad_SessionIdInvalid);
				}
			}
			if (statusCode == null) {
				try {

					IEncodeable uit = request.getUserIdentityToken().decode(getEncoderContext());
					userAuthorization.put(authenticationToken, uit);

					if (uit instanceof UserNameIdentityToken) {
						UserNameIdentityToken userNameIdentityToken = (UserNameIdentityToken) uit;

						String userName = userNameIdentityToken.getUserName();
						activeSessionEventParam.setUserName(userName);
						String policyId = userNameIdentityToken.getPolicyId();
						String encryptionAlgorithm = userNameIdentityToken.getEncryptionAlgorithm();
						String passkey = GenericUtil.readServerPropertyConfigFile(userName);
						if (userName == null) {
							statusCode = new StatusCode(StatusCodes.Bad_IdentityTokenInvalid);
						} else if (userName.equals("username")) {
							statusCode = new StatusCode(StatusCodes.Bad_UserAccessDenied);
						} else if (GenericUtil.readServerPropertyConfigFile(userName) == null) {
							statusCode = new StatusCode(StatusCodes.Bad_IdentityTokenRejected);
						}

						// Checking that policy id and encryption algorithm are
						// valid.
						// Add all supported policy ids and encryption
						// algorithms here.
						if (policyId == null || !policyId.equals("username_basic128") || encryptionAlgorithm == null
								|| !encryptionAlgorithm.equals("http://www.w3.org/2001/04/xmlenc#rsa-1_5")) {
							statusCode = new StatusCode(StatusCodes.Bad_IdentityTokenInvalid);
						} else if (statusCode == null) {
							// user is user1 or user2, decrypt the password and
							// check password correctness

							PrivateKey pk = application.getApplicationInstanceCertificate().privateKey.getPrivateKey();
							ByteString dataToDecrypt = userNameIdentityToken.getPassword();
							// SunJceCryptoProvider needs buffer of at least 256
							// bytes
							byte[] output = new byte[256];
							int outputOffset = 0;

							CryptoUtil.getCryptoProvider().decryptAsymm(pk, SecurityAlgorithm.Rsa15,
									dataToDecrypt.getValue(), output, outputOffset);

							int count = 11; //
							// passwords that are 8
							// characters...
							String plaintextPassword = new String(output, 1, count).trim();

							// These usernames and passwords are defined in CTT
							// GET UserName and Password from serverConfig FIle.

							// settings

							if ((!plaintextPassword.equals(passkey))) {
								statusCode = new StatusCode(StatusCodes.Bad_UserAccessDenied);
							}

						}
					}
				} catch (DecodingException e) {
					e.printStackTrace();
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}

			}
			response.setServerNonce(CryptoUtil.createNonce(32));

			if (statusCode == null) {
				statusCode = StatusCode.GOOD;
				validAuthenticationTokens.add(authenticationToken);

			}
			ResponseHeader h = new ResponseHeader(DateTime.currentTime(), requestHeader.getRequestHandle(), statusCode,
					null, getApplication().getLocaleIds(), null);
			response.setResponseHeader(h);

			if (GenericUtil.enableAuditing("onActivateSession")) {

				InetSocketAddress proxyAddress = (InetSocketAddress) msgExchange.getChannel().getConnection()
						.getRemoteAddress();
				activeSessionEventParam.setIpAddress(proxyAddress.getAddress().getHostAddress());
				activeSessionEventParam.setPort(proxyAddress.getPort());

				EventModel eventModel = new EventModel("onActivateSession", activeSessionEventParam);
				EventLogUtil.writeToServerEventLog(eventModel);
			}

			msgExchange.sendResponse(response);
		}

		public void onCancel(EndpointServiceRequest<CancelRequest, CancelResponse> msgExchange)
				throws ServiceFaultException {
			CancelResponse response = new CancelResponse();
			ResponseHeader h = new ResponseHeader(DateTime.currentTime(), msgExchange.getRequest().getRequestHandle(),
					new StatusCode(StatusCodes.Bad_NotSupported), null, getApplication().getLocaleIds(), null);
			response.setResponseHeader(h);

			msgExchange.sendResponse(response);
		}

		public void onCloseSession(EndpointServiceRequest<CloseSessionRequest, CloseSessionResponse> msgExchange)
				throws ServiceFaultException {
			CloseSessionResponse res = new CloseSessionResponse();
			CloseSessionRequest req = msgExchange.getRequest();

			ResponseHeader h = checkRequestHeader(req.getRequestHeader());

			// take authentication token out of valid tokens
			validAuthenticationTokens.remove(req.getRequestHeader().getAuthenticationToken());
			sessions.remove(req.getRequestHeader().getAuthenticationToken());
			userAuthorization.remove(req.getRequestHeader().getAuthenticationToken());
			// Set continuation point to null, this also means that more than
			// one concurrent sessions cannot use continuation points
			continuationPoint = null;

			res.setResponseHeader(h);

			msgExchange.sendResponse(res);
		}

		public void onCreateSession(EndpointServiceRequest<CreateSessionRequest, CreateSessionResponse> msgExchange)
				throws ServiceFaultException {
			CreateSessionRequest request = msgExchange.getRequest();
			CreateSessionResponse response = new CreateSessionResponse();
			StatusCode statusCode = null;
			byte[] token = new byte[32];
			byte[] nonce = new byte[32];
			Random r = new Random();
			r.nextBytes(nonce);
			r.nextBytes(token);

			// Check client nonce
			ByteString clientNonce = request.getClientNonce();
			if (clientNonce != null) {
				if (clientNonce.getLength() < 32) {
					statusCode = new StatusCode(StatusCodes.Bad_NonceInvalid);
				}
			}
			ByteString clientCertificate = request.getClientCertificate();
			if (clientCertificate != null) {
				String clientApplicationUri = request.getClientDescription().getApplicationUri();
				X509Certificate clientCertificateDecoded = null;
				String applicationUriOfDecoded = null;
				try {
					clientCertificateDecoded = CertificateUtils.decodeX509Certificate(clientCertificate.getValue());
					applicationUriOfDecoded = CertificateUtils.getApplicationUriOfCertificate(clientCertificateDecoded);
				} catch (CertificateException e) {
					e.printStackTrace();
				}

				if (!clientApplicationUri.equals(applicationUriOfDecoded)) {
					statusCode = new StatusCode(StatusCodes.Bad_CertificateUriInvalid);
				}
			}

			if (statusCode == null) {

				EndpointConfiguration endpointConfiguration = EndpointConfiguration.defaults();
				response.setMaxRequestMessageSize(UnsignedInteger.valueOf(Math.max(
						endpointConfiguration.getMaxMessageSize(), request.getMaxResponseMessageSize().longValue())));
				Double timeout = new Double(60 * 1000);
				if (!request.getRequestedSessionTimeout().equals(new Double(0))) {
					// set revised session timeout to 60 seconds or to lower
					// value if client requests
					timeout = Math.min(request.getRequestedSessionTimeout(), 60 * 1000);
				}
				response.setRevisedSessionTimeout(timeout);

				NodeId tokenId = new NodeId(0, token);
				response.setAuthenticationToken(tokenId);
				// Put authentication to memory in order to check validity of
				// incoming authentication tokens
				sessions.add(tokenId);
				Long time = System.currentTimeMillis();
				timeoutPeriods.put(tokenId, time + timeout.longValue());
			}

			KeyPair cert = getApplication().getApplicationInstanceCertificates()[0];
			response.setServerCertificate(ByteString.valueOf(cert.getCertificate().getEncoded()));
			response.setServerEndpoints(this.getEndpointDescriptions());
			response.setServerNonce(ByteString.valueOf(nonce));

			SecurityPolicy securityPolicy = msgExchange.getChannel().getSecurityPolicy();
			response.setServerSignature(getServerSignature(clientCertificate, clientNonce, securityPolicy,
					cert.getPrivateKey().getPrivateKey()));

			response.setServerSoftwareCertificates(getApplication().getSoftwareCertificates());
			response.setSessionId(new NodeId(0, "Session-" + UUID.randomUUID()));

			if (statusCode == null) {
				statusCode = StatusCode.GOOD;

			}
			// Set response header
			ResponseHeader h = new ResponseHeader(DateTime.currentTime(), request.getRequestHeader().getRequestHandle(),
					statusCode, null, getApplication().getLocaleIds(), null);
			response.setResponseHeader(h);

			if (GenericUtil.enableAuditing("onCreateSession")) {

				CreateSessionEventParam createSessionObject = EventLogUtil.populateCreateSessionObject(
						response.getSessionId(), response.getAuthenticationToken(), request, response);

				InetSocketAddress proxyAddress = (InetSocketAddress) msgExchange.getChannel().getConnection()
						.getRemoteAddress();
				createSessionObject.setIpAddress(proxyAddress.getAddress().getHostAddress());
				createSessionObject.setPort(proxyAddress.getPort());

				EventModel eventModel = new EventModel("onCreateSession", createSessionObject);
				EventLogUtil.writeToServerEventLog(eventModel);
			}

			msgExchange.sendResponse(response);
		}

		private SignatureData getServerSignature(ByteString clientCertificate, ByteString clientNonce,
				SecurityPolicy securityPolicy, final RSAPrivateKey privateKey) throws ServiceFaultException {
			if (clientCertificate != null) {
				ByteArrayOutputStream s = new ByteArrayOutputStream();
				try {
					s.write(clientCertificate.getValue());
				} catch (IOException e) {
					throw new ServiceFaultException(
							ServiceFault.createServiceFault(StatusCodes.Bad_SecurityChecksFailed));
				} catch (Exception e) {
					throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NonceInvalid));
				}
				try {
					s.write(clientNonce.getValue());
				} catch (IOException e) {
					throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NonceInvalid));
				} catch (Exception e) {
					throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NonceInvalid));
				}
				try {
					SecurityAlgorithm algorithm = securityPolicy.getAsymmetricSignatureAlgorithm();
					if (algorithm == null) {
						algorithm = SecurityAlgorithm.RsaSha1;
					}
					return new SignatureData(algorithm.getUri(), ByteString
							.valueOf(CryptoUtil.getCryptoProvider().signAsymm(privateKey, algorithm, s.toByteArray())));
				} catch (ServiceResultException e) {
					throw new ServiceFaultException(e);
				}
			}
			return null;
		}

		public void onCall(EndpointServiceRequest<CallRequest, CallResponse> callRequest) throws ServiceFaultException {
			throw new ServiceFaultException(ServiceFault.createServiceFault(StatusCodes.Bad_NotImplemented));
			/*
			 * CallResponse response = new CallResponse(); CallMethodRequest[]
			 * reqs = callRequest.getRequest().getMethodsToCall();
			 * CallMethodResult[] results = new CallMethodResult[ reqs.length ];
			 * 
			 * for (int i=0; i<reqs.length; i++) { CallMethodRequest req =
			 * reqs[i]; CallMethodResult result = results[i] = new
			 * CallMethodResult();
			 * 
			 * NodeId methodId = req.getMethodId(); if (
			 * LIST_SOLVERS.equals(methodId) ) {
			 * 
			 * 
			 * Variant[] solvers = req.getInputArguments(); Variant
			 * solversInVariant = new Variant( solvers );
			 * 
			 * 
			 * result.setOutputArguments( new Variant[] {solversInVariant} );
			 * result.setStatusCode( StatusCode.GOOD ); }
			 * 
			 * else { // Unknown method result.setStatusCode( new StatusCode(
			 * StatusCodes.Bad_MethodInvalid ) ); }
			 * 
			 * }
			 * 
			 * for(CallMethodRequest req:reqs){
			 * 
			 * NodeId methodId = req.getMethodId();
			 * if(LIST_SOLVERS.equals(methodId)){
			 * 
			 * Variant[] inputArguments = req.getInputArguments(); for(Variant
			 * v: inputArguments){
			 * 
			 * } }
			 * 
			 * }
			 * 
			 * response.setResults( results );
			 * callRequest.sendResponse(response);
			 */

		}

	}

	/**
	 * Check request header contents and return response header accordingly.
	 *
	 * @param requestHeader
	 *            the request header to check.
	 * @return ResponseHeader
	 * @throws UnknownHostException
	 * @throws SocketException
	 *
	 */
	public static ResponseHeader checkRequestHeader(RequestHeader requestHeader) {

		// set responseheader to StatusCode.GOOD by default.
		ResponseHeader responseHeader = new ResponseHeader(DateTime.currentTime(), requestHeader.getRequestHandle(),
				StatusCode.GOOD, null, null, null);

		if (NodeId.isNull(requestHeader.getAuthenticationToken())
				|| !validAuthenticationTokens.contains(requestHeader.getAuthenticationToken())) {
			// AuthenticationToken was null or invalid
			if (sessions.contains(requestHeader.getAuthenticationToken())) {
				// Session is created but not activated
				responseHeader = new ResponseHeader(DateTime.currentTime(), requestHeader.getRequestHandle(),
						new StatusCode(StatusCodes.Bad_SessionNotActivated), null, null, null);
				// This is an error condition: close this session
				sessions.remove(requestHeader.getAuthenticationToken());
			} else {
				responseHeader = new ResponseHeader(DateTime.currentTime(), requestHeader.getRequestHandle(),
						new StatusCode(StatusCodes.Bad_SessionIdInvalid), null, null, null);
			}

		} else if (requestHeader.getTimestamp().equals(new DateTime(0))) {
			// Timestamp is now only checked with value 0
			// TimeStamp was not valid
			responseHeader = new ResponseHeader(DateTime.currentTime(), requestHeader.getRequestHandle(),
					new StatusCode(StatusCodes.Bad_InvalidTimestamp), null, null, null);

		}
		return responseHeader;
	}

	public static class ChartSchedulerTask extends TimerTask {

		/**
		 * Implements TimerTask's abstract run method.
		 */
		@Override
		public void run() {
			System.out.println("..............................Testing Scheduler...................................");

		}

	}

	private static Date getTomorrowMorning1am() {
		Calendar tomorrow = new GregorianCalendar();
		tomorrow.add(Calendar.DATE, 1);

		Calendar result = new GregorianCalendar(tomorrow.get(Calendar.YEAR), tomorrow.get(Calendar.MONTH),
				tomorrow.get(Calendar.DATE), 1, 0);

		return result.getTime();
	}

	/***
	 * Starting point of the application server.
	 * 
	 * @throws Exception
	 */

	public static void start() throws Exception {

		String applicationName = "TimeSyncServer";
		Locale myLocale = new Locale("en");
		LocalizedText myApplicationDescription = new LocalizedText(applicationName, myLocale);

		Application myServerApplication = new Application();
		myServerApplication.setApplicationName(myApplicationDescription);
		myServerApplication.setProductUri("urn:opcfoundation.org:OPCUA:" + applicationName);

		// set custom application URI and not default randomUUID
		String publicHostname = "";
		try {
			publicHostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		myServerApplication.setApplicationUri("urn:" + publicHostname + ":" + applicationName);

		roverServer = new RoverAServerExample(myServerApplication, applicationName);

		roverServer.addServiceHandler(new RoverAttributeServiceHandler());
		roverServer.addServiceHandler(new FindServersServiceHandler());
		roverServer.addServiceHandler(new RoverNodemanagementServiceHandler());

		CryptoUtil.setCryptoProvider(new BcCryptoProvider());

		/***
		 * chartSchedulerTask will get all the values from country hashMap and
		 * store it in chartLog file.
		 */

		TimerTask chartSchedulerTask = new RoverAServer.ChartSchedulerTask();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartSchedulerTask, getTomorrowMorning1am(), 1000 * 60 * 60 * 24);

		logger.info("Type \"exit\" to shutdown the application");

		Scanner scan = new Scanner(System.in);

		for (;;) {
			String myLine = scan.nextLine();
			if ("exit".equals(myLine)) {
				break;
			}
		}

		scan.close();
		roverServer.getApplication().close();
		// ////////////////////////////////////

	}

	public static void main(String[] args) throws Exception {
		start();
	}
}