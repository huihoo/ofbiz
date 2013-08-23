package org.ofbiz.daniel.exercise;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.ofbiz.base.json.JSON;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.ModelService;

public class Events {
	public static final String module = Events.class.getName();

	/*
	 * public static String createPersonJavaEvent(HttpServletRequest request,
	 * HttpServletResponse response) { LocalDispatcher dispatcher =
	 * (LocalDispatcher) request.getAttribute("dispatcher"); GenericDelegator
	 * delegator = (GenericDelegator) request.getAttribute("delegator");
	 * GenericValue userLogin = (GenericValue)
	 * request.getSession().getAttribute("userLogin"); String salutation =
	 * (String) request.getParameter("salutation"); String firstName = (String)
	 * request.getParameter("firstName"); String middleName = (String)
	 * request.getParameter("middleName"); String lastName = (String)
	 * request.getParameter("lastName"); String suffix = (String)
	 * request.getParameter("suffix");
	 * 
	 * try { // Map createPartyCtx = UtilMisc.toMap("partyTypeId", "PERSON"); //
	 * GenericValue party = delegator.makeValue("party", // createPartyCtx); //
	 * party = party.create(); // String partyId = party.getString("partyId");
	 * Map createPersonCtx = UtilMisc.toMap("salutation", salutation,
	 * "firstName", firstName, "middleName", middleName, "lastName", lastName,
	 * "suffix", suffix, "userLogin", userLogin); // GenericValue person =
	 * delegator.makeValue("person", // createPersonCtx); // person.create(); //
	 * person.create(); // delegator.createSetNextSeqId(person); Map person =
	 * dispatcher.runSync("createPerson", createPersonCtx); // Connection
	 * connection = //
	 * ConnectionFactory.getConnection(delegator.getGroupHelperInfo
	 * (person.getEntityName())); //
	 * connection.prepareStatement("INSERT INTO party values()")
	 * 
	 * } catch (Exception e) { Debug.logError(e.toString(), module); return
	 * "error"; } return "success"; }
	 */
	/**
	 * Creates a Person. If no partyId is specified a numeric partyId is
	 * retrieved from the Party sequence.
	 * 
	 * @param ctx
	 *            The DispatchContext that this service is operating in.
	 * @param context
	 *            Map containing the input parameters.
	 * @return Map with the result of the service, the output parameters.
	 */
	public static Map<String, Object> createPersonJavaEvent(DispatchContext ctx, Map<String, ? extends Object> context) {
		Map<String, Object> result = FastMap.newInstance();
		JSON.
		Delegator delegator = ctx.getDelegator();
		Timestamp now = UtilDateTime.nowTimestamp();
		List<GenericValue> toBeStored = FastList.newInstance();
		// in most cases userLogin will be null, but get anyway so we can keep
		// track of that info if it is available
		GenericValue userLogin = (GenericValue) context.get("userLogin");
		String partyId = (String) context.get("partyId");

		if (UtilValidate.isEmpty(partyId)) {
			try {
				partyId = delegator.getNextSeqId("Party");
			} catch (IllegalArgumentException e) {
			}
		}
		// check to see if party object exists, if so make sure it is PERSON
		// type party
		GenericValue party = null;

		String statusId = (String) context.get("statusId");
		if (statusId == null) {
			statusId = "PARTY_ENABLED";
		}
		Map<String, Object> newPartyMap = UtilMisc.toMap("partyId", partyId, "partyTypeId", "PERSON", "createdDate", now, "lastModifiedDate", now,
				"statusId", statusId);
		String preferredCurrencyUomId = (String) context.get("preferredCurrencyUomId");
		if (!UtilValidate.isEmpty(preferredCurrencyUomId)) {
			newPartyMap.put("preferredCurrencyUomId", preferredCurrencyUomId);
		}
		String externalId = (String) context.get("externalId");
		if (!UtilValidate.isEmpty(externalId)) {
			newPartyMap.put("externalId", externalId);
		}
		if (userLogin != null) {
			newPartyMap.put("createdByUserLogin", userLogin.get("userLoginId"));
			newPartyMap.put("lastModifiedByUserLogin", userLogin.get("userLoginId"));
		}
		party = delegator.makeValue("Party", newPartyMap);
		toBeStored.add(party);
		GenericValue statusRec = delegator.makeValue("PartyStatus", UtilMisc.toMap("partyId", partyId, "statusId", statusId, "statusDate", now));
		toBeStored.add(statusRec);

		GenericValue person = delegator.makeValue("Person", UtilMisc.toMap("partyId", partyId));
		person.setNonPKFields(context);
		toBeStored.add(person);

		try {
			delegator.storeAll(toBeStored);
		} catch (GenericEntityException e) {
		}

		result.put("partyId", partyId);
		result.put(ModelService.RESPONSE_MESSAGE, ModelService.RESPOND_SUCCESS);
		return result;
	}

	public static String updatePerson(HttpServletRequest request, HttpServletResponse response) {
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		String partyId = (String) request.getParameter("partyId");
		String salutation = (String) request.getParameter("salutation");
		String firstName = (String) request.getParameter("firstName");
		String middleName = (String) request.getParameter("middleName");
		String lastName = (String) request.getParameter("lastName");
		String suffix = (String) request.getParameter("suffix");
		Map updatePersonCtx = UtilMisc.toMap("partyId", partyId);
		try {
			GenericValue person = delegator.findByPrimaryKey("Person", updatePersonCtx);
			person.put("salutation", salutation);
			person.put("firstName", firstName);
			person.put("middleName", middleName);
			person.put("lastName", lastName);
			person.put("suffix", suffix);
			person.store();
		} catch (GenericEntityException e) {
			Debug.logError(e.toString(), module);
			return "error";
		}
		return "success";
	}
}
