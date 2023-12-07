package algonquin.cst2335.groupappilcation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import algonquin.cst2335.groupappilcation.Data.RecipeModel;
import algonquin.cst2335.groupappilcation.databinding.ActivityRecipeSearchMainBinding;
import algonquin.cst2335.groupappilcation.databinding.ViewHolderRecipeBinding;

public class RecipeSearchMain extends AppCompatActivity {
    private final String API_KEY = "fd88a53bac094038b76412c4e400a3fe";
    protected RequestQueue queue;
    ActivityRecipeSearchMainBinding binding;

    SharedPreferences sharedPreferences;
    RecipeModel recipeModel;
    ArrayList<Recipe> recipes = new ArrayList<>();
    Executor thread;
    RecipeItemDAO riDAO;
    RecyclerView.Adapter<MyRowHolder> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecipeSearchMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recipeModel = new ViewModelProvider(this).get(RecipeModel.class);
        recipes = recipeModel.recipes.getValue();

        if (recipes == null) {
            recipeModel.recipes.postValue(recipes = new ArrayList<>());
        }

        setSupportActionBar(binding.Toolbar);

        RecipeItemDatabase db = Room.databaseBuilder(getApplicationContext(), RecipeItemDatabase.class, "recipes").build();
        riDAO = db.riDAO();

        queue = Volley.newRequestQueue(this);
        thread = Executors.newSingleThreadExecutor();

        recipeModel.selectedRecipe.observe(this, (newRecipeValue) -> getSupportFragmentManager().beginTransaction().add(R.id.fragmentLocation, new RecipeFragment(newRecipeValue)).addToBackStack(null).commit());

        binding.RecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyRowHolder(ViewHolderRecipeBinding.inflate(getLayoutInflater(), parent, false).getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                Recipe obj = recipes.get(position);

                thread.execute(() -> {
                    List<RecipeItem> isFromDatabase = riDAO.getRecipeById(obj.getId());

                    runOnUiThread(() -> {
                        if (!isFromDatabase.isEmpty()) {

                            RecipeItem dbItem = isFromDatabase.get(0);

                            holder.title.setText(dbItem.title);

                            Bitmap bitmap = BitmapFactory.decodeFile(getFilesDir() + File.separator + dbItem.image);
                            holder.icon.setImageBitmap(bitmap);

                            holder.deleteButton.setVisibility(View.VISIBLE);
                            holder.saveButton.setVisibility(View.GONE);
                        } else {
                            holder.title.setText(obj.getTitle());

                            ImageRequest imgReq = new ImageRequest(obj.getIconURL(), bitmap -> runOnUiThread(() -> holder.icon.setImageBitmap(bitmap)), 1024, 1024, ImageView.ScaleType.CENTER, null, error -> {
                            });

                            queue.add(imgReq);
                            holder.saveButton.setVisibility(View.VISIBLE);
                            holder.deleteButton.setVisibility(View.GONE);
                        }
                    });
                });
            }

            @Override
            public int getItemCount() {
                return recipes.size();
            }
        });
        binding.RecyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = getPreferences(Activity.MODE_PRIVATE);
        String savedText = sharedPreferences.getString("search_prompt", "");
        binding.EditText.setText(savedText);


        binding.SearchButton.setOnClickListener(click -> {
            String recipeName = binding.EditText.getText().toString();

            recipes.clear();
            myAdapter.notifyDataSetChanged();

            saveData(recipeName, "search_prompt");

            String requestURL;

            try {
                requestURL = "https://api.spoonacular.com/recipes/complexSearch?query=" + URLEncoder.encode(recipeName, "UTF-8") + "&number=100&apiKey=" + API_KEY;
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL, null, response -> {
                try {
                    JSONArray recipeArray = response.getJSONArray("results");

                    for (int i = 0; i < recipeArray.length(); i++) {
                        JSONObject recipe = recipeArray.getJSONObject(i);

                        int id = recipe.getInt("id");
                        String title = recipe.getString("title");
                        String imageURL = recipe.getString("image");

                        recipes.add(new Recipe(id, title, imageURL));
                        myAdapter.notifyItemInserted(recipes.size() - 1);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }, error -> {
            });
            queue.add(request);
        });

        binding.ViewSavedButton.setOnClickListener(v -> {
            recipes.clear();

            thread.execute(() -> {
                recipes.addAll(riDAO.getAllRecipes().stream().map(Recipe::covertFromItemToRecipe).collect(Collectors.toList()));

                runOnUiThread(() -> myAdapter.notifyDataSetChanged());
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.count_recipes) {
            showSnackbar();
            return true;
        } else if (itemId == R.id.about_item) {
            showAlertDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSnackbar() {
        thread.execute(() -> {
            List<RecipeItem> savedRecipes = riDAO.getAllRecipes();

            runOnUiThread(() -> {
                View rootView = findViewById(android.R.id.content);
                Snackbar.make(rootView, getString(R.string.text_count_recipes) + savedRecipes.size(), Snackbar.LENGTH_SHORT).show();
            });
        });
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.about))
                .setMessage(getString(R.string.recipe_app_desc))
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void saveData(String textToSave, String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, textToSave);
        editor.apply();
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        Button saveButton;

        Button deleteButton;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.textView);
            saveButton = itemView.findViewById(R.id.saveButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);


            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();
                Recipe selected = recipes.get(position);

                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    recipeModel.selectedRecipe.postValue(selected);
                }
            });

            saveButton.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                Recipe selected = recipes.get(position);

                String requestURL = "https://api.spoonacular.com/recipes/" + selected.getId() + "/information?apiKey=" + API_KEY;

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL, null, response -> {
                    try {
                        long id = response.getLong("id");
                        String title = response.getString("title");
                        String summary = response.getString("summary");
                        String sourceURL = response.getString("sourceUrl");
                        String imageType = response.getString("imageType");
                        String fileName = title.toLowerCase().replaceAll("[^a-zA-Z0-9]+", "_") + "." + imageType;

                        ImageRequest imgReq = new ImageRequest(
                                response.getString("image"),
                                image -> {
                                    try (FileOutputStream fOut = openFileOutput(fileName, Context.MODE_PRIVATE)) {
                                        if (imageType.equals("jpg")) {
                                            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                        } else {
                                            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                        }
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                },
                                1024,
                                1024,
                                ImageView.ScaleType.CENTER,
                                null,
                                error -> {
                                });
                        queue.add(imgReq);

                        thread.execute(() -> riDAO.insertRecipe(new RecipeItem(id, title, summary, sourceURL, fileName)));

                        runOnUiThread(() -> {
                            saveButton.setVisibility(View.GONE);
                            deleteButton.setVisibility(View.VISIBLE);
                        });
                    } catch (JSONException e) {
                        Toast.makeText(RecipeSearchMain.this, getString(R.string.recipe_error_saving), Toast.LENGTH_LONG).show();
                    }
                }, error -> {
                });
                queue.add(request);
            });

            deleteButton.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                Recipe selected = recipes.get(position);

                thread.execute(() -> {
                    RecipeItem deletedRecipe = riDAO.getRecipeById(selected.getId()).get(0);
                    File imageFile = new File(getFilesDir(), deletedRecipe.image);

                    riDAO.deleteRecipe(deletedRecipe);
                    imageFile.delete();

                    runOnUiThread(() -> {
                        deleteButton.setVisibility(View.GONE);
                        saveButton.setVisibility(View.VISIBLE);
                    });

                });
            });
        }
    }
}