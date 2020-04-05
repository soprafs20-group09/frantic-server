package ch.uzh.ifi.seal.soprafs20.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public abstract class Pile<E> extends Stack<E> {
    private ArrayList<E> elements;

    public Pile(){
        this.elements = new ArrayList<E>();
    }

    public Pile(E[] list){
        this.elements = new ArrayList<E>();
        this.elements.addAll(Arrays.asList(list));
    }

    public Object[] getCards() {
        return elements.toArray(new Object[elements.size()]);
    }

    public E push(E item) {
        if (elements.add(item))
            return item;
        else
            return null;
    }

    public E pop() {
        return elements.remove(elements.size() - 1);
    }

    public E peek() {
        return elements.get(elements.size() - 1);
    }

    public E peekSecond() {
        return elements.get(elements.size() - 2);
    }
}
