package com.modbusparser.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.modbusparser.model.Register;

public interface RegisterRepository extends JpaRepository<Register, Integer> {
	@Query("SELECT COUNT(id) FROM Register")
	int getNoOfRegisters();
}
