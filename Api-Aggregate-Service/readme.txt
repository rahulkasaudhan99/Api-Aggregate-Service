Requirements
1. Java (Version 8 or above) 
2. Docker Container/ Docker Desktop
3. Spring Tool Suite
4. Postman


Steps to run:

1. Get following docker image running in docker container
    https://hub.docker.com/r/xyzassessment/backend-services

2. IDE setup 
   - Clone project in STS fromgithub repository : ""
   - Import as Maven project in STS 
   - Ensure Maven update and build
  
3. Update application.properties file which is located on path src/main/resources

	a. Add property server.port = portNo - To change port no for running aggregatorApplication
	b. Update properties for External Api's as per Docker container (update uri : hostname and port here) 
           pricing.api.base.url=http://localhost:9080/pricing?q=
           tracking.api.base.url=http://localhost:9080/track?q=
           shipment.api.base.url=http://localhost:9080/shipments?q=

4. Run as Spring Boot Application

5. Open Postman 
    - Do a GET request on below uri
	  http://localhost:8080/aggregation?pricing=CR,NR,PN,RN,BN&track=123456881,109347263,109347272,109347282,109347283&shipments=123456884,109347252,109347292,109347282,109347242
	
	
	
Steps to run automated tests in "src/test/java": 
	- Do run automated test cases as Junit Tests. 
	- see all four automated test cases covering all possible scenarios. 
	

