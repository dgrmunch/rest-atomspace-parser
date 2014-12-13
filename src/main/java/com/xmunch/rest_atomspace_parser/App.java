package com.xmunch.rest_atomspace_parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class App {

	private static Scanner scanInput;
	private static FileReader fileReader;
	private static BufferedReader bufferReader;

	private static final Integer SUCCESS = 200;
	private static final String ACCEPT = "accept";
	private static final String SERVER = "http://localhost:8080/";
	private static final String CREATE_VERTEX = "vertex/create/?";
	private static final String CREATE_EDGE = "edge/create/?";
	private static final String INPUT_FILE = "input.xa";
	private static final String JSON = "application/json";
	private static final String ERROR = "Failed : HTTP error code : ";
	private static final String EMPTY = "";
	private static final String SPACE = " ";
	private static final String LABEL = "label";
	private static final String EQUALS = "=";
	private static final String AND = "&";
	private static final String TYPE = "type";
	private static final String TO = "to";
	private static final String FROM = "from";
	private static final Object IS_A = "is_a";
	private static final String COLON = ":";

	public static void main(String[] args) {	
		String item = EMPTY;
		String data = EMPTY;
		
		try {
			fileReader = new FileReader(INPUT_FILE);
			bufferReader = new BufferedReader(fileReader);

			while ((item = bufferReader.readLine()) != null) {
				atomSpaceRequest(item);
			}

			bufferReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			scanInput = new Scanner(System.in);
			data = scanInput.nextLine();
			atomSpaceRequest(data);
		}

	}

	private static Boolean atomSpaceRequest(String text) {
		Boolean result = false;
		
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(SERVER + restApiParser(text));
			getRequest.addHeader(ACCEPT, JSON);

			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != SUCCESS) {
				throw new RuntimeException(ERROR
						+ response.getStatusLine().getStatusCode());
			}

			httpClient.getConnectionManager().shutdown();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private static String restApiParser(String text) {
		String parsedText = EMPTY;
		String[] splittedText = text.split(SPACE);

		if (splittedText[1].equals(IS_A)) {
			parsedText += CREATE_VERTEX + LABEL + EQUALS + splittedText[0]
					+ AND + TYPE + EQUALS + splittedText[2];

		} else {
			parsedText += CREATE_EDGE + LABEL + EQUALS + splittedText[1] + AND
					+ FROM + EQUALS + splittedText[0] + AND + TO + EQUALS
					+ splittedText[2];
		}

		if (splittedText.length > 3) {
			parsedText += AND;
			for (int i = 3; i < splittedText.length; i++) {
				if (splittedText[i].split(COLON).length == 2) {
					parsedText += splittedText[i];
				}
			}
		}

		return parsedText;
	}

}