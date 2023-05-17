package com.example.sequence.service.impl;

import com.example.sequence.entity.SequenceInfo;
import com.example.sequence.mapper.SequenceInfoService;
import com.example.sequence.service.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


/**
 * @author PlusQi
 */
@Slf4j
@Service
public class TransactionalServiceImpl {

    @Resource
    private SequenceInfoService sequenceInfoService;

    @Resource
    private SequenceService sequenceService;

    /**
     * 序列号递增
     * @param sequenceInfo 序列号信息
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 调用方法时时开启新事务
    public SequenceInfo incrementSequenceStep(SequenceInfo sequenceInfo) {
        // 序列号是否成功增长
        boolean incrementSuccess = false;

        // 序列号增长失败则重试
        while (!incrementSuccess) {
            incrementSuccess = incrementSeqStep(sequenceInfo);

            // 序列号增长失败，重新查询序列号信息，再次重试
            if (!incrementSuccess) {
                sequenceInfo = sequenceService.querySequenceInfo(sequenceInfo.getSeqType());
            }
        }
        return sequenceInfo;
    }

    /**
     * 序列号按步长增长或按周期重置
     * @param sequenceInfo 序列号信息
     * @return true - 序列号更新成功 false - 序列号更新失败
     */
    private boolean incrementSeqStep(SequenceInfo sequenceInfo) {

        // 是否重置序列号
        boolean resetSeqValue = false;

        // 周期重置
        if (1 == sequenceInfo.getCycleFlag()) {
            // 数值周期重置，当前序列号 + 增长步长 > 最大序列值时
            // 重置序列号
            if (1 == sequenceInfo.getCycleType() &&
                    sequenceInfo.getSeqValue() + sequenceInfo.getIncrementStep() > sequenceInfo.getSeqMaxValue()) {
                resetSeqValue = true;
            } // 时间周期重置，当序列号开始日期 + 循环周期天数 >= 当前日期时重置序列号
            else if (2 == sequenceInfo.getCycleType() &&
                    sequenceInfo.getSeqStartTime().plus(sequenceInfo.getCyclePeriod(), ChronoUnit.DAYS).compareTo(LocalDate.now()) >= 0) {
                resetSeqValue = true;
            } else {
                throw new RuntimeException("无效的周期类型");
            }
        }

        // 重置序列号
        if (resetSeqValue) {
            sequenceInfo.setSeqValue(sequenceInfo.getSeqInitValue());
            sequenceInfo.setSeqStartTime(LocalDate.now());
        } else { //序列号按步长增长
            sequenceInfo.setSeqValue(sequenceInfo.getSeqValue() + sequenceInfo.getIncrementStep());
        }

        // 更新序列信息
        // todo 增加乐观锁，适配分布式环境
        int updateResult = sequenceInfoService.updateById(sequenceInfo);
        // 结果数==1，更新成功
        if (1 == updateResult) {
            return true;
        } else {
            log.error("更新序列失败: seqType={}, seqValue={}", sequenceInfo.getSeqType(), sequenceInfo.getSeqValue());
            return false;
        }
    }
}
