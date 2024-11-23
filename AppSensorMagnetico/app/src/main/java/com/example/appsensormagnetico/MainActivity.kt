package com.example.appsensormagnetico

/**
 * Importación de paquetes:
 *
 * android.hardware.*:
 * Para trabajar con sensores del teléfono.
 *
 * android.view.animation.*:
 * Para manejar animaciones, como rotación.
 *
 * androidx.appcompat.app.AppCompatActivity:
 * Base para las actividades de diseño de Android.
 */




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

// Definimos la actividad principal e implementamos
// la clase SensorEventListener
class MainActivity : AppCompatActivity(), SensorEventListener {

    /**
     * Declaramos las variables globales
     * que serán utilizadas para la aplicación
     */

    /**
     * imageView:
     * Referencia al componente que contendrá la imagen de brújula.
     */

    /**
     * mGravity:
     * Almacena datos del acelerómetro. Captura las fuerzas en los ejes X, Y, Z.
     *
     * Su uso principal es en operaciones para conocer cómo está orientado el dispositivo.
     *
     * El cáculo es tomando en cuenta la aceleración percibida debido a la gravedad
     * del eje terrestre.
     */

    /**
     * mGeomagnetic:
     * Mide la fuerza y la dirección del campo magnético terrestre
     * o de cualquier campo magnético cercano.
     *
     * Los valores también se presentan como X, Y, Z
     *
     * Su principal uso es para proporcionar datos sobre Norte magnético
     *
     */

    /**
     * Todos estos filtros se relacionan con las siguientes rotaciones:
     * azimuth: Rotación en torno al eje Z.
     * pitch: Rotación en torno al eje X.
     * roll: Rotación en torno al eje Y.
     */


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
        // Coloca el fondo de pantalla de color negro usando programación
        // window.decorView.setBackgroundColor(Color.BLACK)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageView = findViewById(R.id.compass)
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    }

    /**
    * SENSOR_DElAY_GAME
    * constante que especifica
    * el fecuencia en que los eventos del sensor
    * son reportados la aplicación
    *
    * La tasa de refresco de los datos es de 20 ms
    *
    **/

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

        /**
        *   APLICACION DEL FILTRO DE BAJO PASO
        *
        *   El propósito de utilizar estos filtros
        *   es reducir los picos de ccaídas repentinas en las lecturas
        *   proporcionando valores más estables.
        *
        *   Mejora la precisión de la lectura, obteniendo
        *   datos más representativos del movimiento o
        *   la orientación real del dispositivo
        *
        *   Evita calculos incorrectors al calcular
        *   correctamente las matrices de rotación
        *   y orientación.
        *
        * */

        /**
         * FACTOR DE SUAVIZADO
         *
         * El factor de suavizado determina el peso del valor
         * anterior frente al nuevo
         *
         * Los valores cercanos a 1 priorizan estabilidad de los datos
         * (datos suavizados)
         *
         * Valores menores priorizan la reactividad (datos rápidos pero ruidosos)
         *
         * */

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

        /**
         * MATRICES DE ROTACION E INCLINACIÓN
         *
         *  R -> rotacion
         *  Describe cómo rota el dispositivo tomando
         *  el sistema de coordenadas del mundo real
         *
         *  I -> inclinacion
         *  Describre la orientación del dispositivo
         *  tomando en cuenta la inclinación del campo mangético
         *  terrestre
         *
         *  Su función es determinar azimuth (ángulo respecto al norte)
         *  así como Pitch (inclinación hacia delante y hacia atrás) y
         *  roll (inclinación lateral)
         *
         * */

        val R = FloatArray(9);
        val I = FloatArray(9);

        val success: Boolean = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);


        /**
         * getOrientation: Convierte la matriz de rotación en ángulos (azimuth, pitch, roll).
         * azimuth: Dirección en grados (0° a 360°).
         */


        if (success) {
            val orientation = FloatArray(3);
            SensorManager.getOrientation(R, orientation);
            azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
            azimuth = (azimuth + 360) % 360;

            /**
             *  ANIMACIÓN DE LA BRÚJULA
             *
             *  NOTA: los valores currentAzimuth y azimuth están negativos
             *  en la llamada a RotateAnimation porque el sistema de coordenadas
             *  utilizado para las rotaciones en Android (y muchas otras plataformas gráficas)
             *  sigue la convención de rotaciones en sentido horario como negativas y
             *  rotaciones en sentido antihorario como positivas.
             *
             * */

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