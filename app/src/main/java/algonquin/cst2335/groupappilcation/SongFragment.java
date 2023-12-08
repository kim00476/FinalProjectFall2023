package algonquin.cst2335.groupappilcation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.groupappilcation.databinding.SongFragmentBinding;

public class SongFragment extends Fragment implements View.OnClickListener{
    Executor thread = Executors.newSingleThreadExecutor();
    SongItemDAO songItemDAO;
    SongItem selectedSong;
    private String songName;
    private int duration;
    private String title;
    private String album;
    SongFragmentBinding mBinding;
    //    public SongFragment(SongItem m){
//        selected = m;
//    }
////    public SongFragment(Song selected) {
////        this.selected = selected;
////    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        SongDatabase db = Room.databaseBuilder(requireActivity(), SongDatabase.class, "songs").build();
        songItemDAO = db.songItemDAO();

        mBinding = SongFragmentBinding.inflate(inflater);

        songName = selectedSong.getName();
        duration = selectedSong.getDuration();
        title = selectedSong.getSongTitle();
        album = selectedSong.getAlbumImage();

        mBinding.albumNameView.setText(songName);
        mBinding.titleView.setText(title);
        mBinding.durationView.setText(String.valueOf(duration));
        Picasso.get().load(album).into(mBinding.albumImage);
//        mBinding.albumImage.setText(album);

//        ImageView albumCover = mBinding.albumImage;
//        Glide.with(getContext())
//                .load(album)
//                .into(albumCover);

        mBinding.favButton.setOnClickListener(this);
        return mBinding.getRoot();
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.song_menu, menu);
//        return true;
//    }

    @Override
    public void onClick(View v) {
        thread.execute(()->{
            SongItem song = new SongItem(songName, title, duration, album);
            SongItem songExists = songItemDAO.searchSongByName(songName, title);
            // If the song already is in the db, don't insert it.
            if(songExists == null){
                requireActivity().runOnUiThread(()->{
                    String addQuestion = getString(R.string.addQuestion);
                    String addFavorites = getString(R.string.addTitle);
                    String confirm = getString(R.string.confirm);
                    String favoritesAdded = getString(R.string.favoritesAdded);
                    AlertDialog.Builder builder = new AlertDialog.Builder( requireActivity());
                    builder.setMessage(addQuestion)
                            .setTitle(addFavorites).
                            setNegativeButton("No" , (dialog, cl) -> {

                            })
                            .setPositiveButton(confirm, (dialog, cl) -> {
                                thread.execute(()-> {
                                    long id = songItemDAO.insertSong(song);
                                    song.id = id;
                                    requireActivity().runOnUiThread(()->{
                                        Toast.makeText(requireActivity(), songName + " " + favoritesAdded, Toast.LENGTH_SHORT).show();
                                    });
                                });
                            })
                            .create().show();
                });
            } else {
                String alreadyFavs = getString(R.string.songInFavorties);
                requireActivity().runOnUiThread(()->{
                    Toast.makeText(requireActivity(), alreadyFavs, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
