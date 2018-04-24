package com.mourya.sample;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public class GenericRestClient<T, V> {

	private RestTemplate restTemplate = new RestTemplate();

	public V execute(RequestDetails requestDetails, T data, ResponseErrorHandler errorHandler, Class<V> genericClass)
			throws ResourceAccessException, Exception {
		System.out.println("requestDetails: " + requestDetails);
		System.out.println("data:" + data.toString());
		restTemplate.setErrorHandler(errorHandler);
		HttpHeaders headers = new HttpHeaders();

		HttpEntity<T> entity = new HttpEntity<T>(data, headers);
		ResponseEntity<V> response = restTemplate.exchange(requestDetails.getUrl(), requestDetails.getRequestType(),
				entity, genericClass);
		return response.getBody();
	}

	public List<V> execute(RequestDetails requestDetails, T data, ResponseErrorHandler errorHandler,
			ParameterizedTypeReference<List<V>> responseType) {
		HttpHeaders headers = new HttpHeaders();

		HttpEntity<T> entity = new HttpEntity<T>(data, headers);
		restTemplate.setErrorHandler(errorHandler);

		return restTemplate.exchange(requestDetails.getUrl(), requestDetails.getRequestType(), entity, responseType)
				.getBody();
	}
}
