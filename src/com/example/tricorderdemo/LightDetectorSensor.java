package com.example.tricorderdemo;

//package android.tricorder;
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

@SuppressLint("NewApi")
public class LightDetectorSensor extends BaseSensor {
	LightDetectorSensor(SensorManager sm) {
		p_sensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
	}

	@Override
	public void Update(SensorEvent event) {
		super.Update(event);
	}
}