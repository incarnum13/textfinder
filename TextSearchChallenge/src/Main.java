import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TextSearchChallenge {
    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);

        while (true) {
            System.out.println("What Would you like to search today? Type 'exit' to quit.");
            String searchTermsInput = inputScanner.nextLine();
            if (searchTermsInput.equalsIgnoreCase("exit")) {
                break; // Exiting the program
            }

            System.out.println("Type your search terms");
            String searchTerms = inputScanner.nextLine();

            System.out.println("Print each line containing these terms? (yes/no)");
            String printLinesInput = inputScanner.nextLine();
            boolean printLines = printLinesInput.trim().equalsIgnoreCase("yes");

            System.out.println("Print \"X\" words before the search terms?");
            int wordsBefore = Integer.parseInt(inputScanner.nextLine().trim());

            System.out.println("Print \"X\" words after the search terms?");
            int wordsAfter = Integer.parseInt(inputScanner.nextLine().trim());

            System.out.println("Searched words need to be in order? (yes/no)");
            String inOrderInput = inputScanner.nextLine();
            boolean inOrder = inOrderInput.trim().equalsIgnoreCase("yes");

            System.out.println("maximum word count between each searched term?:");
            int maxWordGap = Integer.parseInt(inputScanner.nextLine().trim());

            long startTime = System.currentTimeMillis();

            try {
                File myFile = new File("C:/Users/Zyida/Desktop/CIAFACTBOOK.txt");
                myFileReader myFileReader = new myFileReader(myFile);
                int matchCount = 0;
                String currentLine;

                while ((currentLine = myFileReader.readLine()) != null) {
                    List<String> wordsInLine = Arrays.asList(currentLine.toLowerCase().split("\\s+"));
                    List<String> searchTermsList = Arrays.asList(searchTerms.toLowerCase().split("\\s+"));

                    if (inOrder) {
                        // Search for exact sequence of words in order
                        int startIndex = indexOfSubList(wordsInLine, searchTermsList);
                        if (startIndex != -1) {
                            printWithContext(wordsInLine, startIndex, searchTermsList.size(), wordsBefore, wordsAfter, printLines);
                            matchCount++;
                        }
                    } else {
                        // Search for words in any order with a maximum gap
                        boolean allWordsFound = true;
                        int firstIndex = -1, lastIndex = -1;

                        for (String word : searchTermsList) {
                            int currentIndex = wordsInLine.indexOf(word);
                            if (currentIndex == -1) {
                                allWordsFound = false;
                                break;
                            }
                            if (firstIndex == -1 || currentIndex < firstIndex) {
                                firstIndex = currentIndex;
                            }
                            if (currentIndex > lastIndex) {
                                lastIndex = currentIndex;
                            }
                            if (lastIndex - firstIndex > maxWordGap * (searchTermsList.size() - 1)) {
                                allWordsFound = false;
                                break;
                            }
                        }
                        if (allWordsFound) {
                            printWithContext(wordsInLine, firstIndex, lastIndex - firstIndex + 1, wordsBefore, wordsAfter, printLines);
                            matchCount++;
                        }
                    }
                }

                myFileReader.close();
                long endTime = System.currentTimeMillis();
                System.out.println("Match Count: " + matchCount);
                System.out.println("Time Taken: " + (endTime - startTime) + " ms");

            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + e.getMessage());
                // Handle the exception appropriately
            }
        }

        inputScanner.close();
    }

    public static void printWithContext(List<String> words, int startIndex, int length, int wordsBefore, int wordsAfter, boolean printLines) {
        if (printLines) {
            // Print the entire line
            for (String word : words) {
                System.out.print(word + " ");
            }
            System.out.println();
        } else {
            // Print specified number of words before, the search terms, and words after
            int start = Math.max(0, startIndex - wordsBefore);
            int end = Math.min(words.size(), startIndex + length + wordsAfter);

            for (int i = start; i < end; i++) {
                System.out.print(words.get(i) + " ");
            }
            System.out.println();
        }
    }

    private static int indexOfSubList(List<String> source, List<String> target) {
        for (int i = 0; i < source.size() - target.size() + 1; i++) {
            boolean found = true;
            for (int j = 0; j < target.size(); j++) {
                if (!source.get(i + j).equals(target.get(j))) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public static class myFileReader {
        private File file;
        private Scanner scanner;

        public myFileReader(File file) throws FileNotFoundException {
            this.file = file;
            this.scanner = new Scanner(file);
        }

        public String readLine() {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            } else {
                return null;
            }
        }

        public void close() {
            scanner.close();
        }
    }
}