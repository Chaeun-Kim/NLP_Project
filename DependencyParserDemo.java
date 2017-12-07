import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.util.CoreMap;

import java.io.StringReader;
import java.util.List;
import java.util.Properties;

public class DependencyParserDemo
{
	public GrammaticalStructure DP(String text)
	{
		String modelPath = DependencyParser.DEFAULT_MODEL;
		//String taggerPath = "/Users/chaeunkim/Desktop/stanford-postagger-2017-06-09/models/english-left3words-distsim.tagger";
		
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
        String text1 = text;
        text = "";
        Annotation document = pipeline.process(text1);

        for(CoreMap sentence: document.get(SentencesAnnotation.class))
        {
            for(CoreLabel token: sentence.get(TokensAnnotation.class))
            {
                String lemma = token.get(LemmaAnnotation.class);
                text += lemma + " ";
            }
        }
        
        text = text.trim();
        
		MaxentTagger tagger = new MaxentTagger(
                        "/Users/chaeunkim/Desktop/stanford-postagger-2017-06-09/models/english-left3words-distsim.tagger");
		DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);

		DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
		
		GrammaticalStructure gs = null;
		
		for (List<HasWord> sentence : tokenizer)
		{
			List<TaggedWord> tagged = tagger.tagSentence(sentence);
			gs = parser.predict(tagged);
			

			String justWondering = gs.toString();
			System.out.println(justWondering);
			System.out.println();
		}
        
		return gs;
	}
}
