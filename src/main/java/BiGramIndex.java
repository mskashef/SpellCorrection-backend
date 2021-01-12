import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class BiGramIndex {
    HashMap<String, List<String>> table = new HashMap<>();

    public void learn(String fileUrl) throws FileNotFoundException {
        File docs = new File(fileUrl);
        System.out.println("Indexing...");
        Scanner fileScanner = new Scanner(docs);
        int i = 0;
        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine().trim().toLowerCase();
            if (line.isEmpty() || !line.equals("<doc>")) continue;
            String name = "", body = "";
            while (true) {
                line = fileScanner.nextLine().trim().toLowerCase();
                if (line.equals("</doc>")) break;
                if (line.contains("<title>") && line.contains("</title>")) {
                    line = line.replace("<title>", "");
                    line = line.replace("</title>", "");
                    name = line;
                }
                if (line.contains("<abstract>") && line.contains("</abstract>")) {
                    line = line.replace("<abstract>", "");
                    line = line.replace("</abstract>", "");
                    body = line;
                }
            }
            Document doc = new Document(name, body);
            this.add(doc);
            if (i % 10000 == 0)
                System.out.printf("%.0f %%\n", i * 100 / (float) 752223);
            i++;
        }
        System.out.printf("\n%d Documents were indexed\n---------------------------\n", i);
    }

    public Pair<Map<String, List<String>>, List<String>> correctQuery(String query) {
        String[] queryTokens = query.trim().split("\\s+");
        Map<String, List<String>> correctedWordsMap = new HashMap<>();
        Map<String, Integer> correctedWordsIndexMap = new HashMap<>();
        for (String token : queryTokens) {
            List<String> correctedToken = correct(token);
            System.out.println("correctedToken: " + correctedToken);
            if (correctedToken.isEmpty() || correctedToken.size() == 1 && correctedToken.get(0).equals("+")) continue;
            correctedWordsMap.put(token, correctedToken);
            System.out.println("correctedWordsMap: " + correctedWordsMap);
            correctedWordsIndexMap.put(token, 0);
        }

        List<String> res = new ArrayList<>();
        int maxLength = 0;
        for (List<String> list : correctedWordsMap.values())
            maxLength = Math.max(maxLength, list.size());

        if (maxLength == 0) return new Pair<>(new HashMap<>(), new ArrayList<>(Arrays.asList(query.trim())));
        for (int i = 0; i < maxLength; i++) {
            StringBuilder sentence = new StringBuilder();
            for (String token : queryTokens) {
                if (correctedWordsMap.get(token) == null) {
                    sentence.append(" " + token);
                    continue;
                }
                if (correctedWordsMap.get(token).size() == 0) {
                    sentence.append(" " + token);
                } else {
                    sentence.append(" " + correctedWordsMap.get(token).get(correctedWordsIndexMap.get(token)));
                    correctedWordsIndexMap.put(token, (correctedWordsIndexMap.get(token) + 1) % correctedWordsMap.get(token).size());
                }
            }
            res.add(sentence.toString().trim());
        }
        return new Pair<>(correctedWordsMap, res);
    }

    private void add(Document document) {
        String[] tokens = NLP.tokenize(document.getBody().toLowerCase());
        HashSet<String> distinctTokens = new HashSet<>();
        distinctTokens.addAll(Arrays.asList(tokens));
        for (String token : distinctTokens) {
            List<String> biGrams = NLP.getBiGrams(token);
            for (String biGram : biGrams) {
                table.putIfAbsent(biGram, new ArrayList<>());
                table.get(biGram).add(token);
            }
        }
    }

    private List<String> correct(String token) {
        List<String> bigrams = NLP.getBiGrams(token);
        HashMap<String, Integer> res = new LinkedHashMap<>();
        for (String bigram : bigrams) {
            List<String> l = table.get(bigram);
            if (l == null) continue;
            for (String s : l) {
                res.putIfAbsent(s, 0);
                res.put(s, res.get(s) + 1);
            }
        }
        List<Token> result = new ArrayList<>();
        res.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> result.add(new Token(x.getKey(), x.getValue())));

        List<String> finalResult = new ArrayList<>();
        int i = 0;
        for (Token t : result) {
            int distance = t.getDistance(token);
            double similariry = t.getDoubleCheckedSimilarity(token);
            if (distance < 3 && similariry >= 0.7) {
                if (distance == 0)
                    return Arrays.asList("+");
                finalResult.add(t.getToken());
                i++;
            }
            if (i >= 10) break;
        }
        System.out.println(finalResult);
        return finalResult;
    }
}
