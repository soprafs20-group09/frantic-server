package ch.uzh.ifi.seal.soprafs20.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

public abstract class Pile<E> extends Stack<E> {
    private ArrayList<E> elements;


    public Pile(){
        this.elements = new ArrayList<E>();
    }

    public Pile(E[] list){
        this.elements = new ArrayList<E>();
        Collections.addAll(this.elements, list);
    }

    public Object[] getCards() {
        return elements.toArray(new Object[elements.size()]);
    }

    public synchronized E push(E item) {
        if (elements.add(item))
            return item;
        else
            return null;
    }

    public synchronized E pop() {
        return elements.remove(elements.size() - 1);
    }

    public synchronized E peek() {
        return elements.get(elements.size() - 1);
    }

    public synchronized E peekSecond() {
        return elements.get(elements.size() - 2);
    }

    public synchronized int size() {
        return this.elements.size();
    }
}
