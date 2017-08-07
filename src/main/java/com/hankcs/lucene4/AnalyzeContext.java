package com.hankcs.lucene4;

import com.hankcs.hanlp.seg.common.Term;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * 分词器上下文状态
 */
class AnalyzeContext {

    //默认缓冲区大小
    public static final int BUFF_SIZE = 4096;
    //缓冲区耗尽的临界值
    private static final int BUFF_EXHAUST_CRITICAL = 100;


    //字符窜读取缓冲
    private char[] segmentBuff;
    //字符类型数组
    private int[] charTypes;

    //记录Reader内已分析的字串总长度
    //在分多段分析词元时，该变量累计当前的segmentBuff相对于reader起始位置的位移
    private int buffOffset;
    //当前缓冲区位置指针
    private int cursor;
    //最近一次读入的,可处理的字串长度
    private int available;

    //最终分词结果集
    private LinkedList<Term> results;

    //子分词器锁
    //该集合非空，说明有子分词器在占用segmentBuff
    private Set<String> buffLocker;

    public AnalyzeContext() {
        this.segmentBuff = new char[BUFF_SIZE];
        this.charTypes = new int[BUFF_SIZE];
        this.buffLocker = new HashSet<String>();
        this.results = new LinkedList<Term>();
    }

    char[] getSegmentBuff() {
        return this.segmentBuff;
    }

    /**
     * 根据context的上下文情况，填充segmentBuff
     *
     * @param reader
     * @return 返回待分析的（有效的）字串长度
     * @throws IOException
     */
    int fillBuffer(Reader reader) throws IOException {
        int readCount = 0;
        if (this.buffOffset == 0) {
            //首次读取reader
            readCount = reader.read(segmentBuff);
        } else {
            int offset = this.available - this.cursor;
            if (offset > 0) {
                //最近一次读取的>最近一次处理的，将未处理的字串拷贝到segmentBuff头部
                System.arraycopy(this.segmentBuff, this.cursor, this.segmentBuff, 0, offset);
                readCount = offset;
            }
            //继续读取reader ，以onceReadIn - onceAnalyzed为起始位置，继续填充segmentBuff剩余的部分
            readCount += reader.read(this.segmentBuff, offset, BUFF_SIZE - offset);
        }
        if (readCount < BUFF_SIZE && readCount > 0) {
            char[] lastSegmentBuff = new char[readCount];
            System.arraycopy(segmentBuff, 0, lastSegmentBuff, 0, readCount);
            segmentBuff = lastSegmentBuff;
        }
        //记录最后一次从Reader中读入的可用字符长度
        this.available = readCount;
        //重置当前指针
        this.cursor = 0;
        return readCount;
    }

    /**
     * 只要buffLocker中存在segmenterName
     * 则buffer被锁定
     *
     * @return boolean 缓冲去是否被锁定
     */
    boolean isBufferLocked() {
        return this.buffLocker.size() > 0;
    }

    /**
     * 当前segmentBuff是否已经用完
     * 当前执针cursor移至segmentBuff末端this.available
     *
     * @return
     */
    boolean bufferConsumed() {
        return this.cursor == this.available;
    }

    /**
     * 判断segmentBuff是否需要读取新数据
     * <p>
     * 满足一下条件时，
     * 1.available == BUFF_SIZE 表示buffer满载
     * 2.buffIndex < available - 1 && buffIndex > available - BUFF_EXHAUST_CRITICAL表示当前指针处于临界区内
     * 3.!context.isBufferLocked()表示没有segmenter在占用buffer
     * 要中断当前循环（buffer要进行移位，并再读取数据的操作）
     *
     * @return
     */
    boolean needRefillBuffer() {
        return this.available == BUFF_SIZE
                && this.cursor < this.available - 1
                && this.cursor > this.available - BUFF_EXHAUST_CRITICAL
                && !this.isBufferLocked();
    }

    /**
     * 累计当前的segmentBuff相对于reader起始位置的位移
     */
    void markBufferOffset() {
        this.buffOffset += this.cursor;
    }

    /**
     * 重置分词上下文状态
     */
    void reset() {
        this.buffLocker.clear();
        this.available = 0;
        this.buffOffset = 0;
        this.charTypes = new int[BUFF_SIZE];
        this.cursor = 0;
        this.segmentBuff = new char[BUFF_SIZE];
        this.results.clear();
    }

    /**
     * term
     * <p>
     * 同时处理合并
     *
     * @return
     */
    Term getNextTerm() {
        //从结果集取出，并移除第一个Lexme
        return this.results.pollFirst();
    }

    /**
     * 添加分词结果到results
     */
    void addToResults(Term term) {
        results.add(term);
    }

}
