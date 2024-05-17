package com.example.projekt.security

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.projekt.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Authentication : AppCompatActivity() {

    private var cancellationSignal: CancellationSignal? = null

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() = @RequiresApi(Build.VERSION_CODES.P)

        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                notifyUser(getString(R.string.auth_error) + " $errString")
                finish()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                notifyUser(getString(R.string.auth_success))
            }
        }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //check if device has security enabled
        checkBiometricSupport()

        //screenshot prevention
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        val biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle(getString(R.string.app_name))
            .setSubtitle(getString(R.string.auth_subtitle))
            .setDescription(getString(R.string.auth_subtitle2))
            .setNegativeButton(getString(R.string.cancel), this.mainExecutor, DialogInterface.OnClickListener { dialog, which ->
                notifyUser(getString(R.string.cancel_notify))
                finish()
            }).build()

        biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
    }

    private fun notifyUser(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getCancellationSignal() : CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser(getString(R.string.cancel_signal))
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager: KeyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        //user does not have screen lock enabled on their device
        if (!keyguardManager.isKeyguardSecure) {
            notifyUser(getString(R.string.auth_not_enabled))
            return false
        }
        return packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }
}
