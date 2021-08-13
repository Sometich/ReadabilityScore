package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File file = new File(args[0]);
        StringBuilder stringBuilder = new StringBuilder("");
        try (Scanner scannerFile = new Scanner(file)){
            while (scannerFile.hasNextLine()) {
                stringBuilder.append(scannerFile.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR");
        }
        System.out.println("The text is:");
        System.out.println(stringBuilder);

        String text = stringBuilder.toString();
        String[] sentences = text.split("[!.?]");
        int countPolysyllables = 0;
        int countOfCharacters = text.replaceAll("\\s", "").length();
        int countOfSentences = sentences.length;
        int countOfWords = text.split(" ").length;
        int countOfSyllables = 0;
        for (String sentence : sentences) {
            String[] words = sentence.split(" ");
            for (String word : words) {
                int localSyllable = 0;
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == 'a' || word.charAt(i) == 'i'
                            || word.charAt(i) == 'o' || word.charAt(i) == 'y' ||word.charAt(i) == 'e') {
                        if ((word.charAt(i) == 'e' && i == word.length() - 1)
                                || (i > 0 && (word.charAt(i - 1) == 'a' || word.charAt(i - 1) == 'i'
                                || word.charAt(i - 1) == 'o' || word.charAt(i - 1) == 'y' ||word.charAt(i - 1) == 'e'))) {
                            continue;
                        }
                        localSyllable++;
                        countOfSyllables++;
                    }
                }
                if (localSyllable >= 2) {
                    countPolysyllables++;
                }
                if (localSyllable == 0) {
                    countOfSyllables++;
                }
            }
        }
        System.out.println("Words: " + countOfWords);
        System.out.println("Sentences: "+ countOfSentences);
        System.out.println("Characters: "+ countOfCharacters);
        System.out.println("Syllables: " + countOfSyllables);
        System.out.println("Polysyllables: " + countPolysyllables);
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.next();
        scanner.close();

        switch (choice) {
            case "ARI":
                Main.ARIndex(countOfCharacters, countOfWords, countOfSentences);
                break;
            case "FK":
                Main.FKIndex(countOfWords, countOfSentences, countOfSentences);
                break;
            case "SMOG":
                Main.SMOGIndex(countPolysyllables, countOfSentences);
                break;
            case "CL":
                Main.CLIndex(countOfWords, countOfCharacters, countOfSentences);
                break;
            case "all":
                double averegaScore = 0.0;
                averegaScore += Main.ARIndex(countOfCharacters, countOfWords, countOfSentences);
                averegaScore += Main.FKIndex(countOfWords, countOfSentences, countOfSyllables);
                averegaScore += Main.SMOGIndex(countPolysyllables, countOfSentences);
                averegaScore += Main.CLIndex(countOfWords, countOfCharacters, countOfSentences);
                averegaScore = averegaScore / 4;
                System.out.println("\nThis text should be understood in average by " + averegaScore + "-year-olds.");
        }
    }

    public static double ARIndex(int characters, int words, int sentences) {
        double score = (4.71 * characters / words) + (0.5 * words / sentences) - 21.43;
        double age = Main.ageScore(score);
        System.out.println("Automated Readability Index: " + score + " (about " + age + "-year-olds).");
        return age;
    }

    public static double FKIndex(int words, int sentences, int syllables) {
        double score = 0.39 * (words * 1.0 / sentences) + 11.8 * (syllables * 1.0/ words) - 15.59;
        double age = Main.ageScore(score);
        System.out.println("Flesch–Kincaid readability tests: " + score + " (about " + age + "-year-olds).");
        return age;
    }

    public static double SMOGIndex(int polysyllables, int sentences) {
        double score = 1.043 * Math.sqrt(polysyllables * (30.0 / sentences)) + 3.1291;
        double age = Main.ageScore(score);
        System.out.println("Simple Measure of Gobbledygook: " + score + " (about " + age + "-year-olds).");
        return age;
    }

    public static double CLIndex(int countOfWords, int countOfCharacter, int sentences) {
        double l = (countOfCharacter * 1.0 / countOfWords) * 100;
        double s = (sentences * 1.0 / countOfWords) * 100;
        double score = 0.0588 * l - 0.296 * s - 15.8;
        double age = Main.ageScore(score);
        System.out.println("Coleman–Liau index: " + score + " (about " + age + "-year-olds).");
        return age;
    }

    public static Integer ageScore(double score) {
        int finalScore = (int) Math.ceil(score);
        switch (finalScore) {
            case 1:
                return 6;
            case 2:
                return 7;
            case 3:
                return 9;
            case 4:
                return 10;
            case 5:
                return 11;
            case 6:
                return 12;
            case 7:
                return 13;
            case 8:
                return 14;
            case 9:
                return 15;
            case 10:
                return 16;
            case 11:
                return 17;
            case 12:
                return 18;
            default:
                return 24;
        }
    }
}
