# parallel-bible-creator
Creates Parallel Bibles of given versions in given formats

# Usage
If you clone from Git or download a source zip, you will need a Java JDK 8 or above (tested up to 11) to build. You can use Eclipse or IntelliJ or Visual Code IDEs to build the binary and you will find a suitable distribution .jar file in the project folder.

If you download a parallel-bible-creator.jar file, you will need a Java Runtime Environment 8 or above, available from [https://www.oracle.com/in/java/technologies/downloads/](https://www.oracle.com/in/java/technologies/downloads/).

Just run

~~~
java -jar parallel-bible-creator.jar
~~~

on the command line for usage information.

# Steps to use parallel-bible-creator.jar
1. Download & Install JRE or Java from https://www.oracle.com/in/java/technologies/downloads/
2. Open your command prompt or Terminal and use the below syntax to run the program.
3. Syntax to run this program:

~~~
java -jar parallel-bible-creator.jar [path to INFORMATION.ini file]
~~~

# INFORMATION.ini is mandatory

Sample INFORMATION.ini file is present in the downloaded program or at https://github.com/yesudas/parallel-bible-creator
Or INFORMATION.ini file can be prepared with the below tips:
- outputFormat => supported outputFormats are TextFiles, TextFilesByDirectory, SingleTextFile
- useLongBookName => supported values are yes, no
- bibleVersions => Use comma separated listed of all bible versions, mention extension as well. Example. TBSI.ont,KJV+.ont,BBE.nt
    - You can download bibles from https://github.com/yesudas/all-bible-databases/tree/main/Bibles/TheWord-Bible-Databases
    - Download both *-information.ini files as well as bible text files in the format *.ont or *.nt
- bibleVersionForBookNames => Optional value, Mention which bible version should be considered for displaying the book names
- bibleSourcePath => give the full directory path where the bible text & corresponding *-information.ini files are stored
    - Use \\ or / in instead of just single \ in the file path
- outputPath => give the full directory path where the results should be stored
    - Use \\ or / in instead of just single \ in the file path
- biblePortions => Keep empty for full bible, otherwise specify as show in the examples.
    - Example 1: biblePortions=Gen 1,2,3,5 (denotes chapters 1,2,3 & 5 from genesis). 
    - Example 2: biblePortions=Gen 12:2,3,4 (denotes genesis chapter 12 and verses 2,3 &4). 
    - Example 3: biblePortions=Gen 10:1-20 (denotes genesis chapter 10 and verses from 1 to 20)
- You can give more than one bible portion separated by comma. 
    - Example: biblePortions=Gen 1:1-5; Mat 5:1-10 (denotes genesis chapter 1 and verses from 1 to 5 as well as matthew chapter 5 and verses from 1 to 10)
- Name of the books should be this from this list only: Gen, Exo, Lev, Num, Deu, Jos, Jdg, Rth, 1Sa, 2Sa, 1Ki, 2Ki, 1Ch, 2Ch, Ezr, Neh, Est, Job, Psa, Pro, Ecc, Son, Isa, Jer, Lam, Eze, Dan, Hos, Joe, Amo, Oba, Jon, Mic, Nah, Hab, Zep, Hag, Zec, Mal, Mat, Mar, Luk, Joh, Act, Rom, 1Co, 2Co, Gal, Eph, Php, Col, 1Th, 2Th, 1Ti, 2Ti, Tit, Phm, Heb, Jas, 1Pe, 2Pe, 1Jn, 2Jn, 3Jn, Jud, Rev


# Supported formats:
1. TextFiles
2. TextFilesByDirectory
3. SingleTextFile

# Where to get Bible Databases?
Download the Bible Databases along with *-information.ini files from [https://github.com/yesudas/all-bible-databases/tree/main/Bibles/TheWord-Bible-Databases/Tamil](https://github.com/yesudas/all-bible-databases/tree/main/Bibles/TheWord-Bible-Databases/Tamil)
