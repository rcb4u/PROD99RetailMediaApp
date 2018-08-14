package com.RSPL.MEDIA.Doc990;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.RSPL.MEDIA.R;

/**
 * Created by rspl-richa on 28/12/17.
 */

public class Successful extends Fragment {

    Button finish;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_success, container, false);
        finish = (Button) view.findViewById(R.id.end);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closefragment();
            }
        });

        return view;
    }


    private void closefragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }


}
