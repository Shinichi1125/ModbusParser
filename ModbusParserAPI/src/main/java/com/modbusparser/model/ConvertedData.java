package com.modbusparser.model;

public class ConvertedData {
	
	private int negativeEnergyAccumulator;
	private int signalQuality;
	
	public ConvertedData() {
		
	}
	
	public int getNegativeEnergyAccumulator() {
		return negativeEnergyAccumulator;
	}

	public void setNegativeEnergyAccumulator(int negativeEnergyAccumulator) {
		this.negativeEnergyAccumulator = negativeEnergyAccumulator;
	}

	public int getSignalQuality() {
		return signalQuality;
	}

	public void setSignalQuality(int signalQuality) {
		this.signalQuality = signalQuality;
	}

}
