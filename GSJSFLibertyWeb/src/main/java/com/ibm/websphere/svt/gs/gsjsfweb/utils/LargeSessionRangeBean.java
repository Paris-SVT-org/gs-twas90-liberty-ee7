/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.utils;

/**
 * @author JAGRAJ
 *
 */
public class LargeSessionRangeBean {
	
	private int counter=0;
	
	private float objectSize=0.0f;
	
	private Float currentObjectSize;

	public Float getCurrentObjectSize() {
		return currentObjectSize;
	}

	public void setCurrentObjectSize(Float currentObjectSize) {
		this.currentObjectSize = currentObjectSize;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public float getObjectSize() {
		return objectSize;
	}

	public void setObjectSize(float objectSize) {
		this.objectSize = objectSize;
	}
	
	

}
