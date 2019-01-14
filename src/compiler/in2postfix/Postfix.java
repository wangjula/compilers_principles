package compiler.in2postfix;

import java.io.IOException;

public class Postfix {

	public static void main(String[] args) throws IOException {
		Parser parser = new Parser();
		parser.expr();
		System.out.write('\n');
	}

}

/**
 * @author wangjiuliang
 * 
 * productions:
 * expr -> expr + term | expr - term | term
 * term -> 0|1|2|...|9
 *
 * translation schemes:
 * expr -> term rest
 * rest -> + term {print('+')} rest | - term {print('-')} rest | ε
 * term -> 0 {print('0')}
 *      | 1 {print('1')}
 *      | 2 {print('2')}
 *             ...
 *      | 9 {print('9')}
 * 
 */
class Parser {
	
	static int lookahead;
	
	public Parser() throws IOException {
		lookahead = System.in.read();
	}
	
	public void expr() throws IOException {
		term();
		
		/*rest*/
		while (true) {         
			if (lookahead == '+') {
				match('+');
				term();
				System.out.write('+');
			} else if (lookahead == '-') {
				match('-');
				term();
				System.out.write('-');
			} else {
				return;
			}
		}
		
	}

	private void match(int t) throws IOException {
		if (lookahead == t) {
			lookahead = System.in.read();
		} else {
			throw new Error("syntax error");
		}
	}

	private void term() throws IOException {
		if (Character.isDigit((char) lookahead)) {
			System.out.write((char) lookahead);
			match(lookahead);
		} else {
			throw new Error("syntax error");
		}
	}
	
}
