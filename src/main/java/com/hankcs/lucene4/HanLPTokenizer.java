package com.hankcs.lucene4;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class HanLPTokenizer extends Tokenizer {
    //词元文本属性
    private final CharTermAttribute termAtt;
    //词元位移属性
    private final OffsetAttribute offsetAtt;
    //词元分类属性（该属性分类参考org.wltea.analyzer.core.Lexeme中的分类常量）
    private final TypeAttribute typeAtt;
    private PositionIncrementAttribute posIncrAtt;
    private Set<String> filter;
    private boolean enablePorterStemming;
    private HanlpSegmenter hanlpSegmenter;
    private int position;

    public HanLPTokenizer(Segment segment, Set<String> filter, boolean enablePorterStemming) {
        super();
        this.filter = filter;
        this.enablePorterStemming = enablePorterStemming;
        offsetAtt = addAttribute(OffsetAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
        posIncrAtt = addAttribute(PositionIncrementAttribute.class);
        hanlpSegmenter = new HanlpSegmenter(input, segment);
    }

    @Override
    final public boolean incrementToken() throws IOException {
        clearAttributes();
        this.position = 0;
        Term term = hanlpSegmenter.next();
        if (term != null) {
            posIncrAtt.setPositionIncrement(this.position + 1);
            termAtt.setEmpty().append(term.word.toLowerCase());
            termAtt.setLength(term.word.length());
            int length = term.word.length();
            offsetAtt.setOffset(term.offset,
                    term.offset + length);
            typeAtt.setType(term.nature.name());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 必须重载的方法，否则在批量索引文件时将会导致文件索引失败
     */
    @Override
    public void reset() throws IOException {
        super.reset();
        this.position = 0;
        hanlpSegmenter.reset(new BufferedReader(this.input));
    }

}
