package in.wordofgod.parallel.bible.creator;

import java.util.HashMap;
import java.util.Map;

public class ParallelBibleToDocLanguageEnglish implements BibleToDocLanguage {

	private Map<String, String[]> charMap = null;
	
	private static Map<String, Object> stringsMap = new HashMap<String, Object>();
	
	static {

		//Specific to Parallel Bible
		stringsMap.put(BibleToDocLanguage.SUB_COMMON_NAME_FONT, "Cambria");
		stringsMap.put(BibleToDocLanguage.SUB_COMMON_NAME_FONT_SIZE, 72);
		stringsMap.put(BibleToDocLanguage.STR_COMMON_NAME, "Parallel Bible");
		stringsMap.put(BibleToDocLanguage.STR_AND, " & ");
		stringsMap.put(BibleToDocLanguage.STR_PARALLEL_BIBLE_SHORT_NAME_FONT_SIZE, 12);
		
		//Title Page
		//stringsMap.put(BibleToDocLanguage.SUB_TITLE_1, "YLT BIBLE");
		//stringsMap.put(BibleToDocLanguage.STR_BOOK_NO, "WOG BOOKS 49");
		stringsMap.put(BibleToDocLanguage.STR_BOOK_EDITION, "First Edition 2022");
		stringsMap.put(BibleToDocLanguage.SUB_TITLE_2, "| 66 Books |\n| 1,189 Chapters |\n| 31,102 Verses |\n| 8,02,871 Words |\n| 14,929 Unique Words |");
		//stringsMap.put(BibleToDocLanguage.SUB_TITLE_4, "Yesudas Solomon, www.WordOfGod.in");
		
		stringsMap.put(BibleToDocLanguage.SUB_TITLE_1_FONT, "Cambria");
		stringsMap.put(BibleToDocLanguage.SUB_TITLE_1_FONT_SIZE, 24);
		stringsMap.put(BibleToDocLanguage.SUB_TITLE_2_FONT, "Cambria");
		stringsMap.put(BibleToDocLanguage.SUB_TITLE_2_FONT_SIZE, 22);

		stringsMap.put(BibleToDocLanguage.SUB_TITLE_3, "By:");
		stringsMap.put(BibleToDocLanguage.SUB_TITLE_3_FONT, "Cambria");
		stringsMap.put(BibleToDocLanguage.SUB_TITLE_3_FONT_SIZE, 16);

		stringsMap.put(BibleToDocLanguage.SUB_TITLE_4_FONT, "Cambria");
		stringsMap.put(BibleToDocLanguage.SUB_TITLE_4_FONT_SIZE, 20);

		stringsMap.put(BibleToDocLanguage.MORE_INFO, "Given free of cost based on Matthew 10:8 - \"Freely Received; Freely Give\". So, Share it freely!");
		stringsMap.put(BibleToDocLanguage.MORE_INFO_FONT, "Calibri (Body)");
		stringsMap.put(BibleToDocLanguage.MORE_INFO_FONT_SIZE, 18);

		//Book Details Page
		stringsMap.put(BibleToDocLanguage.BOOK_DETAILS_FONT, "Calibri (Body)");
		stringsMap.put(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE, 14);
		
		stringsMap.put(BibleToDocLanguage.STR_CREATED_BY_LABEL, "Created By:");
		stringsMap.put(BibleToDocLanguage.STR_CREATED_BY_DETAILS, "Yesudas Solomon, www.WordOfGod.in");

		stringsMap.put(BibleToDocLanguage.STR_COPYRIGHT_DETAILS, "This book is not copyright protected. You are free to download, print and make copies without any permission from us.");
		
		//PDF Issue Guidance Page
		stringsMap.put(BibleToDocLanguage.STR_PDF_INDEX_ISSUE, "");
		stringsMap.put(BibleToDocLanguage.STR_PDF_INDEX_ISSUE_ENGLISH, "If you are using this PDF in mobile, Navigation by Index may not work properly. We would recommend ReadEra App for better performance and navigation experience.");
		stringsMap.put(BibleToDocLanguage.STR_PDF_INDEX_ISSUE_FONT, "Calibri (Body)");
		stringsMap.put(BibleToDocLanguage.STR_PDF_INDEX_ISSUE_FONT_SIZE, 24);

		//Index Page
		stringsMap.put(BibleToDocLanguage.STR_INDEX_TITLE, "Index");
		stringsMap.put(BibleToDocLanguage.STR_INDEX_TITLE_FONT, "Calibri (Body)");
		stringsMap.put(BibleToDocLanguage.STR_INDEX_TITLE_FONT_SIZE, 35);

		stringsMap.put(BibleToDocLanguage.STR_INDEX_FONT, "Calibri (Body)");
		stringsMap.put(BibleToDocLanguage.STR_INDEX_FONT_SIZE, 19);
		stringsMap.put(BibleToDocLanguage.STR_CHAPTER_INDEX_FONT, "Calibri (Body)");
		stringsMap.put(BibleToDocLanguage.STR_CHAPTER_INDEX_FONT_SIZE, 24);
		stringsMap.put(BibleToDocLanguage.STR_SUB_INDEX_FONT, "Calibri (Body)");
		stringsMap.put(BibleToDocLanguage.STR_SUB_INDEX_FONT_SIZE, 12);
		
		//Content Pages
		stringsMap.put(BibleToDocLanguage.BOOK_HEADING_FONT, "Cambria");
		stringsMap.put(BibleToDocLanguage.BOOK_HEADING_FONT_SIZE, 36);
		
		stringsMap.put(BibleToDocLanguage.CHAPTER_HEADING_FONT, "Calibri (Body)");
		stringsMap.put(BibleToDocLanguage.CHAPTER_HEADING_FONT_SIZE, 18);
		stringsMap.put(BibleToDocLanguage.STR_VERSE_INDEX_FONT, "Calibri (Body)");
		stringsMap.put(BibleToDocLanguage.STR_VERSE_INDEX_FONT_SIZE, 16);

		stringsMap.put(BibleToDocLanguage.VERSE_FONT, "Calibri (Body)");
		stringsMap.put(BibleToDocLanguage.VERSE_FONT_SIZE, 14);
		
		//Others
		stringsMap.put(BibleToDocLanguage.PARAGRAPH_SPACING_AFTER, 0);

	}

	@Override
	public String getString(String key) {
		return (String)stringsMap.get(key);
	}

	@Override
	public int getInt(String key) {
		return (Integer) stringsMap.get(key);
	}

}