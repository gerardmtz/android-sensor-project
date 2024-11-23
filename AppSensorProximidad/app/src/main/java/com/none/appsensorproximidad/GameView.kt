package com.none.appsensorproximidad

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random

class GameView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), Runnable {

    private val thread: Thread = Thread(this)
    private var isPlaying = true

    private val playerPaint = Paint().apply { color = Color.BLUE }
    private val obstaclePaint = Paint().apply { color = Color.RED }

    private var playerX = 100f
    private var playerY = 500f
    private var playerRadius = 50f
    private var isJumping = false
    private var jumpVelocity = -20f
    private var gravity = 2f

    private var obstacleX = 800f
    private var obstacleY = 500f
    private var obstacleWidth = 100f
    private var obstacleHeight = 100f
    private var obstacleSpeed = 10f

    private val surfaceHolder: SurfaceHolder = holder

    init {
        surfaceHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                thread.start()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder, format: Int, width: Int, height: Int
            ) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                isPlaying = false
                thread.join()
            }
        })
    }

    override fun run() {
        while (isPlaying) {
            update()
            draw()
            Thread.sleep(16) // ~60fps
        }
    }

    private fun update() {
        // Movimiento del jugador
        if (isJumping) {
            playerY += jumpVelocity
            jumpVelocity += gravity
            if (playerY >= 500f) { // Volver al suelo
                playerY = 500f
                isJumping = false
                jumpVelocity = -20f
            }
        }

        // Movimiento del obstáculo
        obstacleX -= obstacleSpeed
        if (obstacleX + obstacleWidth < 0) {
            obstacleX = Random.nextInt(800, 1200).toFloat()
        }

        // Colisión
        if (playerX + playerRadius > obstacleX && playerX - playerRadius < obstacleX + obstacleWidth &&
            playerY + playerRadius > obstacleY
        ) {
            isPlaying = false // Detener el juego al colisionar
        }
    }

    private fun draw() {
        if (surfaceHolder.surface.isValid) {
            val canvas: Canvas = surfaceHolder.lockCanvas()
            canvas.drawColor(Color.WHITE)

            // Dibujar jugador
            canvas.drawCircle(playerX, playerY, playerRadius, playerPaint)

            // Dibujar obstáculo
            canvas.drawRect(
                obstacleX,
                obstacleY,
                obstacleX + obstacleWidth,
                obstacleY + obstacleHeight,
                obstaclePaint
            )

            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    fun jump() {
        if (!isJumping) {
            isJumping = true
        }
    }
}
