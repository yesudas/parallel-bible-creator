/**
 * 
 */
package in.wordofgod.parallel.bible.creator;

import in.wordofgod.bible.parser.vosgson.Book;
import in.wordofgod.bible.parser.vosgson.Chapter;
import in.wordofgod.bible.parser.vosgson.Verse;

/**
 * 
 */
public class TextFiles extends BiblesMap {

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

	public static void generateTextByBiblePortions() {
		System.out.println("Generating parallel bible Text By BiblePortions started...");
		System.out.println("Given biblePortions: " + ParallelBibleCreator.biblePortions);
		initBibleForNavigation();
		StringBuilder sb = new StringBuilder();
		String[] portions = ParallelBibleCreator.biblePortions.trim().split(";");
		for (String portion : portions) {// TODO அப். 6:5, 8-15
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
		Utilities.createDir(ParallelBibleCreator.outputDirectory);
		Utilities.createFile(ParallelBibleCreator.outputDirectory + "/" + "Parallel-Bible.txt", sb.toString());
	}

	private static void generateTextForWholeBible(boolean filesByDirectory) {
		initBibleForNavigation();
		StringBuilder sb = new StringBuilder();
		for (Book bookForNavigation : bibleForNavigation.getBooks()) {
			String bookDir = null;
			if (filesByDirectory) {
				bookDir = Utilities.getBookNo(bookForNavigation.getBookNo()) + " " + bookForNavigation.getLongName();
				Utilities.createDir(bookDir);
			}
			for (Chapter chapterForNavigation : bookForNavigation.getChapters()) {
				for (Verse verseForNavigation : chapterForNavigation.getVerses()) {
					sb.append(bookForNavigation.getEnglishName()).append(" ").append(chapterForNavigation.getChapter())
							.append(":").append(verseForNavigation.getNumber()).append("\n");
					for (String version : biblesMap.keySet()) {
						Verse verse = versesMap
								.get(Utilities.generateKeyforVerseMap(version, bookForNavigation.getThreeLetterCode(),
										chapterForNavigation.getChapter(), verseForNavigation.getNumber()));
						if (verse != null) {
							String verseText = Utilities.removeHTMLTags(verse.getUnParsedText());
							sb.append(versionToAbbrMap.get(version)).append(" ").append(verseText).append("\n");
						}
					}
					sb.append("\n");
				}
				if (filesByDirectory) {
					String filePath = ParallelBibleCreator.outputDirectory + "/" + bookDir + "/"
							+ Utilities.getChapterNo(bookForNavigation.getBookNo(), chapterForNavigation.getChapter()) + ".txt";
					Utilities.createFile(filePath, sb.toString());
					sb = new StringBuilder();
				}
			}
		}
		if (!filesByDirectory) {
			Utilities.createDir(ParallelBibleCreator.outputDirectory);
			Utilities.createFile(ParallelBibleCreator.outputDirectory + "/" + "Parallel-Bible.txt", sb.toString());
		}
	}

}
