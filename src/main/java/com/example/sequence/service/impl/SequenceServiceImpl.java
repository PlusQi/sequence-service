package com.example.sequence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sequence.entity.SequenceContainer;
import com.example.sequence.entity.SequenceInfo;
import com.example.sequence.mapper.SequenceInfoService;
import com.example.sequence.service.SequenceService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 序列号操作实现类
 * @author PlusQi
 */
public class SequenceServiceImpl implements InitializingBean, SequenceService {

    /**
     * 缓存序列号，并按照序列号类型分组
     */
    private static ConcurrentHashMap<SequenceInfo.SequenceType, SequenceContainer> sequenceMap = new ConcurrentHashMap<>();

    @Resource
    private SequenceInfoService sequenceInfoService;

    @Resource
    private TransactionalServiceImpl transactionalService;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 获取所有类型的序列号生成规则配置
        List<SequenceInfo> sequenceInfoList = sequenceInfoService.selectList(null);

        if (!CollectionUtils.isEmpty(sequenceInfoList)) {
            for (SequenceInfo sequenceInfo : sequenceInfoList) {
                // 校验序列号类型是否已配置相关枚举
                SequenceInfo.SequenceType sequenceType = SequenceInfo.SequenceType.fromValue(sequenceInfo.getSeqType());
                if (Objects.isNull(sequenceType)) {
                    throw new RuntimeException("配置项：{" + sequenceType + "} 相关枚举未配置");
                }

                // 序列值按步长增长，当前应用得到的是增长前的序列值——增长后的序列值之间的数值
                // 适配分布式环境，不同会获取到不同的序列值数值段
                sequenceInfo = transactionalService.incrementSequenceStep(sequenceInfo);
                SequenceContainer sequenceContainer = new SequenceContainer();
                // 设置序列开始原子值
                sequenceContainer.setAtomicSeq(new AtomicLong(sequenceInfo.getSeqValue()));
                // 设置序列信息
                sequenceContainer.setSequenceInfo(sequenceInfo);
                // 设置当前序列段最大值
                sequenceContainer.setSeqStepMaxValue(sequenceInfo.getSeqValue() + sequenceInfo.getIncrementStep() - 1);

                sequenceMap.put(sequenceType, sequenceContainer);
            }
        }
    }

    @Override
    public String getSequenceNo(SequenceInfo.SequenceType sequenceType) {
        SequenceContainer sequenceContainer = sequenceMap.get(sequenceType);

        // todo 没想好怎么处理异常，暂时抛空指针异常
        Objects.requireNonNull(sequenceContainer);

        // 获取当前序列值
        Long sequence = sequenceContainer.getSequenceValue();

        // 判断是否需要扩展最大序列值
        Boolean needIncrement = sequenceContainer.needIncrementMaxValue();
        if (needIncrement) {

        }
        // 获取
        return null;
    }

    @Override
    public SequenceInfo querySequenceInfo(String seqType) {
        return sequenceInfoService.selectOne(new QueryWrapper<SequenceInfo>().eq("seqType", seqType));
    }


}
