package com.tnt.apiAggregation.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tnt.apiAggregation.dto.ResponseDto;
import com.tnt.apiAggregation.externalGateway.ExternalAPIGateway;

@Service
public class AggregationServiceImpl implements AggregationService{
	
	private static final Logger log = LoggerFactory.getLogger(AggregationServiceImpl.class);
	
	@Autowired
	ExternalAPIGateway externalAPIGateway;

	@Override
	public ResponseDto buidAggResponse(Set<String> pricingCountryCodes, Set<String> trackOrderNumbers, Set<String> shipmentOrderNumbers)
			throws InterruptedException, ExecutionException {
		
		ResponseDto aggregatedResponse = new ResponseDto();
		ExecutorService executor = Executors.newFixedThreadPool(3);
		
		Future<Map<String, BigDecimal>> pricingFuture=null;
		Future<Map<String, String>> trackFuture=null;
		Future<Map<String, List<String>>> shipmentFuture=null;
		
		Callable<Map<String, BigDecimal>>  task1=() -> {
			Map<String, BigDecimal> pricingResult = generatePricingResp(pricingCountryCodes);
			return pricingResult;
		};
		
		Callable<Map<String, String>>  task2=() -> {
			Map<String, String> trackingResult = generateTrackingResp(trackOrderNumbers);
			return trackingResult;
		};
		
		Callable<Map<String, List<String>>>  task3=() -> {
			Map<String, List<String>> shipmentResult = generateShipmentResp(shipmentOrderNumbers);
			return shipmentResult;
		};
		pricingFuture = executor.submit(task1);
		trackFuture = executor.submit(task2);
		shipmentFuture = executor.submit(task3);
		
		Map<String, BigDecimal> pricingResult = pricingFuture.get();
		Map<String, String> trackResult = trackFuture.get();
		Map<String, List<String>> shipmentResult = shipmentFuture.get();
		
		aggregatedResponse.setPricing(pricingResult);
		aggregatedResponse.setTrack(trackResult);
		aggregatedResponse.setShipments(shipmentResult);
		log.info("All Tasks responses Aggregated!!");		
		return aggregatedResponse;
	}
	
	private Map<String, BigDecimal> generatePricingResp(Set<String> pricingCountryCodes) {
		log.info("Task for generating pricing response Started: " + pricingCountryCodes);
		Map<String, BigDecimal> responseMap = new HashMap<String, BigDecimal>();
		try {	
			for(String pricingCC:pricingCountryCodes) {
				BigDecimal res=externalAPIGateway.getPricingResponse(pricingCC);
				if(res!=null) {
					responseMap.put(pricingCC, res);
				}
			}
		}
		catch(Exception e) {
			log.error("Exception Occurred in calling external API : {}",e.getMessage());
		}
		log.info("Task for generating pricing response Completed!!");
		return responseMap;
	}
	
	private Map<String, String> generateTrackingResp(Set<String> trackOrderNumbers)
			throws InterruptedException {
		log.info("Task for generating Tracking response Started: {}" + trackOrderNumbers);
		Map<String,String> responseMap = new HashMap<String,String>();
		try {
			for(String trackON:trackOrderNumbers) {
				String res=externalAPIGateway.getTrackResponse(trackON);
				if(res!=null) {
					String resStr=new String(res);
					
					responseMap.put(trackON,resStr);
				}
			}
		}
		catch(Exception e) {
			log.error("Exception Occurred in calling external API : {}",e.getMessage());
		}
		log.info("Task for generating Tracking response Completed!!");
		return responseMap;
	}
	  
	private	 Map<String, List<String>> generateShipmentResp(Set<String> shipmentOrderNumbers)
			throws InterruptedException {
		log.info("Task for generating shipment response Sarted: {}" + shipmentOrderNumbers);
		Map<String, List<String>> responseMap = new HashMap<String, List<String>>();
		try {	
			for(String shipmentON:shipmentOrderNumbers) {
				List<String> res=externalAPIGateway.getShipmentsResponse(shipmentON);
				if(res!=null) {
					responseMap.put(shipmentON, res);
				}
			}
		}
		catch(Exception e) {
			log.error("Exception Occurred in calling external API : {}",e.getMessage());
		}
		log.info("Task for generating shipment response Completed");
		return responseMap;
	}
	
	@Override
	public Set<String> generateSet(String reqStr) {
		Set<String> set = new HashSet<String>();
		String[] strArray = reqStr.split(",");
		for (String str : strArray) {
			set.add(str);
		}
		return set;
	}

}