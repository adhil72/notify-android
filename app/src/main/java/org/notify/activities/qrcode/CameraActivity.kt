package org.notify.activities.qrcode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.json.JSONObject
import org.notify.R

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0) // Use the device's back camera
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(false)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                this@CameraActivity.finish()
            } else {
                // Handle successful scan
                val scannedText = result.contents
                try {
                    val data = JSONObject(scannedText)
                    if (!data.has("access") || !data.has("token")) return this@CameraActivity.finish()

                    this@CameraActivity.setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra("data",scannedText)
                    });
                    this@CameraActivity.finish()
                }catch (e:Exception){
                    this@CameraActivity.finish()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
