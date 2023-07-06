package org.opennms.karaf.httpclient.manual;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * This test class can be run stand alone against a separate running container in which case
 * these tests need the camel features to be installed on your container before they are run. 
 * 
 * to do this log into the karaf server
 * 
 * 		feature:repo-add mvn:org.opennms.karaf.examples/karaf-camel-example-features/0.0.1-SNAPSHOT/xml
 *      feature:install karaf-camel-example-blueprint
 * 
 * The tests here replicate the curl tests in the feature documentation.
 * 
 * These tests are a also injected into the test probe run within the container by the CamelBlueprintTest and CamelJavaTest
 * 
 * @author CGallen
 *
 */
public class HttpClientTests {
	private static Logger LOG = LoggerFactory.getLogger(HttpClientTests.class);

	/*
	 * curl -X GET -H "Content-Type: application/json" http://localhost:9090/example
	 * expected response: { "error": "only POST is accepted" }
	 * (500 server error)
	 */
	@Test
	public void testErrorGet() throws IOException {
		String url = "http://localhost:9090/example";
		Integer expectedResponseCode=500;
		String expectedResponse = "{ \"error\": \"only POST is accepted\" }";
		testGet( url, expectedResponseCode, expectedResponse);
	}
	
	/*
	 * curl -X POST -H "Content-Type: application/json" http://localhost:9090/example -d '{ "notification": { "type": "email", "to": "foo@bar.com", "message": "This is a test" }}'
	 * expected response : { "status": "email sent", "to": "foo@bar.com", "subject": "Notification"}
	*/
	@Test
	public void testPostNotification() throws IOException {
		String url = "http://localhost:9090/example";
		String requestBody="{ \"notification\": { \"type\": \"email\", \"to\": \"foo@bar.com\", \"message\": \"This is a test\" }}";
		Integer expectedResponseCode=200;
		String expectedResponse = "{ \"status\": \"email sent\", \"to\": \"foo@bar.com\", \"subject\": \"Notification\" }";
		testPost( url, requestBody, expectedResponseCode, expectedResponse);
	}
	
	
	/*
	 * curl -X POST -H "Content-Type: application/json" http://localhost:9090/example -d '{ "notification": { "type": "http", "service": "http://foo" }}'
	 * expected response: { "status": "http requested", "service": "http://foo" }
	 * 
	*/
	@Test
	public void testPostHttp() throws IOException {
		String url = "http://localhost:9090/example";
		String requestBody="{ \"notification\": { \"type\": \"http\", \"service\": \"http://foo\" }}";
		Integer expectedResponseCode=200;
		String expectedResponse = "{ \"status\": \"http requested\", \"service\": \"http://foo\" }";
		testPost( url, requestBody, expectedResponseCode, expectedResponse);
	}
	
	
	public void testPost(String url, String requestBody, Integer expectedResponseCode, String expectedResponse) throws IOException {
	     CloseableHttpClient httpclient = HttpClients.createDefault();
	        try {
	            HttpPost httpPost = new HttpPost(url);
	            
	            if(requestBody !=null) {
	                httpPost.setEntity(new StringEntity(requestBody));
	            }

	            LOG.debug("Executing request " + httpPost.getURI());
	            CloseableHttpResponse response = httpclient.execute(httpPost);
	            try {
	                LOG.debug(response.getStatusLine().toString());
	                
	                if(expectedResponseCode!=null) {
	                     assertEquals(expectedResponseCode.intValue() ,response.getStatusLine().getStatusCode());
	                }
	                
	                InputStream is = response.getEntity().getContent();
	                // Wrap a BufferedReader around the InputStream
	                BufferedReader rd = new BufferedReader(new InputStreamReader(is));

	                String line = "";
	                StringBuilder receivedData = new StringBuilder();

	                // Read response until the end
	                while ((line = rd.readLine()) != null) { 
	                    receivedData.append(line); 
	                }
	                
	                // Return full string
	                LOG.debug("received string "+receivedData);
	                
	                assertEquals(expectedResponse, receivedData.toString());
	                
	                // Do not feel like reading the response body
	                // Call abort on the request object
	                httpPost.abort();
	            } finally {
	                response.close();
	            }
	        } finally {
	            httpclient.close();
	        }
	    }

	public void testGet(String url, Integer expectedResponseCode, String expectedResponse) throws IOException {
	     CloseableHttpClient httpclient = HttpClients.createDefault();
	        try {
	            HttpGet httpget = new HttpGet(url);

	            LOG.debug("Executing request " + httpget.getURI());
	            CloseableHttpResponse response = httpclient.execute(httpget);
	            try {
	                LOG.debug(response.getStatusLine().toString());
	                
	                if(expectedResponseCode!=null) {
	                     assertEquals(expectedResponseCode.intValue() ,response.getStatusLine().getStatusCode());
	                }
	                
	                InputStream is = response.getEntity().getContent();
	                // Wrap a BufferedReader around the InputStream
	                BufferedReader rd = new BufferedReader(new InputStreamReader(is));

	                String line = "";
	                StringBuilder receivedData = new StringBuilder();

	                // Read response until the end
	                while ((line = rd.readLine()) != null) { 
	                    receivedData.append(line); 
	                }
	                
	                // Return full string
	                LOG.debug("received string "+receivedData);
	                
	                assertEquals(expectedResponse, receivedData.toString());
	                
	                // Do not feel like reading the response body
	                // Call abort on the request object
	                httpget.abort();
	            } finally {
	                response.close();
	            }
	        } finally {
	            httpclient.close();
	        }
	    }


}
