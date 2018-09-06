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
public class HanLPAnalyzerProvider extends AbstractIndexAnalyzerProvider<HanLPIndexAnalyzer> {

    private final HanLPIndexAnalyzer analyzer;

    @Inject
    public HanLPAnalyzerProvider(IndexSettings indexSettings, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(indexSettings, name, settings);
        Path pluginsDir = env.pluginsFile();
        Predefine.HANLP_PROPERTIES_PATH = pluginsDir.toString() + File.separator + "analysis-hanlp" + File.separator + "hanlp.properties";
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
