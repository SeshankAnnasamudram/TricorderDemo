package com.example.tricorderdemo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Build;
import android.renderscript.Float3;

/**
 * @Model Sensor Super Class - Here be our abstract observer*
 * 
 *        Recap: At the core of the Model, is the ever faithful Observer
 *        pattern. And observers are their own separate thing from the subject.
 * 
 *        Concrete sensors of we're interest in (now or in the future) will
 *        extend off this thing to keep their Behavioral differences neat and
 *        decoupled.
 * 
 *        Among the responsibilities of the model in MVC is to encapsulate and
 *        provide update methods for the controller to utilize upon events of
 *        user input. In our case, user input is done as the user waggles the
 *        phone and what not.
 * 
 *        *(ad: Wait or technically is this the abstract Subject in pattern
 *        lingo speak...? Nah, I think that's Sensors class handled within
 *        Tricorder.java but yeah, whatever. Future me knows for certain and
 *        rants like this is what gets him there).
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class BaseSensor {
	// references
	protected Sensor p_sensor;
	protected Float3 p_data;

	// constructor
	BaseSensor() {
		p_sensor = null;
		p_data = new Float3(0, 0, 0);
	}

	/**
	 * The Ever Important Update Method
	 */
	public void Update(SensorEvent event) {
		p_data.x = event.values[0];
		p_data.y = event.values[1];
		p_data.z = event.values[2];
	}

	// Getter Methods - Sensor, Name, Vendor, Version, Resolution, Max Range and
	// Data
	public Sensor getSensor() {
		return p_sensor;
	}

	public String getName() {
		return p_sensor.getName();
	}

	public String getVendor() {
		return p_sensor.getVendor();
	}

	public int getVersion() {
		return p_sensor.getVersion();
	}

	public float getResolution() {
		return p_sensor.getResolution();
	}

	public float getMaximumRange() {
		return p_sensor.getMaximumRange();
	}

	public Float3 getData() {
		return p_data;
	}
}