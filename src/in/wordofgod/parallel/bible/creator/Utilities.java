/**
 * 
 */
package in.wordofgod.parallel.bible.creator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * 
 */
public class Utilities {

	public static String generateKeyforChaptersMap(String version, String threeLetterCode, String chapter) {
		return version + "-" + threeLetterCode + "-" + chapter;
	}

	public static String generateKeyforVerseMap(String version, String threeLetterCode, String chapter, String number) {
		return version + "-" + threeLetterCode + "-" + chapter + "-" + number;
	}

	public static String stripExtension(String version) {
		return version != null && version.lastIndexOf(".") > 0 ? version.substring(0, version.lastIndexOf("."))
				: version;
	}

	public static void createDir(String dirPath) {
		File dir = new File(ParallelBibleCreator.outputDirectory + "/" + dirPath);
		if (dir.exists()) {
			System.out.println("Directory already exists: " + dir.getAbsolutePath());
		} else {
			dir.mkdirs();
			System.out.println("Created the directory: " + dir.getAbsolutePath());
		}
	}

	public static void createFile(String filePath, String text) {
		try {
			Files.writeString(Path.of(filePath), text);
			System.out.println("Created the file: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getBookNo(int bookNumber) {
		DecimalFormat df = new DecimalFormat("00");
		return df.format(bookNumber);
	}

	public static String getChapterNo(int bookNumber, String number) {
		int chapterNumber = Integer.parseInt(number);

		if (bookNumber == 19) {
			DecimalFormat df = new DecimalFormat("000");
			return df.format(chapterNumber);
		} else {
			DecimalFormat df = new DecimalFormat("00");
			return df.format(chapterNumber);
		}
	}

	public static String removeHTMLTags(String text) {
		if (ParallelBibleCreator.keepStrongNumbers) {
			text = text.replaceAll("<(WH[0-9]+)>", "@$1#");
			text = text.replaceAll("<(WG[0-9]+)>", "@$1#");
		}

		// <TS1>The History of Creation<Ts>
		while (text.contains("<TS1>")) {
			//text = text.replaceFirst("<TS1>.+<Ts>", "");
			int startPos = text.indexOf("<TS1>");
			int endPos = text.indexOf("<Ts>");
			String htmlTag = text.substring(startPos, endPos + 4);
			text = text.replace(htmlTag, "");
		}

		// <RF>Ps. 33:6, 9<Rf>
		while (text.contains("<RF>")) {
			//text = text.replaceFirst("<RF>.+<Rf>", "");
			int startPos = text.indexOf("<RF>");
			int endPos = text.indexOf("<Rf>");
			String htmlTag = text.substring(startPos, endPos + 4);
			text = text.replace(htmlTag, "");
		}

		while (text.contains("<")) {
			int startPos = text.indexOf("<");
			int endPos = text.indexOf(">");
			String htmlTag = text.substring(startPos, endPos + 1);
			text = text.replace(htmlTag, "");
		}
		if (ParallelBibleCreator.keepStrongNumbers) {
			text = text.replaceAll("@", "<");
			text = text.replaceAll("#", ">");
		}
		return text.replaceAll("&nbsp;", "");
	}

	public static String replaceLastOccurrence(String original, String target, String replacement) {
		int lastIndex = original.lastIndexOf(target);

		if (lastIndex == -1) {
			// Target substring not found
			return original;
		}

		String before = original.substring(0, lastIndex);
		String after = original.substring(lastIndex + target.length());

		return before + replacement + after;
	}

	public static String reverseText(String verseText) {
		String[] words = verseText.split(" ");
		// Reverse the array using a loop
		for (int i = 0; i < words.length / 2; i++) {
			String temp = words[i];
			words[i] = words[words.length - 1 - i];
			words[words.length - 1 - i] = temp;
		}
		verseText = "";
		words[0] = "." + replaceLastOccurrence(words[0], ".", "");
		for (int i = 0; i < words.length; i++) {
			verseText += words[i];
			if (i < words.length - 2) {
				verseText += " ";
			}
		}
		return verseText;
	}

}
