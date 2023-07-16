package com.ssafy.fingerprint

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.fido)

        btn.setOnClickListener {
            if(checkBiometricAvailability()) authenticateWithFingerprint()
        }
    }

    private fun authenticateWithFingerprint() {
        val executor = ContextCompat.getMainExecutor(this)

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("지문 인식") // 다이얼로그 타이틀 설정
            .setDescription("지문을 사용하여 인증합니다.") // 다이얼로그 설명 설정
            .setNegativeButtonText("취소") // 취소 버튼 텍스트 설정
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun checkBiometricAvailability(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "생체 인식을 지원하는 하드웨어가 없습니다.", Toast.LENGTH_SHORT).show()
                false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "생체 인식 하드웨어를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(this, "생체 인식이 등록되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
                showBiometricEnrollmentSettings()
                false
            }
            else -> {
                Toast.makeText(this, "생체 인식을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }
    private fun showBiometricEnrollmentSettings() {
        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
            putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG)
        }
        if (enrollIntent.resolveActivity(packageManager) != null) {
            startActivity(enrollIntent)
        } else {
            Toast.makeText(this, "생체 인식 등록 화면을 열 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}