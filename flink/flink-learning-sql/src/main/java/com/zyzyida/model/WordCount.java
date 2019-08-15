package com.zyzyida.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Desc: wordcount
 * Created by zhouyizhe on 2019-08-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordCount {
    /**
     * word
     */
    public String word;

    /**
     * 出现的次数
     */
    public long count;
}
