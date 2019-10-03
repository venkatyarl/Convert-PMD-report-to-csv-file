package com.venkatyarlagadda.pmd;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PmdResult {
	private String filePath;
	private int lineNumber;
	private String rule;
	private String errorMessage;
	
	public static PmdResult build(final String...line) {
		//@formatter:off
		return PmdResult
			.builder()
			.filePath(line[0].trim())
			.lineNumber(Integer.parseInt(line[1]))
			.rule(line[2].trim())
			.errorMessage(line[3].trim())
			.build();
		//@formatter:on
	}
}
