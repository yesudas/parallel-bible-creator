/**
 * 
 */
package in.wordofgod.parallel.bible.creator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import in.wordofgod.bible.parser.Bible;
import in.wordofgod.bible.parser.TheWord;
import in.wordofgod.bible.parser.vosgson.Book;
import in.wordofgod.bible.parser.vosgson.Chapter;
import in.wordofgod.bible.parser.vosgson.Verse;

/**
 * 
 */
public class TextFiles {

	private static Map<String, Bible> biblesMap = new HashMap<String, Bible>();
	private static Map<String, String> versionToAbbrMap = new HashMap<String, String>();
	private static Map<String, Book> booksMap = new HashMap<String, Book>();
	private static Map<String, Chapter> chaptersMap = new HashMap<String, Chapter>();
	private static Map<String, Verse> versesMap = new HashMap<String, Verse>();
	private static Bible bibleForNavigation = null;

	public static void createSingleTextFile() {
		System.out.println("SingleTextFile Creation Started...");
		System.out.println("Bibles loading started...");

		buildBiblesMap();

		if (!biblesMap.isEmpty()) {
			System.out.println("Bibles loaded successfully...");
		}

		if (!"".equals(ParallelBibleCreator.biblePortions)) {
			generateTextByBiblePortions();
		} else {
			generateTextForWholeBible(false);
		}

		System.out.println("Results are saved in the directory: " + ParallelBibleCreator.outputDirectory);
	}

	public static void createTextFilesByDirectory() {
		System.out.println("TextFilesByDirectory Creation Started...");
		System.out.println("Bibles loading started...");

		buildBiblesMap();

		if (!biblesMap.isEmpty()) {
			System.out.println("Bibles loaded successfully...");
		}

		if (!"".equals(ParallelBibleCreator.biblePortions)) {
			generateTextByBiblePortions();
		} else {
			generateTextForWholeBible(true);
		}

		System.out.println("Results are saved in the directory: " + ParallelBibleCreator.outputDirectory);
	}

	private static void buildBooksMap(String version, Bible bible) {
		for (Book book : bible.getBooks()) {
			booksMap.put(generateKeyforBooksMap(version, book.getThreeLetterCode()), book);
			buildChaptersMap(version, book);
		}
	}

	private static String generateKeyforBooksMap(String version, String threeLetterCode) {
		return version + "-" + threeLetterCode;
	}

	private static void buildChaptersMap(String version, Book book) {
		for (Chapter chapter : book.getChapters()) {
			chaptersMap.put(generateKeyforChaptersMap(version, book.getThreeLetterCode(), chapter.getChapter()),
					chapter);
			buildVersesMap(version, book, chapter);
		}
	}

	private static String generateKeyforChaptersMap(String version, String threeLetterCode, String chapter) {
		return version + "-" + threeLetterCode + "-" + chapter;
	}

	private static void buildVersesMap(String version, Book book, Chapter chapter) {
		for (Verse verse : chapter.getVerses()) {
			versesMap.put(
					generateKeyforVerseMap(version, book.getThreeLetterCode(), chapter.getChapter(), verse.getNumber()),
					verse);
		}
	}

	private static String generateKeyforVerseMap(String version, String threeLetterCode, String chapter,
			String number) {
		return version + "-" + threeLetterCode + "-" + chapter + "-" + number;
	}

	private static void buildBiblesMap() {
		for (String version : ParallelBibleCreator.bibleVersions) {
			String biblePath = ParallelBibleCreator.bibleSourceDirectory + "/" + version;
			String bibleInformationPath = biblePath != null && biblePath.lastIndexOf(".") > 0
					? biblePath.substring(0, biblePath.lastIndexOf("."))
					: biblePath;

			File file = new File(biblePath);
			Bible bible;
			try {
				System.out.println("Loading the version: " + version);
				bible = TheWord.getBible(file.getAbsolutePath(), bibleInformationPath + "-information.ini");
				version = stripExtension(version);
				biblesMap.put(version, bible);
				versionToAbbrMap.put(version, bible.getAbbr());
				buildBooksMap(version, bible);
			} catch (FileNotFoundException e) {
				System.out.println(
						"Please use \\\\ or / in INFORMATION.ini file for bibleSourceDirectory as well as outputDirectory instead of just single \\");
				e.printStackTrace();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	private static String stripExtension(String version) {
		return version != null && version.lastIndexOf(".") > 0 ? version.substring(0, version.lastIndexOf("."))
				: version;
	}

	public static void generateTextByBiblePortions() {
		System.out.println("Generating parallel bible Text By BiblePortions started...");
		System.out.println("Given biblePortions: " + ParallelBibleCreator.biblePortions);
		initBibleForNavigation();
		StringBuilder sb = new StringBuilder();
		String[] portions = ParallelBibleCreator.biblePortions.trim().split(";");
		for (String portion : portions) {//TODO அப். 6:5, 8-15
			sb.append(portion).append("\n");
			if (!portion.contains(":")) {// Gen 1,2,3,5;
				String[] portionArr = portion.trim().split(" ");
				String bookName = portionArr[0];
				String[] chapterArray = portionArr[1].trim().split(",");
				buildListOfVersesByChapters(sb, bookName, chapterArray);
			} else if (portion.contains("-")) {// Gen 10:1-20;
				String[] portionArr = portion.trim().split(" ");
				String bookName = portionArr[0];
				String[] chapterAndVersesArray = portionArr[1].trim().split(":");
				int chapterNo = Integer.parseInt(chapterAndVersesArray[0]);
				String verses = chapterAndVersesArray[1];
				String[] versesArray = verses.trim().split("-");
				int fromVerseNo = Integer.parseInt(versesArray[0]);
				if (versesArray.length > 1) {
					int toVerseNo = Integer.parseInt(versesArray[1]);
					String[] verseArray = new String[toVerseNo - fromVerseNo + 1];
					for (int i = fromVerseNo; i <= toVerseNo; i++) {
						verseArray[i - 1] = "" + i;
					}
					buildListOfVerses(sb, bookName, "" + chapterNo, verseArray);
				} else {
					String[] verseArray = { "" + fromVerseNo };
					buildListOfVerses(sb, bookName, "" + chapterNo, verseArray);
				}
			} else {// Gen 12:2,3,4;
				String[] portionArr = portion.split(" ");
				String bookName = portionArr[0];
				String[] chapterAndVersesArray = portionArr[1].split(":");
				int chapterNo = Integer.parseInt(chapterAndVersesArray[0]);
				if (chapterAndVersesArray.length > 1) {
					String verses = chapterAndVersesArray[1];
					String[] verseArray = verses.split(",");
					buildListOfVerses(sb, bookName, "" + chapterNo, verseArray);
				}
			}
		}
		System.out.println("Generating parallel bible Text By BiblePortions completed...");
		createDir(ParallelBibleCreator.outputDirectory);
		createFile(ParallelBibleCreator.outputDirectory + "/" + "Parallel-Bible.txt", sb.toString());
	}

	// Gen 1,2,3,5;
	private static void buildListOfVersesByChapters(StringBuilder sb, String bookName, String[] chapterArray) {
		String randomVersion = biblesMap.entrySet().iterator().next().getKey();
		for (String chapter : chapterArray) {
			sb.append(bookName).append(" ").append(chapter).append("\n");
			Chapter chapterForNavigation = chaptersMap.get(generateKeyforChaptersMap(randomVersion, bookName, chapter));
			for (Verse verseForNavigation : chapterForNavigation.getVerses()) {
				sb.append(bookName).append(" ").append(chapterForNavigation.getChapter()).append(":")
						.append(verseForNavigation.getNumber()).append("\n");
				for (String version : biblesMap.keySet()) {
					Verse verse = versesMap.get(generateKeyforVerseMap(version, bookName,
							chapterForNavigation.getChapter(), verseForNavigation.getNumber()));
					if (verse != null) {
						String verseText = removeHTMLTags(verse.getUnParsedText());
						sb.append(versionToAbbrMap.get(version)).append(" ").append(verseText).append("\n");
					}
				}
			}
			sb.append("\n\n");
		}
		sb.append("\n\n");
	}

	// Gen 12:2,3,4; or // Gen 10:1-20;
	private static void buildListOfVerses(StringBuilder sb, String bookName, String chapterNo, String[] verseArray) {
		for (String verseNo : verseArray) {
			sb.append(bookName).append(" ").append(chapterNo).append(":").append(verseNo).append("\n");
			for (String version : biblesMap.keySet()) {
				Verse verse = versesMap.get(generateKeyforVerseMap(version, bookName, chapterNo, verseNo));
				if (verse != null) {
					String verseText = removeHTMLTags(verse.getUnParsedText());
					sb.append(versionToAbbrMap.get(version)).append(" ").append(verseText).append("\n");
				}
			}
		}
		sb.append("\n\n");
	}

	private static void generateTextForWholeBible(boolean filesByDirectory) {
		initBibleForNavigation();
		StringBuilder sb = new StringBuilder();
		for (Book bookForNavigation : bibleForNavigation.getBooks()) {
			String bookDir = null;
			if (filesByDirectory) {
				bookDir = getBookNo(bookForNavigation.getBookNo()) + " " + bookForNavigation.getLongName();
				createDir(bookDir);
			}
			for (Chapter chapterForNavigation : bookForNavigation.getChapters()) {
				for (Verse verseForNavigation : chapterForNavigation.getVerses()) {
					sb.append(bookForNavigation.getEnglishName()).append(" ").append(chapterForNavigation.getChapter())
							.append(":").append(verseForNavigation.getNumber()).append("\n");
					for (String version : biblesMap.keySet()) {
						Verse verse = versesMap
								.get(generateKeyforVerseMap(version, bookForNavigation.getThreeLetterCode(),
										chapterForNavigation.getChapter(), verseForNavigation.getNumber()));
						if (verse != null) {
							String verseText = removeHTMLTags(verse.getUnParsedText());
							sb.append(versionToAbbrMap.get(version)).append(" ").append(verseText).append("\n");
						}
					}
					sb.append("\n");
				}
				if (filesByDirectory) {
					String filePath = ParallelBibleCreator.outputDirectory + "/" + bookDir + "/"
							+ getChapterNo(bookForNavigation.getBookNo(), chapterForNavigation.getChapter()) + ".txt";
					createFile(filePath, sb.toString());
					sb = new StringBuilder();
				}
			}
		}
		if (!filesByDirectory) {
			createDir(ParallelBibleCreator.outputDirectory);
			createFile(ParallelBibleCreator.outputDirectory + "/" + "Parallel-Bible.txt", sb.toString());
		}
	}

	private static void initBibleForNavigation() {
		if (bibleForNavigation != null) {
			return;
		}
		Bible bible = biblesMap.get(ParallelBibleCreator.bibleVersionForBookNames);
		if (bible != null && bible.getBooks().size() > 27) {
			bibleForNavigation = bible;
		} else {
			for (String version : biblesMap.keySet()) {
				bible = biblesMap.get(version);
				if (bible.getBooks().size() > 27) {
					bibleForNavigation = bible;
					break;
				}
			}
		}
		if (bibleForNavigation == null) {
			bibleForNavigation = biblesMap.get(stripExtension(ParallelBibleCreator.bibleVersions[0]));
		}
	}

	private static void createDir(String dirPath) {
		File dir = new File(ParallelBibleCreator.outputDirectory + "/" + dirPath);
		if (dir.exists()) {
			System.out.println("Directory already exists: " + dir.getAbsolutePath());
		} else {
			dir.mkdirs();
			System.out.println("Created the directory: " + dir.getAbsolutePath());
		}
	}

	private static void createFile(String filePath, String text) {
		try {
			Files.writeString(Path.of(filePath), text);
			System.out.println("Created the file: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getBookNo(int bookNumber) {
		DecimalFormat df = new DecimalFormat("00");
		return df.format(bookNumber);
	}

	private static String getChapterNo(int bookNumber, String number) {
		int chapterNumber = Integer.parseInt(number);

		if (bookNumber == 19) {
			DecimalFormat df = new DecimalFormat("000");
			return df.format(chapterNumber);
		} else {
			DecimalFormat df = new DecimalFormat("00");
			return df.format(chapterNumber);
		}
	}

	private static String removeHTMLTags(String text) {
		if (ParallelBibleCreator.keepStrongNumbers) {
			text = text.replaceAll("<(WH[0-9]+)>", "@$1#");
			text = text.replaceAll("<(WG[0-9]+)>", "@$1#");
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

}
