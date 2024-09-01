package in.wordofgod.parallel.bible.creator;

import java.util.HashMap;
import java.util.Map;

public class BibleToDocLanguageTamil implements BibleToDocLanguage {

	private Map<String, String[]> charMap = null;
	
	private static Map<String, Object> stringsMap = new HashMap<String, Object>();
	
	static {

		//Title Page
		//stringsMap.put(BibleToDocLanguage.SUB_TITLE_1, "பரிசுத்த வேதாகமம் O.V.");
		//stringsMap.put(BibleToDocLanguage.STR_BOOK_NO, "WOG BOOKS 50");
		stringsMap.put(BibleToDocLanguage.STR_BOOK_EDITION, "First Edition 2022");
		//stringsMap.put(BibleToDocLanguage.SUB_TITLE_2, "| 66 புத்தகங்கள் |\n| 1,189 அதிகாரங்கள் |\n| 31,104 வசனங்கள் |\n| 4,18,077 வார்த்தைகள் |\n| 95,731 தனிச் சொற்கள் |");
		//stringsMap.put(BibleToDocLanguage.SUB_TITLE_4, "ஏசுதாஸ் சாலொமோன், www.WordOfGod.in");
		
		stringsMap.put(BibleToDocLanguage.SUB_TITLE_1_FONT, "Uni Ila.Sundaram-08");
		stringsMap.put(BibleToDocLanguage.SUB_TITLE_1_FONT_SIZE, 45);

		stringsMap.put(BibleToDocLanguage.SUB_TITLE_2_FONT, "Uni Ila.Sundaram-08");
		stringsMap.put(BibleToDocLanguage.SUB_TITLE_2_FONT_SIZE, 24);

		stringsMap.put(BibleToDocLanguage.SUB_TITLE_3, "By:");
		stringsMap.put(BibleToDocLanguage.SUB_TITLE_3_FONT, "Cambria");
		stringsMap.put(BibleToDocLanguage.SUB_TITLE_3_FONT_SIZE, 16);

		stringsMap.put(BibleToDocLanguage.SUB_TITLE_4_FONT, "Uni Ila.Sundaram-08");
		stringsMap.put(BibleToDocLanguage.SUB_TITLE_4_FONT_SIZE, 24);

		stringsMap.put(BibleToDocLanguage.MORE_INFO, "மத்தேயு 10:8-ன் அடிப்படையில், இலவசமாக கொடுக்கப்படுகிறது - \"இலவசமாய்ப் பெற்றீர்கள், இலவசமாய்க் கொடுங்கள்\". ஆகையால், இலவசமாய் பகிருங்கள்.");
		stringsMap.put(BibleToDocLanguage.MORE_INFO_FONT, "Uni Ila.Sundaram-04");
		stringsMap.put(BibleToDocLanguage.MORE_INFO_FONT_SIZE, 16);

		//Book Details Page
		stringsMap.put(BibleToDocLanguage.BOOK_DETAILS_FONT, "Calibri (Body)");
		stringsMap.put(BibleToDocLanguage.BOOK_DETAILS_FONT_SIZE, 14);
		
		//stringsMap.put(BibleToDocLanguage.STR_CREATED_BY_LABEL, "Created By:");
		//stringsMap.put(BibleToDocLanguage.STR_CREATED_BY_DETAILS, "Yesudas Solomon, www.WordOfGod.in");

		//stringsMap.put(BibleToDocLanguage.STR_COPYRIGHT_DETAILS, "This book is not copyright protected. You are free to download, print and make copies without any permission from us.");
		
		//PDF Issue Guidance Page
		stringsMap.put(BibleToDocLanguage.STR_PDF_INDEX_ISSUE, "இந்த PDF-ஐ மொபைலில் பயன்படுத்தினால், அட்டவணையில் உள்ள லிங்க்கள்(Index) கூகிள் ட்ரைவ்(Google Drive PDF Viewer) என்னும் ஆப்பில் (செயலியில்) வேலை செய்யாது, ReadEra என்னும் ஆப்பை (செயலி) பயன்படுத்துங்கள்.");
		stringsMap.put(BibleToDocLanguage.STR_PDF_INDEX_ISSUE_ENGLISH, "If you are using this PDF in mobile, Navigation by Index may not work with Google Drive's PDF viewer. I would recommend ReadEra App for better performance and navigation experience.");
		stringsMap.put(BibleToDocLanguage.STR_PDF_INDEX_ISSUE_FONT, "Uni Ila.Sundaram-04");
		stringsMap.put(BibleToDocLanguage.STR_PDF_INDEX_ISSUE_FONT_SIZE, 18);

		//Index Page
		stringsMap.put(BibleToDocLanguage.STR_INDEX_TITLE, "அட்டவணை");
		stringsMap.put(BibleToDocLanguage.STR_INDEX_TITLE_FONT, "Uni Ila.Sundaram-02");
		stringsMap.put(BibleToDocLanguage.STR_INDEX_TITLE_FONT_SIZE, 22);

		stringsMap.put(BibleToDocLanguage.STR_INDEX_FONT, "Uni Ila.Sundaram-06");
		stringsMap.put(BibleToDocLanguage.STR_INDEX_FONT_SIZE, 20);
		stringsMap.put(BibleToDocLanguage.STR_CHAPTER_INDEX_FONT, "Uni Ila.Sundaram-06");
		stringsMap.put(BibleToDocLanguage.STR_CHAPTER_INDEX_FONT_SIZE, 24);
		stringsMap.put(BibleToDocLanguage.STR_SUB_INDEX_FONT, "Uni Ila.Sundaram-06");
		stringsMap.put(BibleToDocLanguage.STR_SUB_INDEX_FONT_SIZE, 14);
		
		//Content Pages
		stringsMap.put(BibleToDocLanguage.BOOK_HEADING_FONT, "Uni Ila.Sundaram-02");
		stringsMap.put(BibleToDocLanguage.BOOK_HEADING_FONT_SIZE, 32);
		
		stringsMap.put(BibleToDocLanguage.CHAPTER_HEADING_FONT, "Uni Ila.Sundaram-08");
		stringsMap.put(BibleToDocLanguage.CHAPTER_HEADING_FONT_SIZE, 18);

		stringsMap.put(BibleToDocLanguage.VERSE_FONT, "Uni Ila.Sundaram-04");
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