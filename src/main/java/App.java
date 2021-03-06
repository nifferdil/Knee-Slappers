import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;


public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String type = request.queryParams("type");
      Category newCategory = new Category(type);
      newCategory.save();
      response.redirect("/categories");
      return null;
    });

    get("/search/results", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String mainSearch = request.queryParams("main-search");
      model.put("jokes", Joke.search(mainSearch));
      model.put("categories", Category.search(mainSearch));
      model.put("template", "templates/search_results.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/jokes", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("jokes", Joke.all());
      model.put("categories", Category.all());
      model.put("template", "templates/jokes.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/jokes/add", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String question = request.queryParams("question");
      String answer = request.queryParams("answer");
      Joke newJoke = new Joke(question, answer);
      newJoke.save();
      response.redirect("/jokes");
      return null;
    });

    get("/categories", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("categories", Category.all());
      model.put("template", "/templates/categories.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/categories/add", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String type = request.queryParams("type");
      Category newCategory = new Category(type);
      newCategory.save();
      response.redirect("/categories");
      return null;
    });

    get("/categories/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Category category = Category.find(id);
      model.put("category", category);
      model.put("jokes", Joke.all());
      model.put("template", "templates/category.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    post("categories/:id/add-jokes", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int jokeId = Integer.parseInt(request.queryParams("joke_id"));
      int categoryId = Integer.parseInt(request.queryParams("category_id"));
      Category category = Category.find(categoryId);
      Joke joke = Joke.find(jokeId);
      category.addJoke(joke);
      response.redirect("/categories/" + categoryId);
      return null;
    });

    post("categories/:id/add-new-joke", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int categoryId = Integer.parseInt(request.queryParams("category_id"));
      Category category = Category.find(categoryId);
      String question = request.queryParams("question");
      String answer = request.queryParams("answer");
      Joke newJoke = new Joke(question, answer);
      newJoke.save();
      response.redirect("/categories/" + categoryId);
      return null;
    });

    get("/jokes/:id/up", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Joke joke = Joke.find(id);
      joke.hilarityUp();
      response.redirect("/jokes");
      return null;
    });

    get("/jokes/:id/down", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Joke joke = Joke.find(id);
      joke.hilarityDown();
      response.redirect("/jokes");
      return null;
    });

    // get("/categories/:id/up", (request, response) -> {
    //   int categoryId = Integer.parseInt(request.params("id"));
    //   Category category = Category.find(categoryId);
    //   int id = Integer.parseInt(request.params("id"));
    //   Joke joke = Joke.find(id);
    //   joke.hilarityUp();
    //   response.redirect("/categories/" + categoryId);
    //   return null;
    // });
    //
    // get("/categories/:id/down", (request, response) -> {
    //   int categoryId = Integer.parseInt(request.params("id"));
    //   Category category = Category.find(categoryId);
    //   int id = Integer.parseInt(request.params("id"));
    //   Joke joke = Joke.find(id);
    //   joke.hilarityDown();
    //   response.redirect("/categories/" + categoryId);
    //   return null;
    // });

  }
}
