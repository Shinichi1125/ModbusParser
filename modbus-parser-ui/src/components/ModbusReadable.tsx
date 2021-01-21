import React from 'react';
import ParsedDataService from '../api/ParsedDataService'; 
import ParsedData from '../interfaces/ParsedData.interface';
import RawData from '../interfaces/RawData.interface';
import { Formik, Form, Field } from 'formik'; 

class ModbusReadable extends React.Component{

  state = {
    count: 1,
    ParsedData: {
      negativeEnergyAccumulator: 0,
      signalQuality: 0
    }, 
    noOfRegisters: 0,
    
  }

  updatePage(){
    ParsedDataService.getNoOfRegisters()
    .then(res => {
      this.setState({noOfRegisters: res.data})
    })

    const count = this.state.count; 
    ParsedDataService.retrieveParsedData(count)
    .then(res => {
      this.setState({ParsedData:res.data});
    }) 
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

  onSubmit(){

  }

  render(){
    let parsedData: ParsedData;
    parsedData = this.state.ParsedData; 

    let init: RawData = {
      reg21: '', reg22: '', reg92: ''
    }

    return(
      <div>
        <br/>
        <button onClick={() => this.countUpdate()}>Update values</button>
        <p>The current values: </p>
        <p>Negative Energy Accumulator: {parsedData.negativeEnergyAccumulator}</p>
        <p>Signal Quality: {parsedData.signalQuality}</p>
        <div>
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
      </div>
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
*/