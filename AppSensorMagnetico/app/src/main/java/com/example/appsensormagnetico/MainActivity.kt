package com.example.appsensormagnetico

import android.graphics.Color
import android.hardware.GeomagneticField
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), SensorEventListener {

    // Declaring class variables

    lateinit var imageView: ImageView
    val mGravity = FloatArray(3)
    val mGeomagnetic = FloatArray(3)
    var azimuth: Float = 0f
    var currentAzimuth: Float = 0f
    lateinit var mSensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // Set the main app view to black programatically
        // window.decorView.setBackgroundColor(Color.BLACK)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageView = findViewById(R.id.compass)
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    }

    // SENSOR_DELAY_GAME
    // constant that specifies the rate at which
    // the sensor events are delivered to your
    // application.

    // Sets the sensor update rate to
    // 20 miliseconds per update

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this,
            mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_GAME)

        mSensorManager.registerListener(this,
            mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val alpha: Float = 0.97f;

        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            mGravity[0] = alpha*mGravity[0]+(1-alpha)*sensorEvent.values[0];
            mGravity[1] = alpha*mGravity[1]+(1-alpha)*sensorEvent.values[1];
            mGravity[2] = alpha*mGravity[2]+(1-alpha)*sensorEvent.values[2];
        }

        if (sensorEvent.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic[0] = alpha*mGeomagnetic[0]+(1-alpha)*sensorEvent.values[0];
            mGeomagnetic[1] = alpha*mGeomagnetic[1]+(1-alpha)*sensorEvent.values[1];
            mGeomagnetic[2] = alpha*mGeomagnetic[2]+(1-alpha)*sensorEvent.values[2];
        }

        val R = FloatArray(9);
        val I = FloatArray(9);

        val success: Boolean = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);

        if (success) {
            val orientation = FloatArray(3);
            SensorManager.getOrientation(R, orientation);
            azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
            azimuth = (azimuth + 360) % 360;

            // Animate the compass rotation
            val anim: Animation = RotateAnimation(-currentAzimuth, -azimuth, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            currentAzimuth = azimuth;

            anim.duration = 500;
            anim.repeatCount = 0;
            anim.fillAfter= true;

            imageView.startAnimation(anim);
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}