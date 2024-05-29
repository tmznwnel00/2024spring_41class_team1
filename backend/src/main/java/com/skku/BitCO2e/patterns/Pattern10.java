package com.skku.BitCO2e.patterns;

import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Pattern10 {
    public String main(String inputText) {
        String[] codes = inputText.split("\n");
        ArrayList<String> lines = new ArrayList<>(List.of(codes));
        boolean isDetected = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            // Check for FileInputStream use which might indicate file reading block
            if (line.contains("new FileInputStream")) {
                // Find the start of the try block to ensure replacing within the right scope
                int startOfTry = findStartOfTryBlock(lines, i);
                int endOfBufferedReaderUsage = findEndOfBufferedReaderUsage(lines, startOfTry);
                if (endOfBufferedReaderUsage != -1) {
                    List<String> replacementLines = generateNewFileReadLines();
                    replaceCodeBlock(lines, startOfTry, endOfBufferedReaderUsage, replacementLines);
                    isDetected = true;
                }
            }
        }

        if (isDetected) {
            replaceClassName(lines, "Buggy", "Fixed");
        }

        return String.join("\n", lines);
    }

    private List<String> generateNewFileReadLines() {
        List<String> newLines = new ArrayList<>();
        newLines.add("    List<String> lines = Files.readAllLines(Paths.get(\"파일경로\"), StandardCharsets.UTF_8);");
        newLines.add("    for (String line : lines) {");
        newLines.add("        System.out.println(line); // 파일에서 한 줄씩 읽어 화면에 출력");
        newLines.add("    }");
        return newLines;
    }

    private void replaceCodeBlock(ArrayList<String> lines, int start, int end, List<String> newLines) {
        for (int i = start; i <= end; i++) {
            lines.remove(start);
        }
        lines.addAll(start, newLines);
    }

    private void replaceClassName(ArrayList<String> lines, String oldName, String newName) {
        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, lines.get(i).replace(oldName, newName));
        }
    }

    private int findStartOfTryBlock(ArrayList<String> lines, int startIndex) {
        for (int i = startIndex; i >= 0; i--) {
            if (lines.get(i).contains("try {")) {
                return i;
            }
        }
        return startIndex; // Default to startIndex if no try block found
    }

    private int findEndOfBufferedReaderUsage(ArrayList<String> lines, int startIndex) {
        int depth = 0;
        boolean insideTry = false;
        for (int i = startIndex; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("try {")) {
                insideTry = true;
                depth++; // Increase depth when a new block is entered
            }
            if (line.contains("{")) {
                depth++;
            }
            if (line.contains("}")) {
                depth--;
                if (depth == 0 && insideTry) {
                    return i; // Return the end of the try block
                }
            }
        }
        return -1; // If not found
    }
}
