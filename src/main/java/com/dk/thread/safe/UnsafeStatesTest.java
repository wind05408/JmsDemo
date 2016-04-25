package com.dk.thread.safe;

/**
 * @author dk
 * @date 2016/4/25
 * 发布和逸出的测试
 */
public class UnsafeStatesTest {
    public static void main(String[] args) {
        UnsafeStates dog = new UnsafeStates();
        String[] states = dog.getStates();
        states[0] = "SB";

        for (int i = 0; i < states.length; i++) {
            System.out.println(states[i]);
        }
    }
}
