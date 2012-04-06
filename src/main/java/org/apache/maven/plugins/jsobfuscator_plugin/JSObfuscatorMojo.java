package org.apache.maven.plugin.jsobfuscator_plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal garble
 * 
 * @phase process-sources
 */
public class JSObfuscatorMojo extends AbstractMojo {

	/**
	 * External reference resource.
	 * 
	 * @parameter alias="externSource"
	 */
	private String externSource;

	/**
	 * External reference resource type, can be file or code
	 * 
	 * @parameter alias="externType"
	 */
	private String externType;

	/**
	 * File system location of the base build directory.
	 * 
	 * @parameter 
	 *            default-value="${project.build.directory}/${project.build.finalName}"
	 */
	private String base;

	/**
	 * JavaScript source filename list - these are the files to be obfuscated.
	 * 
	 * @parameter alias="jsSourceFiles"
	 */
	private List<String> jsSourceFiles;

//	/**
//	 * Output file name decorator - can be 'prefix' or 'suffix' or 'none'.
//	 * 
//	 * @parameter alias="nameDecorator" default-value="none"
//	 */
//	private String nameDecorator;
//
//	/**
//	 * Based on the file name decorator, this value will be either prepended or
//	 * appended to the original file name.
//	 * 
//	 * Leave blank to write file with the same name
//	 * 
//	 * @parameter alias="nameDecoratorValue" default-value=""
//	 */
//	private String nameDecoratorValue;

	public void execute() throws MojoExecutionException {
		System.out
				.println("*****************************************\nJSObfuscatorMojo IS BEING RUN\n***************************************");
		
		ExecutorService executor = Executors.newFixedThreadPool(4);
		CompletionService<Boolean> pool = new ExecutorCompletionService<Boolean>(
				executor);

		if (jsSourceFiles != null) {
			for (String sourceFile : jsSourceFiles) {
				System.out.println("processing file: " + sourceFile);
				pool.submit(new JSObfuscator(sourceFile, externSource, externType));
			}

			for (int i = 0; i < jsSourceFiles.size(); i++) {
				try {
					// Will block till tasks are not completed
					pool.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			executor.shutdown();
		}
	}
}
