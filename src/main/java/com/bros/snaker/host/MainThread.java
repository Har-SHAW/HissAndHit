package com.bros.snaker.host;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@RequiredArgsConstructor
public class MainThread implements Runnable {
    @NonNull
    Socket socket;
    @NonNull CyclicBarrier cyclicBarrier;

    @Override
    public void run() {
        while (true) {
            try {
                cyclicBarrier.await();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(1);
            } catch (InterruptedException | BrokenBarrierException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
