package org.elasticsearch.index.analysis;

import com.hankcs.hanlp.utility.Predefine;
import com.hankcs.lucene4.HanLPIndexAnalyzer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import java.io.File;
import java.nio.file.Path;

/**
 */
public class HanLPTokenizerFactory extends AbstractTokenizerFactory {

    private boolean enablePorterStemming;
    private boolean enableIndexMode;

    public HanLPTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
        Path pluginsDir = env.pluginsFile();
        Predefine.HANLP_PROPERTIES_PATH = pluginsDir.toString() + File.separator + "analysis-hanlp" + File.separator + "hanlp.properties";
        enablePorterStemming = settings.getAsBoolean("enablePorterStemming", false);
    }

    public static HanLPTokenizerFactory getIndexTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, env, name, settings).setIndexMode(true);
    }

    public static HanLPTokenizerFactory getSmartTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, env, name, settings).setIndexMode(false);
    }

    private HanLPTokenizerFactory setIndexMode(boolean enableIndexMode) {
        this.enableIndexMode = enableIndexMode;
        return this;
    }

    @Override
    public Tokenizer create() {
        return new HanLPTokenizer(HanLP.newSegment().enablePlaceRecognize(true).enableCustomDictionary(true).enableIndexMode(enableIndexMode).enableOffset(true), null, enablePorterStemming);
    }

}

