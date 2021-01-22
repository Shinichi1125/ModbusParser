package com.modbusparser.controllers;

import java.util.ArrayList;
import java.util.List;
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
		String lowByteStr = bin.substring(bin.length() - 8);
		lowByte = Integer.parseInt(lowByteStr, 2);
		return lowByte;
	}
	
	ConvertedData toHumanReadable(Register register) {
		ConvertedData convertedData = new ConvertedData();
		
		int combined = combineRegisters(register.getReg21(), register.getReg22());
		convertedData.setNegativeEnergyAccumulator(convertToSigned(combined));
		int lowByte = getLowByte(register.getReg92());
		convertedData.setSignalQuality(lowByte);
		
		return convertedData;
	}
	
	@RequestMapping(value = "/register/{id}", method = RequestMethod.GET)
	public ConvertedData getConvertedData(@PathVariable("id") int id) {
		Register register = repository.findById(id).get();
		ConvertedData convertedData = toHumanReadable(register);
		return convertedData;
	}
	
	@RequestMapping(value = "/all-registers", method = RequestMethod.GET)
	public List<ConvertedData> getAllConvertedData() {
		List<ConvertedData> convertedDataList = new ArrayList<ConvertedData>();
		List<Register> registersList = repository.findAll(); 
		
		for(Register register: registersList) {		
			ConvertedData convertedData = toHumanReadable(register);
			convertedDataList.add(convertedData);
		}	
		return convertedDataList;
	}
	
	@RequestMapping(value = "/no-of-registers", method = RequestMethod.GET)
	public int getNoOfRegisters() {
		int noOfRegisters = repository.getNoOfRegisters();
		return noOfRegisters; 
	}
	
	// remove colons from the raw inputs
	// because the raw inputs consist of the register number, colon, and the value 
	// like this. 22:65535 92:806, and all that is needed is the value
	String parseString(String rawString) {
		String parsedString; 
		
		// split the raw data by colon, so the register number will be 
		//stored in index 0, the value in index 1
		String[] parsedStrings = rawString.split(":", 0);
		parsedString = parsedStrings[1];
		
		return parsedString; 
	}
	
	@RequestMapping(value = "/save-register", method = RequestMethod.POST)
	public Register saveRegister(
			@RequestParam("reg21") String reg21,
			@RequestParam("reg22") String reg22,
			@RequestParam("reg92") String reg92) {
		// EXAMPLE: When the input values for reg21, reg22, reg92 are 65480, 65535, 806 respectively
		
		// Before storing them in rawRegister (data type: String)
		// reg21: "21:65480"
		// reg22: "22:65535"
		// reg92: "92:806"
		
		// After storing them in rawRegister parsing the data 
		// by the parseString method (data type: String)
		RawRegister rawRegister = new RawRegister();	
		rawRegister.setReg21(parseString(reg21));    // "65480"
		rawRegister.setReg22(parseString(reg22));    // "65535"
		rawRegister.setReg92(parseString(reg92));    // "806"
			
		// After storing them in register (data type: int)
		Register register = new Register(); 
		register.setReg21(Integer.parseInt(rawRegister.getReg21()));    // 65480
		register.setReg22(Integer.parseInt(rawRegister.getReg22()));    // 65535
		register.setReg92(Integer.parseInt(rawRegister.getReg92()));    // 806
		
		return repository.save(register); 
	}
}
