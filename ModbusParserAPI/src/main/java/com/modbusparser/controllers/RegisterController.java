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
	private int latestRegisters = 16;
	
	@Autowired
	RegisterController(RegisterRepository repository){
		this.repository = repository; 
	}
	
	int combineRegisters(int reg21, int reg22) {
		// EXAMPLE: if the values of reg21 and reg22 are 65535 and 65480, 
		// the binary version of them will be
		// 1111111111111111 and 1111111111001000.
		
		// by shifting 1111111111111111 (65535) to the left, it will be 
		// 11111111111111110000000000000000. 
		// and 1111111111001000(65480) should be added to it. 
		int combined = (reg22 << 16) + reg21;
		
		// the combined value is 11111111111111111111111111001000 (4294967240)
		return combined;
	}
	
	int convertToSigned(int number) {
		// EXAMPLE: if the number is 4294967240 (11111111111111111111111111001000)
		// Check if the furthest left bit is 1 or 0. 
		// If it's 1, the sign is minus. If it's 0, the sign is plus. 
		
		// Comparing 01111111111111111111111111111111 and 11111111111111111111111111001000 (4294967240)
		// (1 << 31) equals to 10000000000000000000000000000000. 
		// ((1 << 31) - 1) equals to 01111111111111111111111111111111
		// 01111111111111111111111111111111 is smaller than 11111111111111111111111111001000 (4294967240)
		// Therefore, 4294967240 has 1 as its furthest left bit, meaning that it has a minus sign. 
		if(((1 << 31) - 1) < number) {
			// (1 << 32) equals to 100000000000000000000000000000000.
			// 100000000000000000000000000000000 (4294967296) - 11111111111111111111111111001000 (4294967240)
			// when a certain number's binary version's bits are flipped and 1 is added to it, that becomes the number's two's complement.
			// Two's complement is a number that is used to express a negative value in the binary form. 
			// 11111111111111111111111111111111 (4294967295) - 11111111111111111111111111001000 (4294967240) flips the bits, 
			// which makes it 110111 (55). When 1 is added to it (56), that is the two's complement in this case. 
			// so in order to find out what is the value whose value is greater than that by 1, perform the following subtraction 
			// 100000000000000000000000000000000 (4294967296) - 11111111111111111111111111001000 (4294967240).
			
			// When the value is returned it should be in the form of decimal, 
			// so the value is to be multiplied by (-1) to add a minus sign. 
	    	return ((1 << 32) - number) * (-1);
	    }
	    else {
	    	return number;
	    }
	}
	
	int getLowByte(int number) {
		// EXAMPLE: When the number is 806
		int lowByte =  0;
		
		// Convert the number 806 to "0000001100100110"
		String bin = Integer.toBinaryString(number);
		
		// Save the right half of the string "00100110", and cut off the left half "00000011" 
		String lowByteStr = bin.substring(bin.length() - 8);
		
		// Revert the string "00100110" back to an integer
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
	
	@RequestMapping(value = "/latest-registers", method = RequestMethod.GET)
	public List<ConvertedData> getLatestConvertedData() {
		List<ConvertedData> convertedDataList = new ArrayList<ConvertedData>();
		List<Register> registersList = repository.findAll(); 
		
		int arraySize = registersList.size();
		
		if(arraySize > latestRegisters) {
			for(int i = arraySize - latestRegisters; i < arraySize; i++) {
				ConvertedData convertedData = toHumanReadable(registersList.get(i));
				convertedDataList.add(convertedData);
			}
		} else {
			for(Register register: registersList) {		
				ConvertedData convertedData = toHumanReadable(register);
				convertedDataList.add(convertedData);
			}	
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
