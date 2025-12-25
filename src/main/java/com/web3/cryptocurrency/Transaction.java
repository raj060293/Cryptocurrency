package com.web3.cryptocurrency;

import com.web3.blockchain.Blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class Transaction {

    //id of the transaction is a hash
    private String transactionId;

    //we use public key to reference sender or receiver
    private PublicKey sender;

    private PublicKey receiver;

    //amount of coins the transaction sends to reciever from sender
    private double amount;

    //make sure the transaction is signedto prevent anyone else from spending the coins
    private byte[] signature;


    public List<TransactionInput> inputs;

    public List<TransactionOutput> outputs;

    public Transaction(PublicKey sender, PublicKey receiver, double amount, List<TransactionInput> inputs) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.inputs = inputs;
        calculateHash();
    }

    public boolean verifyTransaction() {
        if (!verifySignature()) {
            System.out.println("Invalid transaction because of invalid signature...");
            return false;
        }

        //let's gather the unspent transactions(we have to consider the inputs)
        for (TransactionInput transactionInput: inputs) {
            transactionInput.setUTXO(Blockchain.UTXOs.get(transactionInput.getTransactionOutputId()));
        }

        //transactions have 2 parts: send an amount to receiver + send the (balance-amount)
        //back to sender
        //send value to recipient
        outputs.add(new TransactionOutput(this.receiver, amount, transactionId));
        //send the leftover 'change' back to sender
        outputs.add(new TransactionOutput(this.sender, getInputsSum()-amount, transactionId));

        //WE HAVE TO UPDATE THE UTXOS
        //the outputs will be inputs for other transactions (so put them in blockchain UTXOs)
        for (TransactionOutput transactionOutput: outputs) {
            Blockchain.UTXOs.put(transactionOutput.getId(), transactionOutput);
        }

        //remove transaction inputs from blockchain's UTXOs list because they have been spent

        for (TransactionInput transactionInput: inputs) {
            if (transactionInput.getUTXO() != null) {
                Blockchain.UTXOs.remove(transactionInput.getUTXO().getId());
            }
        }
        return true;
    }

    //This is how we calculate how much money the sender has
    private double getInputsSum() {
        double sum = 0;
        for (TransactionInput transactionInput :  inputs) {
            if (transactionInput != null) {
                sum += transactionInput.getUTXO().getAmount();
            }
        }
        return sum;
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = sender.toString() + receiver.toString() + amount;
        signature = CryptographyHelper.sign(privateKey, data);
    }

    public boolean verifySignature() {
        String data = sender.toString() + receiver.toString() + amount;
        return CryptographyHelper.verify(sender, data, signature);
    }
    private void calculateHash() {
        String data = sender.toString() + receiver.toString() + amount;
        this.transactionId =  CryptographyHelper.generateHash(data);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<TransactionInput> inputs) {
        this.inputs = inputs;
    }


    public PublicKey getReceiver() {
        return receiver;
    }

    public void setReceiver(PublicKey receiver) {
        this.receiver = receiver;
    }

    public PublicKey getSender() {
        return sender;
    }

    public void setSender(PublicKey sender) {
        this.sender = sender;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TransactionOutput> outputs) {
        this.outputs = outputs;
    }
}
