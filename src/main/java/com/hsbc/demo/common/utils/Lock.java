package com.hsbc.demo.common.utils;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lock util
 */
public class Lock {
    private final ConcurrentHashMap<String, LockInfo> lockMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    private static class LockInfo {
        final java.util.concurrent.locks.Lock lock = new ReentrantLock();
        ScheduledFuture<?> renewalTask;
    }


    public void tryLock(String lockKey, long waitTime, TimeUnit timeUnit) throws InterruptedException, TimeoutException {
        LockInfo lockInfo = lockMap.computeIfAbsent(lockKey, k -> new LockInfo());
        if (!lockInfo.lock.tryLock(waitTime, timeUnit)) {
            throw new TimeoutException("Failed to acquire lock for key: " + lockKey);
        }
        startRenewalTask(lockInfo, lockKey);
    }


    public void unlock(String lockKey) {
        LockInfo lockInfo = lockMap.get(lockKey);
        if (lockInfo != null) {
            stopRenewalTask(lockInfo);
            lockInfo.lock.unlock();
            lockMap.remove(lockKey);
        }
    }


    private void startRenewalTask(LockInfo lockInfo, String lockKey) {
        long renewalInterval = 30;
        lockInfo.renewalTask = scheduler.scheduleAtFixedRate(() -> {
            if (lockInfo.lock.tryLock()) {
                try {
                    System.out.println("Lock renewed for key: " + lockKey);
                } finally {
                    lockInfo.lock.unlock();
                }
            } else {
                System.out.println("Lock renewal failed for key: " + lockKey);
            }
        }, renewalInterval, renewalInterval, TimeUnit.SECONDS);
    }


    private void stopRenewalTask(LockInfo lockInfo) {
        if (lockInfo.renewalTask != null) {
            lockInfo.renewalTask.cancel(true);
        }
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}
