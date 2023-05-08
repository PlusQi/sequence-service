package com.example.sequence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;

import java.time.LocalDate;

/**
 * 序列号信息
 * @author PlusQi
 */
@Data
public class SequenceInfo {
    @TableId
    String id;

    /**
     * 序列号业务类型
     */
    String seqType;

    /**
     * 当前序列号
     */
    Long seqValue;

    /**
     * 序列号长度，不足则左侧补0
     */
    Integer seqLength;

    /**
     * 序列号初始值
     */
    Long seqInitValue;

    /**
     * 序列号增长步长
     */
    Integer incrementStep;

    /**
     * 序列号最大值
     */
    Long seqMaxValue;

    /**
     * 是否周期循环
     */
    Integer cycleFlag;

    /**
     * 循环类型
     */
    Integer cycleType;

    /**
     * 循环周期天数
     */
    Integer cyclePeriod;

    /**
     * 当前序列号生成时间
     */
    LocalDate seqStartTime;

    /**
     * 序列号类型枚举，关联SequenceInfo.seq_type
     */
    public enum SequenceType {
        /**
         * 测试序列号业务类型
         */
        TEST_BIZ_SEQ_TYPE("test_biz_seq_type");

        private String key;

        public String getKey() {
            return key;
        }

        SequenceType(String key) {
            this.key = key;
        }

        public static SequenceType fromValue(String key) {
            if (StringUtils.isBlank(key)) {
                return null;
            }
            for (SequenceType type : SequenceType.values()) {
                if (type.getKey().equals(key)) {
                    return type;
                }
            }
            return null;
        }
    }
}
