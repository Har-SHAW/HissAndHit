package com.bros.snaker.host;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@RequiredArgsConstructor
public class PingThread implements Runnable {
    @NonNull
    CyclicBarrier cyclicBarrier;

    @Override
    public void run() {
        while (true) {
            try {
                cyclicBarrier.await();
                cyclicBarrier.reset();
                Thread.sleep(Duration.ofMillis(150));
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
