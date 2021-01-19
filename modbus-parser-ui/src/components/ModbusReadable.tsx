import React from 'react';
import ParsedDataService from '../api/ParsedDataService'; 
import ParsedData from '../interfaces/ParsedData.interface';

class ModbusReadable extends React.Component{

  state = {
    count: 1,
    ParsedData: {
      negativeEnergyAccumulator: 0,
      signalQuality: 0
    }, 
    noOfRegisters: 0,
    interval: setInterval(this.intervalLog, 100)
  }

  intervalLog(){
    console.log("setting interval...");
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

    this.countUpdate();
  }

  componentDidMount(){
    this.state.interval = setInterval(() => {
      this.updatePage();
    }, 2000);
  }

  componentWillUnmount() {
    clearInterval(this.state.interval);
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

  render(){
    let parsedData: ParsedData;
    parsedData = this.state.ParsedData; 
    return(
      <div>
        <br/>
        
        <p>The current values: </p>
        <p>Negative Energy Accumulator: {parsedData.negativeEnergyAccumulator}</p>
        <p>Signal Quality: {parsedData.signalQuality}</p>
      </div>
    )
  }
}

export default ModbusReadable; 

/*
  componentDidUpdate(prevProps: any, prepState: any){
    if(this.state.count !== prepState.count){
      this.updatePage()
    }
  } 

<button onClick={() => this.countUpdate()}>Update values</button>
*/