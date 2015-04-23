import java.io.StringReader;
import java.util.*;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordToSentenceProcessor;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.nio.file.*;
import java.io.*;


class EnglishTagger{
	MaxentTagger postagger;
	Morphology lemmatizer;
	WordToSentenceProcessor<CoreLabel> ssplit;
	TokenizerFactory<CoreLabel> tokenizer;


	public EnglishTagger(){
		this.postagger  = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
		this.lemmatizer = new Morphology();
		this.ssplit     = new WordToSentenceProcessor();
	}

    public void tag(String text){
		StringReader r = new StringReader(text);
		PTBTokenizer tokenizer = new PTBTokenizer(r,new CoreLabelTokenFactory(),"invertible,ptb3Escaping=true");
		List<CoreLabel> 		tokens = tokenizer.tokenize();

		for (List<CoreLabel> sntc: this.ssplit.process(tokens)) {
			try{
				for (TaggedWord wt:  this.postagger.tagSentence(sntc)) {
					String lemma=this.lemmatizer.lemma(wt.word(),wt.tag());
					System.out.print(wt.word());
					System.out.print("\t");
					System.out.print(wt.tag());
					System.out.print("\t");
					System.out.print(lemma);
					System.out.print("\n");
				}
			}catch(Exception e){
				System.out.print("::Error with sentence:");
				System.out.println(sntc);
			}
		}
	}

	public static void main (String[] args) {
		EnglishTagger sp = new EnglishTagger();
		for (String s: args) {
			try{
				File file = new File(s.toString());
				FileInputStream fis = new FileInputStream(file);
				byte[] data = new byte[(int) file.length()];
				fis.read(data);
				fis.close();
				String str = new String(data, "UTF-8");
				sp.tag(str);
			}catch(Exception e){}
		}
				
	}
}
