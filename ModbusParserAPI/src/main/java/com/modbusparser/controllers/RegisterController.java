package com.modbusparser.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.modbusparser.model.ConvertedData;
import com.modbusparser.model.RawRegister;
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
		//LOGGER.info("bin: " + bin);
		String lowByteStr = bin.substring(bin.length() - 8);
		//LOGGER.info("lowByteStr: " + lowByteStr );
		lowByte = Integer.parseInt(lowByteStr, 2);
		return lowByte;
	}
	
	@RequestMapping(value = "/register/{id}", method = RequestMethod.GET)
	public ConvertedData getConvertedData(@PathVariable("id") int id) {
		ConvertedData convertedData = new ConvertedData();
		Register register = repository.findById(id).get();
		
		int combined = combineRegisters(register.getReg21(), register.getReg22());
		convertedData.setNegativeEnergyAccumulator(convertToSigned(combined));
		//LOGGER.info("reg92: " + register.getReg92());
		int lowByte = getLowByte(register.getReg92());
		convertedData.setSignalQuality(lowByte);
				
		return convertedData;
	}
	
	@RequestMapping(value = "/no-of-registers", method = RequestMethod.GET)
	public int getNoOfRegisters() {
		int noOfRegisters = repository.getNoOfRegisters();
		return noOfRegisters; 
	}
	
	String parseString(String rawString) {
		String parsedString; 
		
		String[] parsedStrings = rawString.split(":", 0);
		parsedString = parsedStrings[1];
		
		return parsedString; 
	}
	
	@RequestMapping(value = "/save-register", method = RequestMethod.POST)
	public Register saveRegister(
			@RequestParam("reg21") String reg21,
			@RequestParam("reg22") String reg22,
			@RequestParam("reg92") String reg92) {
		
		RawRegister rawRegister = new RawRegister();
		
		rawRegister.setReg21(parseString(reg21));
		rawRegister.setReg22(parseString(reg22));
		rawRegister.setReg92(parseString(reg92));
		
		LOGGER.info("After parsing...");
		LOGGER.info("reg21: " + rawRegister.getReg21());
		LOGGER.info("reg22: " + rawRegister.getReg22());
		LOGGER.info("reg92: " + rawRegister.getReg92());
		
		Register register = new Register(); 
		register.setReg21(Integer.parseInt(rawRegister.getReg21()));
		register.setReg22(Integer.parseInt(rawRegister.getReg22()));
		register.setReg92(Integer.parseInt(rawRegister.getReg92()));
		
		return repository.save(register); 
	}
}
