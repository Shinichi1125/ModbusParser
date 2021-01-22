import React from 'react';
import DataService from '../api/DataService'; 
import ConvertedData from '../interfaces/ConvertedData.interface';
import RawData from '../interfaces/RawData.interface';
import { Formik, Form, Field } from 'formik'; 

class ModbusReadable extends React.Component{

  state = {
    count: 1,
    convertedData: {
      negativeEnergyAccumulator: 0,
      signalQuality: 0
    }, 
    noOfRegisters: 0,
    allRegisters: new Array<ConvertedData>()
  }

  updatePage(){
    DataService.retrieveAllConvertedData()
    .then(res => {
      //console.log("Retrieved data: ");
      //console.log(res.data);
      this.setState({
        allRegisters:[...this.state.allRegisters, ...res.data]
      })
      console.log("allRegisters: ");
      console.log(this.state.allRegisters);
    })

    DataService.getNoOfRegisters()
    .then(res => {
      this.setState({noOfRegisters: res.data})
    })

    const count = this.state.count; 
    DataService.retrieveConvertedData(count)
    .then(res => {
      this.setState({convertedData:res.data});
    }) 

    //console.log("The number of registers: " + this.state.noOfRegisters);
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

  onSubmit(values: RawData){
    DataService.saveRegister(values)
    /*.then(res => {
      console.log("the content of res.data: ");
      console.log(res.data);
    })*/
  }

  render(){
    let convertedData: ConvertedData;
    convertedData = this.state.convertedData; 

    let init: RawData = {
      reg21: '', reg22: '', reg92: ''
    }

    return(
      <div>
        <br/>
        <button onClick={() => this.countUpdate()}>Update values</button>
        <p>The current values: </p>
        <p>Negative Energy Accumulator: {convertedData.negativeEnergyAccumulator}</p>
        <p>Signal Quality: {convertedData.signalQuality}</p>
        <div className="object-details">
          <h3>Enter New Values</h3>
          <div>
            <Formik
              initialValues = { init }
              onSubmit = {this.onSubmit}
              enableReinitialize={true}
            >
              {
                (props) => (
                  <Form>
                    <div className="form-group">
                    <fieldset>
                      <Field type="text" name="reg21" placeholder="reg21" size="35"/>
                    </fieldset>
                    </div>
                    <div className="form-group">
                    <fieldset>
                      <Field type="text" name="reg22" placeholder="reg22" size="35"/>
                    </fieldset>
                    </div>
                    <div className="form-group">
                    <fieldset>
                      <Field type="text" name="reg92" placeholder="reg92" size="35"/>
                    </fieldset>
                    </div>
                    <button type="submit" className="btn btn-primary">Submit</button>
                  </Form>
                )
              }
            </Formik>        
          </div>
        </div>
        <br/>
      </div>
    )
  }
}

export default ModbusReadable; 

/*
//interval: setInterval(this.intervalLog, 100)

intervalLog(){
    //console.log("setting interval...");
  }

componentDidMount(){
    this.state.interval = setInterval(() => {
      this.updatePage();
    }, 2000);
  }

componentWillUnmount() {
    clearInterval(this.state.interval);
  }

let savedRegister = {
      reg21: this.state.savedRegister.reg21,
      reg22: this.state.savedRegister.reg22,
      reg92: this.state.savedRegister.reg92,
    }
<p>
          Saved register... reg21: {savedRegister.reg21}, reg22: {savedRegister.reg22}, reg92: {savedRegister.reg92}
        </p>

*/