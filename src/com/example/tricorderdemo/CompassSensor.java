package com.example.tricorderdemo;

//package android.tricorder;
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

@SuppressLint("NewApi")
public class CompassSensor extends BaseSensor {
	@SuppressLint("InlinedApi")
	CompassSensor(SensorManager sm) {
		p_sensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	@Override
	public void Update(SensorEvent event) {
		super.Update(event);
	}
}