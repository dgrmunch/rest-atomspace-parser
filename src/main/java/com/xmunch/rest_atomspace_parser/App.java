package com.xmunch.rest_atomspace_parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class App extends AppDescription{

	final static Logger logger = Logger.getLogger(App.class);
	
	public static void main(String[] args) {	
		String item = EMPTY;
		String data = EMPTY;
		
		BasicConfigurator.configure();
		
		try {
			fileReader = new FileReader(INPUT_FILE);
			bufferReader = new BufferedReader(fileReader);

			while ((item = bufferReader.readLine()) != null) {
				logger.info(item);
				atomSpaceRequest(item);
			}

			bufferReader.close();

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
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
			String parsedText = restApiParser(text);
			HttpGet getRequest = new HttpGet(SERVER + parsedText);
			getRequest.addHeader(ACCEPT, JSON);

			HttpResponse response = httpClient.execute(getRequest);
			logger.info(SERVER + parsedText);
			logger.info(SUCCESS);

			if (response.getStatusLine().getStatusCode() == SUCCESS) {
				logger.info(SUCCESS);
				result = true;
			} else {
				logger.error(ERROR + response.getStatusLine().getStatusCode());
			}

			httpClient.getConnectionManager().shutdown();
			
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
			parsedText += AND + PARAMS + EQUALS;
			for (int i = 3; i < splittedText.length; i++) {
				if (splittedText[i].split(COLON).length == 2) {
					parsedText += splittedText[i];
				} else {
					logger.error(PARAM_PROBLEM+splittedText[i]);
				}
			}
		}

		return parsedText;
	}

}