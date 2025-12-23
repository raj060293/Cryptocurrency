package com.web3.cryptocurrency;

import com.web3.blockchain.Block;
import com.web3.blockchain.Blockchain;
import com.web3.constants.Constants;

public class Miner {

    private double reward;

    public void mine(Block block, Blockchain blockchain) {

        while(!isGoldenHash(block)) {
            block.incrementNonce();
            block.generateHash();
        }

        System.out.println(block + " has just been mined");
        System.out.println("Hash is  " + block.getHash());
        blockchain.addBlock(block);
        reward += Constants.REWARD;
    }

    private boolean isGoldenHash(Block block) {
        String leadingZeroes = new String(new char[Constants.DIFFICULTY]).replace('\0', '0');
        return block.getHash().substring(0, Constants.DIFFICULTY).equals(leadingZeroes);
    }

    public double getReward() {
        return reward;
    }
}
