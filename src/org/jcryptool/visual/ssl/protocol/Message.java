package org.jcryptool.visual.ssl.protocol;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Class that enables communication between the views.
 * 
 * @author Kapfer
 *
 */
public class Message 
{
	private static int clientHelloVersion;
	private static String clientHelloRandom="";
	
	private static List<String> clientHellotls0CipherSuites;
	private static List<String> clientHellotls1CipherSuites;
	private static List<String> clientHellotls2CipherSuites;
	
	private static int clientHelloSessionID;
	
	private static String clientCertificateClientCertificateRequest="";
	private static String clientCertificateClientKeyExchange="";
	private static String clientCertificateCertificateVerfify="";
	
	private static String clientChangeCipherSpecChangeCipherSpec="";
	
	private static String clientFinishedFinished="";
	
	private static int serverHelloVersion;
	private static String serverHelloRandom;
	private static String serverHelloCipherSuite="";
	private static String serverHelloCipher="";
	private static String serverHelloHash="";
	private static String serverHelloCipherMode="";
	private static String serverHelloKeyExchange="";
	private static int serverHelloSessionID;
	
	/**
	 * Holds if the ServerCertificate requests a certificate from the client
	 */
	private static boolean serverCertificateServerCertificateRequest=false;
	
	/**
	 * Holds the certificate from the server
	 */
	private static X509Certificate serverCertificateServerCertificate;

	/**
	 * Holds the used hash
	 */
	private static String serverCertificateHash;
	
	/**
	 * Holds the used signature algorithm
	 */
	private static String serverCertificateSignature;
	
	/**
	 * Holds the Exchange KeyPair
	 */
	private static KeyPair serverCertificateServerKeyExchange;
	private static String serverCertificateServerHelloDone="";
	
	private static String serverChangeCipherSpecChangeCipherSpec="";
	
	private static String serverFinishedFinished="";
	
	/**
	 * 
	 */
	private Message(){}
	
	/**
	 * @return the clientHelloVersion
	 */
	public static int getClientHelloVersion() {
		return clientHelloVersion;
	}
	
	/**
	 * @param clientHelloVersion the clientHelloVersion to set
	 */
	public static void setClientHelloVersion(int clientHelloVersion) {
		Message.clientHelloVersion = clientHelloVersion;
	}
	
	/**
	 * @return the clientHelloRandom
	 */
	public static String getClientHelloRandom() {
		return clientHelloRandom;
	}
	
	/**
	 * @param clientHelloRandom the clientHelloRandom to set
	 */
	public static void setClientHelloRandom(String clientHelloRandom) {
		Message.clientHelloRandom = clientHelloRandom;
	}
	
	/**
	 * @return the clientHelloSessionID
	 */
	public static int getClientHelloSessionID() {
		return clientHelloSessionID;
	}
	
	/**
	 * @param clientHelloSessionID the clientHelloSessionID to set
	 */
	public static void setClientHelloSessionID(int clientHelloSessionID) {
		Message.clientHelloSessionID = clientHelloSessionID;
	}
	
	/**
	 * @return the clientCertificateClientCertificateRequest
	 */
	public static String getClientCertificateClientCertificateRequest() {
		return clientCertificateClientCertificateRequest;
	}
	
	/**
	 * @param clientCertificateClientCertificateRequest the clientCertificateClientCertificateRequest to set
	 */
	public static void setClientCertificateClientCertificateRequest(
			String clientCertificateClientCertificateRequest) {
		Message.clientCertificateClientCertificateRequest = clientCertificateClientCertificateRequest;
	}
	
	/**
	 * @return the clientCertificateClientKeyExchange
	 */
	public static String getClientCertificateClientKeyExchange() {
		return clientCertificateClientKeyExchange;
	}
	
	/**
	 * @param clientCertificateClientKeyExchange the clientCertificateClientKeyExchange to set
	 */
	public static void setClientCertificateClientKeyExchange(
			String clientCertificateClientKeyExchange) {
		Message.clientCertificateClientKeyExchange = clientCertificateClientKeyExchange;
	}
	
	/**
	 * @return the clientCertificateCertificateVerfify
	 */
	public static String getClientCertificateCertificateVerfify() {
		return clientCertificateCertificateVerfify;
	}
	
	/**
	 * @param clientCertificateCertificateVerfify the clientCertificateCertificateVerfify to set
	 */
	public static void setClientCertificateCertificateVerfify(
			String clientCertificateCertificateVerfify) {
		Message.clientCertificateCertificateVerfify = clientCertificateCertificateVerfify;
	}
	
	/**
	 * @return the clientChangeCipherSpecChangeCipherSpec
	 */
	public static String getClientChangeCipherSpecChangeCipherSpec() {
		return clientChangeCipherSpecChangeCipherSpec;
	}
	
	/**
	 * @param clientChangeCipherSpecChangeCipherSpec the clientChangeCipherSpecChangeCipherSpec to set
	 */
	public static void setClientChangeCipherSpecChangeCipherSpec(
			String clientChangeCipherSpecChangeCipherSpec) {
		Message.clientChangeCipherSpecChangeCipherSpec = clientChangeCipherSpecChangeCipherSpec;
	}
	
	/**
	 * @return the clientFinishedFinished
	 */
	public static String getClientFinishedFinished() {
		return clientFinishedFinished;
	}
	
	/**
	 * @param clientFinishedFinished the clientFinishedFinished to set
	 */
	public static void setClientFinishedFinished(String clientFinishedFinished) {
		Message.clientFinishedFinished = clientFinishedFinished;
	}
	
	/**
	 * @return the serverHelloVersion
	 */
	public static int getServerHelloVersion() {
		return serverHelloVersion;
	}
	
	/**
	 * @param serverHelloVersion the serverHelloVersion to set
	 */
	public static void setServerHelloVersion(int serverHelloVersion) {
		Message.serverHelloVersion = serverHelloVersion;
	}
	
	/**
	 * @return the serverHelloRandom
	 */
	public static String getServerHelloRandom() {
		return serverHelloRandom;
	}
	
	/**
	 * @param serverHelloRandom the serverHelloRandom to set
	 */
	public static void setServerHelloRandom(String serverHelloRandom) {
		Message.serverHelloRandom = serverHelloRandom;
	}
	
	/**
	 * @return the serverHelloCipherSuite
	 */
	public static String getServerHelloCipherSuite() {
		return serverHelloCipherSuite;
	}
	
	/**
	 * @param serverHelloCipherSuite the serverHelloCipherSuite to set
	 */
	public static void setServerHelloCipherSuite(String serverHelloCipherSuite) {
		Message.serverHelloCipherSuite = serverHelloCipherSuite;
	}
	
	/**
	 * @return the serverHelloSessionID
	 */
	public static int getServerHelloSessionID() {
		return serverHelloSessionID;
	}
	
	/**
	 * @param serverHelloSessionID the serverHelloSessionID to set
	 */
	public static void setServerHelloSessionID(int serverHelloSessionID) {
		Message.serverHelloSessionID = serverHelloSessionID;
	}
	
	/**
	 * @return the serverCertificateServerCertificateRequest
	 */
	public static boolean getServerCertificateServerCertificateRequest() {
		return serverCertificateServerCertificateRequest;
	}
	
	/**
	 * @param serverCertificateServerCertificateRequest the serverCertificateServerCertificateRequest to set
	 */
	public static void setServerCertificateServerCertificateRequest(
			boolean serverCertificateServerCertificateRequest) {
		Message.serverCertificateServerCertificateRequest = serverCertificateServerCertificateRequest;
	}
	
	/**
	 * @return the serverCertificateServerCertificate
	 */
	public static X509Certificate getServerCertificateServerCertificate() {
		return serverCertificateServerCertificate;
	}
	
	/**
	 * @param serverCertificateServerCertificate the serverCertificateServerCertificate to set
	 */
	public static void setServerCertificateServerCertificate(
			X509Certificate serverCertificateServerCertificate) {
		Message.serverCertificateServerCertificate = serverCertificateServerCertificate;
	}
	
	/**
	 * @return the serverCertificateServerKeyExchange
	 */
	public static KeyPair getServerCertificateServerKeyExchange() {
		return serverCertificateServerKeyExchange;
	}
	
	/**
	 * @param serverCertificateServerKeyExchange the serverCertificateServerKeyExchange to set
	 */
	public static void setServerCertificateServerKeyExchange(
			KeyPair serverCertificateServerKeyExchange) {
		Message.serverCertificateServerKeyExchange = serverCertificateServerKeyExchange;
	}
	
	/**
	 * @return the serverCertificateServerHelloDone
	 */
	public static String getServerCertificateServerHelloDone() {
		return serverCertificateServerHelloDone;
	}
	
	/**
	 * @param serverCertificateServerHelloDone the serverCertificateServerHelloDone to set
	 */
	public static void setServerCertificateServerHelloDone(
			String serverCertificateServerHelloDone) {
		Message.serverCertificateServerHelloDone = serverCertificateServerHelloDone;
	}
	
	/**
	 * @return the serverChangeCipherSpecChangeCipherSpec
	 */
	public static String getServerChangeCipherSpecChangeCipherSpec() {
		return serverChangeCipherSpecChangeCipherSpec;
	}
	
	/**
	 * @param serverChangeCipherSpecChangeCipherSpec the serverChangeCipherSpecChangeCipherSpec to set
	 */
	public static void setServerChangeCipherSpecChangeCipherSpec(
			String serverChangeCipherSpecChangeCipherSpec) {
		Message.serverChangeCipherSpecChangeCipherSpec = serverChangeCipherSpecChangeCipherSpec;
	}
	
	/**
	 * @return the serverFinishedFinished
	 */
	public static String getServerFinishedFinished() {
		return serverFinishedFinished;
	}
	
	/**
	 * @param serverFinishedFinished the serverFinishedFinished to set
	 */
	public static void setServerFinishedFinished(String serverFinishedFinished) {
		Message.serverFinishedFinished = serverFinishedFinished;
	}

	/**
	 * @return the serverHelloCipher
	 */
	public static String getServerHelloCipher() {
		return serverHelloCipher;
	}

	/**
	 * @param serverHelloCipher the serverHelloCipher to set
	 */
	public static void setServerHelloCipher(String serverHelloCipher) {
		Message.serverHelloCipher = serverHelloCipher;
	}

	/**
	 * @return the serverHelloHash
	 */
	public static String getServerHelloHash() {
		return serverHelloHash;
	}

	/**
	 * @param serverHelloHash the serverHelloHash to set
	 */
	public static void setServerHelloHash(String serverHelloHash) {
		Message.serverHelloHash = serverHelloHash;
	}

	/**
	 * @return the serverHelloKeyExchange
	 */
	public static String getServerHelloKeyExchange() {
		return serverHelloKeyExchange;
	}

	/**
	 * @param serverHelloKeyExchange the serverHelloKeyExchange to set
	 */
	public static void setServerHelloKeyExchange(String serverHelloKeyExchange) {
		Message.serverHelloKeyExchange = serverHelloKeyExchange;
	}

	/**
	 * @return the tls0CipherSuites
	 */
	public static List<String> getClientHelloTls0CipherSuites() {
		return clientHellotls0CipherSuites;
	}

	/**
	 * @param tls0CipherSuites the tls0CipherSuites to set
	 */
	public static void setClientHelloTls0CipherSuites(List<String> tls0CipherSuites) {
		Message.clientHellotls0CipherSuites = tls0CipherSuites;
	}

	/**
	 * @return the tls1CipherSuites
	 */
	public static List<String> getClientHelloTls1CipherSuites() {
		return clientHellotls1CipherSuites;
	}

	/**
	 * @param tls1CipherSuites the tls1CipherSuites to set
	 */
	public static void setClientHelloTls1CipherSuites(List<String> tls1CipherSuites) {
		Message.clientHellotls1CipherSuites = tls1CipherSuites;
	}

	/**
	 * @return the tls2CipherSuites
	 */
	public static List<String> getClientHelloTls2CipherSuites() {
		return clientHellotls2CipherSuites;
	}

	/**
	 * @param tls2CipherSuites the tls2CipherSuites to set
	 */
	public static void setClientHelloTls2CipherSuites(List<String> tls2CipherSuites) {
		Message.clientHellotls2CipherSuites = tls2CipherSuites;
	}

	public static String getServerCertificateHash() {
		return serverCertificateHash;
	}

	public static void setServerCertificateHash(String serverCertificateHash) {
		Message.serverCertificateHash = serverCertificateHash;
	}

	public static String getServerCertificateSignature() {
		return serverCertificateSignature;
	}

	public static void setServerCertificateSignature(
			String serverCertificateSignature) {
		Message.serverCertificateSignature = serverCertificateSignature;
	}

	/**
	 * @return the serverHelloCipherMode
	 */
	public static String getServerHelloCipherMode() {
		return serverHelloCipherMode;
	}

	/**
	 * @param serverHelloCipherMode the serverHelloCipherMode to set
	 */
	public static void setServerHelloCipherMode(String serverHelloCipherMode) {
		Message.serverHelloCipherMode = serverHelloCipherMode;
	}
}
