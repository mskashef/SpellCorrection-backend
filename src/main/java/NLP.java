
import info.debatty.java.stringsimilarity.JaroWinkler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NLP {
    public static List<String> getBiGrams(String token) {
        List<String> res = new ArrayList<>();
        token = "$" + token + "$";
        for (int i = 0; i < token.length() - 1; i++)
            res.add(token.charAt(i) + "" + token.charAt(i + 1));
        return res;
    }

    public static int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }
        for (int i = 0; i < len1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = word2.charAt(j);
                if (c1 == c2) {
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;
                    int min = Math.min(replace, insert);
                    min = Math.min(delete, min);
                    dp[i + 1][j + 1] = min;
                }
            }
        }
        return dp[len1][len2];
    }

    static double getSimilariy(String t1, String t2) {
        return new JaroWinkler().similarity(t1, t2);
    }

    static List<String> stopWords = Arrays.asList("i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "you're", "you've", "you'll", "you'd", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "she's", "her", "hers", "herself", "it", "it's", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "that'll", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "but", "if", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "don't", "should", "should've", "now", "d", "ll", "m", "o", "re", "ve", "y", "ain", "aren", "aren't", "couldn", "couldn't", "didn", "didn't", "doesn", "doesn't", "hadn", "hadn't", "hasn", "hasn't", "haven", "haven't", "isn", "isn't", "ma", "mightn", "mightn't", "mustn", "mustn't", "needn", "needn't", "shan", "shan't", "shouldn", "shouldn't", "wasn", "wasn't", "weren", "weren't", "won", "won't", "wouldn", "wouldn't");

    static String normalizeChar(char ch) {
        switch (ch) {
            // removables -----------------------------------------
            case 'ً':
            case 'ٌ':
            case 'ٍ':
            case 'َ':
            case 'ُ':
            case 'ِ':
            case 'ْ':
            case 'ّ':
            case 'ـ':
                return "";

            // separators -----------------------------------------
            case '!':
            case '~':
            case '?':
            case '-':
            case '_':
            case '.':
            case ';':
            case ',':
            case '[':
            case ']':
            case '(':
            case ')':
            case '{':
            case '}':
            case '»':
            case '«':
            case '\'':
            case '\"':
            case '/':
            case '\\':
            case '،':
            case '|':
            case '+':
            case '؛':
            case '*':
            case '@':
            case '^':
            case '=':
            case '<':
            case '>':
            case '%':
            case '#':
            case '&':
                return " ";

            // persian digits -----------------------------------------
            case '٠':
                return ("0");
            case '۱':
                return ("1");
            case '۲':
                return ("2");
            case '۳':
                return ("3");
            case '۴':
                return ("4");
            case '۵':
                return ("5");
            case '۶':
                return ("6");
            case '۷':
                return ("7");
            case '۸':
                return ("8");
            case '۹':
                return ("9");

            // equivalent chars -----------------------------------------
            case 'ك':
                return ("ک");
            case 'ي':
                return ("ی");
            case 'ئ':
            case 'أ':
            case 'إ':
                return ("ئ");
            case 'ة':
            case 'ۀ':
                return ("ه");
            case 'ؤ':
                return ("و");
            case '\u200C':
                return (" ");
            default:
                return ch + "";
        }
    }

    static String normalize(String text) {
        String res = text.toLowerCase().trim();
        StringBuilder finalResult = new StringBuilder();
        for (int i = 0; i < res.length(); i++) {
            char ch = res.charAt(i);
            finalResult.append(normalizeChar(ch));
        }
        return finalResult.toString();
    }

    public static String[] tokenize(String text) {
        String[] tokens = normalize(text).split("\\s+");
        List<String> res = new ArrayList<>();
        for (String token : tokens) {
            if (!stopWords.contains(token))
                res.add(token);
        }

        String[] result = new String[res.size()];
        for (int i = 0; i < res.size(); i++) {
            result[i] = res.get(i);
        }
        return result;
    }
}
