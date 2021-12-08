package com.philips.project.msdb.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.project.msdb.beans.Person;
import com.philips.project.msdb.moh.BaseData;
import com.philips.project.msdb.repository.PersonRepository;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class PersonManager {

	@Autowired
	private PersonRepository personRepo;
	@Autowired
	private RestTemplate client;
	private static String report_URL = "http://localhost:8081/report/daily";


	public void addPerson(Person person) throws Exception
	{
		Optional<Person> existing = this.personRepo.findById(person.get_id());
		if(!existing.isPresent())
		{
			throw new Exception("Person with id "+person.get_id()+" already exists");
		}
		this.personRepo.save(person);
	}
	
	public void updatePersonResult(int id,String result) throws Exception
	{
		if(result.length() == 0)
		{
			throw new Exception("Error quantity");
		}
		int res = this.personRepo.updatePersonResult(id, result);
		if(res==0)
		{
			throw new Exception("Person was not found");
		}
	}
	
	public void updatePersonDate(int id,String date) throws Exception
	{
		if(date.length() == 0)
		{
			throw new Exception("Error date");
		}
		int res = this.personRepo.updatePersonDate(id, date);
		if(res==0)
		{
			throw new Exception("Person was not found");
		}
	}
	
	public void removePerson(int productId) {
		this.personRepo.deleteById(productId);
	}

	public Iterable<Person> getAllPersons() {		
		return this.personRepo.findAll();
	}

	//date format: yyyy-MM-dd
	public void fetchAPIData(String date){
		//TODO: Check the date format ['yyyy-MM-dd']

		String criterion = "{\"result_date\":"+"\""+date+"\""+"}";
		String url = "https://data.gov.il/api/action/datastore_search?resource_id=dcf999c1-d394-4b57-a5e0-9d014a62e046&limit=800";

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("filters", criterion);

//		System.out.println(builder.build().toUriString());
		ResponseEntity<BaseData> baseData = this.client.getForEntity(builder.build().toUri(), BaseData.class);


		List<Person> persons = baseData.getBody().getResult().getRecords();
//		System.out.println(baseData.getBody().getResult());
		

		for(Person person : persons) {
			if(person.getTest_for_corona_diagnosis().equals("0")) {
				person.setBool_of_corona(true);
			}
			else
				person.setBool_of_corona(false);

//			System.out.println(person);
			personRepo.save(person);
		}
		
//		JSONObject data = new JSONObject(res);
//		JSONObject results = data.getJSONObject("result");
//		JSONArray records = results.getJSONArray("records");
//		List<Person> persons = new ArrayList<>();
//		for(int i=0;i<records.length();i++)
//		{
//			JSONObject entity = records.getJSONObject(i);
//			ObjectMapper objectMapper = new ObjectMapper();
//			Person person = null;
//			try {
//				person = objectMapper.readValue(entity.toString(), Person.class);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			persons.add(person);
//		}
//		this.personRepo.saveAll(persons);
	}

	public HashMap<String, Integer> sendDailyParams(String date) {

		int positives=0, numberOfPCRs=0;
		List<Person> persons = personRepo.findByResultDate(date);
		numberOfPCRs = persons.size();
		for (Person p:persons) {
			if(p.isBool_of_corona()){
				positives++;
			}
		}
		System.out.println(positives);
		System.out.println(numberOfPCRs);
		System.out.println(persons);

		HashMap<String, Integer> results = new HashMap<>();
		results.put("positives", positives);
		results.put("numberOfPCRs", numberOfPCRs);

		return results;

	}

	public class PersonList {
		private List<Person> persons;

		public PersonList() {
			persons = new ArrayList<>();
		}


		public List<Person> getPersons() {
			return persons;
		}

		public void setPersons(List<Person> persons) {
			this.persons = persons;
		}
		// standard constructor and getter/setter
	}

//	public boolean isValid(String dateStr) {
//        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//        sdf.setLenient(false);
//        try {
//			sdf.parse(dateStr);
//		} catch (java.text.ParseException e) {
//			e.printStackTrace();
//		}
//        return true;
//    }
    

}
