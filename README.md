# ModbusParser
This program converts some of the TUF-2000M Modbus data into human-readable data and displays it on the frontend.

# Top page screenshot
![ModbusParser](https://user-images.githubusercontent.com/37083992/105675170-58825d80-5ef1-11eb-9c87-462b83140d0c.png)

# How to use it
* Provide an input value in each of the fields
* Click the "Submit" button
* Click the "Update the graph" button so the graph below will be updated 

# The important parts of the code (detailed explanations given as comments as well) 
* How the input values are sent (as a raw string input) from the frontend
  * [ModbusReadable.tsx](https://github.com/Shinichi1125/ModbusParser/blob/master/modbus-parser-ui/src/components/ModbusReadable.tsx#L86-L99)
  * [DataService.ts](https://github.com/Shinichi1125/ModbusParser/blob/master/modbus-parser-ui/src/api/DataService.ts#L28-L46) 
* How the raw strings are parsed and converted to human-readable data (RegisterController.java)
  * [How two registers (register 21 and register 22) are combined](https://github.com/Shinichi1125/ModbusParser/blob/master/ModbusParserAPI/src/main/java/com/modbusparser/controllers/RegisterController.java#L35-L47)
  * [How to check if the return value should be given a minus sign or not](https://github.com/Shinichi1125/ModbusParser/blob/master/ModbusParserAPI/src/main/java/com/modbusparser/controllers/RegisterController.java#L49-L76)
  * [How to obtain the low bite of register 92](https://github.com/Shinichi1125/ModbusParser/blob/master/ModbusParserAPI/src/main/java/com/modbusparser/controllers/RegisterController.java#L78-L91)
