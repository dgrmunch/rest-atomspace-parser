package com.xmunch.rest_atomspace_parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class App {

	public static String SERVER = "http://localhost:8080/";
	public static String CREATE_VERTEX = "vertex/create/?";
	public static String CREATE_EDGE = "edge/create/?";
	public static String INPUT_FILE = "input.xa";

	public static void main(String[] args) {
		String data;
		Scanner scanInput = new Scanner(System.in);

		try {
			FileReader fileReader = new FileReader(INPUT_FILE);
			BufferedReader bufferReader = new BufferedReader(fileReader);
			String item;

			while ((item = bufferReader.readLine()) != null) {
				atomSpaceRequest(item);
			}

			bufferReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			data = scanInput.nextLine();
			atomSpaceRequest(data);
		}

	}

	private static void atomSpaceRequest(String text) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(SERVER + restApiParser(text));
			getRequest.addHeader("accept", "application/json");

			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output;
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			httpClient.getConnectionManager().shutdown();

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private static String restApiParser(String text) {
		String parsedText = "";
		String[] splittedText = text.split(" ");

			if (splittedText[1].equals("is_a")) {
				parsedText += CREATE_VERTEX + "label=" + splittedText[0] + "&"
						+ "type=" + splittedText[2];
				// TODO ADD PARAMS

			} else {
				parsedText += CREATE_EDGE + "label=" + splittedText[1] + "&"
						+ "from=" + splittedText[0] + "&" + "to="
						+ splittedText[2];
				// TODO ADD PARAMS
			}
			return parsedText;
	}

}