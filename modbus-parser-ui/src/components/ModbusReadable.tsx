import React from 'react';
import DataService from '../api/DataService'; 
import ConvertedData from '../interfaces/ConvertedData.interface';
import RawData from '../interfaces/RawData.interface';
import { Formik, Form, Field, ErrorMessage } from 'formik'; 
import { LineChart, Line, CartesianGrid, XAxis, YAxis, Tooltip } from 'recharts';

class ModbusReadable extends React.Component{

  state = {
    count: 1,
    /*convertedData: {
      negativeEnergyAccumulator: 0,
      signalQuality: 0
    }, */
    noOfRegisters: 0,
    allRegisters: new Array<ConvertedData>()
  }

  updatePage(){
    DataService.retrieveAllConvertedData()
    .then(res => {
      this.setState({
        allRegisters:[...res.data]
      })
      //console.log("allRegisters: ");
      //console.log(this.state.allRegisters);
    })

    DataService.getNoOfRegisters()
    .then(res => {
      this.setState({noOfRegisters: res.data})
    })
/*
    const count = this.state.count; 
    DataService.retrieveConvertedData(count)
    .then(res => {
      this.setState({convertedData:res.data});
    }) */
  }

  componentDidMount(){
    this.updatePage()
  }

  componentDidUpdate(prevProps: any, prepState: any){
    if(this.state.count !== prepState.count){
      this.updatePage()
    }
  } 

  countUpdate(){
    if(this.state.count >= this.state.noOfRegisters){
      this.setState({count: 1});
    } else {
      let updateCount = this.state.count; 
      updateCount++;
      this.setState({count: updateCount})
    }
  }

  validate(values: RawData){
    let errors: Partial<RawData> = {};
    let reg21: Number; 
    let reg22: Number;
    let reg92: Number;
    reg21 = parseInt(values.reg21, 10);
    reg22 = parseInt(values.reg22, 10);
    reg92 = parseInt(values.reg92, 10);

    const errorMsg = '⚠️You should enter a valid number (0-65535)⚠️'
    if(!(reg21 >= 0 && reg21 <= 65535)){
      errors.reg21 = errorMsg;
    }
    if(!(reg22 >= 0 && reg22 <= 65535)){
      errors.reg22 = errorMsg;
    }
    if(!(reg92 >= 0 && reg92 <= 65535)){
      errors.reg92 = errorMsg;
    }
    return errors; 
  }

  onSubmit(values: RawData){
    DataService.saveRegister(values)
  }

  render(){
    //let convertedData: ConvertedData;
    //convertedData = this.state.convertedData; 

    let init: RawData = {
      reg21: '', reg22: '', reg92: ''
    }

    return(
      <div>
        <br/>
        <button className="update-button" onClick={() => this.countUpdate()}>Update the graph</button>
        <br/>
        <div className="input-fields">
          <h3>Enter New Values</h3>
          <div>
            <Formik
              initialValues = { init }
              onSubmit = {this.onSubmit}
              validate = {this.validate}
              validateOnChange = {false}
              validateOnBlur = {false}
              enableReinitialize={true}
            >
              {
                (props) => (
                  <Form>
                    <ErrorMessage name="reg21" component="div" className="text-danger"/>
                    <div className="form-group">
                      <Field type="text" name="reg21" placeholder="reg21: (0-65535)" size="45"/>
                    </div>
                    <ErrorMessage name="reg22" component="div" className="text-danger"/>
                    <div className="form-group">
                      <Field type="text" name="reg22" placeholder="reg22: (0-65535)" size="45"/>
                    </div>
                    <ErrorMessage name="reg92" component="div" className="text-danger"/>
                    <div className="form-group">
                      <Field type="text" name="reg92" placeholder="reg92: (0-65535)" size="45"/>
                    </div>
                    <button type="submit" className="submit-button">Submit</button>
                  </Form>
                )
              }
            </Formik>        
          </div>
        </div>
        <br/>
        <div className="graphs">
        <h3>Negative Energy Accumulator</h3>
        <LineChart width={600} height={300} data={this.state.allRegisters}>
          <Line type="monotone" dataKey="negativeEnergyAccumulator" stroke="#8884d8" />
          <CartesianGrid stroke="#ccc" />
          <XAxis />
          <YAxis />
          <Tooltip />
        </LineChart>
        <br/>
        <h3>Signal Quality</h3>
        <LineChart width={600} height={300} data={this.state.allRegisters}>
          <Line type="monotone" dataKey="signalQuality" stroke="#8884d8" />
          <CartesianGrid stroke="#ccc" />
          <XAxis />
          <YAxis />
          <Tooltip />
        </LineChart>
        </div>
      </div>
    )
  }
}

export default ModbusReadable; 

