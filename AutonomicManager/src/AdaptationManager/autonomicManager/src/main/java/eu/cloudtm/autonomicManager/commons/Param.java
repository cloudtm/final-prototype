package eu.cloudtm.autonomicManager.commons;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/8/13
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public enum Param {

    NumAbortedTxDueToReadLock ( 0, "NumAbortedTxDueToReadLock", Long.class),
    AverageReplicationTime ( 0, "AverageReplicationTime", Long.class),
    RemoteUpdateTxPrepareServiceTime  ( 0, "RemoteUpdateTxPrepareServiceTime", Long.class),
    ReadWriteRatio  ( 0, "ReadWriteRatio", Double.class),
    NumberOfCommits  ( 0, "NumberOfCommits", Long.class),
    LocalUpdateTxPrepareResponseTime  ( 0, "LocalUpdateTxPrepareResponseTime", Long.class),
    AvgCommitAsync  ( 0, "AvgCommitAsync", Long.class),
    AvgNTCBTime  ( 0, "AvgNTCBTime", Long.class),
    AvgGetsPerWrTransaction ( 0, "AvgGetsPerWrTransaction", Long.class),
    AvgNumSyncSentCommitMsgs  ( 0, "AvgNumSyncSentCommitMsgs", Long.class),
    NumberOfLocalCommits  ( 0, "NumberOfLocalCommits", Long.class),
    NumberOfLocksHeld  ( 0, "NumberOfLocksHeld", Integer.class),
    AvgClusteredGetCommandSize  ( 0, "AvgClusteredGetCommandSize", Long.class),
    ReadOnlyTxTotalCpuTime  ( 0, "ReadOnlyTxTotalCpuTime", Double.class),
    FailedCommits ( 0, "FailedCommits", Long.class),
    LocalReadOnlyTxCommitResponseTime ( 0, "LocalReadOnlyTxCommitResponseTime", Long.class),
    AvgPutsPerWrTransaction ( 0, "AvgPutsPerWrTransaction", Double.class),
    SuccessfulCommits ( 0, "SuccessfulCommits", Long.class),
    NumAbortedTxDueToValidation ( 0, "NumAbortedTxDueToValidation", Long.class),
    LocalReadOnlyTxLocalServiceTime ( 0, "LocalReadOnlyTxLocalServiceTime", Long.class),
    AvgNumNodesPrepare ( 0, "AvgNumNodesPrepare", Long.class),
    AvgLocalSuccessfulLockHoldTime ( 0, "AvgLocalSuccessfulLockHoldTime", Long.class),
    AvgLocalRemoteAbortLockHoldTime ( 0, "AvgLocalRemoteAbortLockHoldTime", Long.class),
    RemoteUpdateTxRollbackResponseTime ( 0, "RemoteUpdateTxRollbackResponseTime", Long.class),
    AvgLocalGetTime ( 0, "AvgLocalGetTime", Long.class),
    AvgSuccessfulTxCommit ( 0, "AvgSuccessfulTxCommit", Double.class),
    AvgTCBTime ( 0, "AvgTCBTime", Long.class),
    LocalReadOnlyTxLocalResponseTime ( 0, "LocalReadOnlyTxLocalResponseTime", Long.class),
    AvgRollbackAsync ( 0, "AvgRollbackAsync", Long.class),
    AvgFailedTxCommit ( 0, "AvgFailedTxCommit", Double.class),
    AvgTxArrivalRate ( 0, "AvgTxArrivalRate", Double.class),
    AvgCommitRtt ( 0, "AvgCommitRtt", Long.class),
    RemoteGetServiceTime ( 0, "RemoteGetServiceTime", Long.class),
    AvgLockHoldTime ( 0, "AvgLockHoldTime", Long.class),
    GMUClusteredGetCommandResponseTime ( 0, "GMUClusteredGetCommandResponseTime", Double.class),
    AvgLocalLockHoldTime ( 0, "AvgLocalLockHoldTime", Long.class),
    AverageWriteTime ( 0, "AverageWriteTime", Long.class),
    NumLocalRemoteLockContentions ( 0, "NumLocalRemoteLockContentions", Long.class),
    AvgNumAsyncSentCommitMsgs ( 0, "AvgNumAsyncSentCommitMsgs", Long.class),
    NumOwnedRdItemsInRemotePrepare ( 0, "NumOwnedRdItemsInRemotePrepare", Double.class),
    AvgGetsPerROTransaction ( 0, "AvgGetsPerROTransaction", Long.class),
    LocalUpdateTxTotalResponseTime ( 0, "LocalUpdateTxTotalResponseTime", Double.class),
    AvgRemoteTxCompleteNotifyTime ( 0, "AvgRemoteTxCompleteNotifyTime", Long.class),
    SwitchCoolDownTime ( 0, "SwitchCoolDownTime", Integer.class),
    LocalUpdateTxCommitServiceTime ( 0, "LocalUpdateTxCommitServiceTime", Long.class),
    AvgNumOfLockRemoteTx ( 0, "AvgNumOfLockRemoteTx", Long.class),
    WaitedTimeInRemoteCommitQueue ( 0, "WaitedTimeInRemoteCommitQueue", Long.class),
    LocalUpdateTxRemoteRollbackResponseTime ( 0, "LocalUpdateTxRemoteRollbackResponseTime", Long.class),
    PercentageSuccessWriteTransactions ( 0, "PercentageSuccessWriteTransactions", Double.class),
    LockContentionProbability ( 0, "LockContentionProbability", Double.class),
    ReadOnlyTxTotalResponseTime ( 0, "ReadOnlyTxTotalResponseTime", Double.class),
    NumberOfRemotePuts ( 0, "NumberOfRemotePuts", Long.class),
    AvgCompleteNotificationAsync ( 0, "AvgCompleteNotificationAsync", Long.class),
    AvgNumNodesCompleteNotification ( 0, "AvgNumNodesCompleteNotification", Long.class),
    NumAbortedTxDueToNotLastValueAccessed ( 0, "NumAbortedTxDueToNotLastValueAccessed", Long.class),
    NumOwnedRdItemsInLocalPrepare ( 0, "NumOwnedRdItemsInLocalPrepare", Double.class),
    ElapsedTime ( 0, "ElapsedTime", Long.class),
    TerminationCost ( 0, "TerminationCost", Long.class),
    NumEarlyAborts ( 0, "NumEarlyAborts", Double.class),
    LocalCommits ( 0, "LocalCommits", Long.class),
    RemotelyDeadXact ( 0, "RemotelyDeadXact", Double.class),
    Throughput ( 0, "Throughput", Double.class),
    CoolDownTime ( 0, "CoolDownTime", Long.class),
    LocalReadOnlyTxPrepareServiceTime ( 0, "LocalReadOnlyTxPrepareServiceTime", Long.class),
    RemoveMisses ( 0, "RemoveMisses", Long.class),
    AvgRemoteGetRtt ( 0, "AvgRemoteGetRtt", Long.class),
    AvgPrepareRtt ( 0, "AvgPrepareRtt", Long.class),
    UpdateXactToPrepare ( 0, "UpdateXactToPrepare", Double.class),
    WriteSkewProbability ( 0, "WriteSkewProbability", Double.class),
    GMUClusteredGetCommandServiceTime ( 0, "GMUClusteredGetCommandServiceTime", Double.class),
    NumRemoteLocalLockContentions ( 0, "NumRemoteLocalLockContentions", Long.class),
    NumLocalLocalLockContentions ( 0, "NumLocalLocalLockContentions", Long.class),
    NumOwnedWrItemsInLocalPrepare ( 0, "NumOwnedWrItemsInLocalPrepare", Double.class),
    ReplicationFailures ( 0, "ReplicationFailures", Long.class),
    ReplicationDegree ( 0, "ReplicationDegree", Long.class),
    RemoteUpdateTxRollbackServiceTime ( 0, "RemoteUpdateTxRollbackServiceTime", Long.class),
    TimeSinceReset ( 0, "TimeSinceReset", Long.class),
    NumberOfRemoteGets ( 0, "NumberOfRemoteGets", Long.class),
    AvgRemoteGetsPerROTransaction ( 0, "AvgRemoteGetsPerROTransaction", Double.class),
    LocalReadOnlyTxCommitServiceTime ( 0, "LocalReadOnlyTxCommitServiceTime", Long.class),
    Rollbacks ( 0, "Rollbacks", Long.class),
    AvgRollbackRtt ( 0, "AvgRollbackRtt", Long.class),
    RemoteContentionProbability ( 0, "RemoteContentionProbability", Double.class),
    LocalUpdateTxLocalServiceTime ( 0, "LocalUpdateTxLocalServiceTime", Long.class),
    LocalUpdateTxLocalRollbackResponseTime ( 0, "LocalUpdateTxLocalRollbackResponseTime", Long.class),
    AvgCommitCommandSize ( 0, "AvgCommitCommandSize", Long.class),
    NumberOfPuts ( 0, "NumberOfPuts", Long.class),
    LocalPrepares ( 0, "LocalPrepares", Long.class),
    LocalReadOnlyTxPrepareResponseTime ( 0, "LocalReadOnlyTxPrepareResponseTime", Long.class),
    HitRatio ( 0, "HitRatio", Double.class),
    Evictions ( 0, "Evictions", Long.class),
    PercentageWriteTransactions ( 0, "PercentageWriteTransactions", Double.class),
    NumWaitedRemoteGets ( 0, "NumWaitedRemoteGets", Long.class),
    AvgNumOfLockLocalTx ( 0, "AvgNumOfLockLocalTx", Long.class),
    AvgNumNodesRollback ( 0, "AvgNumNodesRollback", Long.class),
    NumRemoteRemoteLockContentions ( 0, "NumRemoteRemoteLockContentions", Long.class),
    AvgRemoteGetsPerWrTransaction ( 0, "AvgRemoteGetsPerWrTransaction", Double.class),
    LocalUpdateTxLocalRollbackServiceTime ( 0, "LocalUpdateTxLocalRollbackServiceTime", Long.class),
    NumReadsBeforeWrite ( 0, "NumReadsBeforeWrite", Double.class),
    NumOwnedWrItemsInRemotePrepare ( 0, "NumOwnedWrItemsInRemotePrepare", Double.class),
    AbortRate ( 0, "AbortRate", Double.class),
    RemoteUpdateTxCommitResponseTime ( 0, "RemoteUpdateTxCommitResponseTime", Long.class),
    WaitedTimeInLocalCommitQueue ( 0, "WaitedTimeInLocalCommitQueue", Long.class),
    RemotePutExecutionTime ( 0, "RemotePutExecutionTime", Long.class),
    NumLocalPrepareAborts ( 0, "NumLocalPrepareAborts", Double.class),
    NumAbortedTxDueDeadlock ( 0, "NumAbortedTxDueDeadlock", Long.class),
    AvgNumNodesRemoteGet ( 0, "AvgNumNodesRemoteGet", Long.class),
    AvgClusteredGetCommandReplySize ( 0, "AvgClusteredGetCommandReplySize", Long.class),
    AvgWriteTxDuration ( 0, "AvgWriteTxDuration", Long.class),
    SuccessRatioFloatingPoint ( 0, "SuccessRatioFloatingPoint", Double.class),
    NumNodes ( 0, "NumNodes", Long.class),
    MaxNumberOfKeysToRequest ( 0, "MaxNumberOfKeysToRequest", Integer.class),
    LocalUpdateTxTotalCpuTime ( 0, "LocalUpdateTxTotalCpuTime", Double.class),
    RemoteGetResponseTime ( 0, "RemoteGetResponseTime", Long.class),
    LocalUpdateTxLocalResponseTime ( 0, "LocalUpdateTxLocalResponseTime", Long.class),
    GMUClusteredGetCommandWaitingTime ( 0, "GMUClusteredGetCommandWaitingTime", Double.class),
    NumKilledTxDueToValidation ( 0, "NumKilledTxDueToValidation", Long.class),
    RemoteUpdateTxPrepareResponseTime ( 0, "RemoteUpdateTxPrepareResponseTime", Long.class),
    ApplicationContentionFactor ( 0, "ApplicationContentionFactor", Double.class),
    AvgRemoteLockHoldTime ( 0, "AvgRemoteLockHoldTime", Long.class),
    NumAbortedXacts ( 0, "NumAbortedXacts", Long.class),
    RemoteUpdateTxCommitServiceTime ( 0, "RemoteUpdateTxCommitServiceTime", Long.class),
    AvgNumPutsBySuccessfulLocalTx ( 0, "AvgNumPutsBySuccessfulLocalTx", Double.class),
    NumberOfLocksAvailable ( 0, "NumberOfLocksAvailable", Integer.class),
    NumAbortedTxDueToWriteLock ( 0, "NumAbortedTxDueToWriteLock", Long.class),
    Misses ( 0, "Misses", Long.class),
    NumWaitedLocalCommits ( 0, "NumWaitedLocalCommits", Long.class),
    ConcurrencyLevel ( 0, "ConcurrencyLevel", Integer.class),
    AvgLockWaitingTime ( 0, "AvgLockWaitingTime", Long.class),
    Stores ( 0, "Stores", Long.class),
    CurrentRoundId ( 0, "CurrentRoundId", Long.class),
    AvgRemotePutsPerWrTransaction ( 0, "AvgRemotePutsPerWrTransaction", Double.class),
    LocalUpdateTxPrepareServiceTime ( 0, "LocalUpdateTxPrepareServiceTime", Long.class),
    AvgNumOfLockSuccessLocalTx ( 0, "AvgNumOfLockSuccessLocalTx", Long.class),
    LocalActiveTransactions ( 0, "LocalActiveTransactions", Long.class),
    LocalUpdateTxRemoteRollbackServiceTime ( 0, "LocalUpdateTxRemoteRollbackServiceTime", Long.class),
    LocalUpdateTxCommitResponseTime ( 0, "LocalUpdateTxCommitResponseTime", Long.class),
    AvgPrepareCommandSize ( 0, "AvgPrepareCommandSize", Long.class),
    ReplicationCount ( 0, "ReplicationCount", Long.class),
    AvgNumNodesCommit ( 0, "AvgNumNodesCommit", Long.class),
    NumberOfGets ( 0, "NumberOfGets", Long.class),
    AvgAbortedWriteTxDuration ( 0, "AvgAbortedWriteTxDuration", Long.class),
    AvgRollbackCommandSize ( 0, "AvgRollbackCommandSize", Long.class),
    LocalContentionProbability ( 0, "LocalContentionProbability", Double.class),
    AvgReadOnlyTxDuration ( 0, "AvgReadOnlyTxDuration", Long.class),
    Hits ( 0, "Hits", Long.class),
    Commits ( 0, "Commits", Long.class),
    NumberOfEntries ( 0, "NumberOfEntries", Integer.class),
    LocalRollbacks ( 0, "LocalRollbacks", Long.class),
    RemoveHits ( 0, "RemoveHits", Long.class),
    CurrentEpoch ( 0, "CurrentEpoch", Long.class),
    Prepares ( 0, "Prepares", Long.class),
    NumWaitedRemoteCommits ( 0, "NumWaitedRemoteCommits", Long.class),
    AverageReadTime ( 0, "AverageReadTime", Long.class),
    AvgPrepareAsync ( 0, "AvgPrepareAsync", Long.class),
    AvgLocalLocalAbortLockHoldTime ( 0, "AvgLocalLocalAbortLockHoldTime", Long.class),
    MemoryInfo_free ( 0, "MemoryInfo.free", Long.class),
    MemoryInfo_used ( 0, "MemoryInfo.used", Long.class),
    ;



    private final int id;
    private final String key;
    private final Class clazz;

    private Param(int id, String name, Class clazz){
        this.id = id;
        this.key = name;
        this.clazz = clazz;
    }

    public int getId(){
        return id;
    }

    public String getKey(){
        return key;
    }

    public static Param getById(int id){
        for( Param param : values() ){
            if( param.getId() == id )
                return param;
        }
        return null;
    }

    public static Param getByName(String name){
        for( Param param : values() ){
            if( param.getKey() == name )
                return param;
        }
        return null;
    }

    public <T> T castTo(Object val, Class<T> clz){
        return clz.cast(val);
    }

    public Class getClazz(){
        return clazz;
    }
}
