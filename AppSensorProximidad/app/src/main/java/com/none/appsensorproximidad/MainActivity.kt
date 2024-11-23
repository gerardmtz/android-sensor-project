package com.none.appsensorproximidad

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var proximitySensor: Sensor? = null
    private lateinit var tvStatus: TextView

    private val PERMISSION_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tv_status)

        // Verifica y solicita permisos
        checkAndRequestPermissions()

        // Configura el sensor de proximidad
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (proximitySensor == null) {
            tvStatus.text = "Sensor de proximidad no disponible"
            Toast.makeText(this, "El dispositivo no tiene sensor de proximidad", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Registra el listener del sensor
        proximitySensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        // Desregistra el listener del sensor
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_PROXIMITY) {
            val maxRange = proximitySensor?.maximumRange ?: Float.MAX_VALUE
            if (event.values[0] < maxRange) {
                tvStatus.text = "Sensor cubierto: colgando llamada"
                hangUpCall()
            } else {
                tvStatus.text = "Sensor no cubierto"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No se utiliza
    }

    private fun hangUpCall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
            try {
                val success = telecomManager.endCall()
                if (!success) {
                    Toast.makeText(this, "Error: No se pudo colgar la llamada.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: SecurityException) {
                Toast.makeText(this, "Error: Permiso no concedido para colgar llamadas.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Función no soportada en versiones de Android anteriores a 9.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
                // Solicita permisos
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ANSWER_PHONE_CALLS),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido para colgar llamadas", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permiso denegado para colgar llamadas", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
