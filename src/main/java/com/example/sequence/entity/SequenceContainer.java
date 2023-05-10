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
     * @return true - 序列长度超过阈值，需要增大最大序列值，fasle - 不需要增大序列最大值
     */
    public Boolean needIncrementMaxValue() {
        if ((1d * (this.seqStepMaxValue - this.atomicSeq.get()) / this.sequenceInfo.getIncrementStep()) < 0.1d) {
            return true;
        }
        return false;
    }

    /**
     * @return 序列长度
     */
    public Integer sequenceLength() {
        return this.sequenceInfo.getSeqLength();
    }

}
