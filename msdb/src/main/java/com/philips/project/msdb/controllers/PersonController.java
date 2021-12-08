package com.philips.project.msdb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.philips.project.msdb.beans.Person;
import com.philips.project.msdb.services.PersonManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("person")
public class PersonController {

	@Autowired
	private PersonManager personManager;
	@Autowired
	private RestTemplate client;
	private static String report_URL = "http://localhost:8081/report/";


	@GetMapping("all")
	public Iterable<Person> getAll()
	{
		return this.personManager.getAllPersons();
	}

	@PostMapping("add")
	public ResponseEntity<?> addProduct(Person person)
	{
		try {
			personManager.addPerson(person);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("Error");
			return new ResponseEntity<Exception>(e,HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@DeleteMapping("remove")
	public ResponseEntity<?> removePerson(int personId)
	{
		personManager.removePerson(personId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PutMapping("updateResult")
	public ResponseEntity<?> updatePersonResult(int id,String result)
	{
		try {
			personManager.updatePersonResult(id, result);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Exception>(e,HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PutMapping("updateDate")
	public ResponseEntity<?> updatePersonDate(int id,String date)
	{
		try {
			personManager.updatePersonDate(id, date);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Exception>(e,HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@GetMapping("list/{date}")
	public void refreshDB(@PathVariable String date){
		this.personManager.fetchAPIData(date);
	}

	@GetMapping("daily/{date}")
	public void sendDailyParams(@PathVariable String date){


		System.out.println("Date: " + date);

		HashMap<String, Integer> result ;//= new HashMap<>();
		result = personManager.sendDailyParams(date);

		int positives = result.get("positives");
		int numberOfPCRs = result.get("numberOfPCRs");
		String url = report_URL+"daily/"+date+"/"+positives+"/"+numberOfPCRs;

		System.out.println(positives);
		System.out.println(numberOfPCRs);
		System.out.println(url);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

//		this.client.postForEntity(url, HttpMethod.POST ,result, String.class);
		this.client.postForEntity(url,HttpMethod.POST,String.class,result);

//		return client.exchange(url, HttpMethod.POST, result, String.class);


		// create headers
//		HttpHeaders headers = new HttpHeaders();
		// set `content-type` header
//		headers.setContentType(MediaType.APPLICATION_JSON);
		// set `accept` header
//		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		// create a map for post parameters
//		Map<String, Object> map = new HashMap<>(result);
//		map.put("userId", 1);
//		map.put("title", "Introduction to Spring Boot");
//		map.put("body", "Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications.");

		// build the request
//		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
//
		// send POST request
//		ResponseEntity<String> response = this.client.postForEntity(url, entity, String.class);


//		System.out.println(response);
	}

}

