package com.wallet.crypto.repository;

import com.wallet.crypto.entity.NetworkInfo;
import com.wallet.crypto.entity.ServiceException;
import com.wallet.crypto.entity.Transaction;
import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.service.BlockExplorerClient;
import com.wallet.crypto.service.GethKeystoreAccountService;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class TransactionRepository {

    private final EthereumNetworkRepository networkRepository;
    private final GethKeystoreAccountService accountKeystoreService;
    private final TransactionLocalSource transactionLocalSource;
    private final BlockExplorerClient blockExplorerClient;

    public TransactionRepository(
            EthereumNetworkRepository networkRepository,
            GethKeystoreAccountService accountKeystoreService,
            TransactionLocalSource inMemoryCache,
            TransactionLocalSource inDiskCache,
            BlockExplorerClient blockExplorerClient) {
        this.networkRepository = networkRepository;
        this.accountKeystoreService = accountKeystoreService;
        this.blockExplorerClient = blockExplorerClient;
        this.transactionLocalSource = inMemoryCache;

        this.networkRepository.addOnChangeDefaultNetwork(this::onNetworkChanged);
    }

    public Observable<Transaction[]> fetchTransaction(Wallet wallet) {
        return Observable.create(e -> {
            Transaction[] transactions = transactionLocalSource.fetchTransaction(wallet).blockingGet();
            if (transactions != null && transactions.length > 0) {
                e.onNext(transactions);
            }
            transactions = blockExplorerClient.fetchTransactions(wallet.getAddress()).blockingFirst();
            transactionLocalSource.clear();
            transactionLocalSource.putTransactions(wallet, transactions);
            e.onNext(transactions);
            e.onComplete();
        });
    }

    public Maybe<Transaction> findTransaction(Wallet wallet, String transactionHash) {
        return fetchTransaction(wallet)
                .firstElement()
                .flatMap(transactions -> {
                    for (Transaction transaction : transactions) {
                        if (transaction.hash.equals(transactionHash)) {
                            return Maybe.just(transaction);
                        }
                    }
                    return null;
                });
    }

    public Single<String> createTransaction(Wallet from, String toAddress, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, String data, String password) {
        final Web3j web3j = Web3jFactory.build(new HttpService(networkRepository.getDefaultNetwork().rpcServerUrl));

        return Single.fromCallable(() -> {
            EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(from.getAddress(), DefaultBlockParameterName.LATEST)
                    .send();
            return ethGetTransactionCount.getTransactionCount();
        })
                .flatMap(
                        nonce -> accountKeystoreService.signTransaction(from, password, toAddress, subunitAmount, gasPrice, gasLimit, nonce.longValue(), data, networkRepository.getDefaultNetwork().chainId))
                .flatMap(
                        signedMessage -> Single.fromCallable(() -> {
                            EthSendTransaction raw = web3j
                                    .ethSendRawTransaction(Numeric.toHexString(signedMessage))
                                    .send();
                            if (raw.hasError()) {
                                throw new ServiceException(raw.getError().getMessage());
                            }
                            return raw.getTransactionHash();

                })).subscribeOn(Schedulers.io());
    }

    private void onNetworkChanged(NetworkInfo networkInfo) {
        transactionLocalSource.clear();
    }
}
