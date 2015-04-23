import java.util.*;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.international.spanish.process.SpanishTokenizer;
import edu.stanford.nlp.process.WordToSentenceProcessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.nio.file.*;
import java.io.*;


class SpanishTagger{
    MaxentTagger postagger;
	WordToSentenceProcessor<CoreLabel> ssplit;
	TokenizerFactory<CoreLabel> tokenizer;

	public SpanishTagger(){
	    this.tokenizer  = SpanishTokenizer.factory(new CoreLabelTokenFactory(),"invertible,ptb3Escaping=truei,splitAll=True");
		this.postagger  = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/spanish/spanish-distsim.tagger");
        this.ssplit     = new WordToSentenceProcessor();

	}

    public void tag(String text){
		StringReader r = new StringReader(text);
		List<CoreLabel> tokens = this.tokenizer.getTokenizer(r).tokenize();

		for (List<CoreLabel> sntc: this.ssplit.process(tokens)) {
			try{
				for (TaggedWord wt:  this.postagger.tagSentence(sntc)) {
					System.out.print(wt.word());
					System.out.print("\t");
					System.out.print(wt.tag());
					System.out.print("\n");
				}
			}catch(Exception e){
				System.out.print("::Error with sentence:");
				System.out.println(sntc);
			}
		}
	}

	public static void main (String[] args) {
		SpanishTagger sp = new SpanishTagger();
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
