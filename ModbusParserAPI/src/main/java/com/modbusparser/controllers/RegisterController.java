package com.modbusparser.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.modbusparser.model.ParsedData;
import com.modbusparser.model.Register;
import com.modbusparser.repos.RegisterRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RegisterController {
	
	private RegisterRepository repository; 
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);
	
	@Autowired
	RegisterController(RegisterRepository repository){
		this.repository = repository; 
	}
	
	int combineRegisters(int reg21, int reg22) {
		int combined = (reg22 << 16) + reg21;
		return combined;
	}
	
	int convertToSigned(int number) {
		if(((1 << 31) - 1) < number) {
	    	return ((1 << 32) - number) * (-1);
	    }
	    else {
	    	return number;
	    }
	}
	
	int getLowByte(int number) {
		int lowByte =  0;
		String bin = Integer.toBinaryString(number);
		LOGGER.info("bin: " + bin);
		String lowByteStr = bin.substring(bin.length() - 8);
		LOGGER.info("lowByteStr: " + lowByteStr );
		lowByte = Integer.parseInt(lowByteStr, 2);
		return lowByte;
	}
	
	@RequestMapping(value = "/register/{id}", method = RequestMethod.GET)
	public ParsedData getParsedData(@PathVariable("id") int id) {
		ParsedData parsedData = new ParsedData();
		Register register = repository.findById(id).get();
		
		int combined = combineRegisters(register.getReg21(), register.getReg22());
		parsedData.setNegativeEnergyAccumulator(convertToSigned(combined));
		LOGGER.info("reg92l: " + register.getReg92());
		int lowByte = getLowByte(register.getReg92());
		parsedData.setSignalQuality(lowByte);
				
		return parsedData;
	}
	
	@RequestMapping(value = "/no-of-registers", method = RequestMethod.GET)
	public int getNoOfRegisters() {
		int noOfRegisters = repository.getNoOfRegisters();
		return noOfRegisters; 
	}
}
