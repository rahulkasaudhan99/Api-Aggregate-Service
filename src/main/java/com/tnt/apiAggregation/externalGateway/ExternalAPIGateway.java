package com.tnt.apiAggregation.externalGateway;

import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;



@Component
public class ExternalAPIGateway {
	
	private static final Logger log = LoggerFactory.getLogger(ExternalAPIGateway.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${pricing.api.base.url}")
	private String pricing_api_url;
	
	@Value("${shipment.api.base.url}")
	private String shipment_api_url;
	
	@Value("${tracking.api.base.url}")
	private String tracking_api_url;
	
	@HystrixCommand(fallbackMethod="getPricingResponseFallback",commandProperties= {
			@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value="1")
	})
	public BigDecimal getPricingResponse(String pricingStr) throws Exception {
		log.info("Calling External pricingAPI: "+pricingStr);
		ParameterizedTypeReference<BigDecimal> responseType = new ParameterizedTypeReference<BigDecimal>() {};
		String pricingUri = pricing_api_url + pricingStr;
		RequestEntity<Void> request = RequestEntity.get(pricingUri).accept(MediaType.APPLICATION_JSON).build();
		BigDecimal response = restTemplate.exchange(request, responseType).getBody();
		log.info("Response received from pricing API: "+response);
		return response;
	}
	
	@HystrixCommand(fallbackMethod="getTrackResponseFallback",commandProperties= {
			@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value="1")
	})
	public String getTrackResponse(String trackingStr) throws Exception {
		log.info("Calling External trackingAPI: " + trackingStr);
		String trackingUri = tracking_api_url + trackingStr;
		ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {};
		RequestEntity<Void> request = RequestEntity.get(trackingUri).accept(MediaType.APPLICATION_JSON).build();
		String response = restTemplate.exchange(request, responseType).getBody();
		log.info("Response received from tracking API: "+response);
		return response;
	}
	
	@HystrixCommand(fallbackMethod="getShipmentsResponseFallback",commandProperties= {
			@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value="1")
	})
	public List<String> getShipmentsResponse(String shipmentsStr){
		log.info("Calling External shipmentAPI: "+shipmentsStr);
		ParameterizedTypeReference<List<String>> responseType = new ParameterizedTypeReference<List<String>>() {};
		String shipmentUri = shipment_api_url + shipmentsStr;
		RequestEntity<Void> request = RequestEntity.get(shipmentUri).accept(MediaType.APPLICATION_JSON).build();
		List<String> response = restTemplate.exchange(request, responseType).getBody();
		log.info("Response received from shipment API: "+response);
		return response;
	}
	
	public BigDecimal getPricingResponseFallback(String pricingStr) {
		log.info("-------------In Pricing fallback------------");
		BigDecimal dummyResponse=null;
		return dummyResponse;
	}
	
	public String getTrackResponseFallback(String trackingStr) {
		String  dummyResponse=null;
		log.info("-------------In Tracking fallback------------");
		return dummyResponse;
	}
	
	public List<String> getShipmentsResponseFallback(String shipmentsStr) {
		log.info("-------------In Shipments fallback------------");
		List<String> dummyResponse=null;
		return dummyResponse;
	}

}
