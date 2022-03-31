package pl.simo333.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void wordInWordsCounter() {
        int counter = 0;
        List<String> allWords = readWords().stream()
                .filter(word -> word.length() > 2)
                .map(word -> word.contains("/") ? word.split("/")[0] : word) //part of words list contains sign '/' so here I cut it
                .collect(Collectors.toList());
        System.out.println("Ilość słow załadowanych do bazy: " + allWords.size());
        List<String> resultList = new ArrayList<>();
        String keyWord = getStringInput("Z jakiego wyrazu wyszukać słowa?");
        for (String word : allWords) {
            if (analyzer(keyWord, word)) {
                counter++;
                resultList.add(word);
            }
        }
        System.out.println("Znalazlem: " + counter + ". Przeszukalem: " + allWords.size());
        System.out.println(resultList);
        fileSaver(resultList);
    }

    public static List<String> readWords() {
        Path path = Paths.get(getStringInput("Podaj ścieżkę do pliku z listą słów:"));
        List<String> words = new ArrayList<>();
        try {
            words = Files.readAllLines(path);
        } catch (IOException e) {
            System.out.println("Nie mogę wczytać pliku. " + e.getMessage());
        }
        return words;
    }

    //TODO rewite for loop
    public static boolean analyzer(String keyWord, String wordToAnalyze) {
        char[] charsArr = keyWord.toCharArray();
        List<Character> keyChars = new ArrayList<>();
        for (char aChar : charsArr) {
            keyChars.add(aChar);
        }
        char[] chars = wordToAnalyze.toCharArray();
        List<Character> charsToAnalyze = new ArrayList<>();
        for (char aChar : chars) {
            charsToAnalyze.add(aChar);
        }

        for (int i = 0; i < charsToAnalyze.size(); i = i) {
            for (int j = 0; j < keyChars.size(); j++) {
                if (charsToAnalyze.get(0) == keyChars.get(j)) {
                    charsToAnalyze.remove(0);
                    keyChars.remove(j);
                    if (charsToAnalyze.isEmpty()) {
                        return true;
                    }
                    break;
                }
                if (j == keyChars.size() - 1) {
                    return false;
                }
            }
        }
        return false;
    }

    public static void fileSaver(List<String> listToSave) {
        Path path = Paths.get("znalezione-slowa.txt");

        try {
            Files.write(path, listToSave, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.out.println("Nie mozna utworzyc pliku. " + e.getMessage());
        }
    }

    public static String getStringInput(String caption) {
        Scanner scan = new Scanner(System.in);
        System.out.println(caption);
        return scan.nextLine();
    }

    public static void main(String[] args) {
        wordInWordsCounter();

    }
}
