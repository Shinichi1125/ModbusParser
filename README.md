# ModbusParser
This program converts some of the TUF-2000M Modbus data into human-readable data and displays it on the frontend.

# Top page screenshot
![ModbusParser](https://user-images.githubusercontent.com/37083992/105675170-58825d80-5ef1-11eb-9c87-462b83140d0c.png)

# How to start the project in a local environment
* Replicate the entire project to your own PC by git clone
* Start the backend of the "ModbusParserAPI" directory 
* Go to the directory "modbus-parser-ui", and then hit the "npm start" command

# How to use this app
* Provide an input value in each of the fields
* Click the "Submit" button
* Click the "Update the graph" button so the graph below will be updated 

# What this app is currently capable of
* Allowing the user to provide input values to add a set of data 
* Parsing the data (taking away the register number and colon, so that only the value will remain) 
* Converting the data to human-readable data
* Fetching the stored data from the database and displaying it on the frontend
* Visualizing the data by line graphs 
* Updating the graph without reloading the entire page (AJAX call) 

# The important parts of the code (Please click each of the links below, as detailed explanations are given as comments as well) 
* How the input values are sent (as a raw string input) from the frontend
  * [ModbusReadable.tsx](https://github.com/Shinichi1125/ModbusParser/blob/master/modbus-parser-ui/src/components/ModbusReadable.tsx#L86-L99)
  * [DataService.ts](https://github.com/Shinichi1125/ModbusParser/blob/master/modbus-parser-ui/src/api/DataService.ts#L28-L46) 
* How the raw strings are parsed and converted to human-readable data (RegisterController.java)
  * [How two registers (register 21 and register 22) are combined](https://github.com/Shinichi1125/ModbusParser/blob/master/ModbusParserAPI/src/main/java/com/modbusparser/controllers/RegisterController.java#L35-L47)
  * [How to check if the return value should be given a minus sign or not](https://github.com/Shinichi1125/ModbusParser/blob/master/ModbusParserAPI/src/main/java/com/modbusparser/controllers/RegisterController.java#L49-L76)
  * [How to obtain the low bite of register 92](https://github.com/Shinichi1125/ModbusParser/blob/master/ModbusParserAPI/src/main/java/com/modbusparser/controllers/RegisterController.java#L78-L91)
  * [How a new set of inputs are saved on the backend side](https://github.com/Shinichi1125/ModbusParser/blob/master/ModbusParserAPI/src/main/java/com/modbusparser/controllers/RegisterController.java#L143-L169)

# What I'm currently working on to brush up this project 
* Deployment via AWS, so that you don't have to clone the project into your local device.
* Automatically updating the graph when the submit button is clicked, so that the "Update the graph" button won't be necessary. 
* Only displaying the latest 10 pieces of data or so. (Currently it displays all the data, so when the number of pieces of get gets big the graph can look ugly)
* Maybe adding another page where you can check the log of all the data
* etc 
