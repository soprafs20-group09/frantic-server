package ch.uzh.ifi.seal.soprafs20.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrawStackTest {

    @Test
    public void createDrawStack(){
        DrawStack drawStack = new DrawStack();
        // check if constructor works
        System.out.println(drawStack.peek().toString());

        // dummy test
        assertEquals(DrawStack.class, drawStack.getClass());
    }
}