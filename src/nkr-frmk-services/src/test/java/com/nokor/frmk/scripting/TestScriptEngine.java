package com.nokor.frmk.scripting;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.nokor.frmk.testing.BaseTestCase;

/**
 * http://docs.oracle.com/javase/6/docs/technotes/guides/scripting/programmer_guide/index.html
 * @author prasnar
 * 
 */
public class TestScriptEngine extends BaseTestCase {

	/**
	 * 
	 */
	public TestScriptEngine() {
		
	}
	
	/**
     * 
     * @return
     */
    protected boolean isInitSecApplicationContext() {
    	return false;
    }
	/**
	 * 
	 */
	public void xxtestJS() {
		// create a script engine manager
		ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		// evaluate JavaScript code from String
		try {
			double i = 1.2;
			double j = 2.3; 
	        engine.put("i", i);
	        engine.put("j", j);
			engine.eval("print('Hello, World' + (i+j) )");
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * 
	 */
	public void testJSFile() {
		
		// Resource in folder: resources\misc\rules
		String rule1 = "EffectiveInterestRate";
		String rule2 = "InstalmentPayment";
		
		// evaluate JavaScript code from String
		try {
			JavaScriptEngine engine = new JavaScriptEngine("rules");
			engine.loadRule(rule1);
	        engine.setParam("principal", 2000);
	        engine.setParam("nbMonths", 24);
			engine.eval();
	        Object objIR = engine.getValue("interestRate");
	        logger.info("Interest Rate1: " + (Double) objIR);

	        engine.loadRule(rule2);
	        engine.setParam("principal", 2000);
	        engine.setParam("nbMonths", 24);
	        engine.setParam("interestRate", .1);
			engine.eval();
	        Object objIP = engine.getValue("payment");
	        logger.info("Instalment payment: " + (Double) objIP);
	        
	        engine.loadRule(rule1);
	        engine.setParam("principal", 2000);
	        engine.setParam("nbMonths", 24);
			engine.eval();
	        objIR = engine.getValue("interestRate");
	        logger.info("Interest Rate2: " + (Double) objIR);

		} catch (Exception e) {
			logger.errorStackTrace(e);
		}
	}
	/**
	 * 
	 */
	public void xxtestListAllEngines() {

		ScriptEngineManager mgr = new ScriptEngineManager();
		List<ScriptEngineFactory> factories = mgr.getEngineFactories();

		for (ScriptEngineFactory factory : factories) {

			System.out.println("ScriptEngineFactory Info");

			String engName = factory.getEngineName();
			String engVersion = factory.getEngineVersion();
			String langName = factory.getLanguageName();
			String langVersion = factory.getLanguageVersion();

			System.out
					.printf("\tScript Engine: %s (%s)%n", engName, engVersion);

			List<String> engNames = factory.getNames();
			for (String name : engNames) {
				System.out.printf("\tEngine Alias: %s%n", name);
			}

			System.out.printf("\tLanguage: %s (%s)%n", langName, langVersion);

		}

	}
}
