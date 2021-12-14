package com.philips.project.msdb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.philips.project.msdb.beans.Person;
import com.philips.project.msdb.services.PersonService;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("api")
public class PersonController {

	@Autowired
	private PersonService personService;
	@Autowired
	private RestTemplate client;
	private static String report_URL = "http://localhost:8081/report/";

	/**
	 * Send all patients data
	 * @return
	 */
	@GetMapping("get/all")
	public Iterable<Person> getAll()
	{
		return this.personService.getAllPersons();
	}

	/**
	 * Add new patient to DB
	 * @param person - patient data
	 * @return add confirmation
	 */
	@PostMapping("add/person")
	public ResponseEntity<?> addPerson(@RequestBody Person person)
	{
		try {
			personService.addPerson(person);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("Error");
			return new ResponseEntity<Exception>(e,HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	/**
	 * Delete patient by id
	 * @param personId
	 */
	@DeleteMapping("remove/patient/{personId}")
	public ResponseEntity<?> removePerson(@PathVariable int personId)
	{
		personService.removePerson(personId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * Update the result of PCR for patient by id
	 * @param personId
	 * @param result
	 * @return success/not success
	 */
	@PutMapping("update/patient/result/{personId}/{result}")
	public ResponseEntity<?> updatePersonResult(@PathVariable int personId,@PathVariable String result)
	{
		try {
			personService.updatePersonResult(personId, result);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Exception>(e,HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	/**
	 * Update db from MOH API by date.
	 * this will not replace existing data
	 * @param date - get data from specific date
	 * @return Response
	 */
	@PostMapping("update/db/{date}")
	public ResponseEntity<?> updateDBFromAPIByDate(@PathVariable String date) {
		List<Person> result = new ArrayList<>();

		if (!date.isEmpty()) {
			result = this.personService.fetchAPIData(date);
		}

		if (result.size()>0) {
			return new ResponseEntity<String>("Updated results for date: " + date, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Wrong date format", HttpStatus.NOT_ACCEPTABLE);
		}
	}

	/**
	 * This function send daily data to Analytics
	 * @param date
	 */
	@GetMapping("get/db/daily/{date}")
	public void sendDailyParams(@PathVariable String date){

		HashMap<String, Integer> result = new HashMap<>();
		int positives = result.get("positives");
		int numberOfPCRs = result.get("numberOfPCRs");
		System.out.println("Date: " + date );

		result = personService.sendDailyParams(date);
		String url = report_URL+"daily/"+date+"/"+positives+"/"+numberOfPCRs;

		this.client.postForEntity(url,HttpMethod.POST,String.class,result);
	}

	/**
	 *
	 * @return number of positive patients
	 */
	@GetMapping("get/positive")
	public ResponseEntity<Integer> getPositive()
	{
		int positive = personService.getPositive();
		return new ResponseEntity<Integer>(positive,HttpStatus.OK);

	}

	/**
	 *
	 * @return number of negative patients
	 */
	@GetMapping("get/negative")
	public ResponseEntity<Integer> getNegative()
	{
		int negative = personService.getNegative();
		return new ResponseEntity<Integer>(negative,HttpStatus.OK);
	}
}

