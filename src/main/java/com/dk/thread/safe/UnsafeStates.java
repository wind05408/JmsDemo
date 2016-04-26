package com.dk.thread.safe;

/**
 * @author dk
 * @date 2016/4/25
 * 发布和逸出
 */
public class UnsafeStates {
    private String[] states = new String[]{"AK","AL"};

    public String[] getStates(){
        return states;
    }
}
