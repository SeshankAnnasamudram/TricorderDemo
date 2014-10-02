package com.example.tricorderdemo;

//package android.tricorder;
import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Build;
/**
 * I find it funny how the IDE made me add @SuppressLint("NewApi") for
 * everything else when I started commenting and refactoring this Tricorder
 * stuff on Oct 1 2014. Yet this @TargetAPI thing from back in January isn't
 * causing a new magic error.
 * 
 * One of these days I'm going to have to master the answer as to why. This this
 * newfangled annotation stuff for Java 5 is still all young and confuzzled and
 * loitering on my lawn for an ol' J2EE dinosaur like me.
 * 
 * Dag gum IDEs just adding dem @Target whatever whenever dey want. No respect.
 * 
 * Signed -Another Rant Brought to you by Sleep Deprivation
 */

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class LinearAccelerationSensor extends BaseSensor {
	LinearAccelerationSensor(SensorManager sm) {
		p_sensor = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	}

	@Override
	public void Update(SensorEvent event) {
		super.Update(event);
	}
}