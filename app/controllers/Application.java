package controllers;

import play.*;
import play.mvc.*;

import views.html.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import com.bloomberglp.blpapi.*;
import javax.json.*;
import javax.json.stream.*;

public class Application extends Controller {
	protected static JsonArray historicalData;
	protected static JsonObject realTimeData;
	protected static Session session;

	public static Result index() throws Exception {
		historicalData = null;
		realTimeData = null;
		startSession();
		return ok(index.render());
	}

	public static String printHistorical() {
		return historicalData.toString();
	}

	public static String printRealTime() {
		return realTimeData.toString();
	}

	public static void startSession() throws Exception {
		SessionOptions options = new SessionOptions();
		options.setServerHost("10.8.8.1");
		options.setServerPort(8194);
		session = new Session(options);
		if (!session.start()) {
			System.out.println("Could not start session.");
			System.exit(1);
		}
		if (!session.openService("//blp/refdata")) {
			System.out.println("Could not open service " + "//blp/refdata");
			System.exit(1);
		}
		// PE : PE_RATIO
		// RETURN_ON_ASSET
		// DIVIDENED_YIELD
		// TOT_DEBT_TO_TOT_EQTY
		// GROSS_MARGIN
		// VOLUME_AVG_(30,10,20);
		// EQY_BETA
		// PROF_MARGIN
		// CUR_MKT_CAP
		CorrelationID requestID = new CorrelationID(1);
		Service refDataSvc = session.getService("//blp/refdata");
		Request request = refDataSvc.createRequest("HistoricalDataRequest");
		request.append("securities", "MSFT US EQUITY");
		// request.append("fields", "PX_LAST");
		request.append("fields", "OPEN");
		request.set("startDate", "20091231");
		request.set("endDate", "20101231");
		request.set("periodicitySelection", "DAILY");
		// Request request = refDataSvc.createRequest("ReferenceDataRequest");
		// request.append("securities", choice);
		// request.append("fields", "PX_LAST");
		session.sendRequest(request, requestID);
		boolean continueToLoop = true;

		// following loop handles the response from the bloomberg server if it
		// comes in parts.
		while (continueToLoop) {
			Event event = session.nextEvent();
			switch (event.eventType().intValue()) {
			case Event.EventType.Constants.RESPONSE: // final eventi
				continueToLoop = false; // fall through
			case Event.EventType.Constants.PARTIAL_RESPONSE:
				handleHistoricalEvent(event);
				break;
			default:
				handleOtherEvent(event);
				break;
			}
			queryRealTime();
		}
	}

	public static void queryRealTime() throws Exception {

		CorrelationID requestID = new CorrelationID(2);
		Service refDataSvc = session.getService("//blp/refdata");
		Request request = refDataSvc.createRequest("ReferenceDataRequest");
		request.append("securities", "MSFT US EQUITY");
		request.append("fields", "PX_LAST");
		request.append("fields", "OPEN");
		request.append("fields", "PE_RATIO");
		request.append("fields", "RETURN_ON_ASSET");
		request.append("fields", "DIVIDEND_YIELD");
		// request.append("fields", "TOT_DEBT_TO_TOT_EQTY");
		request.append("fields", "GROSS_MARGIN");
		// request.append("fields", "VOLUME_AVG_D10");
		// request.append("fields", "EQY_BETA");
		request.append("fields", "PROF_MARGIN");
		request.append("fields", "CUR_MKT_CAP");
		session.sendRequest(request, requestID);
		boolean continueToLoop = true;
		// following loop handles the response from the bloomberg server if it
		// comes in parts.
		while (continueToLoop) {
			Event event = session.nextEvent();
			switch (event.eventType().intValue()) {
			case Event.EventType.Constants.RESPONSE: // final eventi
				continueToLoop = false; // fall through
			case Event.EventType.Constants.PARTIAL_RESPONSE:
				handleRealTimeEvent(event);
				break;
			default:
				handleOtherEvent(event);
				break;
			}
		}
	}

	private static void handleHistoricalEvent(Event event) throws Exception {
		// System.out.println("EventType =" + event.eventType());
		MessageIterator iter = event.messageIterator();

		while (iter.hasNext()) {
			Message message = iter.next();
			// System.out.println("correlationID=" + message.correlationID());
			// System.out.println("messageType =" + message.messageType());
			Element securityData = message.getElement("securityData");
			if (securityData.hasElement("fieldData")) {
				Element fieldData = securityData.getElement("fieldData");
				JsonArrayBuilder builder = Json.createArrayBuilder();
				//JsonObjectBuilder debug = Json.createObjectBuilder();
				//debug.add("DEBUG",message.toString());
				//builder.add(debug);
				for (int i = 0; i < fieldData.numValues(); i++) {
					JsonObjectBuilder objBuilder = Json.createObjectBuilder();
					Element item = fieldData.getValueAsElement(i);
					if (item.hasElement("date")) {
						objBuilder.add("date", item.getElementAsDate("date")
								.toString());
					}
					if (item.hasElement("OPEN")) {
						objBuilder
								.add("OPEN", item.getElementAsFloat64("OPEN"));
					}
					// hasElement("value")
					builder.add(objBuilder);
				}
				JsonArray jarray = builder.build();
				historicalData = jarray;
				message.print(System.out);
			}
		}
	}

	private static void handleRealTimeEvent(Event event) throws Exception {
		// System.out.println("EventType =" + event.eventType());
		MessageIterator iter = event.messageIterator();

		while (iter.hasNext()) {

			Message message = iter.next();
			// System.out.println("correlationID=" + message.correlationID());
			// System.out.println("messageType =" + message.messageType());
			Element securityData = message.getElement("securityData");
			JsonObjectBuilder objBuilder = Json.createObjectBuilder();
			objBuilder.add("DATA", message.toString());
			JsonObject jObject = objBuilder.build();
			realTimeData = jObject;
			/*
			 * Element fieldData = securityData.getElement("fieldData"); //
			 * JsonArray jArray = new JsonArray(); JsonArrayBuilder builder =
			 * Json.createArrayBuilder(); // JsonObject object =
			 * Json.createObjectBuilder(); for (int i = 0; i <
			 * fieldData.numValues(); i++) { JsonObjectBuilder objBuilder =
			 * Json.createObjectBuilder(); Element item =
			 * fieldData.getValueAsElement(i); if (item.hasElement("PE_RATIO"))
			 * { objBuilder.add("PE_RATIO",
			 * item.getElementAsFloat64("PE_RATIO")); } if
			 * (item.hasElement("RETURN_ON_ASSET")) {
			 * objBuilder.add("RETURN_ON_ASSET",
			 * item.getElementAsFloat64("RETURN_ON_ASSET")); } if
			 * (item.hasElement("DIVDEND_YIELD")) {
			 * objBuilder.add("DIVDEND_YIELD",
			 * item.getElementAsFloat64("DIVDEND_YIELD")); } if
			 * (item.hasElement("GROSS_MARGIN")) {
			 * objBuilder.add("GROSS_MARGIN",
			 * item.getElementAsFloat64("GROSS_MARGIN")); } if
			 * (item.hasElement("PROF_MARGIN")) { objBuilder.add("PROF_MARGIN",
			 * item.getElementAsFloat64("PROF_MARGIN")); } if
			 * (item.hasElement("CUR_MKT_CAP")) { objBuilder.add("CUR_MKT_CAP",
			 * item.getElementAsFloat64("CUR_MKT_CAP")); } }
			 */
		}
	}

	private static void handleOtherEvent(Event event) throws Exception {
		System.out.println("EventType=" + event.eventType());
		MessageIterator iter = event.messageIterator();
		while (iter.hasNext()) {
			Message message = iter.next();
			System.out.println("correlationID=" + message.correlationID());
			System.out.println("messageType=" + message.messageType());
			message.print(System.out);
			if (Event.EventType.Constants.SESSION_STATUS == event.eventType()
					.intValue()
					&& "SessionTerminated" == message.messageType().toString()) {
				System.out.println("Terminating: " + message.messageType());
				System.exit(1);
			}
		}
	}

}
