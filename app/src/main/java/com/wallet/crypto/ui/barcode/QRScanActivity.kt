package com.wallet.crypto.ui.barcode

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.wallet.crypto.R
import com.wallet.crypto.ext.toast
import kotlinx.android.synthetic.main.activity_qrscan.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.io.IOException
import java.util.*

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 08/03/2018 17:51
 * @description
 */
class QRScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var type: Byte = 0

    private var mScannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscan)

        type = intent.getByteExtra(EXTRA_TYPE, SCAN_ONLY)

        // BarcodeCapture barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.barcode);
        // barcodeCapture.setRetrieval(this);


        if (hasPermission(this))
            initQRScan(bar_code)
        else
            askForPermissionRead(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initQRScan(frame: FrameLayout?) {
        mScannerView = ZXingScannerView(this)
        frame!!.addView(mScannerView)
        mScannerView!!.setResultHandler(this)
        val supported = ArrayList<BarcodeFormat>()
        supported.add(BarcodeFormat.QR_CODE)
        mScannerView!!.setFormats(supported)
        mScannerView!!.startCamera()
    }

    public override fun onPause() {
        super.onPause()
        if (mScannerView != null)
            mScannerView!!.stopCamera()
    }

    private fun hasPermission(c: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (c.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                return true
            }
        } else {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initQRScan(bar_code)
                } else {
                    toast(R.string.qr_scan_permission).show()
                }
                return
            }
        }
    }

    override fun handleResult(result: Result?) {
        if (result == null) return
        val address = result.text
        try {
            val scanned = AddressEncoder.decode(address)
            val data = Intent()
            data.putExtra(EXTRA_ADDRESS, scanned.address!!.toLowerCase())

            if (scanned.address!!.length > 42 && !scanned.address!!.startsWith("0x") && scanned.amount == null)
                type = PRIVATE_KEY

            if (scanned.amount != null) {
                data.putExtra(EXTRA_AMOUNT, scanned.amount)
                type = REQUEST_PAYMENT
            }

            data.putExtra(EXTRA_TYPE, type)
            setResult(Activity.RESULT_OK, data)
            finish()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {

        const val REQUEST_CODE = 100
        const val REQUEST_CAMERA_PERMISSION = 106
        const val EXTRA_ADDRESS = "ADDRESS"
        const val EXTRA_AMOUNT = "AMOUNT"
        const val EXTRA_TYPE = "TYPE"

        const val SCAN_ONLY: Byte = 0
        const val ADD_TO_WALLETS: Byte = 1
        const val REQUEST_PAYMENT: Byte = 2
        const val PRIVATE_KEY: Byte = 3

        fun askForPermissionRead(c: Activity) {
            if (Build.VERSION.SDK_INT < 23) return
            ActivityCompat.requestPermissions(c, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }

}