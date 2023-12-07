package algonquin.cst2335.groupappilcation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import algonquin.cst2335.groupappilcation.Data.SongViewModel;
import algonquin.cst2335.groupappilcation.databinding.ActivitySongRoomBinding;
import algonquin.cst2335.groupappilcation.databinding.ActivitySongSearchMainBinding;
import algonquin.cst2335.groupappilcation.databinding.SongListBinding;

public class SongSearchMain extends AppCompatActivity {
    public SongViewModel mainViewModel;
    public ActivitySongSearchMainBinding songSearchMainBinding;
    public ActivitySongRoomBinding songRoomBinding;
    public SongListBinding songListBinding;
//    public RecyclerView.Adapter<SongRoom.MyRowHolder> songAdaper;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mainViewModel = new ViewModelProvider(this).get(SongViewModel.class);

        songSearchMainBinding = ActivitySongSearchMainBinding.inflate(getLayoutInflater());
        setContentView(songSearchMainBinding.getRoot());

//        songListBinding = SongListBinding.inflate(getLayoutInflater());
//
//        songRoomBinding = ActivitySongRoomBinding.inflate(getLayoutInflater());
//        setContentView(songRoomBinding.getRoot());

        TextView mainTextView = songSearchMainBinding.textView;
        EditText mainEditText = songSearchMainBinding.artistName;
        Button searchBtn = songSearchMainBinding.searchButton;
        CheckBox checkBox = songListBinding.itemCheckBox;
        Button saveBtn = songListBinding.saveBtn;

        songSearchMainBinding.artistName.setText(mainViewModel.editString);
        searchBtn.setOnClickListener((click ->
        {
            String editString = mainEditText.getText().toString();
            mainViewModel.editString.postValue(editString);

            artistName = songSearchMainBinding.artistName.getText().toString();
            String stringUrl = "https://api.deezer.com/search/artist/?q=" + artistName;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringUrl,null,
                    (response) -> {
                try {
                        JSONArray results = response.getJSONArray("data");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject anAlbum = results.getJSONObject(i);
                            String tracklist = anAlbum.getString("tracklist");
                            JsonObjectRequest songLookup =
                                    new JsonObjectRequest(Request.Method.GET, tracklist, null,
                                            (songRequest) -> {
                                                try {
                                                    JSONArray albumData = songRequest.getJSONArray("data");

                                                    for (int j = 0; j < albumData.length(); j++)
                                                    {
                                                        JSONObject thisAlbum = albumData.getJSONObject(j);
                                                        String title = thisAlbum.getString("title");
                                                        int duration = thisAlbum.getInt("duration");
                                                        JSONArray contributors = thisAlbum.getJSONArray("contributors");
                                                        for(int k = 0; k < contributors.length(); k++){
                                                            JSONObject contributeObj = contributors.getJSONObject(k);
                                                            String album = contributeObj.getString("picture_small");
                                                            String name = contributeObj.getString("name");
//                                                        String albumImage = albumData.getJSONObject(j).getString("picture_small");

                                                            String imageUrl = "https://e-cdns-images.dzcdn.net/images/artist/" + album + ".jpg";
                                                            String pathname = getFilesDir() + "/" + album + ".jpg";
                                                            File file = new File(pathname);
                                                            if (file.exists()) {
                                                                mBinding.albumImage.setImageBitmap(BitmapFactory.decodeFile(pathname));
                                                            }else{
                                                                ImageRequest imgReq = new ImageRequest(imageUrl,
                                                                        (Response.Listener<Bitmap>) bitmap -> {
                                                                            FileOutputStream fOut = null;
                                                                            try {
                                                                                fOut = openFileOutput(album + ".jpg", Context.MODE_PRIVATE);

                                                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                                                                fOut.flush();
                                                                                fOut.close();
                                                                            } catch (
                                                                                    FileNotFoundException e) {
                                                                                e.printStackTrace();
                                                                            } catch (
                                                                                    IOException e) {
                                                                                throw new RuntimeException(e);
                                                                            }
                                                                            mBinding.albumImage.setImageBitmap(bitmap);
                                                                        }, 200, 200, ImageView.ScaleType.CENTER, null,
                                                                        (error) -> {

                                                                        });
                                                                queue.add(imgReq);
                                                            }
                                                            songList.add(new SongItem(title, duration, album, name));
                                                        }

        }));//click search button listener end
        songListBinding.itemCheckBox.setOnCheckedChangeListener((btn1, isSelected) ->{
            mainViewModel.isSelected.postValue(isSelected);
        });

        mainViewModel.isSelected.observe(this, selected ->{
            songListBinding.itemCheckBox.setChecked(selected);
        });
    }//onCreate end





}//class end

