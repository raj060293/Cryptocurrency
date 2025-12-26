package com.web3.app;

import com.web3.blockchain.Block;
import com.web3.blockchain.Blockchain;
import com.web3.constants.Constants;
import com.web3.cryptocurrency.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;

public class App {

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        //Now create wallets + blockchain + the single miner in the network
        Wallet userA = new Wallet();
        Wallet userB = new Wallet();
        Wallet lender = new Wallet();
        Blockchain blockchain = new Blockchain();
        Miner miner = new Miner();

        //Create Genesis transaction that sends 500 coins to userA
        Transaction genesisTransaction = new Transaction(lender.getPublicKey(), userA.getPublicKey(), 500, null);
        genesisTransaction.generateSignature(lender.getPrivateKey());
        genesisTransaction.setTransactionId("0");
        genesisTransaction.outputs
                .add(new TransactionOutput(genesisTransaction.getReceiver(),
                        genesisTransaction.getAmount(),
                        genesisTransaction.getTransactionId()));
        Blockchain.UTXOs.put(genesisTransaction.outputs.get(0).getId(), genesisTransaction.outputs.get(0));

        System.out.println("Constructing the genesis block...");
        Block genesis = new Block(Constants.GENESIS_PREV_HASH);
        genesis.addTransaction(genesisTransaction);
        miner.mine(genesis, blockchain);

        Block block1 = new Block(genesis.getHash());
        System.out.println("\nuserA's balance is " + userA.calculateBalance());
        System.out.println("\nuserA tries to send money(120 coins) to userB....");
        block1.addTransaction(userA.transferMoney(userB.getPublicKey(), 120));
        miner.mine(block1, blockchain);
        System.out.println("\nuserA's balance is " + userA.calculateBalance());
        System.out.println("\nuserB's balance is " + userB.calculateBalance());

        Block block2 = new Block(block1.getHash());
        System.out.println("\nuserA sends more funds(600) than it has...");
        block2.addTransaction(userA.transferMoney(userB.getPublicKey(), 600));
        miner.mine(block2, blockchain);
        System.out.println("\nuserA's balance is " + userA.calculateBalance());
        System.out.println("\nuserB's balance is " + userB.calculateBalance());

        Block block3 = new Block(block2.getHash());
        System.out.println("\nuserB is attempting to send funds(110) to userA...");
        block3.addTransaction(userB.transferMoney(userA.getPublicKey(), 110));
        System.out.println("\nuserA's balance is " + userA.calculateBalance());
        System.out.println("\nuserB's balance is " + userB.calculateBalance());
        miner.mine(block3, blockchain);

        System.out.println("Miner's reward:" + miner.getReward());
    }
}
