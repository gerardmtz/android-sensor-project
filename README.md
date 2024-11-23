# Aplicaciones Android: AppSensorMagnetico y AppSensorProximidad

Este repositorio contiene dos aplicaciones independientes que utilizan los sensores del dispositivo Android para funcionalidades específicas.

# 1. AppSensorMagnetico

## Descripción

Una aplicación sencilla que muestra la orientación en tiempo real utilizando los sensores del dispositivo para apuntar hacia el norte magnético.

## Características

- Muestra la dirección hacia el norte magnético.
- Animaciones suaves que reflejan los cambios de orientación.
- Utiliza el acelerómetro y el magnetómetro del dispositivo.

## Requisitos

- Android 5.0 o superior.
- Sensores: Acelerómetro y Magnetómetro.
- Descarga e instalación
- Permitir instalaciones desde fuentes desconocidas:
- Ve a Ajustes > Seguridad > Orígenes desconocidos y actívalo.

## Uso
- Abre la aplicación.
- Mantén el dispositivo en posición horizontal y observa cómo la brújula apunta al norte magnético.
- Si la brújula no es precisa, calibra el sensor moviendo el dispositivo en forma de "8".

# 2. AppSensorProximidad

## Descripción
Una aplicación que utiliza el sensor de proximidad del dispositivo para detectar si el sensor está cubierto o descubierto. Si se cubre el sensor durante una llamada, la aplicación intenta colgar automáticamente.

## Características
- Detecta si el sensor de proximidad está cubierto.
- Muestra mensajes en tiempo real sobre el estado del sensor.
- Finaliza llamadas automáticamente al cubrir el sensor (requiere permisos).

## Requisitos
- Android 6.0 o superior.
- Sensor de proximidad.

## Permisos:
- Responder y finalizar llamadas.
- Descarga e instalación
- Permitir instalaciones desde fuentes desconocidas:
- Ve a Ajustes > Seguridad > Orígenes desconocidos y actívalo.
- Instalar la aplicación:

## Uso
- Abre la aplicación.
- Durante una llamada telefónica:
- Si cubres el sensor de proximidad, la aplicación intentará colgar automáticamente.
- Si no estás en una llamada, simplemente muestra el estado del sensor.

## Notas importantes

- **Permisos:** Es necesario conceder el permiso de finalizar llamadas para que la funcionalidad de colgar llamadas funcione correctamente.
Compatibilidad: La función de colgar llamadas está soportada solo en Android 9 (Pie) o superior.
Contribuciones

Si deseas colaborar con mejoras o tienes ideas para nuevas funcionalidades, ¡no dudes en enviar tus sugerencias o abrir un pull request!
¡Gracias por usar nuestras aplicaciones! ♥️
