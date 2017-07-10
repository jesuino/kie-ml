package org.fxapps.ml.services.resources;

import static org.kie.server.api.rest.RestURI.CONTAINER_ID;
import static org.kie.server.remote.rest.common.util.RestUtils.buildConversationIdHeader;
import static org.kie.server.remote.rest.common.util.RestUtils.createCorrectVariant;
import static org.kie.server.remote.rest.common.util.RestUtils.getContentType;
import static org.kie.server.remote.rest.common.util.RestUtils.getVariant;
import static org.kie.server.remote.rest.common.util.RestUtils.internalServerError;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import javax.ws.rs.core.Response.Status;

import org.fxapps.ml.api.KieMLConstants;
import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.Model;
import org.fxapps.ml.api.model.ModelList;
import org.fxapps.ml.api.model.Result;
import org.fxapps.ml.services.KieMLServicesBase;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.remote.rest.common.Header;
import org.kie.server.services.api.KieServerRegistry;
import org.kie.server.services.impl.marshal.MarshallerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("server/")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class KieMLResource {
	
	public static final Logger logger = LoggerFactory.getLogger(KieMLResource.class);

	private KieMLServicesBase kieMLServicesBase;
	private KieServerRegistry context;

	private MarshallerHelper marshallerHelper;

	public KieMLResource(KieMLServicesBase kieMLServicesBase) {
		this.kieMLServicesBase = kieMLServicesBase;
		this.context = kieMLServicesBase.getContext();
		context.getContainers().forEach(c -> c.getResource().getReleaseId());
		this.marshallerHelper = new MarshallerHelper(context);
	}

	@GET
	@Path(KieMLConstants.URI_GET_MODELS)
	public Response getModels(@PathParam(CONTAINER_ID) String containerId) {
		Response response;
		try {
			ServiceResponse<ModelList> result = kieMLServicesBase.getModels(containerId);
			if (result.getType() == ServiceResponse.ResponseType.SUCCESS) {
				response = Response.ok(result).build();
			} else {
				response = Response.status(Status.NOT_FOUND).build();
			}
		} catch (Exception e) {
			logger.warn("Unexpected error retrieving Model List. Message: '{}'", e.getMessage(), e);
			response = Response.serverError().entity("Error retrieving model list: " + e.getMessage()).build();
		}
		return response;
	}

	@GET
	@Path(KieMLConstants.URI_GET_MODEL)
	public Response getModel(@javax.ws.rs.core.Context HttpHeaders headers, @PathParam(CONTAINER_ID) String containerId,
			@PathParam(KieMLConstants.PARAM_MODEL_ID) String modelId) {
		Variant v = getVariant(headers);
		Header conversationIdHeader = buildConversationIdHeader(containerId, context, headers);
		try {
			ServiceResponse<Model> result = kieMLServicesBase.getModel(containerId, modelId);
			if (result.getType() == ServiceResponse.ResponseType.SUCCESS) {
				return createCorrectVariant(marshallerHelper, containerId, result, headers, Response.Status.OK,
						conversationIdHeader);
			}
			return createCorrectVariant(marshallerHelper, containerId, result, headers, Response.Status.NOT_FOUND,
					conversationIdHeader);
		} catch (Exception e) {
			logger.error("Unexpected error retrieving Model List. Message: '{}'", e.getMessage(), e);
			return internalServerError("Unexpected error retrieving model list: " + e.getMessage(), v,
					conversationIdHeader);
		}
	}
	
	@GET
	@Path(KieMLConstants.URI_KIEML_CONTAINERS)
	public Response listContainers(){
		Response response;		
		try {
			ServiceResponse<KieContainerResourceList> result = kieMLServicesBase.listContainers();
			response =  Response.ok(result).build();
		} catch (Exception e) {
			logger.warn("Unexpected error retrieving container List. Message: '{}'", e.getMessage(), e);
			response = Response.serverError().entity("Unexpected error retrieving container list: " + e.getMessage()).build();
		}
		return response;
    }	

	@POST
	@Path(KieMLConstants.URI_PREDICTION)
	@Consumes({  MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response predict(@javax.ws.rs.core.Context HttpHeaders headers, @PathParam(CONTAINER_ID) String containerId,
			@PathParam(KieMLConstants.PARAM_MODEL_ID) String modelId, String inputPayload) {
		Variant v = getVariant(headers);
		String contentType = getContentType(headers);
		Header conversationIdHeader = buildConversationIdHeader(containerId, context, headers);
		try {
			Input input = marshallerHelper.unmarshal(containerId, inputPayload, contentType, Input.class);
			ServiceResponse<Result> result = kieMLServicesBase.predict(containerId, modelId, input);
			return createCorrectVariant(marshallerHelper, containerId, result, headers, Response.Status.OK,
					conversationIdHeader);
		} catch (Exception e) {
			logger.error("Unexpected error running prediction. Message: '{}'", e.getMessage(), e);
			return internalServerError("Unexpected error running prediction: " + e.getMessage(), v,
					conversationIdHeader);
		}
	}

}
