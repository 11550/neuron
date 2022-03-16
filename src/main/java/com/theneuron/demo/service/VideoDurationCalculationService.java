package com.theneuron.demo.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class VideoDurationCalculationService {

    public synchronized int calc() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextLong(1000, 10001));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ThreadLocalRandom.current().nextInt(61);
    }
}