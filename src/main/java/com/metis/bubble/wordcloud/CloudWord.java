package com.metis.bubble.wordcloud;

import lombok.Data;

/**
 * @author ygzheng
 * 访谈纪要中的词频分析：词，提及的次数；
 */
@Data
public class CloudWord {
    private String name;
    private Integer count;
}
