/**
 * 
 */
package in.wordofgod.parallel.bible.creator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import in.wordofgod.bible.parser.Bible;
import in.wordofgod.bible.parser.TheWord;
import in.wordofgod.bible.parser.vosgson.Book;
import in.wordofgod.bible.parser.vosgson.Chapter;
import in.wordofgod.bible.parser.vosgson.Verse;

/**
 * 
 */
public class BiblesMap {

	protected static Map<String, Bible> biblesMap = new LinkedHashMap<String, Bible>();
	protected static Map<String, String> versionToAbbrMap = new HashMap<String, String>();
	protected static Map<String, Book> booksMap = new HashMap<String, Book>();
	protected static Map<String, Chapter> chaptersMap = new HashMap<String, Chapter>();
	protected static Map<String, Verse> versesMap = new HashMap<String, Verse>();
	protected static Bible bibleForNavigation = null;

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
			chaptersMap.put(Utilities.generateKeyforChaptersMap(version, book.getThreeLetterCode(), chapter.getChapter()),
					chapter);
			buildVersesMap(version, book, chapter);
		}
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

	protected static void buildBiblesMap() {
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
				version = Utilities.stripExtension(version);
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

	protected static void initBibleForNavigation() {
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
			bibleForNavigation = biblesMap.get(Utilities.stripExtension(ParallelBibleCreator.bibleVersions[0]));
		}
	}

	// Gen 1,2,3,5;
	protected static void buildListOfVersesByChapters(StringBuilder sb, String bookName, String[] chapterArray) {
		String randomVersion = biblesMap.entrySet().iterator().next().getKey();
		for (String chapter : chapterArray) {
			sb.append(bookName).append(" ").append(chapter).append("\n");
			Chapter chapterForNavigation = chaptersMap.get(Utilities.generateKeyforChaptersMap(randomVersion, bookName, chapter));
			for (Verse verseForNavigation : chapterForNavigation.getVerses()) {
				sb.append(bookName).append(" ").append(chapterForNavigation.getChapter()).append(":")
						.append(verseForNavigation.getNumber()).append("\n");
				for (String version : biblesMap.keySet()) {
					Verse verse = versesMap.get(generateKeyforVerseMap(version, bookName,
							chapterForNavigation.getChapter(), verseForNavigation.getNumber()));
					if (verse != null) {
						String verseText = Utilities.removeHTMLTags(verse.getUnParsedText());
						sb.append(versionToAbbrMap.get(version)).append(" ").append(verseText).append("\n");
					}
				}
			}
			sb.append("\n\n");
		}
		sb.append("\n\n");
	}

	// Gen 12:2,3,4; or // Gen 10:1-20;
	protected static void buildListOfVerses(StringBuilder sb, String bookName, String chapterNo, String[] verseArray) {
		for (String verseNo : verseArray) {
			sb.append(bookName).append(" ").append(chapterNo).append(":").append(verseNo).append("\n");
			for (String version : biblesMap.keySet()) {
				Verse verse = versesMap.get(generateKeyforVerseMap(version, bookName, chapterNo, verseNo));
				if (verse != null) {
					String verseText = Utilities.removeHTMLTags(verse.getUnParsedText());
					sb.append(versionToAbbrMap.get(version)).append(" ").append(verseText).append("\n");
				}
			}
		}
		sb.append("\n\n");
	}

}
