package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Model.Model;
import com.example.myapplication.Model.User;
import com.example.myapplication.R;

import java.util.List;

public class FriendsListActivity extends AppCompatActivity {
    List<User> data;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_friends_list);

        data = Model.instance.getUserLst();

        list = findViewById(R.id.friends_list_recycleView);
        list.setHasFixedSize(true); // for better performance

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        MyAdapter adapter = new MyAdapter();
        list.setAdapter(adapter);

        // on click will display the friend user profile
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG", "Position: "+ position);
            }
        });
    }

    // static- class here doesnt attach to the father class
    static class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView dogName;
        TextView ownerName;
        ImageView userImage;
        OnItemClickListener listener;

        public FriendsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            dogName = itemView.findViewById(R.id.user_list_dogName);
            ownerName = itemView.findViewById(R.id.user_list_ownerName);
            userImage = itemView.findViewById(R.id.user_list_userImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onClick(position);
                        }
                    }
                }
            });
        }

        void bind(User u) {
            dogName.setText(u.dogName);
            ownerName.setText(u.ownerName);
        }
    }

    interface OnItemClickListener{
        void onClick(int position);
    }

    class MyAdapter extends RecyclerView.Adapter<FriendsViewHolder> {
        private OnItemClickListener listener;

        void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        // what happens when we create a row object
        @NonNull
        @Override
        public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.user_list_row, viewGroup, false);
            FriendsViewHolder vh = new FriendsViewHolder(v, listener);
            return vh;
        }

        // when we take a row and we attach data to it (attach  the view to data)
        @Override
        public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
            User u = data.get(position);
            holder.bind(u);
        }

        // get number of rows
        @Override
        public int getItemCount() {
            return data.size();
        }
    }


}