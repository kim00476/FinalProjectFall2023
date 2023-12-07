package algonquin.cst2335.groupappilcation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.groupappilcation.databinding.SongFragmentBinding;

public class SongFragment extends Fragment {
    private SongFragmentBinding mBinding;
    SongItem songItem;
    public SongFragment(SongItem song){

        songItem = song;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mBinding = SongFragmentBinding.inflate(inflater, container, false);

        mBinding.titleView.setText("Id = " + songItem.id);
        mBinding.durationView.setText(String.valueOf(songItem.duration));
        mBinding.albumNameView.setText(songItem.name);
////        mBinding.albumImage.setImageBitmap(selected.picture_small);  //Question

        mBinding.addButton.setOnClickListener(click ->{

        });

        mBinding.favButton.setOnClickListener(cli ->{
//            startActivity(new Intent(SongFragment.this, FavoriteSong.class));
        });

        return mBinding.getRoot();
    }
}
