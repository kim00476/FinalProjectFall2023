package algonquin.cst2335.groupappilcation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.groupappilcation.databinding.FragmentRecipeDetailsBinding;

public class RecipeFragment extends Fragment {

    private final String API_KEY = "fd88a53bac094038b76412c4e400a3fe";

    Recipe selected;

    public RecipeFragment() {
    }

    public RecipeFragment(Recipe selected) {
        this.selected = selected;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        FragmentRecipeDetailsBinding binding = FragmentRecipeDetailsBinding.inflate(inflater);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        if (selected != null) {
            Executor thread = Executors.newSingleThreadExecutor();
            RecipeItemDatabase db = Room.databaseBuilder(requireContext(), RecipeItemDatabase.class, "recipes").build();
            RecipeItemDAO riDAO = db.riDAO();
            RequestQueue queue = Volley.newRequestQueue(requireContext());

            RecipeSearchMain mainActivity = (RecipeSearchMain) requireActivity();

            binding.saveButton.setOnClickListener(v -> {
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
                                    try (FileOutputStream fOut = requireContext().openFileOutput(fileName, Context.MODE_PRIVATE)) {
                                        if (imageType.equals("jpg")) {
                                            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                        } else {
                                            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                        }

                                        thread.execute(() -> {
                                            riDAO.insertRecipe(new RecipeItem(id, title, summary, sourceURL, fileName));
                                        });

                                        requireActivity().runOnUiThread(() -> {
                                            binding.saveButton.setVisibility(View.GONE);
                                            binding.deleteButton.setVisibility(View.VISIBLE);

                                            mainActivity.myAdapter.notifyDataSetChanged();
                                        });
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                },
                                1024,
                                1024,
                                ImageView.ScaleType.CENTER,
                                null,
                                error -> {
                                    try (FileOutputStream fOut = requireContext().openFileOutput("recipe_placeholder.png", Context.MODE_PRIVATE)) {
                                        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.recipe_placeholder);
                                        image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }

                                    thread.execute(() -> {
                                        riDAO.insertRecipe(new RecipeItem(id, title, summary, sourceURL, "recipe_placeholder.png"));
                                    });

                                    requireActivity().runOnUiThread(() -> {
                                        binding.saveButton.setVisibility(View.GONE);
                                        binding.deleteButton.setVisibility(View.VISIBLE);

                                        mainActivity.myAdapter.notifyDataSetChanged();
                                    });
                                });
                        queue.add(imgReq);
                    } catch (JSONException e) {
                        Log.w("JSON", e);
                    }
                }, error -> {
                });
                queue.add(request);
            });

            binding.deleteButton.setOnClickListener(v -> {
                thread.execute(() -> {
                    RecipeItem deletedRecipe = riDAO.getRecipeById(selected.getId()).get(0);
                    File imageFile = new File(requireContext().getFilesDir(), deletedRecipe.image);

                    riDAO.deleteRecipe(deletedRecipe);
                    imageFile.delete();

                    requireActivity().runOnUiThread(() -> {
                        binding.deleteButton.setVisibility(View.GONE);
                        binding.saveButton.setVisibility(View.VISIBLE);

                        mainActivity.myAdapter.notifyDataSetChanged();
                    });
                });
            });


            thread.execute(() -> {
                List<RecipeItem> isFromDatabase = riDAO.getRecipeById(selected.getId());

                if (!isFromDatabase.isEmpty()) {
                    RecipeItem selectedItem = riDAO.getRecipe(selected.getId());

                    requireActivity().runOnUiThread(() -> {
                        binding.title.setText(selectedItem.title);
                        binding.summary.loadDataWithBaseURL(null, selectedItem.summary, "text/html", "UTF-8", null);
                        binding.sourceURL.setText(selectedItem.sourceURL);

                        Bitmap bitmap = BitmapFactory.decodeFile(requireContext().getFilesDir() + File.separator + selectedItem.image);
                        binding.imageView.setImageBitmap(bitmap);

                        binding.deleteButton.setVisibility(View.VISIBLE);
                    });
                } else {
                    String requestURL = "https://api.spoonacular.com/recipes/" + selected.getId() + "/information?apiKey=" + API_KEY;

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL, null, response -> {
                        try {
                            String title = response.getString("title");
                            String summary = response.getString("summary");
                            String sourceURL = response.getString("sourceUrl");

                            requireActivity().runOnUiThread(() -> {
                                binding.title.setText(title);
                                binding.summary.loadDataWithBaseURL(null, summary, "text/html", "UTF-8", null);
                                binding.sourceURL.setText(sourceURL);
                            });

                            ImageRequest imgReq = new ImageRequest(
                                    response.getString("image"),
                                    image -> {
                                        requireActivity().runOnUiThread(() -> {
                                            binding.imageView.setImageBitmap(image);
                                        });
                                    },
                                    1024,
                                    1024,
                                    ImageView.ScaleType.CENTER,
                                    null,
                                    error -> binding.imageView.setImageResource(R.drawable.recipe_placeholder));
                            queue.add(imgReq);

                            binding.saveButton.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            Toast.makeText(requireContext(), getString(R.string.recipe_error_display), Toast.LENGTH_LONG).show();
                        }
                    }, error -> {
                    });
                    queue.add(request);
                }
            });
        }

        return binding.getRoot();
    }
}
