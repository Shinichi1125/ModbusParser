package com.modbusparser.model;

public class ParsedData {
	
	private int negativeEnergyAccumulator;
	private int signalQuality;
	
	public ParsedData() {
		
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
