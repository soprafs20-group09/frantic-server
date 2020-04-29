package ch.uzh.ifi.seal.soprafs20.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public abstract class Pile<E> extends Stack<E> {
    private final ArrayList<E> elements;


    public Pile() {
        this.elements = new ArrayList<E>();
    }

    public Object[] getCards() {
        return elements.toArray(new Object[elements.size()]);
    }

    @Override
    public synchronized E push(E item) {
        if (elements.add(item))
            return item;
        else
            return null;
    }

    @Override
    public synchronized E pop() {
        return elements.remove(elements.size() - 1);
    }

    @Override
    public synchronized E peek() {
        return elements.get(elements.size() - 1);
    }

    public synchronized E peekN(int n) {
        return elements.get(elements.size() - n);
    }

    @Override
    public synchronized int size() {
        return this.elements.size();
    }

    public void shuffle() {
        Collections.shuffle(elements);
    }
}
