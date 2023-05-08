package com.example.sequence.service;

import com.example.sequence.entity.SequenceInfo;
import org.springframework.util.StringUtils;

/**
 * 定义操作序列号的方法
 * @author PlusQi
 */
public interface SequenceService {
    /**
     * 生成指定类型序列号
     * @param sequenceType 序列号类型
     * @return
     */
    String getSequenceNo(SequenceInfo.SequenceType sequenceType);

    /**
     * 根据序列号类型查询序列号生成规则配置
     * @param seqType 序列号类型
     * @return
     */
    SequenceInfo querySequenceInfo(String seqType);
}
