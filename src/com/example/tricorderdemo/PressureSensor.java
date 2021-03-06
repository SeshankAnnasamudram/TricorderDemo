package com.example.tricorderdemo;

//package android.tricorder;
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

@SuppressLint("NewApi")
public class PressureSensor extends BaseSensor {
	PressureSensor(SensorManager sm) {
		p_sensor = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);
	}

	@Override
	public void Update(SensorEvent event) {
		super.Update(event);
	}
}