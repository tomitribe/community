package org.superbiz.rest;

import javax.annotation.Resource;
import javax.ejb.Stateless;

import org.apache.cxf.jaxrs.client.WebClient;

@Stateless
public class GithubRestConnection {

	@Resource
	private String url;
	
	public void getData() {
		
		WebClient webClient = WebClient.create(url);
		webClient = null;
	}
	
}
