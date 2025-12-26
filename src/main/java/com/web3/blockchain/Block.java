package com.web3.blockchain;

import com.web3.constants.Constants;
import com.web3.cryptocurrency.CryptographyHelper;
import com.web3.cryptocurrency.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {

    private int id;
    private int nonce;
    private long timestamp;
    private String hash;
    private String previousHash;
    private List<Transaction> transactions;

    public Block(String previousHash) {
        this.transactions = new ArrayList<>();
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        generateHash();

    }

    public void generateHash() {
        String dataToHash = id
                + previousHash
                + timestamp
                + nonce
                + transactions.toString();

        this.hash = CryptographyHelper.generateHash(dataToHash);
    }

    public void incrementNonce() {
        this.nonce++;
    }

    public String getHash() {
        return hash;
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }

        if (!previousHash.equals(Constants.GENESIS_PREV_HASH)) {
            if (!transaction.verifyTransaction()) {
                System.out.println("Transaction is not valid");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction is valid and is added to the block " + this);
        return true;

    }

    @Override
    public String toString() {
        return "Block{" +
                "id=" + id +
                ", hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                //", transaction='" + transactions.toString() + '\'' +
                '}';
    }
}
