/*
 * Android Tricorder
 * @author David Morris
 * @version 1.0.0
 * @date 1/18/2014
 */

package com.example.tricorderdemo;

//package android.tricorder;

import java.math.BigDecimal;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Float2;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

//main class
//@TargetApi(Build.VERSION_CODES.CUPCAKE)
//@SuppressLint("NewApi")
public class Tricorder extends Activity {

	DrawView drawView;
	String title = "";
	Sensors sensors;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// must initialize sensors before the view
		sensors = new Sensors();

		// initialize view
		drawView = new DrawView(this);
		setContentView(drawView);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public void onResume() {
		super.onResume();
		drawView.resume();
		sensors.Resume();
	}

	@Override
	public void onPause() {

		super.onPause();
		drawView.pause();
		sensors.Pause();
	}

	/*
	 * @Model
	 * 
	 * Part of setting up the Subject side of things for the concrete observers
	 * that are our Sensor objects. Of course, this will wind up calling a lot
	 * helper methods and private classes for more on past this comment for more
	 * on that.
	 */

	@SuppressLint("NewApi")
	public class DrawView extends SurfaceView implements Runnable {
		volatile boolean running = false;
		Thread gameloop = null;
		SurfaceHolder surface = null;
		Paint paint = new Paint();
		TextPrinter text;
		SensorPanel[] panels;

		// constructor
		public DrawView(Context context) {
			super(context);
			surface = getHolder();
			text = new TextPrinter();
			createPanels();
		}

		// pause and resuming
		public void resume() {
			running = true;
			gameloop = new Thread(this);
			gameloop.start();
		}

		public void pause() {
			running = false;
			while (true) {
				try {
					gameloop.join();
				} catch (InterruptedException e) {
				}
			}
		}

		// thread process
		@Override
		public void run() {
			while (running) {
				if (!surface.getSurface().isValid())
					continue;
				Canvas canvas = surface.lockCanvas();
				updateSensors();
				drawSensors(canvas);
				surface.unlockCanvasAndPost(canvas);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

		/**
		 * @View
		 * 
		 *       Panel creation helper method - called in the drawView
		 *       constructor Recall that the View is principally a composite of
		 *       nested components. In our case, our component objects is going
		 *       to be things like TextPrinter, SensorPanel, etc.
		 * 
		 *       The View is also responsible for attaching the command slots
		 *       (aka controllers) to their relevant panels. Methods for doing
		 *       are provided
		 */

		@SuppressLint("NewApi")
		public void createPanels() {

			panels = new SensorPanel[8];
			paint.setTextSize(18);
			paint.setTextAlign(Align.CENTER);

			float y = 0;

			// create Accelerometer Panel
			panels[0] = new SensorPanel(300, 200);
			panels[0].getCanvas().drawText("ACCELEROMETER", 150, 22, paint);
			panels[0].position = new Float2(0, y);
			panels[0].attachSensor(sensors.accelerometer);

			// create Gravity Panel
			panels[1] = new SensorPanel(300, 200);
			panels[1].getCanvas().drawText("GRAVITY", 150, 22, paint);
			panels[1].position = new Float2(310, y);
			panels[1].attachSensor(sensors.gravity);
			y += 230;

			// create Linear Acceleration Panel
			panels[2] = new SensorPanel(300, 200);
			panels[2].getCanvas().drawText("LINEAR ACCELERATION", 150, 22,
					paint);
			panels[2].position = new Float2(0, y);
			panels[2].attachSensor(sensors.linear);

			// create proximity sensor
			panels[3] = new SensorPanel(300, 200);
			panels[3].getCanvas().drawText("PROXIMITY", 150, 22, paint);
			panels[3].position = new Float2(310, y);
			panels[3].attachSensor(sensors.proximity);
			y += 230;

			// create pressure panel
			panels[4] = new SensorPanel(300, 200);
			panels[4].getCanvas().drawText("PRESSURE", 150, 22, paint);
			panels[4].position = new Float2(0, y);
			panels[4].attachSensor(sensors.pressure);

			// create gyroscope panel
			panels[5] = new SensorPanel(300, 200);
			panels[5].getCanvas().drawText("GYROSCOPE", 150, 22, paint);
			panels[5].position = new Float2(310, y);
			panels[5].attachSensor(sensors.gyroscope);
			y += 230;

			// create compass panel
			panels[6] = new SensorPanel(300, 200);
			panels[6].getCanvas().drawText("COMPASS", 150, 22, paint);
			panels[6].position = new Float2(0, y);
			panels[6].attachSensor(sensors.compass);

			// create light detector panel
			panels[7] = new SensorPanel(300, 200);
			panels[7].getCanvas().drawText("LIGHT DETECTOR", 150, 22, paint);
			panels[7].position = new Float2(310, y);
			panels[7].attachSensor(sensors.light);

		}

		/*
		 * @View
		 * 
		 * An update helper method - called in run()
		 * 
		 * Delegates the job to the updateSensor (that's update sensor WITHOUT a
		 * S at the end) method
		 */
		public void updateSensors() { // <--that's updateSensors WITH AN S
			for (int n = 0; n < panels.length; n++) {
				BaseSensor sensor = panels[n].getSensor();
				if (sensor.getSensor() != null)
					updateSensor(panels[n]);
			}
		}

		/*
		 * @View
		 * 
		 * Here's we the delegation is handled in the View's update method,
		 * courtesy of the TextPrinter class covered later down the code
		 */
		public void updateSensor(SensorPanel panel) {
			BaseSensor sensor = panel.getSensor();
			panel.Clear();
			text.setCanvas(panel.getCanvas());
			text.setColor(Color.BLUE);
			text.Draw("" + sensor.getName(), 125, 55);
			text.Draw("" + sensor.getVendor());
			text.Draw("" + sensor.getVersion());
			text.Draw("" + sensor.getResolution());
			text.Draw("" + sensor.getMaximumRange());
			text.Draw("X: " + round(sensor.getData().x));
			text.Draw("Y: " + round(sensor.getData().y));
			text.Draw("Z: " + round(sensor.getData().z));

		}

		// drawing helper method called in run
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@SuppressLint("NewApi")
		public void drawSensors(Canvas canvas) {
			Paint paint = new Paint();
			for (int n = 0; n < panels.length; n++) {
				canvas.drawBitmap(panels[n].getBitmap(), panels[n].position.x,
						panels[n].position.y, paint);
			}

		}

		// helper to round numbers to 2 by default
		public double round(double value) {
			return round(value, 2);
		}

		// Or any number of decimals, just in case
		public double round(double value, int precision) {
			try {
				BigDecimal bd = new BigDecimal(value);
				BigDecimal rounded = bd.setScale(precision,
						BigDecimal.ROUND_HALF_UP);
				return rounded.doubleValue();
			} catch (Exception e) {
				Log.e("round", "error rounding");
			}
			return 0;
		}
	}

	/**
	 * @Model
	 * @Controller
	 * 
	 *             Amongst the first questions we gotta ask is
	 *             "Just what kind of sensors is this particular machine packing?"
	 * 
	 *             This Sensors class encapsulates the answer to this question,
	 *             as not all mobile devices are certain to contain every type
	 *             of hardware sensor.
	 * 
	 *             Pattern Blend: Command and Observer Pattern. Technically,
	 *             methinks this would be the "Invoker" of a command pattern
	 *             and/or the Concrete Observer in an Observer pattern at once.
	 *             SensorEventListener would be the abstract Observer in this
	 *             case.
	 * 
	 *             Whatever.
	 * 
	 *             Regardless of the above statement's academic accuracy, either
	 *             way, the Sensors class handles all that hardware registration
	 *             and Update calls. Provided the hardware supports it.
	 * 
	 *             What happens on Update, is naturally, the BaseSensor's job
	 *             and stuff. Blah blah Strategy Pattern.
	 * 
	 *             I was getting sleepy when I wrote the rant on this section.
	 */

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	public class Sensors implements SensorEventListener {
		// Reference variables initialized as null for device safety and
		// compatibility
		SensorManager sensors = null;
		AccelerometerSensor accelerometer = null;
		GravitySensor gravity = null;
		CompassSensor compass = null;
		GyroscopeSensor gyroscope = null;
		LightDetectorSensor light = null;
		PressureSensor pressure = null;
		ProximitySensor proximity = null;
		LinearAccelerationSensor linear = null;

		// Class constructor.
		Sensors() {
			// Step 1: Create sensor manager object with SENSOR_SERVICE (as
			// opposed to other Android services, like LOCATION or WIFI or
			// whatever)
			sensors = (SensorManager) getSystemService(SENSOR_SERVICE);

			/*
			 * Step 2: Create individual sensors. Anything not on the device is
			 * already made null.
			 */
			accelerometer = new AccelerometerSensor(sensors);
			gravity = new GravitySensor(sensors);
			compass = new CompassSensor(sensors);
			gyroscope = new GyroscopeSensor(sensors);
			light = new LightDetectorSensor(sensors);
			pressure = new PressureSensor(sensors);
			proximity = new ProximitySensor(sensors);
			linear = new LinearAccelerationSensor(sensors);

		}

		/**
		 * @Controller An onSensorChanged method like this is among the prime
		 *             meat of proper Controller code.
		 * 
		 *             Recap that the Controller in MVC = the Strategy Pattern
		 *             (for the view).
		 * 
		 *             It listens for input from the user and figures out what
		 *             output updates it needs from the Model objects based on
		 *             Strategy Pattern.
		 * 
		 *             In our case, as the user moves the device about,
		 *             sensorEvents grabbed from android.hardware triggers the
		 *             call to update appropriately in accordance to proper
		 *             behavior.
		 * 
		 *             Behavior that's been decoupled via the BaseSensor Class
		 *             and its subclasses.
		 */
		// Invalids to be handled by previous null declarations
		@Override
		public void onSensorChanged(SensorEvent event) {
			switch (event.sensor.getType()) {

			// process accelerometer update
			case Sensor.TYPE_ACCELEROMETER:
				if (accelerometer.getSensor() != null)
					accelerometer.Update(event);
				break;

			// process gravity update
			case Sensor.TYPE_GRAVITY:
				if (gravity.getSensor() != null)
					gravity.Update(event);
				break;

			// process compass update
			case Sensor.TYPE_MAGNETIC_FIELD:
				if (compass.getSensor() != null)
					compass.Update(event);
				break;

			// process gyroscope update
			case Sensor.TYPE_GYROSCOPE:
				if (gyroscope.getSensor() != null)
					gyroscope.Update(event);
				break;

			// process light update
			case Sensor.TYPE_LIGHT:
				if (light.getSensor() != null)
					light.Update(event);
				break;

			// process pressure update
			case Sensor.TYPE_PRESSURE:
				if (pressure.getSensor() != null)
					pressure.Update(event);
				break;

			// process proximity update
			case Sensor.TYPE_PROXIMITY:
				if (proximity.getSensor() != null)
					proximity.Update(event);
				break;

			// process proximity update
			case Sensor.TYPE_LINEAR_ACCELERATION:
				if (linear.getSensor() != null)
					linear.Update(event);
				break;
			}
		}

		// This override is necessary as the SensorEventListener Interface
		// requires it.
		@SuppressLint("NewApi")
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}

		// pause conditions
		public void Pause() {
			sensors.unregisterListener(this);
		}

		// Resume conditions
		// @SuppressLint("NewApi")
		public void Resume() {

			// register accelerometer sensor listener - missing hardware,
			// naturally, won't be registered
			if (accelerometer.getSensor() != null)
				sensors.registerListener(this, accelerometer.getSensor(),
						SensorManager.SENSOR_DELAY_NORMAL);
			else
				Log.d("Resume", "Accelerometer sensor is null");

			if (gravity.getSensor() != null)
				sensors.registerListener(this, gravity.getSensor(),
						SensorManager.SENSOR_DELAY_NORMAL);
			else
				Log.d("Resume", "Gravity sensor is null");

			if (compass.getSensor() != null)
				sensors.registerListener(this, compass.getSensor(),
						SensorManager.SENSOR_DELAY_NORMAL);
			else
				Log.d("Resume", "Compass sensor is null");

			if (gyroscope.getSensor() != null)
				sensors.registerListener(this, gyroscope.getSensor(),
						SensorManager.SENSOR_DELAY_NORMAL);
			else
				Log.d("Resume", "Gyroscope sensor is null");

			if (light.getSensor() != null)
				sensors.registerListener(this, light.getSensor(),
						SensorManager.SENSOR_DELAY_NORMAL);
			else
				Log.d("Resume", "Light sensor is null");

			if (pressure.getSensor() != null)
				sensors.registerListener(this, pressure.getSensor(),
						SensorManager.SENSOR_DELAY_NORMAL);
			else
				Log.d("Resume", "Pressure sensor is null");

			if (proximity.getSensor() != null)
				sensors.registerListener(this, proximity.getSensor(),
						SensorManager.SENSOR_DELAY_NORMAL);
			else
				Log.d("Resume", "Proximity sensor is null");

			if (linear.getSensor() != null)
				sensors.registerListener(this, linear.getSensor(),
						SensorManager.SENSOR_DELAY_NORMAL);
			else
				Log.d("Resume", "Linear Acceleration sensor is null");

		}
	}

	/**
	 * @View Another Component Class amongst the View Composite. Eases the
	 *       reprinting of multiple lines. No need to keep track of the exact
	 *       position of a line on account of how it calls in the initial
	 *       overload on Draw, so that subsequent calls can't help but to fall
	 *       into position.
	 */
	public class TextPrinter {
		private Canvas p_canvas;
		private Paint p_paint;
		private float p_x, p_y;
		private float p_spacing;

		TextPrinter() {
			this(null);
		}

		TextPrinter(Canvas canvas) {
			p_canvas = canvas;
			p_paint = new Paint();
			p_x = p_y = 0;
			p_spacing = 22;
			setTextSize(18);
			setColor(Color.WHITE);

		}

		public void setCanvas(Canvas canvas) {
			p_canvas = canvas;
		}

		public void setLineSpacing(float spacing) {
			p_spacing = spacing;

		}

		public void setTextSize(float size) {
			p_paint.setTextSize(size);
		}

		public void setColor(int color) {
			p_paint.setColor(color);

		}

		public void Draw(String text, float x, float y) {
			p_x = x;
			p_y = y;
		}

		public void Draw(String text) {
			p_canvas.drawText(text, p_x, p_y, p_paint);
			p_y += p_spacing;
		}
	}

	/**
	 * @View =VIEW of MVC= GUI container for the project. Standardizes the
	 *       bitmap display amongst sensors. Includes background color, boarder
	 *       color, title and content area.
	 * 
	 *       Its decoupled from controller registrations, so if we ever want to
	 *       create alternate sensor panel displays we'll just make a modified
	 *       version of this.
	 * */

	@SuppressLint("NewApi")
	public class SensorPanel {
		// TO-DO - reference variables
		private Bitmap p_bitmap;
		private Canvas p_canvas;
		private int p_width, p_height;
		private Paint p_paint;
		private TextPrinter p_text;
		private BaseSensor p_sensor;
		public Float2 position;

		SensorPanel(int width, int height) {

			p_width = width;
			p_height = height;
			p_sensor = new BaseSensor();
			position = new Float2(0, 0);
			p_bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			p_canvas = new Canvas(p_bitmap);
			p_paint = new Paint();
			p_canvas.drawColor(Color.rgb(180, 180, 180));
			p_paint.setColor(Color.rgb(230, 230, 230));
			p_canvas.drawRect(3, 3, p_width - 4, 28, p_paint);

			p_text = new TextPrinter();
			Clear();
		}

		public BaseSensor getSensor() {
			return p_sensor;
		}

		/**
		 * @Controller p_sensor variable in the construction of a sensor is null
		 *             until this method fills it. This amongst other things
		 *             makes it easier for us to extend the Tricorder with new
		 *             sensor types in the future.
		 */
		public void attachSensor(BaseSensor sensor) {
			p_sensor = sensor;
		}

		// clears only content, not the title
		public void Clear() {
			p_paint.setColor(Color.rgb(230, 230, 230));
			p_canvas.drawRect(3, 32, p_width - 4, p_height - 4, p_paint);
			p_text.setCanvas(p_canvas);
			p_text.setColor(Color.BLACK);
			p_text.Draw("Model: ", 10, 55);
			p_text.Draw("Vendor: ");
			p_text.Draw("Version: ");
			p_text.Draw("Resolution: ");
			p_text.Draw("Max range: ");
			p_text.Draw("DATA: ");
		}

		public Bitmap getBitmap() {
			return p_bitmap;
		}

		public Canvas getCanvas() {
			return p_canvas;
		}

	}
}