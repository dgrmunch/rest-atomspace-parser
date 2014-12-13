package com.xmunch.rest_atomspace_parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class App {
	public static Scanner scanInput;
	public static FileReader fileReader;
	public static BufferedReader bufferReader;

	final static Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) {
		String item = AppDescription.EMPTY;
		String data = AppDescription.EMPTY;

		BasicConfigurator.configure();

		try {
			fileReader = new FileReader(AppDescription.INPUT_FILE);
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
			HttpGet getRequest = new HttpGet(AppDescription.SERVER + parsedText);
			getRequest.addHeader(AppDescription.ACCEPT, AppDescription.JSON);

			HttpResponse response = httpClient.execute(getRequest);
			logger.info(AppDescription.SERVER + parsedText);
			logger.info(AppDescription.SUCCESS);

			if (response.getStatusLine().getStatusCode() == AppDescription.SUCCESS) {
				logger.info(AppDescription.SUCCESS);
				result = true;
			} else {
				logger.error(AppDescription.ERROR
						+ response.getStatusLine().getStatusCode());
			}

			httpClient.getConnectionManager().shutdown();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private static String restApiParser(String text) {
		String parsedText = AppDescription.EMPTY;
		String[] splittedText = text.split(AppDescription.SPACE);

		if (splittedText[1].equals(AppDescription.IS_A)) {
			parsedText += 
					  AppDescription.CREATE_VERTEX 
					+ AppDescription.LABEL
					+ AppDescription.EQUALS
					+ splittedText[0]
					+ AppDescription.AND
					+ AppDescription.TYPE
					+ AppDescription.EQUALS
					+ splittedText[2];

		} else {
			parsedText += 
					  AppDescription.CREATE_EDGE 
					+ AppDescription.LABEL
					+ AppDescription.EQUALS 
					+ splittedText[1]
					+ AppDescription.AND 
					+ AppDescription.FROM
					+ AppDescription.EQUALS 
					+ splittedText[0]
					+ AppDescription.AND 
					+ AppDescription.TO
					+ AppDescription.EQUALS 
					+ splittedText[2];
		}

		if (splittedText.length > 3) {
			parsedText += 
					AppDescription.AND
					+ AppDescription.PARAMS
					+ AppDescription.EQUALS;
			for (int i = 3; i < splittedText.length; i++) {
				if (splittedText[i].split(AppDescription.COLON).length == 2) {
					parsedText += splittedText[i];
				} else {
					logger.error(AppDescription.PARAM_PROBLEM + splittedText[i]);
				}
			}
		}

		return parsedText;
	}

}