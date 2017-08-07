package org.elasticsearch.plugin.analysis.hanlp;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.index.analysis.*;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * The HanLP Analysis HanLP module into elasticsearch.
 */
public class AnalysisHanlpPlugin extends Plugin implements AnalysisPlugin {

    public static String PLUGIN_NAME = "analysis-hanlp";

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> extra = new HashMap<>();

        extra.put("hanlp-index", HanLPTokenizerFactory::getIndexTokenizerFactory);
        extra.put("hanlp-smart", HanLPTokenizerFactory::getSmartTokenizerFactory);
        extra.put("hanlp", HanLPTokenizerFactory::getIndexTokenizerFactory);
        return extra;
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> extra = new HashMap<>();

        extra.put("hanlp-index", HanLPAnalyzerProvider::getIndexAnalyzerProvider);
        extra.put("hanlp-smart", HanLPAnalyzerProvider::getSmartAnalyzerProvider);
        extra.put("hanlp", HanLPAnalyzerProvider::getIndexAnalyzerProvider);
        return extra;
    }

}
