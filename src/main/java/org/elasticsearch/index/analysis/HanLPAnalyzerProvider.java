package org.elasticsearch.index.analysis;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.utility.Predefine;
import com.hankcs.lucene4.HanLPIndexAnalyzer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

/**
 */
public class HanLPAnalyzerProvider extends AbstractIndexAnalyzerProvider<HanLPIndexAnalyzer> {

    private final HanLPIndexAnalyzer analyzer;
    private static String sysPath = String.valueOf(System.getProperties().get("user.dir"));

    @Inject
    public HanLPAnalyzerProvider(IndexSettings indexSettings, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(indexSettings, name, settings);
        Predefine.HANLP_PROPERTIES_PATH = sysPath.substring(0, sysPath.length()-4) + File.separator  + "plugins" + File.separator + "analysis-hanlp" + File.separator + "hanlp.properties";
        analyzer = new HanLPIndexAnalyzer(true);
    }

    public static HanLPAnalyzerProvider getIndexAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPAnalyzerProvider(indexSettings, env, name, settings);
    }

    public static HanLPAnalyzerProvider getSmartAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPAnalyzerProvider(indexSettings, env, name, settings);
    }

    @Override
    public HanLPIndexAnalyzer get() {
        return this.analyzer;
    }

}
