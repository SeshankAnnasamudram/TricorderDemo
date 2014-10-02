package com.example.tricorderdemo;

//package android.tricorder;
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

@SuppressLint("NewApi")
public class GyroscopeSensor extends BaseSensor {
	GyroscopeSensor(SensorManager sm) {
		p_sensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
	}

	@Override
	public void Update(SensorEvent event) {
		super.Update(event);
	}
}