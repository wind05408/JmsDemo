package com.dk.thread.safe;

/**
 * @author dk
 * @date 2016/4/25
 * �������ݳ��Ĳ���
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
