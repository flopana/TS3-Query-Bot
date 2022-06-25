package Algorand;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.transaction.SignedTransaction;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.util.Encoder;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.AssetHolding;
import com.algorand.algosdk.v2.client.model.PostTransactionsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;


public class AlgorandWallet {
    private final Logger logger = LoggerFactory.getLogger(AlgorandWallet.class.getName());
    private final String ALGOD_API_ADDR;
    private final int ALGOD_PORT;
    private final String ALGOD_API_TOKEN_KEY;
    private final String ALGOD_API_TOKEN;
    private final AlgodClient algodClient;
    private final Account account;

    public AlgorandWallet(String mnemonicSeed, String ALGOD_API_ADDR, int ALGOD_PORT, String ALGOD_API_TOKEN_KEY, String ALGOD_API_TOKEN) throws GeneralSecurityException {
        this.ALGOD_API_ADDR = ALGOD_API_ADDR;
        this.ALGOD_PORT = ALGOD_PORT;
        this.ALGOD_API_TOKEN_KEY = ALGOD_API_TOKEN_KEY;
        this.ALGOD_API_TOKEN = ALGOD_API_TOKEN;
        this.algodClient = connectToNetwork();
        this.account = new Account(mnemonicSeed);
    }

    private AlgodClient connectToNetwork() {
        return new AlgodClient(
            this.ALGOD_API_ADDR,
            this.ALGOD_PORT,
            this.ALGOD_API_TOKEN,
            this.ALGOD_API_TOKEN_KEY);
    }

    public Response<PostTransactionsResponse> sendAsaAsset(String receiver, long amount, long assetId) throws Exception {
        return sendAsaAsset(receiver, "", amount, assetId);
    }

    public Response<PostTransactionsResponse> sendAsaAsset(String receiver, String note, long amount, long assetId) throws Exception {
        var suggestedParams = algodClient.TransactionParams().execute().body();
        logger.debug("Suggested params: " + suggestedParams);
        Transaction txn = Transaction.AssetTransferTransactionBuilder()
                .assetIndex(assetId)
                .sender(account.getAddress())
                .assetAmount(amount) // Amount of assets to send in the assets respective units. In case of SAFT this is 2 decimal places. So amount = 69 means 0.69 SAFT.
                .assetReceiver(receiver)
                .noteUTF8(note)
                .suggestedParams(suggestedParams) // Use suggested params for fee/time values.
                .build();
        SignedTransaction stxn = account.signTransaction(txn);
        var postTxResponse = algodClient.RawTransaction().rawtxn(Encoder.encodeToMsgPack(stxn)).execute();
        logger.info("Send transaction with id: " + postTxResponse.body().txId);

        return postTxResponse;
    }

    public boolean isValidAddressAndIsSubscribedToAsa(String address, long assetId) {
        Address algoAddress;
        try {
            algoAddress = new Address(address);
        } catch (Exception e) {
            logger.info("Invalid address: " + address);
            return false;
        }
        try {
            var accountInfo = algodClient.AccountInformation(algoAddress).execute().body();
            for (AssetHolding assetHolding : accountInfo.assets) {
                if (assetHolding.assetId == assetId) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("Error while checking if address is subscribed to ASA: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return true;
    }

}
