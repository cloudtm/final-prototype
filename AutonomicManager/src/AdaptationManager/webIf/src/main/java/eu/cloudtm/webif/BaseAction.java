/*
 * $Id: ExampleSupport.java 739661 2009-02-01 00:06:00Z davenewton $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package eu.cloudtm.webif;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import eu.cloudtm.webif.exceptions.RESTControllerException;

/**
 * Base Action class for the Tutorial package.
 */
public class BaseAction extends ActionSupport implements Preparable, ServletContextAware {
	
	private static Log log = LogFactory.getLog(BaseAction.class);
	
	protected static String REST_ERROR = "rest_error";
	protected String restHost;
	protected String restPort;

	public void prepare() throws Exception {
//		Client client = Client.create();
//
//		WebResource webResource = client.resource("http://46.252.152.83:9998/status");
//		ClientResponse response;
//		try{
//			response = webResource.accept("application/json").get(ClientResponse.class);					
//		} catch(ClientHandlerException e){
//			log.debug(e,e);
//			throw new RESTControllerException(e);				
//		}
//		
//		if (response.getStatus() != 200) {
//			//throw new RuntimeException("Failed : HTTP error code : " + response.getState());
//			throw new RESTControllerException("Failed : HTTP error code : " + response.getStatus());
//		}
		
	}

	public void setServletContext(ServletContext context) {
		restHost = (String) context.getInitParameter("rest_host");	
		log.info(restHost);
		restPort = (String) context.getInitParameter("rest_port");	
		log.info(restPort);
	}
	
	public String getRestHost() {
        return this.restHost;
    }
		
	public String getRestPort() {
        return this.restPort;
    }
	
	
}
