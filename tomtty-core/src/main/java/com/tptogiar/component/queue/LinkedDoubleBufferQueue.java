package com.tptogiar.component.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tptogiar
 * @description
 * @date 2022/6/8 - 0:56
 */
public class LinkedDoubleBufferQueue<E>{

    private LinkedList<E> writeQueue = new LinkedList<>();

    private LinkedList<E> readQueue = new LinkedList<>();

    private ReentrantLock readLock = new ReentrantLock();

    private ReentrantLock writeLock = new ReentrantLock();


    public boolean add(E ojb){
        writeLock.lock();
        try {
            return writeQueue.offer(ojb);
        } finally {
            writeLock.unlock();
        }
    }


    public E poll(){
        readLock.lock();
        try {
            if (readQueue.size() == 0){
                switchQueue();
            }
            return readQueue.poll();
        } finally {
            readLock.unlock();
        }
    }

    public E peek(){
        readLock.lock();
        try {
            if (readQueue.size()==0){
                switchQueue();
            }
            return readQueue.peek();
        } finally {
            readLock.unlock();
        }
    }

    public void switchQueue(){
        writeLock.lock();
        try {
            if (writeQueue.size() > 0){
                LinkedList<E> temp = readQueue;
                readQueue = writeQueue;
                writeQueue = temp;
            }
        } finally {
            writeLock.unlock();
        }
    }

    public int size(){
        readLock.lock();
        writeLock.lock();
        try {
            return readQueue.size() + writeQueue.size();
        }finally {
            try {
                writeLock.unlock();
            } finally {
                readLock.unlock();
            }
        }
    }














}
