package com.example.tricorderdemo;

//package android.tricorder;
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

@SuppressLint("NewApi")
public class ProximitySensor extends BaseSensor {
	ProximitySensor(SensorManager sm) {
		p_sensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
	}

	@Override
	public void Update(SensorEvent event) {
		super.Update(event);
	}
}