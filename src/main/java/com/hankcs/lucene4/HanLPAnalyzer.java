package com.hankcs.lucene4;

import com.hankcs.hanlp.tokenizer.StandardTokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import java.io.Reader;
import java.util.Set;

public class HanLPAnalyzer extends Analyzer
{

    boolean enablePorterStemming;
    public Set<String> filter;

    /**
     * @param filter    停用词
     * @param enablePorterStemming 是否分析词干（仅限英文）
     */
    public HanLPAnalyzer(Set<String> filter, boolean enablePorterStemming)
    {
        this.filter = filter;
    }

    /**
     * @param enablePorterStemming 是否分析词干.进行单复数,时态的转换
     */
    public HanLPAnalyzer(boolean enablePorterStemming)
    {
        this.enablePorterStemming = enablePorterStemming;
    }

    public HanLPAnalyzer()
    {
        super();
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new HanLPTokenizer(StandardTokenizer.SEGMENT.enableOffset(true), filter, enablePorterStemming);
        return new TokenStreamComponents(tokenizer);
    }
}
