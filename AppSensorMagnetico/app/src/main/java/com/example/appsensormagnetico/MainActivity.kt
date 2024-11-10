package com.example.appsensormagnetico

import android.graphics.Color
import android.hardware.GeomagneticField
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
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
    val azimuth: Float = 0f
    val currentAzimuth: Float = 0f
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

    // Sets the sensor uptdate rate to
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
            
        }


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

}