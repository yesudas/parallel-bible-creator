/**
 * 
 */
package in.wordofgod.parallel.bible.creator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.apache.poi.ooxml.POIXMLProperties.CoreProperties;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumns;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;

import in.wordofgod.bible.parser.Bible;
import in.wordofgod.bible.parser.vosgson.Book;
import in.wordofgod.bible.parser.vosgson.Chapter;
import in.wordofgod.bible.parser.vosgson.Verse;

/**
 * 
 */
public class WordDoc extends BiblesMap {

	private static BibleToDocLanguage languageEnglish = new ParallelBibleToDocLanguageEnglish();

	public static final String EXTENSION = ".docx";

	private static boolean LIMIT_OUTPUT = true;
	private static boolean INCLUDE_VERSION_LABEL = true;

	private static boolean NEW_TESTAMENT_ONLY = false;

	private static int uniqueBookMarkCounter = 1;

	public static void createWordDocument() {
		System.out.println("WordDocument Creation Started...");
		System.out.println("Bibles loading started...");

		buildBiblesMap();

		if (!biblesMap.isEmpty()) {
			System.out.println("Bibles loaded successfully...");
		}

		if (!"".equals(ParallelBibleCreator.biblePortions)) {
			generateTextByBiblePortions();
		} else {
			generateTextForWholeBible();
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

	private static void generateTextForWholeBible() {
		initBibleForNavigation();
		StringBuilder sb = new StringBuilder();

		// languageEnglish = createLanguageInstance("English");
		// loadBibleProperties(bible1Properties, BIBLE_NAME_1);
		// loadBibleProperties(bible2Properties, BIBLE_NAME_2);
		// createBibleVersionLabels();

		// System.out.println("Started Loading Bible: " + BIBLE_NAME_1);
		// Bible bible1 = BibleLoaderUtils.getBible(LANGUAGE_NAME_1, BIBLE_NAME_1,
		// BIBLE_INPUT_FOLDER_PATH + LANGUAGE_NAME_1 + "\\");
		// System.out.println("Completed Loading Bible: " + BIBLE_NAME_1);

		// System.out.println("Started Loading Bible: " + BIBLE_NAME_2);
		// Bible bible2 = BibleLoaderUtils.getBible(LANGUAGE_NAME_2, BIBLE_NAME_2,
		// BIBLE_INPUT_FOLDER_PATH + LANGUAGE_NAME_2 + "\\");
		// System.out.println("Completed Loading Bible: " + BIBLE_NAME_2);

		// System.out.println("BIBLE 2 Verse Map Creation started");
		// createParallelVerse(bible2);
		// System.out.println("BIBLE 2 Verse Map - Total Verses :: " +
		// bible2VersesMap.size());
		// System.out.println("BIBLE 2 Verse Map Creation completed");

		System.out.println("Word Document BIBLE Creation started");

		try {

			XWPFDocument document = new XWPFDocument();

			createPageSettings(document);
			createMetaData(document);
			createTitlePage(document);
			createBookDetailsPage(document, languageEnglish);
			createPDFIssuePage(document);
			createIndex(document);
			createContent(document);
			outputStatistics(bibleForNavigation);

			// Write the Document in file system
			Utilities.createDir(ParallelBibleCreator.outputDirectory);
			File file = new File(ParallelBibleCreator.outputDirectory + "/" + "Parallel-Bible" + EXTENSION);
			FileOutputStream out = new FileOutputStream(file);
			document.write(out);
			out.close();
			System.out.println("Word Document BIBLE Created Successfully :: " + ParallelBibleCreator.outputDirectory
					+ "/" + "Parallel-Bible" + EXTENSION);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FileNotFoundException occurs.." + e.getMessage());
		}
	}

	private static void createPageSettings(XWPFDocument document) {
		CTDocument1 doc = document.getDocument();
		CTBody body = doc.getBody();

		if (!body.isSetSectPr()) {
			body.addNewSectPr();
		}

		CTSectPr section = body.getSectPr();

		if (!section.isSetPgSz()) {
			section.addNewPgSz();
		}
		CTPageSz pageSize = section.getPgSz();
		pageSize.setOrient(STPageOrientation.PORTRAIT);
		pageSize.setW(BigInteger.valueOf(595 * 20));
		pageSize.setH(BigInteger.valueOf(842 * 20));

		// CTPageMar pageMar = section.getPgMar();
		// if (pageMar == null) {
		// pageMar = section.addNewPgMar();
		// }
		// pageMar.setLeft(BigInteger.valueOf(648));// 0.45"*72*20
		// 720 TWentieths of an Inch Point (Twips) = 720/20 = 36 pt; 36/72 = 0.5"
		// pageMar.setRight(BigInteger.valueOf(648));
		// pageMar.setTop(BigInteger.valueOf(360));
		// pageMar.setBottom(BigInteger.valueOf(648));

		System.out.println("Page Setting completed");
	}

	private static void createMetaData(XWPFDocument document) {
		CoreProperties props = document.getProperties().getCoreProperties();
		// props.setCreated("2019-08-14T21:00:00z");
		props.setLastModifiedByUser(languageEnglish.getString(BibleToDocLanguage.STR_CREATED_BY_DETAILS));
		props.setCreator(languageEnglish.getString(BibleToDocLanguage.STR_CREATED_BY_DETAILS));
		// props.setLastPrinted("2019-08-14T21:00:00z");
		// props.setModified("2019-08-14T21:00:00z");
		try {
			document.getProperties().commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Meta Data Creation completed");
	}

	private static void createTitlePage(XWPFDocument document) {
		XWPFParagraph paragraph = null;
		XWPFRun run = null;

		paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		run = paragraph.createRun();
		run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.SUB_COMMON_NAME_FONT));
		run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.SUB_COMMON_NAME_FONT_SIZE));
		run.setText(languageEnglish.getString(BibleToDocLanguage.STR_COMMON_NAME));

		paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		run = paragraph.createRun();

		StringBuilder sb = new StringBuilder();
		for (String version : biblesMap.keySet()) {
			sb.append(biblesMap.get(version).getLanguageCode() + " - " + biblesMap.get(version).getCommonName());
			sb.append(languageEnglish.getString(BibleToDocLanguage.STR_AND));
		}
		String result = Utilities.replaceLastOccurrence(sb.toString(),
				languageEnglish.getString(BibleToDocLanguage.STR_AND), "");

		run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.SUB_TITLE_1_FONT));
		run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.SUB_TITLE_1_FONT_SIZE));
		run.setText(result);

		run.addBreak();
		run.addBreak();
		run.addBreak();

		paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		run = paragraph.createRun();
		run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.SUB_TITLE_2_FONT));
		run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.SUB_TITLE_2_FONT_SIZE));
		run.setText("| xxxx Books |");
		paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		run = paragraph.createRun();
		run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.SUB_TITLE_2_FONT));
		run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.SUB_TITLE_2_FONT_SIZE));
		run.setText("| xxxx Chapters |");
		paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		run = paragraph.createRun();
		run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.SUB_TITLE_2_FONT));
		run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.SUB_TITLE_2_FONT_SIZE));
		run.setText("| xxxx Verses |");

		paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		run = paragraph.createRun();
		run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.SUB_TITLE_3_FONT));
		run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.SUB_TITLE_3_FONT_SIZE));
		run.addBreak();
		run.addBreak();
		run.addBreak();
		run.addBreak();
		run.setText(languageEnglish.getString(BibleToDocLanguage.STR_CREATED_BY_LABEL));
		paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		run = paragraph.createRun();
		run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.SUB_TITLE_4_FONT));
		run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.SUB_TITLE_4_FONT_SIZE));
		run.setText(languageEnglish.getString(BibleToDocLanguage.STR_CREATED_BY_DETAILS));

		run.addBreak();
		paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		run = paragraph.createRun();
		run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.MORE_INFO_FONT));
		run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.MORE_INFO_FONT_SIZE));
		run.setText(languageEnglish.getString(BibleToDocLanguage.MORE_INFO));

		run.addBreak(BreakType.PAGE);
		System.out.println("Title Page Creation completed");
	}

	private static void createPDFIssuePage(XWPFDocument document) {
		XWPFParagraph paragraph = null;
		XWPFRun run = null;
		paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		run = paragraph.createRun();
		run.addBreak();
		run.addBreak();
		run.addBreak();
		run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.STR_PDF_INDEX_ISSUE_FONT));
		run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.STR_PDF_INDEX_ISSUE_FONT_SIZE));
		run.setText(languageEnglish.getString(BibleToDocLanguage.STR_PDF_INDEX_ISSUE_ENGLISH));
		run.addBreak();
		run.addBreak();
		run.addBreak();
		run.addBreak();
		run.addBreak();
		run.setText(languageEnglish.getString(BibleToDocLanguage.STR_PDF_INDEX_ISSUE));

		addSectionBreak(document, 1, false);
	}

	private static void createIndex(XWPFDocument document) throws Exception {
		System.out.println("Index Creation Started...");
		XWPFParagraph paragraph;
		XWPFRun run = null;
		paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		paragraph.setStyle("Heading1");

		// Index Page Heading
		run = paragraph.createRun();
		run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.STR_INDEX_TITLE_FONT));
		run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.STR_INDEX_TITLE_FONT_SIZE));
		run.setText(languageEnglish.getString(BibleToDocLanguage.STR_INDEX_TITLE));
		CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
		cTShd.setVal(STShd.CLEAR);
		cTShd.setFill("ABABAB");

		CTBookmark bookmark = paragraph.getCTP().addNewBookmarkStart();
		bookmark.setName(languageEnglish.getString(BibleToDocLanguage.STR_INDEX_TITLE));
		bookmark.setId(BigInteger.valueOf(uniqueBookMarkCounter));
		paragraph.getCTP().addNewBookmarkEnd().setId(BigInteger.valueOf(uniqueBookMarkCounter));
		uniqueBookMarkCounter++;

		// Books Index
		paragraph = document.createParagraph();
		paragraph.setSpacingAfter(languageEnglish.getInt(BibleToDocLanguage.PARAGRAPH_SPACING_AFTER));
		for (Book book : bibleForNavigation.getBooks()) {
			if (NEW_TESTAMENT_ONLY && book.getBookNo() < 40) {
				continue;
			}
			createAnchorLink(paragraph, book.getLongName(), book.getLongName().replaceAll(" ", "_"), true, "",
					languageEnglish.getString(BibleToDocLanguage.STR_INDEX_FONT),
					languageEnglish.getInt(BibleToDocLanguage.STR_INDEX_FONT_SIZE));
		}
		paragraph = document.createParagraph();
		run = paragraph.createRun();

		addSectionBreak(document, 3, true);

		// Chapters Index
		for (Book book : bibleForNavigation.getBooks()) {
			if (NEW_TESTAMENT_ONLY && book.getBookNo() < 40) {
				continue;
			}
			paragraph = document.createParagraph();
			// paragraph.setSpacingAfter(language.getInt(BibleToDocLanguage.PARAGRAPH_SPACING_AFTER));
			run = paragraph.createRun();
			run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.STR_SUB_INDEX_FONT));
			run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.STR_SUB_INDEX_FONT_SIZE));
			run.setBold(true);
			run.setText(book.getLongName() + " : ");

			// bookmark = paragraph.getCTP().addNewBookmarkStart();
			// bookmark.setName(book.getBookNameFull().replaceAll(" ", "_"));
			// bookmark.setId(BigInteger.valueOf(uniqueBookMarkCounter));
			// paragraph.getCTP().addNewBookmarkEnd().setId(BigInteger.valueOf(uniqueBookMarkCounter));
			// uniqueBookMarkCounter++;
			for (Chapter chapter : book.getChapters()) {
				createAnchorLink(paragraph, chapter.getChapter(),
						book.getLongName().replaceAll(" ", "_") + "_" + chapter.getChapter(), false, "   ",
						languageEnglish.getString(BibleToDocLanguage.STR_SUB_INDEX_FONT),
						languageEnglish.getInt(BibleToDocLanguage.STR_SUB_INDEX_FONT_SIZE));
			}
		}
		paragraph = document.createParagraph();
		run = paragraph.createRun();

		addSectionBreak(document, 2, true);
		System.out.println("Index Creation Completed...");

	}

	private static void createAnchorLink(XWPFParagraph paragraph, String linkText, String bookMarkName,
			boolean carriageReturn, String space, String fontFamily, int fontSize) {
		CTHyperlink cthyperLink = paragraph.getCTP().addNewHyperlink();
		cthyperLink.setAnchor(bookMarkName);
		cthyperLink.addNewR();
		XWPFHyperlinkRun hyperlinkrun = new XWPFHyperlinkRun(cthyperLink, cthyperLink.getRArray(0), paragraph);
		hyperlinkrun.setFontFamily(fontFamily);
		hyperlinkrun.setFontSize(fontSize);
		hyperlinkrun.setText(linkText);
		hyperlinkrun.setColor("0000FF");
		hyperlinkrun.setUnderline(UnderlinePatterns.SINGLE);
		if (space != null && space != "") {
			XWPFRun run = paragraph.createRun();
			run.setText(space);
		}
		if (carriageReturn) {
			XWPFRun run = paragraph.createRun();
			run.addCarriageReturn();
		}
	}

	private static void createBookDetailsPage(XWPFDocument document, BibleToDocLanguage language) {

		XWPFParagraph paragraph = null;
		XWPFRun run = null;

		paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.LEFT);
		run = paragraph.createRun();
		run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
		run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
		run.setText("WOG BOOKS XXXXX");
		run.setBold(true);
		run.addBreak();

		run = paragraph.createRun();
		run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
		run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
		run.setText("First Edition 2024");
		run.addBreak();
		run.addBreak();

		for (String version : biblesMap.keySet()) {
			Bible bible = biblesMap.get(version);

			// Bible Details
			run = paragraph.createRun();
			run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
			run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
			run.setBold(true);
			run.setText(BibleToDocLanguage.STR_COMMON_NAME_LABEL);
			run = paragraph.createRun();
			run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
			run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
			run.setText(bible.getCommonName());

			run = paragraph.createRun();
			run.addCarriageReturn();
			run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
			run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
			run.setBold(true);
			run.setText(BibleToDocLanguage.STR_SHORT_NAME_LABEL);
			run = paragraph.createRun();
			run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
			run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
			run.setText(bible.getShortName());

			run = paragraph.createRun();
			run.addCarriageReturn();
			run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
			run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
			run.setBold(true);
			run.setText(BibleToDocLanguage.STR_LONG_NAME_LABEL);
			run = paragraph.createRun();
			run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
			run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
			run.setText(bible.getLongName());

			run = paragraph.createRun();
			run.addCarriageReturn();
			run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
			run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
			run.setBold(true);
			run.setText(BibleToDocLanguage.STR_LONG_NAME_ENGLISH_LABEL);
			run = paragraph.createRun();
			run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
			run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
			run.setText(bible.getLongEnglishName());

			run = paragraph.createRun();
			run.addCarriageReturn();
			run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
			run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
			run.setBold(true);
			run.setText(BibleToDocLanguage.STR_TRANSLATED_BY_LABEL);
			run = paragraph.createRun();
			run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
			run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
			run.setText(bible.getTranslatedBy());

			run = paragraph.createRun();
			run.addCarriageReturn();
			run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
			run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
			run.setBold(true);
			run.setText(BibleToDocLanguage.STR_PUBLISHED_BY_LABEL);
			run = paragraph.createRun();
			run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
			run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
			run.setText(bible.getPublishedBy());

			// Copy Right
			run = paragraph.createRun();
			run.addCarriageReturn();
			run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
			run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
			run.setBold(true);
			run.setText(BibleToDocLanguage.STR_COPYRIGHT_LABEL);
			// run.addCarriageReturn();
			run = paragraph.createRun();
			run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
			run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
			run.setText(bible.getCopyRight());
			run.addCarriageReturn();

			run = paragraph.createRun();
			run.addBreak();
			run.addBreak();

		}

		// Created By
		run = paragraph.createRun();
		run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
		run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
		run.setBold(true);
		run.setText(BibleToDocLanguage.STR_CREATED_BY_LABEL);
		run.addCarriageReturn();
		run = paragraph.createRun();
		run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
		run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
		run.setText(BibleToDocLanguage.STR_CREATED_BY_DETAILS);
		run.addBreak();
		run.addBreak();

		run = paragraph.createRun();
		run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
		run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
		run.setBold(true);
		run.setText(BibleToDocLanguage.STR_DOWNLOAD);
		run.addCarriageReturn();
		run = paragraph.createRun();
		createExternalLinks(paragraph, BibleToDocLanguage.STR_DOWNLOAD_LINK1_TEXT,
				BibleToDocLanguage.STR_DOWNLOAD_LINK1__URL);
		run = paragraph.createRun();
		run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
		run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
		run.setText(" and ");
		createExternalLinks(paragraph, BibleToDocLanguage.STR_DOWNLOAD_LINK2_TEXT,
				BibleToDocLanguage.STR_DOWNLOAD_LINK2__URL);
		run = paragraph.createRun();
		run.addBreak();
		run.addBreak();

		run = paragraph.createRun();
		run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
		run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
		run.setBold(true);
		run.setText(BibleToDocLanguage.STR_CONTACT_US);
		run.addCarriageReturn();
		run = paragraph.createRun();
		run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
		run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
		run.setText(BibleToDocLanguage.STR_CONTACT_US_EMAIL);
		run.addCarriageReturn();
		run = paragraph.createRun();
		run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
		run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
		run.setText(BibleToDocLanguage.STR_CONTACT_US_MOBILE);
		run.addBreak();
		run.addBreak();

		run = paragraph.createRun();
		run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
		run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
		run.setBold(true);
		run.setText(BibleToDocLanguage.STR_FOLLOW_US);
		run.addCarriageReturn();
		run = paragraph.createRun();
		run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
		run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
		run.setText(BibleToDocLanguage.STR_YOUTUBE);
		createExternalLinks(paragraph, BibleToDocLanguage.STR_YOUTUBE_TEXT, BibleToDocLanguage.STR_YOUTUBE_URL);
		run = paragraph.createRun();
		run.addCarriageReturn();
		run.setFontFamily(language.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
		run.setFontSize(language.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
		run.setText(BibleToDocLanguage.STR_FACEBOOK);
		createExternalLinks(paragraph, BibleToDocLanguage.STR_FACEBOOK_TEXT, BibleToDocLanguage.STR_FACEBOOK_URL);

		addSectionBreak(document, 1, false);
	}

	private static void createExternalLinks(XWPFParagraph paragraph, String linkText, String linkURL) {
		String id = paragraph.getDocument().getPackagePart()
				.addExternalRelationship(linkURL, XWPFRelation.HYPERLINK.getRelation()).getId();
		CTHyperlink cthyperLink = paragraph.getCTP().addNewHyperlink();
		cthyperLink.setId(id);
		cthyperLink.addNewR();
		XWPFHyperlinkRun hyperlinkrun = new XWPFHyperlinkRun(cthyperLink, cthyperLink.getRArray(0), paragraph);
		hyperlinkrun.setFontFamily(languageEnglish.getString(BibleToDocLanguage.BOOK_DETAILS_FONT));
		hyperlinkrun.setFontSize(languageEnglish.getInt(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE));
		hyperlinkrun.setText(linkText);
		hyperlinkrun.setColor("0000FF");
		hyperlinkrun.setUnderline(UnderlinePatterns.SINGLE);
	}

	/**
	 * Adds Section Settings for the contents added so far
	 * 
	 * @param document
	 */
	private static CTSectPr addSectionBreak(XWPFDocument document, int noOfColumns, boolean setMargin) {
		XWPFParagraph paragraph = document.createParagraph();
		paragraph = document.createParagraph();
		CTSectPr ctSectPr = paragraph.getCTP().addNewPPr().addNewSectPr();
		CTColumns ctColumns = ctSectPr.addNewCols();
		ctColumns.setNum(BigInteger.valueOf(noOfColumns));

		if (setMargin) {
			CTPageMar pageMar = ctSectPr.getPgMar();
			if (pageMar == null) {
				pageMar = ctSectPr.addNewPgMar();
			}
			pageMar.setLeft(BigInteger.valueOf(648));// 0.45"*72*20
			// 720 TWentieths of an Inch Point (Twips) = 720/20 = 36 pt; 36/72 = 0.5"
			pageMar.setRight(BigInteger.valueOf(648));
			pageMar.setTop(BigInteger.valueOf(648));
			pageMar.setBottom(BigInteger.valueOf(648));
			// pageMar.setFooter(BigInteger.valueOf(720));
			// pageMar.setHeader(BigInteger.valueOf(720));
			// pageMar.setGutter(BigInteger.valueOf(0));
		}
		return ctSectPr;
	}

	private static void createContent(XWPFDocument document) {
		System.out.println("Content Creation Started...");
		XWPFParagraph paragraph = null;
		XWPFRun run = null;
		int counter = 0;
		for (Book bookForNavigation : bibleForNavigation.getBooks()) {
			if (LIMIT_OUTPUT && counter++ > 2) {
				return;
			}
			if (NEW_TESTAMENT_ONLY && bookForNavigation.getBookNo() < 40) {
				continue;
			}
			// Book Heading Page
			paragraph = document.createParagraph();
			paragraph.setAlignment(ParagraphAlignment.CENTER);
			run = paragraph.createRun();
			run.addBreak();
			run.addBreak();
			run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.BOOK_HEADING_FONT));
			run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.BOOK_HEADING_FONT_SIZE));
			run.setText(bookForNavigation.getLongName());

			CTBookmark bookmark = paragraph.getCTP().addNewBookmarkStart();
			bookmark.setName(bookForNavigation.getLongName().replaceAll(" ", "_"));
			bookmark.setId(BigInteger.valueOf(uniqueBookMarkCounter));
			paragraph.getCTP().addNewBookmarkEnd().setId(BigInteger.valueOf(uniqueBookMarkCounter));
			uniqueBookMarkCounter++;
			paragraph = document.createParagraph();
			paragraph.setAlignment(ParagraphAlignment.CENTER);
			run = paragraph.createRun();
			run.addBreak();

			for (Chapter chapterForNavigation : bookForNavigation.getChapters()) {
				createAnchorLink(paragraph, chapterForNavigation.getChapter(),
						bookForNavigation.getLongName().replaceAll(" ", "_") + "_" + chapterForNavigation.getChapter(),
						false, "    ", languageEnglish.getString(BibleToDocLanguage.STR_CHAPTER_INDEX_FONT),
						languageEnglish.getInt(BibleToDocLanguage.STR_CHAPTER_INDEX_FONT_SIZE));
			}
			run = paragraph.createRun();
			run.addBreak();

			addSectionBreak(document, 1, false);

			// Chapters and Contents
			if (ParallelBibleCreator.useTableFormatForVerses) {
				createChaptersContentByTable(bookForNavigation, document);
			} else {
				createChaptersContentByText(bookForNavigation, document);
			}

			paragraph = document.createParagraph();
			run = paragraph.createRun();

			addSectionBreak(document, 1, true);
		}

		System.out.println("Content Creation Completed...");
	}

	private static void createChaptersContentByText(Book bookForNavigation, XWPFDocument document) {
		XWPFParagraph paragraph = null;
		XWPFRun run = null;
		for (Chapter chapterForNavigation : bookForNavigation.getChapters()) {
			paragraph = document.createParagraph();
			paragraph.setAlignment(ParagraphAlignment.CENTER);
			run = paragraph.createRun();
			run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.CHAPTER_HEADING_FONT));
			run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.CHAPTER_HEADING_FONT_SIZE));
			run.setText(bookForNavigation.getLongName() + " " + chapterForNavigation.getChapter());
			CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
			cTShd.setVal(STShd.CLEAR);
			cTShd.setFill("ABABAB");
			CTBookmark bookmark = paragraph.getCTP().addNewBookmarkStart();
			bookmark.setName(
					bookForNavigation.getLongName().replaceAll(" ", "_") + "_" + chapterForNavigation.getChapter());
			bookmark.setId(BigInteger.valueOf(uniqueBookMarkCounter));
			paragraph.getCTP().addNewBookmarkEnd().setId(BigInteger.valueOf(uniqueBookMarkCounter));
			uniqueBookMarkCounter++;
			for (Verse verseForNavigation : chapterForNavigation.getVerses()) {
				paragraph = document.createParagraph();
				if (ParallelBibleCreator.bibleTextDirectionRTL) {
					// paragraph.setAlignment(ParagraphAlignment.RIGHT);
				}
				run = paragraph.createRun();
				run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.VERSE_FONT));
				run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.VERSE_FONT_SIZE));
				run.setBold(true);
				run.setText(verseForNavigation.getNumber() + ". ");

				for (String version : biblesMap.keySet()) {
					Verse verse = versesMap
							.get(Utilities.generateKeyforVerseMap(version, bookForNavigation.getThreeLetterCode(),
									chapterForNavigation.getChapter(), verseForNavigation.getNumber()));
					if (verse != null) {
						String verseText = Utilities.removeHTMLTags(verse.getUnParsedText());
						if (INCLUDE_VERSION_LABEL) {
							run = paragraph.createRun();
							run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.VERSE_FONT));
							run.setFontSize(
									languageEnglish.getInt(BibleToDocLanguage.STR_PARALLEL_BIBLE_SHORT_NAME_FONT_SIZE));
							run.setText(version + " ");
						}
						run = paragraph.createRun();
						run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.VERSE_FONT));
						run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.VERSE_FONT_SIZE));
						if (ParallelBibleCreator.bibleTextDirectionRTL
								&& !"iw".equalsIgnoreCase(biblesMap.get(version).getLanguageCode())) {
							run.setText(Utilities.reverseText(verseText));
						} else {
							run.setText(verseText);
						}
						run.addBreak();
					}
				}
			}
		}
	}

	private static void createChaptersContentByTable(Book bookForNavigation, XWPFDocument document) {
		XWPFParagraph paragraph = null;
		XWPFRun run = null;
		for (Chapter chapterForNavigation : bookForNavigation.getChapters()) {
			paragraph = document.createParagraph();
			paragraph.setAlignment(ParagraphAlignment.CENTER);
			run = paragraph.createRun();
			run.setFontFamily(languageEnglish.getString(BibleToDocLanguage.CHAPTER_HEADING_FONT));
			run.setFontSize(languageEnglish.getInt(BibleToDocLanguage.CHAPTER_HEADING_FONT_SIZE));
			run.setText(bookForNavigation.getLongName() + " " + chapterForNavigation.getChapter());
			CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
			cTShd.setVal(STShd.CLEAR);
			cTShd.setFill("ABABAB");
			CTBookmark bookmark = paragraph.getCTP().addNewBookmarkStart();
			bookmark.setName(
					bookForNavigation.getLongName().replaceAll(" ", "_") + "_" + chapterForNavigation.getChapter());
			bookmark.setId(BigInteger.valueOf(uniqueBookMarkCounter));
			paragraph.getCTP().addNewBookmarkEnd().setId(BigInteger.valueOf(uniqueBookMarkCounter));
			uniqueBookMarkCounter++;

			// create table
			XWPFTable table = document.createTable();

			// create first row
			XWPFTableRow tableRowOne = table.getRow(0);
			tableRowOne.getCell(0).setText("#");
			for (String version : biblesMap.keySet()) {
				tableRowOne.addNewTableCell().setText(biblesMap.get(version).getAbbr());
			}

			for (Verse verseForNavigation : chapterForNavigation.getVerses()) {
				int i = 0;
				// create second row
				XWPFTableRow tableRowTwo = table.createRow();
				tableRowTwo.getCell(i++).setText(verseForNavigation.getNumber() + ". ");

				for (String version : biblesMap.keySet()) {
					Verse verse = versesMap
							.get(Utilities.generateKeyforVerseMap(version, bookForNavigation.getThreeLetterCode(),
									chapterForNavigation.getChapter(), verseForNavigation.getNumber()));
					if (verse != null) {
						String verseText = Utilities.removeHTMLTags(verse.getUnParsedText());
						tableRowTwo.getCell(i++).setText(verseText);
					}
				}
			}
		}
	}

	private static void outputStatistics(Bible bible) {
		System.out.println("Total No of Books :: " + bible.getBooks().size());
		int chaptersCount = 0;
		int versesCount = 0;
		// int wordsCount = 0;
		// Set<String> uniqueWords = new HashSet<String>();
		for (Book book : bible.getBooks()) {
			chaptersCount += book.getChapters().size();
			for (Chapter chapter : book.getChapters()) {
				versesCount += chapter.getVerses().size();
				/*
				 * for (Verse verse : chapter.getVerses()) { String[] words =
				 * verse.getText().split("[\\s']"); wordsCount += words.length; for (String word
				 * : words) { word =
				 * word.replaceAll("[\\\"\\(\\)\\.\\:\\,\\;\\}\\{\\?\\]\\[\\?]", "");
				 * uniqueWords.add(word); } }
				 */
			}
		}
		System.out.println("Total No of Chapters :: " + chaptersCount);
		System.out.println("Total No of Verses :: " + versesCount);
		// System.out.println("Total No of Words :: " + wordsCount);
		// System.out.println("Total No of Unique Words :: " + uniqueWords.size());
	}

}