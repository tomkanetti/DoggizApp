package com.example.myapplication.fragments.Drawer.UserProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.fragments.Drawer.feed.FeedFragment;
import com.example.myapplication.fragments.Drawer.feed.FeedViewModel;
import com.example.myapplication.model.Post;
import com.example.myapplication.model.PostModel;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class UserProfileFragment extends Fragment {

    private UserProfileViewModel mViewModel;
    TextView dogName;
    TextView ownerName;
    ImageView userImage;
    View view;
    User user;
    LiveData<List<Post>> liveData;
    UserProfileViewModel viewModel;
    User data = new User();
    List<Post> myPostsList = new LinkedList<Post>();
    RecyclerView list;
    UserPostListAdapter adapter;
    ProgressBar progressBar;
    Button editProfileBtn;


    public UserProfileFragment() {
    }

    public interface Delegate{
        void onItemSelectedFromUserProfile(Post post);
    }

    Delegate parent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(UserProfileViewModel.class);
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        dogName=view.findViewById(R.id.profile_dogName_text);
        ownerName=view.findViewById(R.id.profile_ownerName_text);
        userImage=view.findViewById(R.id.profile_image);
//        UserModel.instance.getCurrentUserDetails(new UserModel.Listener<User>() {
//            @Override
//            public void onComplete(User u) {
//                bind(u);
//                user=u;
//            }
//        });
//
        user=UserProfileFragmentArgs.fromBundle((getArguments())).getUser();

        if (user != null)
            bind(user);

        list = view.findViewById(R.id.profile_posts_recycleView);
        list.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        adapter = new UserPostListAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new UserProfileFragment.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG","row was clicked" + position);
                Post p = myPostsList.get(position);
                parent.onItemSelectedFromUserProfile(p);
            }
        });

        liveData = viewModel.getData(user);

        // when tha values in liveData changes this function observes
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                //Log.d("TAG", "3");
                myPostsList = posts;
                adapter.notifyDataSetChanged(); //refresh
            }
        });

        final SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.profile_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh(user, new PostModel.CompListener() {
                    @Override
                    public void onComplete() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });


        return view;
    }

    public void bind(User u) {
        dogName.setText(u.dogName);
        ownerName.setText(u.ownerName);
        if (u.imgUrl != null && !u.imgUrl.equals("")) {
            Log.d("TAG", " if - UsersListFragment - bind");
            Picasso.get().load(u.imgUrl).placeholder(R.drawable.f).into(userImage);
        } else {
            Log.d("TAG", " else - UsersListFragment - bind");
            userImage.setImageResource(R.drawable.f);
        }
        dogName.setVisibility(View.VISIBLE);
        ownerName.setVisibility(View.VISIBLE);
        userImage.setVisibility(View.VISIBLE);
    }

    //------------------------------------------------------------------------

    interface OnItemClickListener {
        void onClick (int position);
    }

    static class MyPostsRowViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView authorName;
        ImageView userImage;
        ImageView postImage;

        public MyPostsRowViewHolder(@NonNull View itemView, final UserProfileFragment.OnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.post_list_PostTitle);
            userImage = itemView.findViewById(R.id.post_list_profile_img);
            postImage = itemView.findViewById(R.id.post_list_postImg);
            authorName = itemView.findViewById(R.id.post_list_PostAuthorName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onClick(position);
                        }
                    }
                }
            });
        }

        public void bind(Post p) {
            title.setText(p.getTitle());
            authorName.setText(p.getUsername());
            if (p.getImage() != null && !p.getImage().equals("")) {
                Picasso.get().load(p.getImage()).placeholder(R.drawable.f).into(postImage);
            } else {
                postImage.setImageResource(R.drawable.f);
            }

            if (p.getUserImage() != null && !p.getUserImage().equals("")) {
                Picasso.get().load(p.getUserImage()).placeholder(R.drawable.f).into(userImage);
            } else {
                userImage.setImageResource(R.drawable.f);
            }
        }
    }


    class UserPostListAdapter extends RecyclerView.Adapter<UserProfileFragment.MyPostsRowViewHolder>{
        private UserProfileFragment.OnItemClickListener listener;

        void setOnItemClickListener(UserProfileFragment.OnItemClickListener listener) {
            this.listener = listener;
        }


        @NonNull
        @Override
        public UserProfileFragment.MyPostsRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.post_list_row, viewGroup,false );
            UserProfileFragment.MyPostsRowViewHolder vh = new UserProfileFragment.MyPostsRowViewHolder(v, listener);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull UserProfileFragment.MyPostsRowViewHolder postRowViewHolder, int position) {
            Post p = myPostsList.get(position);
            postRowViewHolder.bind(p);
        }

        @Override
        public int getItemCount() {
            return myPostsList.size();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Delegate) {
            parent = (Delegate) getActivity();
        } else {
            throw new RuntimeException(context.toString() + "student list parent activity must implement dtudent ;list fragment Delegate");
        }
        setHasOptionsMenu(true);
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parent = null;
    }
}