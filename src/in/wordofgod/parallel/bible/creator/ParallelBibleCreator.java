/**
 * 
 */
package in.wordofgod.parallel.bible.creator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * 
 */
public class ParallelBibleCreator {

	public static Properties INFORMATION = null;
	public static String outputFormat;
	public static String useLongBookName;
	public static String[] bibleVersions;
	public static String bibleSourceDirectory;
	public static String outputDirectory;
	public static String biblePortions;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws ParserConfigurationException, TransformerException {

		if (!validateInput(args)) {
			return;
		}

		switch (outputFormat.toUpperCase()) {
		case Constants.FORMAT_SINGLE_TEXTFILE:
			TextFiles.createSingleTextFile();
			break;

		case Constants.FORMAT_TEXTFILES:
			TextFiles.createSingleTextFile();
			break;

		case Constants.FORMAT_TEXTFILES_BY_DIRECTORY:
			TextFiles.createSingleTextFile();
			break;

		case Constants.FORMAT_JSON:

			break;

		default:
			System.out.println("Given format is not supported, pls check the supported format below.");
			printHelpMessage();
			break;
		}
	}

	private static boolean validateInput(String[] args) {
		if (args.length < 1) {
			System.out.println("Please give file path for INFORMATION.ini file");
			printHelpMessage();
			return false;
		} else {
			loadInformation(args[0]);
			if (INFORMATION.get("outputFormat") == null) {
				System.out.println("outputFormat is mandatory, pls mention it in the INFORMATION.ini file");
			}
			if (INFORMATION.get("bibleVersions") == null) {
				System.out.println("bibleVersions is mandatory, pls mention it in the INFORMATION.ini file");
			}
			if (INFORMATION.get("bibleSourceDirectory") == null) {
				System.out.println("bibleSourceDirectory is mandatory, pls mention it in the INFORMATION.ini file");
			}
			if (INFORMATION.get("outputDirectory") == null) {
				System.out.println("outputDirectory is mandatory, pls mention it in the INFORMATION.ini file");
			}
			outputFormat = INFORMATION.getProperty("outputFormat");
			useLongBookName = INFORMATION.getProperty("useLongBookName");
			bibleVersions = INFORMATION.getProperty("bibleVersions").split(",");
			bibleSourceDirectory = INFORMATION.getProperty("bibleSourceDirectory");
			outputDirectory = INFORMATION.getProperty("outputDirectory");
			biblePortions = INFORMATION.getProperty("biblePortions");
		}
		return true;
	}

	public static void printHelpMessage() {
		System.out.println("\nHelp on Usage of this program:");
		System.out.println("\nSupported formats:\n\t1. TextFiles\n\t2. TextFilesByDirectory\n\t3. SingleTextFile");
		System.out.println(
				"\nSyntax to run this program:\njava -jar parallel-bible-creator.jar [path to INFORMATION.ini file]\n\n");
		System.out.println(
				"Sample INFORMATION.ini file is present in the downloaded program or at https://github.com/yesudas/parallel-bible-creator");
		System.out.println("Or INFORMATION.ini file can be prepared with the below tips:");
		System.out.println(
				"Please use \\\\ or / in INFORMATION.ini file for bibleSourceDirectory as well as outputDirectory instead of just single \\");
		System.out.println(
				"# outputFormat => supported outputFormats are TextFiles, TextFilesByDirectory, SingleTextFile");
		System.out.println("# useLongBookName => supported values are yes, no");
		System.out.println(
				"# bibleVersions => Use comma separated listed of all bible versions, mention extension as well. Ex. TBSI.ont,KJV+.ont,BBE.nt");
		System.out.println(
				"# You can download bibles from https://github.com/yesudas/all-bible-databases/tree/main/Bibles/TheWord-Bible-Databases");
		System.out.println(
				"# Download both *-information.ini files as well as bible text files in the format *.ont or *.nt");
		System.out.println(
				"# bibleSourcePath => give the full directory path where the bible text & corresponding *-information.ini files are stored");
		System.out.println("# outputPath => give the full directory path where the results should be stored");
		System.out.println(
				"# biblePortions => Keep empty for full bible, otherwise specify as show in the examples. Example 1: biblePortions=Gen 1,2,3,5 (denotes chapters 1,2,3 & 5 from genesis). Example 2: biblePortions=Gen 12:2,3,4 (denotes genesis chapter 12 and verses 2,3 &4). Example 3: biblePortions=Gen 10:1-20 (denotes genesis chapter 10 and verses from 1 to 20)");
		System.out.println(
				"# You can give more than one bible portion separated by comma. Example: biblePortions=Gen 1:1-5; Mat 5:1-10 (denotes genesis chapter 1 and verses from 1 to 5 as well as matthew chapter 5 and verses from 1 to 10)");
		System.out.println(
				"# Name of the books should be this from this list only: Gen, Exo, Lev, Num, Deu, Jos, Jdg, Rth, 1Sa, 2Sa, 1Ki, 2Ki, 1Ch, 2Ch, Ezr, Neh, Est, Job, Psa, Pro, Ecc, Son, Isa, Jer, Lam, Eze, Dan, Hos, Joe, Amo, Oba, Jon, Mic, Nah, Hab, Zep, Hag, Zec, Mal, Mat, Mar, Luk, Joh, Act, Rom, 1Co, 2Co, Gal, Eph, Php, Col, 1Th, 2Th, 1Ti, 2Ti, Tit, Phm, Heb, Jas, 1Pe, 2Pe, 1Jn, 2Jn, 3Jn, Jud, Rev");
	}

	private static void loadInformation(String informationFilePath) {
		INFORMATION = new Properties();
		BufferedReader propertyReader;
		try {
			File infoFile = new File(informationFilePath);
			propertyReader = new BufferedReader(new InputStreamReader(new FileInputStream(infoFile), "UTF8"));
			INFORMATION.load(propertyReader);
			propertyReader.close();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
