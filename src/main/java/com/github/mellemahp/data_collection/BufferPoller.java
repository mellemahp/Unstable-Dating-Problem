package com.github.mellemahp.data_collection;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import lombok.CustomLog;

@CustomLog
public class BufferPoller implements Callable<Integer> {
    private BlockingQueue<SQLiteDataContainer> dataBus; 
    private final PoisonVial poisonVial;
    private final SQLiteWriter writer;

    public BufferPoller(BlockingQueue<SQLiteDataContainer> dataBusRef, int capacity, SQLiteWriter sqliteWriter) {
        this.dataBus = dataBusRef;
        this.poisonVial = new PoisonVial(capacity);
        writer = sqliteWriter;
    }

    @Override
    public Integer call() { 
        log.info("Polling for data");
        pollTillPoisoned();
        log.info("Poison Vial full. Closing poller...");

        return 0;
    }

    private void pollForData() { 
        log.info("Data bus size: " + this.dataBus.size() + "\t Poison Vial Size: " + this.poisonVial.getSize());

        SQLiteDataContainer result = this.dataBus.poll();
        if (result != null) {
            if (result instanceof PoisonPill) {
                this.poisonVial.addPoisonPill(result);
                log.info("I've been poisoned! x.x");
            } else {
                this.writer.add(result);
            }
        }
    }

    private void pollTillPoisoned() { 
        while (this.poisonVial.isNotFull()) {
            pollForData();
        }

        log.info("Flushing buffer...");
        this.writer.flushBuffer();
        log.info("Done.");
    }
}