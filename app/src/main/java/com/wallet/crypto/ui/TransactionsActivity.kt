package com.wallet.crypto.ui

import android.app.AlertDialog
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.design.widget.BottomSheetDialog
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.wallet.crypto.R
import com.wallet.crypto.TrustConstants
import com.wallet.crypto.TrustConstants.ETHEREUM_NETWORK_NAME
import com.wallet.crypto.entity.ErrorEnvelope
import com.wallet.crypto.entity.NetworkInfo
import com.wallet.crypto.entity.Transaction
import com.wallet.crypto.entity.Wallet
import com.wallet.crypto.ext.toast
import com.wallet.crypto.ui.widget.OnDepositClickListener
import com.wallet.crypto.ui.widget.OnTransactionClickListener
import com.wallet.crypto.ui.widget.adapter.TransactionsAdapter
import com.wallet.crypto.util.RootUtil
import com.wallet.crypto.viewmodel.BaseNavigationActivity
import com.wallet.crypto.viewmodel.TransactionsViewModel
import com.wallet.crypto.viewmodel.TransactionsViewModelFactory
import com.wallet.crypto.widget.DepositView
import com.wallet.crypto.widget.EmptyTransactionsView
import com.wallet.crypto.widget.SystemView
import javax.inject.Inject

class TransactionsActivity : BaseNavigationActivity(), View.OnClickListener {

    @Inject
    internal lateinit var transactionsViewModelFactory: TransactionsViewModelFactory
    private var viewModel: TransactionsViewModel? = null

    private var systemView: SystemView? = null
    private var adapter: TransactionsAdapter? = null
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_transactions)

        toolbar()
        setTitle(getString(R.string.unknown_balance_with_symbol))
        setSubtitle("")
        initBottomNavigation()
        dissableDisplayHomeAsUp()

        adapter = TransactionsAdapter(OnTransactionClickListener { view, transaction -> this.onTransactionClick(view, transaction) })
        val refreshLayout = findViewById<SwipeRefreshLayout>(R.id.refresh_layout)
        systemView = findViewById(R.id.system_view)

        val list = findViewById<RecyclerView>(R.id.list)

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        systemView!!.attachRecyclerView(list)
        systemView!!.attachSwipeRefreshLayout(refreshLayout)

        viewModel = ViewModelProviders.of(this, transactionsViewModelFactory!!)
                .get(TransactionsViewModel::class.java)
        viewModel!!.progress().observe(this, Observer<Boolean> { systemView!!.showProgress(it!!) })
        viewModel!!.error().observe(this, Observer<ErrorEnvelope> { this.onError(it!!) })
        viewModel!!.defaultNetwork().observe(this, Observer<NetworkInfo> { this.onDefaultNetwork(it!!) })
        viewModel!!.defaultWalletBalance().observe(this, Observer<Map<String, String>> { this.onBalanceChanged(it!!) })
        viewModel!!.defaultWallet().observe(this, Observer<Wallet> { this.onDefaultWallet(it!!) })
        viewModel!!.transactions().observe(this, Observer<Array<Transaction>> { this.onTransactions(it) })

        refreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { viewModel!!.fetchTransactions() })
    }

    private fun onTransactionClick(view: View, transaction: Transaction) {
        viewModel!!.showDetails(view.context, transaction)
    }

    override fun onResume() {
        super.onResume()

        setTitle(getString(R.string.unknown_balance_without_symbol))
        setSubtitle("")
        adapter!!.clear()
        viewModel!!.prepare()
        checkRoot()
    }

    override fun onPause() {
        super.onPause()

        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)

        val networkInfo = viewModel!!.defaultNetwork().value
        if (networkInfo != null && networkInfo.name == ETHEREUM_NETWORK_NAME) {
            menuInflater.inflate(R.menu.menu_deposit, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                viewModel!!.showSettings(this)
            }
            R.id.action_deposit -> {
                openExchangeDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.try_again -> {
                viewModel!!.fetchTransactions()
            }
            R.id.action_buy -> {
                openExchangeDialog()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_my_address -> {
                viewModel!!.showMyAddress(this)
                return true
            }
            R.id.action_my_tokens -> {
                viewModel!!.showTokens(this)
                return true
            }
            R.id.action_send -> {
                viewModel!!.showSend(this)
                return true
            }
        }
        return false
    }

    private fun onBalanceChanged(balance: Map<String, String>) {
        val actionBar = supportActionBar
        val networkInfo = viewModel!!.defaultNetwork().value
        val wallet = viewModel!!.defaultWallet().value
        if (actionBar == null || networkInfo == null || wallet == null) {
            return
        }
        if (TextUtils.isEmpty(balance[TrustConstants.USD_SYMBOL])) {
            actionBar.title = balance[networkInfo.symbol] + " " + networkInfo.symbol
            actionBar.subtitle = ""
        } else {
            actionBar.title = "$" + balance[TrustConstants.USD_SYMBOL]
            actionBar.subtitle = balance[networkInfo.symbol] + " " + networkInfo.symbol
        }
    }

    private fun onTransactions(transaction: Array<Transaction>?) {
        if (transaction == null || transaction.size == 0) {
            val emptyView = EmptyTransactionsView(this, this)
            emptyView.setNetworkInfo(viewModel!!.defaultNetwork().value)
            systemView!!.showEmpty(emptyView)
        }
        adapter!!.addTransactions(transaction)
        invalidateOptionsMenu()
    }

    private fun onDefaultWallet(wallet: Wallet) {
        adapter!!.setDefaultWallet(wallet)
    }

    private fun onDefaultNetwork(networkInfo: NetworkInfo) {
        adapter!!.setDefaultNetwork(networkInfo)
        setBottomMenu(R.menu.menu_main_network)
    }

    private fun onError(errorEnvelope: ErrorEnvelope) {
        systemView!!.showError(getString(R.string.error_fail_load_transaction), this)
    }

    private fun checkRoot() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        if (RootUtil.isDeviceRooted() && pref.getBoolean("should_show_root_warning", true)) {
            pref.edit().putBoolean("should_show_root_warning", false).apply()
            AlertDialog.Builder(this)
                    .setTitle(R.string.root_title)
                    .setMessage(R.string.root_body)
                    .setNegativeButton(R.string.ok) { dialog, which -> }
                    .show()
        }
    }

    private fun openExchangeDialog() {
        val wallet = viewModel!!.defaultWallet().value
        if (wallet == null) {
            Toast.makeText(this, getString(R.string.error_wallet_not_selected), Toast.LENGTH_SHORT)
                    .show()
        } else {
            val dialog = BottomSheetDialog(this)
            val view = DepositView(this, wallet)
            view.setOnDepositClickListener(OnDepositClickListener { view, uri -> this.onDepositClick(view, uri) })
            dialog.setContentView(view)
            dialog.show()
            this.dialog = dialog
        }
    }

    private fun onDepositClick(view: View, uri: Uri) {
        viewModel!!.openDeposit(this, uri)
    }

    /**
     * 双击返回键退出
     */
    private var mIsExit: Boolean = false

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            when {
//                container.currentItem != TAB_HOME -> container.currentItem = TAB_HOME
                mIsExit -> this.finish()
                else -> {
                    toast(R.string.exit_by_double_click, Toast.LENGTH_SHORT).show()
                    mIsExit = true
                    Handler().postDelayed(Runnable { mIsExit = false }, 2000)
                }
            }
            return true
        }

        return super.onKeyDown(keyCode, event)
    }
}
