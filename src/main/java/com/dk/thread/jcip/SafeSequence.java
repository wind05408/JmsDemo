package com.dk.thread.jcip;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * @author dk
 * @date 2016/4/22
 * 线程安全的序列生成器
 */
@ThreadSafe
public class SafeSequence {
    //@GuardedBy
    @GuardedBy("this") private int value;

    public synchronized int getNext(){
        return value++;
    }
}
