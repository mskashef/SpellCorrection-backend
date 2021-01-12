import io.javalin.Javalin;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws FileNotFoundException {
        BiGramIndex index = new BiGramIndex();
        index.learn("D:/unitest/informationRetrieval/simple.xml");
        Javalin app = Javalin.start(7000);

        app.get("/api/correct", ctx -> {
            String query = ctx.queryParam("query");
            if (query == null || query.trim().equals("")) {
                HashMap<String, String> res = new HashMap<>();
                res.put("error", "Query can not be empty!");
                ctx.json(res);
                return;
            }
            Pair<Map<String, List<String>>, List<String>> res = index.correctQuery(query);
            HashMap<String, Object> response = new HashMap<>();
            response.put("corrected_queries", res.getValue());
            response.put("corrected_tokens", res.getKey());
            System.out.println(response);
            ctx.json(response);
        });
    }
}
