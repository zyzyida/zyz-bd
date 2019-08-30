package com.zyzyida.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Desc: Metric工具类
 * Created by zhouyizhe on 2019-08-14
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetricEvent {
    /**
     * Metric name
     */
    private String name;

    /**
     * Metric timestamp
     */
    private Long timestamp;

    /**
     * Metric fields
     */
    private Map<String, Object> fields;

    /**
     * Metric tags
     */
    private Map<String, String> tags;
}
