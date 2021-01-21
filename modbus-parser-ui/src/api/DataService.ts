import axios from 'axios';
import { API_URL } from '../Constants';

class DataService {
  retrieveConvertedData(id: number){
    return axios.get(`${API_URL}/register/${id}`);
  } 

  getNoOfRegisters(){
    return axios.get(`${API_URL}/no-of-registers`);
  }
}

export default new DataService()