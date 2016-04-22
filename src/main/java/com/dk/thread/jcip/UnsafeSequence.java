package com.dk.thread.jcip;

import net.jcip.annotations.NotThreadSafe;

/**
 * @author dk
 * @date 2016/4/22
 * 非线程安全的序列生成器
 */
@NotThreadSafe
public class UnsafeSequence {
    private int value;

    public int getNext(){
        return value++;
    }
}
