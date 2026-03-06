package dev.glabay.jvm.test;

// Sample implementation of ITest for a test which checks if 
public final class StringStringTest implements ITest {

	private String in;
	private String expect;
	private IStringStringSolution solution;

	public StringStringTest(String in, String expect, IStringStringSolution solution) {
		this.in = in;
		this.expect = expect;
		this.solution = solution;
	}

	@Override
	public boolean do_test() {
		return solution.solve(in).equals(expect);
	}

	// Builder pattern
	public static StringStringTest of(IStringStringSolution solution) {
		return new StringStringTest(null, null, solution);
	}

	public StringStringTest withInput(String input) {
		this.in = input;
		return this;
	}

	public StringStringTest withExpected(String expected) {
		this.expect = expected;
		return this;
	}

}
