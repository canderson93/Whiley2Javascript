package tests;

import javax.script.ScriptException;

import org.junit.Test;

public class ValidTests {
	
	@Test
	public void ValidAssert(){
		TestUtils.runTest("ValidAssert");
	}
	
	@Test
	public void ValidAssert_Conditionals(){
		TestUtils.runTest("ValidAssert_Conditionals");
	}
}
