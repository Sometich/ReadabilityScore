package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        File file = new File(args[0]);
        try (Scanner reader = new Scanner(file)) {
            StringBuilder builder = new StringBuilder();
            while (reader.hasNextLine()) {
                builder.append(reader.nextLine());
            }
            String text = builder.toString();

            int characters = text.replaceAll("\\s+", "").length();
            int sentences = text.split("[.!?]").length;
            String[] wordsArr = text.replaceAll("[.!?]", "").split("\\s+");
            int words = wordsArr.length;
            Pattern syllablePattern = Pattern.compile("[aeiouy][^aeiouy\\s]|[aiouy]$", Pattern.CASE_INSENSITIVE);
            int syllables = 0;
            int polySyllables = 0;
            for (String word : wordsArr) {
                int wordSyllables = 0;
                Matcher syllableMatcher = syllablePattern.matcher(word);
                while (syllableMatcher.find()) {
                    wordSyllables++;
                }
                if (wordSyllables == 0) {
                    wordSyllables = 1;
                }
                if (wordSyllables > 2) {
                    polySyllables++;
                }
                syllables += wordSyllables;
            }

            System.out.println("The text is:");
            System.out.println(text);
            System.out.println();
            System.out.println("Words: " + words);
            System.out.println("Sentences: " + sentences);
            System.out.println("Characters: " + characters);
            System.out.println("Syllables: " + syllables);
            System.out.println("Polysyllables: " + polySyllables);

            int[] ages = {6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 24, 24};
            System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            Scanner input = new Scanner(System.in);
            String cmd = input.next();
            boolean calcAri = false, calcFkrt = false, calcSmog = false, calcCli = false;
            int ariAge = 0, fkrtAge = 0, smogAge = 0, cliAge = 0;
            switch (cmd) {
                case "ARI":
                    calcAri = true;
                    break;
                case "FK":
                    calcFkrt = true;
                    break;
                case "SMOG":
                    calcSmog = true;
                    break;
                case "CL":
                    calcCli = true;
                    break;
                default:
                    calcAri = true;
                    calcFkrt = true;
                    calcSmog =true;
                    calcCli = true;
                    break;
            }
            System.out.println();
            if (calcAri) {
                double ari = 4.71 * (characters / (double) words) + 0.5 * (words / (double) sentences) - 21.43;
                ariAge = ages[(int) Math.round(ari) - 1];
                System.out.printf(Locale.US, "Automated Readability Index: %.2f (about %d year olds).\n", ari, ariAge);
            }

            if (calcFkrt) {
                double fkrt = 0.39 * (words / (double) sentences) + 11.8 * (syllables / (double) words) - 15.59;
                fkrtAge = ages[(int) Math.round(fkrt) - 1];
                System.out.printf(Locale.US, "Flesch–Kincaid readability tests: %.2f (about %d year olds).\n", fkrt, fkrtAge);
            }

            if (calcSmog) {
                double smog = 1.043 * Math.sqrt(polySyllables * (30 / (double) sentences)) + 3.1291;
                smogAge = ages[(int) Math.round(smog) - 1];
                System.out.printf(Locale.US, "Simple Measure of Gobbledygook: %.2f (about %d year olds).\n", smog, smogAge);
            }

            if (calcCli) {
                double cli = 0.0588 * (characters / (double) words) * 100 - 0.296 * (sentences / (double) words) * 100 - 15.8;
                cliAge = ages[(int) Math.round(cli) - 1];
                System.out.printf(Locale.US, "Coleman–Liau index: %.2f (about %d year olds).\n", cli, cliAge);
            }

            if (calcAri && calcFkrt && calcSmog && calcCli) {
                double average = (ariAge + fkrtAge + smogAge + cliAge) / 4.0;
                System.out.printf(Locale.US, "This text should be understood in average by %.2f year olds.", average);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}