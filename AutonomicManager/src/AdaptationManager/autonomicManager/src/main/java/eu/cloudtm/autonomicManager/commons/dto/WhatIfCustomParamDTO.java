package eu.cloudtm.autonomicManager.commons.dto;

import eu.cloudtm.autonomicManager.commons.Forecaster;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/17/13
 */
public class WhatIfCustomParamDTO {

    public enum Xaxis {
        NODES, DEGREE, PROTOCOL
    }

    private Xaxis xaxis = Xaxis.NODES;
    private Integer fixedDegree = 2;
    private Integer fixedNodes = 10;
    private ReplicationProtocol fixedProtocol = ReplicationProtocol.TWOPC;
    private Set<Forecaster> forecasters = new HashSet<Forecaster>();

    private Double acf = -1D;
    private double avgGetsPerWrTransaction = -1; //  # GET per write transaction
    private Long avgGetsPerROTransaction = -1L;   // #GET per read only transaction
    private Long avgRemoteGetRtt = -1L;  //  Remote get latency
    private Long localReadOnlyTxLocalServiceTime = -1L; // Read Only transaction demand
    private Long localUpdateTxLocalServiceTime = -1L;  // Write transaction demand
    private Double percentageSuccessWriteTransactions = -1D;  // Write Transactions Percentage
    private Double avgNumPutsBySuccessfulLocalTx = -1D;  // #put per write tx
    private Long avgPrepareCommandSize = -1L; // size of prepare msg
    private Long avgPrepareAsync = -1L;   // prepare latency
    private Long avgCommitAsync = -1L;    // commit latency

    public Xaxis getXaxis() {
        return xaxis;
    }

    public void setXaxis(Xaxis xaxis) {
        this.xaxis = xaxis;
    }

    public Integer getFixedNodes() {
        return fixedNodes;
    }

    public void setFixedNodes(Integer fixedNodes) {
        this.fixedNodes = fixedNodes;
    }

    public void setFixedDegree(Integer val){
        if(val<=0)
            throw new IllegalArgumentException("Replication degree must be >0");
        fixedDegree = val;
    }

    public int getFixedDegree(){
        return fixedDegree;
    }

    public void setFixedProtocol(ReplicationProtocol repProt){
        this.fixedProtocol = repProt;
    }

    public ReplicationProtocol getFixedProtocol(){
        return fixedProtocol;
    }

    public void addForecaster(Forecaster forecaster){
        forecasters.add(forecaster);
    }

    public boolean removeForecaster(Forecaster forecaster){
        return forecasters.remove(forecaster);
    }

    public Set<Forecaster> getForecasters(){
        return forecasters;
    }

    public double getACF() {
        return acf;
    }

    public void setACF(double acf) {
        this.acf = acf;
    }

    public double getAvgGetsPerWrTransaction() {
        return avgGetsPerWrTransaction;
    }

    public void setAvgGetsPerWrTransaction(double avgGetsPerWrTransaction) {
        this.avgGetsPerWrTransaction = avgGetsPerWrTransaction;
    }

    public double getAvgGetsPerROTransaction() {
        return avgGetsPerROTransaction;
    }

    public void setAvgGetsPerROTransaction(Long avgGetsPerROTransaction) {
        this.avgGetsPerROTransaction = avgGetsPerROTransaction;
    }

    public double getAvgRemoteGetRtt() {
        return avgRemoteGetRtt;
    }

    public void setAvgRemoteGetRtt(Long avgRemoteGetRtt) {
        this.avgRemoteGetRtt = avgRemoteGetRtt;
    }

    public Long getLocalReadOnlyTxLocalServiceTime() {
        return localReadOnlyTxLocalServiceTime;
    }

    public void setLocalReadOnlyTxLocalServiceTime(Long localReadOnlyTxLocalServiceTime) {
        this.localReadOnlyTxLocalServiceTime = localReadOnlyTxLocalServiceTime;
    }

    public Long getLocalUpdateTxLocalServiceTime() {
        return localUpdateTxLocalServiceTime;
    }

    public void setLocalUpdateTxLocalServiceTime(Long localUpdateTxLocalServiceTime) {
        this.localUpdateTxLocalServiceTime = localUpdateTxLocalServiceTime;
    }

    public Double getPercentageSuccessWriteTransactions() {
        return percentageSuccessWriteTransactions;
    }

    public void setPercentageSuccessWriteTransactions(Double percentageSuccessWriteTransactions) {
        this.percentageSuccessWriteTransactions = percentageSuccessWriteTransactions;
    }

    public Double getAvgNumPutsBySuccessfulLocalTx() {
        return avgNumPutsBySuccessfulLocalTx;
    }

    public void setAvgNumPutsBySuccessfulLocalTx(Double avgNumPutsBySuccessfulLocalTx) {
        this.avgNumPutsBySuccessfulLocalTx = avgNumPutsBySuccessfulLocalTx;
    }

    public Long getAvgPrepareCommandSize() {
        return avgPrepareCommandSize;
    }

    public void setAvgPrepareCommandSize(Long avgPrepareCommandSize) {
        this.avgPrepareCommandSize = avgPrepareCommandSize;
    }

    public double getAvgPrepareAsync() {
        return avgPrepareAsync;
    }

    public void setAvgPrepareAsync(Long avgPrepareAsync) {
        this.avgPrepareAsync = avgPrepareAsync;
    }

    public Long getAvgCommitAsync() {
        return avgCommitAsync;
    }

    public void setAvgCommitAsync(Long avgCommitAsync) {
        this.avgCommitAsync = avgCommitAsync;
    }

}
