package com.tnt.apiAggregation.service;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;
import com.tnt.apiAggregation.dto.ResponseDto;

@Service
public interface AggregationService {

	public ResponseDto buidAggResponse(Set<String> pricingCountryCodes, Set<String> trackOrderNumbers, Set<String> shipmentOrderNumbers)
			throws InterruptedException, ExecutionException;
	
	public Set<String> generateSet(String reqStr);
	
}
