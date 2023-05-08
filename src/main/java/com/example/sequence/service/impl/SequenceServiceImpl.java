package com.example.sequence.service.impl;

import com.example.sequence.entity.SequenceInfo;
import com.example.sequence.mapper.SequenceInfoService;
import com.example.sequence.service.SequenceService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author PlusQi
 */
public class SequenceServiceImpl implements InitializingBean, SequenceService {

    @Resource
    private SequenceInfoService sequenceInfoService;

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

                // todo 当前序列值按步长增加


            }
        }
    }

    @Override
    public String getSequenceNo(SequenceInfo.SequenceType sequenceType) {
        return null;
    }

    @Override
    public SequenceInfo querySequenceInfo(String seqType) {
        return null;
    }


}
