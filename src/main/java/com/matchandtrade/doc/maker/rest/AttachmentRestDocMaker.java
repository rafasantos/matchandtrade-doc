
package com.matchandtrade.doc.maker.rest;

import com.github.rafasantos.restapidoc.SpecificationFilter;
import com.github.rafasantos.restapidoc.SpecificationParser;
import com.github.rafasantos.restdocmaker.RestDocMaker;
import com.github.rafasantos.restdocmaker.template.TemplateUtil;
import com.matchandtrade.doc.util.MatchAndTradeRestUtil;
import com.matchandtrade.doc.util.PaginationTemplateUtil;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.specification.MultiPartSpecification;

import java.io.File;


public class AttachmentRestDocMaker implements RestDocMaker {
	
	private static final String POST_PLACEHOLDER = "POST_PLACEHOLDER";
	@Override
	public String contentFilePath() {
		return "attachments.html";
	}

	@Override
	public String content() {
		String template = TemplateUtil.buildTemplate(contentFilePath());

		String filePath = AttachmentRestDocMaker.class.getClassLoader().getResource("image-landscape.png").getFile();
		File file = new File(filePath);
		MultiPartSpecification fileSpec = new MultiPartSpecBuilder(file).mimeType("image/png").fileName("my-image.png").build();

		SpecificationFilter filter = new SpecificationFilter();
		SpecificationParser parser = new SpecificationParser(filter);
		RestAssured.given()
			.filter(filter)
			.multiPart(fileSpec)
			.header(MatchAndTradeRestUtil.getLastAuthorizationHeader())
			.post(MatchAndTradeRestUtil.attachmentsUrl());
		parser.getResponse().then().statusCode(201);

		template = TemplateUtil.replacePlaceholder(template, POST_PLACEHOLDER, parser.asHtmlSnippet());
		template = PaginationTemplateUtil.replacePaginationTable(template);
		return TemplateUtil.appendHeaderAndFooter(template);
	}

}
