package com.example.sequence.entity;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author PlusQi
 */
@Data
public class SequenceContainer {

    /**
     * 当前序列原子值
     */
    private AtomicLong atomicSeq;

    /**
     * 当前序列信息
     */
    private SequenceInfo sequenceInfo;

    /**
     * 当前序列段最大值
     */
    private Long seqStepMaxValue;

    public Long getSequenceValue() {
        long currentValue = this.atomicSeq.getAndIncrement();
        if (currentValue > seqStepMaxValue) {
            return null;
        }

        return currentValue;
    }

    /**
     * 是否需要增大序列最大值，(最大序列值-当前序列值)/序列增长步长 不足10%时，需要增加序列最大值
     * @return
     */
    public boolean couldIncrement() {

        return false;
    }

}
