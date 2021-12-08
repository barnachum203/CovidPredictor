package com.philips.project.msdb.services;

import com.github.javafaker.Faker;
import com.philips.project.msdb.beans.AreaEnum;
import com.philips.project.msdb.beans.Hospital;
import com.philips.project.msdb.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class HospitalService {
    @Autowired
    HospitalRepository hospitalRepository;

    public void addHospital(Hospital hospital) throws Exception {
        Optional<Hospital> existing = this.hospitalRepository.findById(hospital.getId());
        if (!existing.isPresent()) {
            throw new Exception("Hospital with id " + hospital.getId() + " already exists");
        }
        this.hospitalRepository.save(hospital);
    }

    public void setRandomData() {
        List<Hospital> hospitals = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            hospitals.add(this.getRandomHospital());
        }
        hospitalRepository.saveAll(hospitals);
    }

    private Hospital getRandomHospital() {
        Faker faker = new Faker();
        Hospital hospital = new Hospital();
        hospital.setName(faker.company().name());
        hospital.setArea(AreaEnum.generateRandomArea());
        hospital.setCityId(faker.number().numberBetween(1, 150));
        hospital.setContactEmail(faker.internet().emailAddress());
        hospital.setNumOfBeds(faker.number().numberBetween(50, 1200));

        return hospital;
    }

    public int calcNumOfBeds(String option) {
        int result = 0;
        List<Hospital> hospitals = hospitalRepository.findAll();

        option = option.toLowerCase(Locale.ROOT);
        for (Hospital h : hospitals) {
            result += h.getNumOfBeds();
        }

        switch (option) {
            case "north":
                for (Hospital h : hospitals) {
                    System.out.println("north");
                    if (h.getArea() == AreaEnum.North) {
                        result += h.getNumOfBeds();
                    }
                }
                break;
            case "south":
                for (Hospital h : hospitals) {
                    System.out.println("South");

                    if (h.getArea() == AreaEnum.South) {
                        result += h.getNumOfBeds();
                    }
                }
                break;
            case "central":
                for (Hospital h : hospitals) {
                    System.out.println("Central");

                    if (h.getArea() == AreaEnum.Central) {
                        result += h.getNumOfBeds();
                    }
                }
                break;
            default:
                for (Hospital h : hospitals) {
                    System.out.println("default");

                    result += h.getNumOfBeds();
                }
                break;
        }
        return result;
    }
}
