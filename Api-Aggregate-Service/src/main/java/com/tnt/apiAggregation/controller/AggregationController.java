package com.tnt.apiAggregation.controller;


import java.util.Set;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tnt.apiAggregation.dto.ResponseDto;
import com.tnt.apiAggregation.service.AggregationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-08-01T22:33:25.097Z[GMT]")
@RestController
@Slf4j
public class AggregationController {

	private static final Logger log = LoggerFactory.getLogger(AggregationController.class);

	@Autowired
	AggregationService aggregationService;

	@GetMapping(value = "/aggregation", produces = { "application/json" })
	public ResponseEntity<ResponseDto> aggregationGet(
			@Parameter(schema = @Schema(), in = ParameterIn.QUERY, description = "", required = false) @Valid @RequestParam(value = "shipmentsOrderNumbers", required = false) String shipmentsOrderNumbers,
			@Parameter(schema = @Schema(), in = ParameterIn.QUERY, description = "", required = false) @Valid @RequestParam(value = "trackOrderNumbers", required = false) String trackOrderNumbers,
			@Parameter(schema = @Schema(), in = ParameterIn.QUERY, description = "", required = false) @Valid @RequestParam(value = "pricingCountryCodes", required = false) String pricingCountryCodes) {

		try {
			Set<String> pricingCountryCodesCSet = null;
			Set<String> trackOrderNoSet = null;
			Set<String> shipmentOrderNoSet = null;
			if(pricingCountryCodes!=null) {
				pricingCountryCodesCSet = aggregationService.generateSet(pricingCountryCodes);
			}
			if(trackOrderNumbers!=null) { 
				trackOrderNoSet = aggregationService.generateSet(trackOrderNumbers);
			}
			if(shipmentsOrderNumbers!=null) {
				shipmentOrderNoSet = aggregationService.generateSet(shipmentsOrderNumbers);
			}
			ResponseDto response = aggregationService.buidAggResponse(pricingCountryCodesCSet, trackOrderNoSet, shipmentOrderNoSet);
			return new ResponseEntity<ResponseDto>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			log.error("Aggregation service Exception : {}", e.getMessage());
			return new ResponseEntity<ResponseDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}