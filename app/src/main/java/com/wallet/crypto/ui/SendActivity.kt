package com.wallet.crypto.ui

//import org.ethereum.geth.Address;

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import com.google.android.gms.common.api.CommonStatusCodes
import com.wallet.crypto.R
import com.wallet.crypto.MercuryConstants
import com.wallet.crypto.ext.toast
import com.wallet.crypto.ui.barcode.QRScanActivity
import com.wallet.crypto.util.BalanceUtils
import com.wallet.crypto.viewmodel.SendViewModel
import com.wallet.crypto.viewmodel.SendViewModelFactory
import org.web3j.crypto.WalletUtils
import javax.inject.Inject

class SendActivity : BaseActivity() {

    @Inject
    internal lateinit var sendViewModelFactory: SendViewModelFactory
    private lateinit var viewModel: SendViewModel

    private var toAddressText: EditText? = null
    private var amountText: EditText? = null

    // In case we're sending tokens
    private var sendingTokens = false
    private var contractAddress: String? = null
    private var decimals: Int = 0
    private var symbol: String? = null
    private var toInputLayout: TextInputLayout? = null
    private var amountInputLayout: TextInputLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_send)
        toolbar()

        viewModel = ViewModelProviders.of(this, sendViewModelFactory)
                .get(SendViewModel::class.java)

        toInputLayout = findViewById(R.id.to_input_layout)
        toAddressText = findViewById(R.id.send_to_address)
        amountInputLayout = findViewById(R.id.amount_input_layout)
        amountText = findViewById(R.id.send_amount)

        contractAddress = intent.getStringExtra(MercuryConstants.EXTRA_CONTRACT_ADDRESS)
        decimals = intent.getIntExtra(MercuryConstants.EXTRA_DECIMALS, MercuryConstants.ETHER_DECIMALS)
        symbol = intent.getStringExtra(MercuryConstants.EXTRA_SYMBOL)
        symbol = if (symbol == null) MercuryConstants.ETH_SYMBOL else symbol
        sendingTokens = intent.getBooleanExtra(MercuryConstants.EXTRA_SENDING_TOKENS, false)

        setTitle(getString(R.string.title_send) + " " + symbol)
        amountInputLayout!!.hint = getString(R.string.hint_amount) + " " + symbol

        // Populate to address if it has been passed forward
        val toAddress = intent.getStringExtra(MercuryConstants.EXTRA_ADDRESS)
        if (toAddress != null) {
            toAddressText!!.setText(toAddress)
        }

        val scanBarcodeButton = findViewById<ImageButton>(R.id.scan_barcode_button)
        scanBarcodeButton.setOnClickListener { view ->
            val intent = Intent(applicationContext, QRScanActivity::class.java)
            intent.putExtra(QRScanActivity.EXTRA_TYPE, QRScanActivity.SCAN_ONLY)
            startActivityForResult(intent, QRScanActivity.REQUEST_CODE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.send_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_next -> {
                onNext()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == QRScanActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val address = data.getStringExtra(QRScanActivity.EXTRA_ADDRESS)
                    if (WalletUtils.isValidAddress(address)) {
                        toAddressText?.setText(address)
                        toAddressText?.setSelection(address?.length ?: 0)
                    } else {
                        toast(R.string.wallet_added_fatal).show()
                    }
                }
            } else {
                Log.e("SEND", String.format(getString(R.string.barcode_error_format),
                        CommonStatusCodes.getStatusCodeString(resultCode)))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun onNext() {
        // Validate input fields
        var inputValid = true
        val to = toAddressText!!.text.toString()
        if (!isAddressValid(to)) {
            toInputLayout!!.error = getString(R.string.error_invalid_address)
            inputValid = false
        }
        val amount = amountText!!.text.toString()
        if (!isValidAmount(amount)) {
            amountInputLayout!!.error = getString(R.string.error_invalid_amount)
            inputValid = false
        }

        if (!inputValid) {
            return
        }

        toInputLayout!!.isErrorEnabled = false
        amountInputLayout!!.isErrorEnabled = false

        val amountInSubunits = BalanceUtils.baseToSubunit(amount, decimals)
        viewModel.openConfirmation(this, to, amountInSubunits, contractAddress, decimals, symbol, sendingTokens)
    }

    internal fun isAddressValid(address: String): Boolean {
        return WalletUtils.isValidAddress(address)
    }

    internal fun isValidAmount(eth: String): Boolean {
        try {
            val wei = BalanceUtils.EthToWei(eth)
            return wei != null
        } catch (e: Exception) {
            return false
        }

    }

    companion object {

        private val BARCODE_READER_REQUEST_CODE = 1
    }
}
