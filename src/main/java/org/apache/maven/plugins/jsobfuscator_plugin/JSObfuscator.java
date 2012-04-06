/**
 * 
 */
package org.apache.maven.plugin.jsobfuscator_plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Callable;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSSourceFile;

/**
 * @author ubhatia
 * 
 */
public class JSObfuscator implements Callable<Boolean> {

	private String inputFile;
	// private String nameDecorator;
	// private String nameDecoratorValue;

	private String externSource;
	private String externType;

	private Compiler compiler;
	private CompilerOptions options;

	/***
	 * Generate a new JavaScript Obfuscator Object.
	 * 
	 * @param inputFile
	 *            - The file to be obfuscated
	 * @param nameDecorator
	 *            - Prefix or Suffix - nameDecoratorValue to be applied as per
	 *            decorator type
	 * @param nameDecoratorValue
	 *            - Decorator Value. Based on nameDecorator, this value will be
	 *            attached to input file name
	 * @param externSource
	 *            - External reference source. Can be code or filename
	 * @param externType
	 *            - External reference source type - 'File' or 'Code'
	 */
	public JSObfuscator(String inputFile,
	// String nameDecorator,
	// String nameDecoratorValue,
			String externSource, String externType) {
		super();
		this.inputFile = inputFile;
		// this.nameDecorator = nameDecorator;
		// this.nameDecoratorValue = nameDecoratorValue;
		this.externSource = externSource;
		this.externType = externType;

		this.compiler = new Compiler();
		this.options = new CompilerOptions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public Boolean call() throws Exception {
		// Advanced mode is used here, but additional options could be set,
		// too.
		CompilationLevel.ADVANCED_OPTIMIZATIONS
				.setOptionsForCompilationLevel(options);

		// To get the complete set of externs, the logic in
		// CompilerRunner.getDefaultExterns() should be used here.
		JSSourceFile extern = null;
		if (externType != null && externType.toLowerCase().equals("file")) {
			extern = JSSourceFile.fromFile(this.externSource);
		} else {
			extern = JSSourceFile.fromCode("externs.js", this.externSource);

			/*
			 * "_nxjeid='MER-???';" + "_nxjetag='TRXN'; _nxjcsid='0';" +
			 * "_nxjmeta = _nxjstrde(12.00, '11.00,1.00', '1.10,0.10', '0.55,0.05', 'H123248', 'A15,X221', 'ProductA,ProductB', 15.96, '6563', 'M');"
			 * + "nxjvisit();");
			 */
		}

		JSSourceFile input = JSSourceFile.fromFile(inputFile);

		System.out.println("input set: " + input.getName());
		// compile() returns a Result, but it is not needed here.
		compiler.compile(extern, input, options);
		System.out.println("compilation done");

		// The compiler is responsible for generating the compiled code; it
		// is
		// not accessible via the Result.
		// String outputFileName = this.inputFile;
		//
		// if (nameDecorator.toLowerCase().equals("prefix"))
		// outputFileName = nameDecoratorValue + this.inputFile;
		// else
		// outputFileName = this.inputFile + nameDecoratorValue;
		// }

		JSObfuscator.writeJSFileCode(this.inputFile, compiler.toSource());
		return true;
	}

	/***
	 * Method to write obfuscated code to file system
	 * 
	 * @param fileName
	 * @param code
	 */
	private static void writeJSFileCode(String fileName, String code) {
		try {
			System.out.println(code);
			File file = new File(fileName);
			System.out.println(file.getAbsolutePath());
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(code);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
