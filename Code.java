import java.io.*; import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner; public class Code { public static void main(String[] args) {long startTime = System.currentTimeMillis(); BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in)); for (int i = 0; i < 10; i++) { String input = scanner.readLine(); System.out.println(input); } scanner.close(); long endTime = System.currentTimeMillis(); Runtime.getRuntime().gc(); long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); String form = String.format("/Runtime: %f/Used Memory: %d", (endTime - startTime) / 1000.0, usedMem); try (FileWriter writer = new FileWriter("output.txt")) { writer.write(form); } catch (IOException e) { e.printStackTrace(); }} }