package com.example.tricorderdemo;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

@SuppressLint("NewApi")
public class AccelerometerSensor extends BaseSensor {
	AccelerometerSensor(SensorManager sm) {
		p_sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	@Override
	public void Update(SensorEvent event) {
		super.Update(event);
	}
}