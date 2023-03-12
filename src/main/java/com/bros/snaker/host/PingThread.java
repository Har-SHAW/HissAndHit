package com.bros.snaker.host;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class PingThread implements Runnable {
    CyclicBarrier cyclicBarrier;

    PingThread(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        while (true) {
            try {
                cyclicBarrier.await();
                cyclicBarrier.reset();
                Thread.sleep(125);
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
