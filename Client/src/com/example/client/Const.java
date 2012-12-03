package com.example.client;

public class Const {
	public static String PROTOCOLE = "http";
	
	public static String IP = "10.0.2.2";
	//public static String IP = "192.168.1.205";
	public static String SERVER = "ManETS/ManETS_ServerServlet";
	public static String PORT = "8080";
	public static String URL = PROTOCOLE+"://"+IP+":";
	public static String URLARTWORK = PROTOCOLE+"://"+IP+":"+PORT;
	public static String GET = PROTOCOLE+"://"+IP+":"+PORT+"/"+SERVER+"?command=";
	
	public static int OK = 200;
	public static int ERROR = 404;
	
	public static String ID = "ID";
	public static String END = "END";
}
