package com.tptogiar.component.queue;

import org.junit.Test;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;


public class LinkedDoubleBufferQueueTest {


    @Test
    public void add() {

        LinkedDoubleBufferQueue<Integer> queue = new LinkedDoubleBufferQueue<>();
        queue.add(1);
        queue.add(2);
        System.out.println(queue.size());

    }


    @Test
    public void poll() {

        LinkedDoubleBufferQueue<Integer> queue = new LinkedDoubleBufferQueue<>();
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.add(5);
        queue.add(6);
        System.out.println(queue.size());
        System.out.println(queue.poll());
    }


    @Test
    public void peek() {

        LinkedDoubleBufferQueue<Integer> queue = new LinkedDoubleBufferQueue<>();
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.add(5);
        queue.add(6);
        System.out.println(queue.size());
        queue.poll();
        System.out.println(queue.peek());

    }


}