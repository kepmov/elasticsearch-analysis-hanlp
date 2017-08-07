package com.hankcs.lucene4;

import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * hanlp分词器主类
 */
public final class HanlpSegmenter {

    //字符窜reader
    private Reader input;
    //分词器上下文
    private AnalyzeContext context;
    private Segment segment;
    private int readNum;

    /**
     * 分词器构造函数
     *
     * @param input
     */
    public HanlpSegmenter(Reader input, Segment segment) {

        this.input = input;
        this.segment = segment;
        this.init();
    }


    /**
     * 初始化
     */
    private void init() {
        //初始化分词上下文
        this.context = new AnalyzeContext();
    }

    /**
     * 分词，获取下一个词元
     *
     * @return Lexeme 词元对象
     * @throws IOException
     */
    public synchronized Term next() {
        try {
            Term term = null;
            while ((term = context.getNextTerm()) == null) {
            /*
             * 从reader中读取数据，填充buffer
			 * 如果reader是分次读入buffer的，那么buffer要  进行移位处理
			 */
                int available = context.fillBuffer(this.input);
                if (available <= 0) {
                    //reader已经读完
                    context.reset();
                    return null;

                } else {
                    List<Term> lists = segment.seg(String.valueOf(context.getSegmentBuff()));
                    for (Term t : lists) {
                        context.addToResults(t);
                    }
                    readNum++;
                    //字符缓冲区接近读完，需要读入新的字符
                    if (context.needRefillBuffer()) {
                        break;
                    }
                }
                //移动指针至available
                context.bufferConsumed();
                //记录本次分词的缓冲区位移
                context.markBufferOffset();
            }
            if (term != null) {
                term.offset = term.offset + (AnalyzeContext.BUFF_SIZE * (readNum - 1));
            }
            return term;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 重置分词器到初始状态
     *
     * @param input
     */
    public synchronized void reset(Reader input) {
        this.input = input;
        this.readNum = 0;
        context.reset();
    }
}
