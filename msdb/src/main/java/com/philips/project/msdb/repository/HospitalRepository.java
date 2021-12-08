package com.philips.project.msdb.repository;


import com.philips.project.msdb.beans.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Integer> {

}
