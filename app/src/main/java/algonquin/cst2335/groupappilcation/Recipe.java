package algonquin.cst2335.groupappilcation;

public class Recipe {
    private final long id;
    private final String title;
    private final String iconURL;

    public Recipe(long id, String title, String iconURL) {
        this.id = id;
        this.title = title;
        this.iconURL = iconURL;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIconURL() {
        return iconURL;
    }

    public static Recipe covertFromItemToRecipe(RecipeItem recipeItem) {
        return new Recipe(recipeItem.id, recipeItem.title, recipeItem.image);
    }
}
