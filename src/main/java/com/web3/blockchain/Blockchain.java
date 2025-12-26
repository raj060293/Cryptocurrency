package com.web3.blockchain;

import com.web3.cryptocurrency.TransactionOutput;

import java.util.ArrayList;
import java.util.HashMap;

public class Blockchain {

    //this is a public ledger- everyone can access
    public static ArrayList<Block> blockChain;

    //we store every unspent transactions
    public static HashMap<String, TransactionOutput> UTXOs;

    public Blockchain() {
        Blockchain.UTXOs = new HashMap<>();
        blockChain = new ArrayList<>();
    }

    public void addBlock(Block block) {
        blockChain.add(block);
    }

    public int size() {
        return blockChain.size();
    }

    @Override
    public String toString() {
        String blockchain = "";
        for (Block block: blockChain) {
            blockchain += block.toString()+"\n";

        }
        return blockchain;
    }
}
