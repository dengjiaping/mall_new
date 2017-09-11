package com.giveu.shoppingmall.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LimitQueue<E> {

    private int limit;

    private LinkedList<E> queue = new LinkedList<E>();

    public LimitQueue(int limit) {
        this.limit = limit;
    }

    public boolean offer(E e) {
        if (queue.contains(e)) {
            queue.remove(e);
        }

        if (queue.size() >= limit) {
            queue.poll();
        }

        return queue.offer(e);
    }

    public E get(int position) {
        return queue.get(position);
    }

    public E getLast() {
        return queue.getLast();
    }

    public int getLimit() {
        return limit;
    }

    public int getSize() {
        return queue.size();
    }

    public LinkedList<E> values() {
        return queue;
    }

    public void empty() {
        while (queue.size() > 0) {
            queue.poll();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        LimitQueue<String> queue = new LimitQueue<String>(5);
        queue.offer("1");
        queue.offer("2");
        queue.offer("3");
        queue.offer("4");
        queue.offer("5");
        queue.offer("6");
        queue.offer("7");

        ArrayList<String> list = new ArrayList<String>();
        list.addAll(queue.values());

        System.out.println("result = " + Arrays.toString(list.toArray()));

    }

}
