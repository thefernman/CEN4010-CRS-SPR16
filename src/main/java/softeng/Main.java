package softeng;

import static spark.Spark.get;

public class Main {
    public static void main(String[] args) {
        get("/hello", (request, response) -> "Hello World");
    }
}
