package compiler.lexer;

public class Float extends Token {
	
	public final double value;

	public Float(double v) {
		super(Tag.FLOAT);
		value = v;
	}

}
