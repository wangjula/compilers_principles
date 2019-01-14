package compiler.lexer;

import java.util.Hashtable;

/**
 * lexical analysis
 * 
 * @author wangjiuliang
 *
 */
public class Lexer {

	public int line = 1;
	
	private char peek = ' ';
	
	private Hashtable<String, Word> words = new Hashtable<>();
	
	void reserve(Word t) {
		words.put(t.lexeme, t);
	}
	
	public Lexer() {
		reserve(new Word(Tag.TRUE, "true"));
		reserve(new Word(Tag.FALSE, "false"));
	}
	
	public Token scan() throws Exception {
		/*clear space*/
		for ( ; ; peek = (char) System.in.read()) {
			if (peek == ' ' || peek == '\t') {
				continue;
			} else if (peek == '\n') {
				++ line;
			} else {
				break;
			}
		}
		
		/*handle comment*/
		if (peek == '/') {
			peek = (char) System.in.read();
			
			if (peek == '/') {
				for ( ; ; peek = (char) System.in.read()) {
					if (peek == '\n') {
						break;
					}
				}
			} else if (peek == '*') {
				char prevPeek = ' ';
				for ( ; ; prevPeek = peek, peek = (char) System.in.read()) {
					if (prevPeek == '*' && peek == '/') {
						break;
					}
				}
			} else {
				throw new Exception("syntax error");
			}
		}
		
		/*handle relation sign*/
		if ("<=!>".indexOf(peek) > -1) {
			StringBuilder b = new StringBuilder();
			b.append(peek);
			peek = (char) System.in.read();
			if (peek == '=') {
				b.append("=");
			}
			return new Word(Tag.REL, b.toString());
		}
		
		/*handle digit*/
		if (Character.isDigit(peek) || peek == '.') {
			boolean isDotExist = false;
			StringBuilder b = new StringBuilder();
			do {
				if (peek == '.') {
					isDotExist = true;
				}
				b.append(peek);
				peek = (char) System.in.read();
			} while (isDotExist ? Character.isDigit(peek) : Character.isDigit(peek) || peek == '.');
			
			if (isDotExist) {
				return new Float(java.lang.Float.parseFloat(b.toString()));
			} else {
				return new Num(Integer.parseInt(b.toString()));
			}
//			int v = 0;
//			do {
//				v = 10 * v + Character.digit(peek, 10);
//				peek = (char) System.in.read();
//			} while (Character.isDigit(peek));
//			return new Num(v);
		}
		
		/*handle letter*/
		if (Character.isLetter(peek)) {
			StringBuilder b = new StringBuilder();
			do {
				b.append(peek);
				peek = (char) System.in.read();
			} while (Character.isLetterOrDigit(peek));
			
			String s = b.toString();
			Word w = words.get(s);
			if (w != null) {
				return w;
			}
			
			w = new Word(Tag.ID, s);
			words.put(s, w);
			return w;
		}
		
		/*build token*/
		Token t = new Token(peek);
		peek = ' ';
		return t;
	}
}
