package com.xmunch.rest_atomspace_parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class AppDescription {
	
	public static Scanner scanInput;
	public static FileReader fileReader;
	public static BufferedReader bufferReader;

	public static final Integer SUCCESS = 200;
	public static final String ACCEPT = "accept";
	public static final String SERVER = "http://localhost:8080/";
	public static final String CREATE_VERTEX = "vertex/create/?";
	public static final String CREATE_EDGE = "edge/create/?";
	public static final String INPUT_FILE = "input.xa";
	public static final String JSON = "application/json";
	public static final String ERROR = "Failed : HTTP error code : ";
	public static final String EMPTY = "";
	public static final String SPACE = " ";
	public static final String LABEL = "label";
	public static final String EQUALS = "=";
	public static final String AND = "&";
	public static final String TYPE = "type";
	public static final String TO = "to";
	public static final String FROM = "from";
	public static final Object IS_A = "is_a";
	public static final String COLON = ":";
	public static final String PARAMS = "params";
	public static final String PARAM_PROBLEM = "There was a problem parsing the param: ";


}
