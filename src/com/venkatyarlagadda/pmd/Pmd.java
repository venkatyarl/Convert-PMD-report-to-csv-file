package com.venkatyarlagadda.pmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pmd {
	static Map<String, String> mapOfAllRules = new HashMap<>();
	static List<PmdResult> listOfPmdResults = new ArrayList<>();
	static String allResults = "../pmdResults.txt";
	static String rules = "../rulesAndMessages.txt";
	static String fileSplitter = "|";

	public static void main(String... arguments) {
		final String fileLocation = "../pmd-report.txt";
		final String fileDilimiter = ":";

		try {
			FileReader fileReader = new FileReader(fileLocation);
			BufferedReader reader = new BufferedReader(fileReader);

			String line = reader.readLine();
			while (line != null) {
				listOfPmdResults.add(PmdResult.build(line.split(fileDilimiter)));
				line = reader.readLine();
			}
			reader.close();

			sortList();
			writeToAllResultsFile();
			writeToRulesAndMessagesFile();
			//printErrorsWithRuleName("LocalVariableCouldBeFinal");
			System.out.println("Finished");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sortList() {
		Comparator<PmdResult> compareByRuleName = (PmdResult r1, PmdResult r2) -> r1.getRule().compareTo(r2.getRule());
		Collections.sort(listOfPmdResults, compareByRuleName);

		for (PmdResult pmdResult : listOfPmdResults) {
			if (!mapOfAllRules.containsKey(pmdResult.getRule())) {
				mapOfAllRules.put(pmdResult.getRule(), pmdResult.getErrorMessage());
			}
		}
	}

	public static void writeToAllResultsFile() {
		try {
			Files.deleteIfExists((new File(allResults)).toPath());

			FileWriter fileWriter = new FileWriter(allResults);
			fileWriter.append("File Path" + fileSplitter + "Line Number" + fileSplitter + "Rule Name" + fileSplitter
					+ "Rule Message" + "\n");

			for (PmdResult pmdResult : listOfPmdResults) {
				fileWriter.append(pmdResult.getFilePath() + fileSplitter + pmdResult.getLineNumber() + fileSplitter
						+ pmdResult.getRule() + fileSplitter + pmdResult.getErrorMessage() + "\n");
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeToRulesAndMessagesFile() {
		try {
			Files.deleteIfExists((new File(rules)).toPath());
			FileWriter fileWriter = new FileWriter(rules);
			fileWriter.append("Rule Name" + fileSplitter + "Rule Message" + "\n");
			for (String ruleName : mapOfAllRules.keySet()) {
				fileWriter.append(ruleName + fileSplitter + mapOfAllRules.get(ruleName) + "\n");
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printErrorsWithRuleName(final String ruleName) {
		for (PmdResult pmdResult : listOfPmdResults) {
			if (pmdResult.getRule().equalsIgnoreCase(ruleName)) {
				System.out.println(pmdResult.getFilePath() + fileSplitter + pmdResult.getLineNumber() + fileSplitter
						+ pmdResult.getRule() + fileSplitter + pmdResult.getErrorMessage());
			}
		}
	}
}
