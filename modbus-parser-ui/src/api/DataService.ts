import axios from 'axios';
import { API_URL } from '../Constants';
import RawData from '../interfaces/RawData.interface';

class DataService {
  retrieveAllConvertedData(){
    return axios.get(`${API_URL}/all-registers`);
  }

  retrieveConvertedData(id: number){
    return axios.get(`${API_URL}/register/${id}`);
  } 

  getNoOfRegisters(){
    return axios.get(`${API_URL}/no-of-registers`);
  }

  makeFormData(data: RawData){
    const formData = new FormData();

    formData.append('reg21', String(data.reg21));
    formData.append('reg22', String(data.reg22));
    formData.append('reg92', String(data.reg92));
    
    return formData; 
  }

  saveRegister(rawData: RawData){
    let nonParsedStrings = {
      reg21: '21:' + rawData.reg21,
      reg22: '22:' + rawData.reg22,
      reg92: '92:' + rawData.reg92
    }

    const formData = this.makeFormData(nonParsedStrings);

    console.log("The data before sending the post request...");
    console.log(formData);

    return axios.post(`${API_URL}/save-register`, formData, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }
}

export default new DataService()