package com.github.rafasantos.matchandtrade.doc.maker.rest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import com.github.rafasantos.matchandtrade.doc.maker.OutputMaker;

public class RestMaker implements OutputMaker {
	
	String filePath = this.getClass().getClassLoader().getResource("doc/rest/rest.md").getFile();

	public String buildDocOutputString() throws IOException {
		File file = new File(filePath);
		String fileAsString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		return fileAsString;
	}

	@Override
	public void execute() {
		// Nothing to do execute for now
	}
	
	@Override
	public String getDocOutput() {
		String result = null;
		try {
			result = buildDocOutputString();
		} catch (IOException e) {
		}
		return result;
	}

	@Override
	public String getDocOutputLocation() {
		return "rest/rest.md";
	}
}
