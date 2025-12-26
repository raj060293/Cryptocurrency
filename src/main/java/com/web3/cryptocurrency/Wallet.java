package com.web3.cryptocurrency;

import com.web3.blockchain.Blockchain;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Wallet {

    //users of the network
    //used for signature
    private PrivateKey privateKey;

    //verification
    //address: RIPMD public key(160 bits)
    private PublicKey publicKey;

    public Wallet() {
        KeyPair keyPair = CryptographyHelper.ellipticCurveCrypto();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    //WE ARE ABLE TO TRANSFER MONEY!!!
    //Miners of the blockchain will put this transaction into the blockchain
    public Transaction transferMoney(PublicKey receiver, double amount) {
        if (calculateBalance() < amount) {
            System.out.println("Invalid transaction. Amount is greater than balance");
            return null;
        }

        //we store the inputs for the transaction in this array
        List<TransactionInput> transactionInputs = new ArrayList<>();

        //let's find our unspent transactions(the blockchain stores all the UTXOs)
        for(Map.Entry<String, TransactionOutput> item: Blockchain.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.isMine(this.publicKey)) {
                transactionInputs.add(new TransactionInput(UTXO.getId()));
            }
        }

        //let's create the new transaction
        Transaction transaction = new Transaction(publicKey, receiver, amount, transactionInputs);
        //the sender signs the transaction
        transaction.generateSignature(privateKey);
        return transaction;

    }

    //there is no balance associated with users
    //UTXOs and consider all the transactions in the past
    public double calculateBalance() {
        double balance = 0;
        for (Map.Entry<String, TransactionOutput> item: Blockchain.UTXOs.entrySet()) {
            TransactionOutput transactionOutput = item.getValue();
            if (transactionOutput.isMine(publicKey)) {
                balance += transactionOutput.getAmount();
            }
        }
        return balance;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
