# elasticsearch-analysis-hanlp

安装步骤： 

1、下载插件并解压到es的plugins目录下，修改analysis-hanlp目录下的hanlp.properties文件，修改root的属性，值为analysis－hanlp下的data 
目录的地址

2、修改es config目录下的jvm.options文件，最后一行添加 
-Djava.security.policy=../plugins/analysis-hanlp/plugin-security.policy

重启es

GET /_analyze?analyzer=hanLp-index&pretty=true 
{ 
“text”:”张柏芝士蛋糕店” 
} 
测试是否安装成功

analyzer有hanLp-index（索引模式）和hanLp-smart（智能模式）

自定义词典 
修改plugins/analysis-hanlp/data/dictionary/custom下的 我的词典.txt文件 
格式遵从[单词] [词性A] [A的频次] 
修改完后删除同目录下的CustomDictionary.txt.bin文件 
重启es服务

目前仅支持5.x版本
