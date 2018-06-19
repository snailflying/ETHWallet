package com.wallet.crypto.repository;

import android.util.Log;

import com.wallet.crypto.entity.NetworkInfo;
import com.wallet.crypto.entity.Token;
import com.wallet.crypto.entity.TokenInfo;
import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.service.EthplorerTokenService;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;

public class TokenRepository {

    private final EthplorerTokenService tokenNetworkService;
    private final RealmTokenSource tokenLocalSource;
    private final OkHttpClient httpClient;
    private final EthereumNetworkRepository ethereumNetworkRepository;
    private Web3j web3j;

    public TokenRepository(
            OkHttpClient okHttpClient,
            EthereumNetworkRepository ethereumNetworkRepository,
            EthplorerTokenService tokenNetworkService,
            RealmTokenSource tokenLocalSource) {
        this.httpClient = okHttpClient;
        this.ethereumNetworkRepository = ethereumNetworkRepository;
        this.tokenNetworkService = tokenNetworkService;
        this.tokenLocalSource = tokenLocalSource;
        this.ethereumNetworkRepository.addOnChangeDefaultNetwork(this::buildWeb3jClient);
        buildWeb3jClient(ethereumNetworkRepository.getDefaultNetwork());
    }

    private void buildWeb3jClient(NetworkInfo defaultNetwork) {
        web3j = Web3jFactory.build(new HttpService(defaultNetwork.rpcServerUrl, httpClient, false));
    }

    public Observable<Token[]> fetch(String walletAddress) {
        return Observable.create(e -> {
            NetworkInfo defaultNetwork = ethereumNetworkRepository.getDefaultNetwork();
            Wallet wallet = new Wallet(walletAddress);
            Token[] tokens = tokenLocalSource.fetch(defaultNetwork, wallet)
                    .map(items -> {
                        int len = items.length;
                        Token[] result = new Token[len];
                        for (int i = 0; i < len; i++) {
                            result[i] = new Token(items[i], null);
                        }
                        return result;
                    })
                    .blockingGet();
            e.onNext(tokens);

            updateTokenInfoCache(defaultNetwork, wallet);
            tokens = tokenLocalSource.fetch(defaultNetwork, wallet)
                    .map(items -> {
                        int len = items.length;
                        Token[] result = new Token[len];
                        for (int i = 0; i < len; i++) {
                            BigDecimal balance = null;
                            try {
                                balance = getBalance(wallet, items[i]);
                            } catch (Exception e1) {
                                Log.d("TOKEN", "Err", e1);
                                /* Quietly */
                            }
                            result[i] = new Token(items[i], balance);
                        }
                        return result;
                    }).blockingGet();
            e.onNext(tokens);
        });
    }

    public Completable addToken(Wallet wallet, String address, String symbol, int decimals) {
        Log.e("aaron", "wallet:" + wallet + "address:" + address + "symbol:" + symbol + "decimals:" + decimals);
        return tokenLocalSource.put(
                ethereumNetworkRepository.getDefaultNetwork(),
                wallet,
                new TokenInfo(address, "", symbol, decimals));
    }

    private void updateTokenInfoCache(NetworkInfo defaultNetwork, Wallet wallet) {
        if (!defaultNetwork.isMainNetwork) {
            return;
        }
        tokenNetworkService
                .fetch(wallet.getAddress())
                .flatMapCompletable(items -> Completable.fromAction(() -> {
                    for (TokenInfo tokenInfo : items) {
                        try {
                            tokenLocalSource.put(
                                    ethereumNetworkRepository.getDefaultNetwork(), wallet, tokenInfo)
                                    .blockingAwait();
                        } catch (Throwable t) {
                            Log.d("TOKEN_REM", "Err", t);
                        }
                    }
                }))
                .blockingAwait();
    }

    private BigDecimal getBalance(Wallet wallet, TokenInfo tokenInfo) throws Exception {
        org.web3j.abi.datatypes.Function function = balanceOf(wallet.getAddress());
        String responseValue = callSmartContractFunction(function, tokenInfo.address, wallet);

        List<Type> response = FunctionReturnDecoder.decode(
                responseValue, function.getOutputParameters());
        if (response.size() == 1) {
            return new BigDecimal(((Uint256) response.get(0)).getValue());
        } else {
            return null;
        }
    }

    private static org.web3j.abi.datatypes.Function balanceOf(String owner) {
        return new org.web3j.abi.datatypes.Function(
                "balanceOf",
                Collections.singletonList(new Address(owner)),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
    }

    private String callSmartContractFunction(
            org.web3j.abi.datatypes.Function function, String contractAddress, Wallet wallet) throws Exception {
        String encodedFunction = FunctionEncoder.encode(function);

        Log.d("aaron", "address:" + wallet.getAddress());
        org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction(wallet.getAddress(), contractAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .sendAsync().get();

        return response.getValue();
    }

    public static String createTokenTransferData(String to, BigInteger tokenAmount) {
        List<Type> params = Arrays.<Type>asList(new Address(to), new Uint256(tokenAmount));

        List<TypeReference<?>> returnTypes = Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
        });

        Function function = new Function("transfer", params, returnTypes);
        String encodedFunction = FunctionEncoder.encode(function);
        //Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(encodedFunction))
        return encodedFunction;
    }
}
