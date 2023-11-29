package algonquin.cst2335.groupappilcation;

import android.graphics.drawable.AnimatedStateListDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.groupappilcation.Data.SongSearchModel;
import algonquin.cst2335.groupappilcation.databinding.ActivitySongSearchMainBinding;

public class SongRoom extends AppCompatActivity {
    ArrayList<SongSearchItem> songList; // in the beginning, there are no messages
//    ActivitySongRoomBinding binding;
    SongSearchModel songModel ;
    SongSearchItemDAO mDao; //Declare the dao here
//    RecyclerView.Adapter<MyRowHolder> myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        binding = ActivitySongRoomBinding.inflate(getLayoutInflater());

    }
}
