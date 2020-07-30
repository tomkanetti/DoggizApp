package com.example.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;
import com.example.myapplication.fragments.Drawer.feed.FeedFragment;
import com.example.myapplication.fragments.Drawer.usersList.UsersListFragment;
import com.example.myapplication.fragments.Drawer.usersList.UsersListViewModel;
import com.example.myapplication.model.Comment;
import com.example.myapplication.model.CommentModel;
import com.example.myapplication.model.Post;
import com.example.myapplication.model.PostModel;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class PostDetailsFragment extends Fragment {

    ImageView postImg;
    TextView postTile;
    TextView authorPostName;
    ImageView authorPostImg;
    TextView postContent;

    ImageView authorCommentImg;
    TextView comment;
    Button addComment;
    LiveData<User> UserLiveData;


    View view;
    RecyclerView list;
    List<Comment> data = new LinkedList<Comment>();
    CommentListAdapter adapter;
    PostDetailsViewModel viewModel;
    LiveData<List<Comment>> liveData;
    Post post;
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_detail,container,false);
        list = view.findViewById(R.id.comment_list_recycleView);
        list.setHasFixedSize(true);

        postImg= view.findViewById(R.id.post_detail_PostImg);
        postTile= view.findViewById(R.id.post_detail_PostTitle);
        authorPostName= view.findViewById(R.id.post_detail_postAuthorName_txt);
        authorPostImg= view.findViewById(R.id.post_detail_postAuthorImg_img);
        postContent= view.findViewById(R.id.post_detail_postContent_txt);

        authorCommentImg= view.findViewById(R.id.post_detail_commentUser_img);
        comment= view.findViewById(R.id.post_detail_comment_txt);
        addComment= view.findViewById(R.id.post_detail_add_comment_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        adapter = new PostDetailsFragment.CommentListAdapter();
        list.setAdapter(adapter);

        post= PostDetailsFragmentArgs.fromBundle(getArguments()).getPost();
        user=PostDetailsFragmentArgs.fromBundle(getArguments()).getUser();
        if(post!=null){
            update_Post_display();
        }

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });



//        final SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.post_details_comments_swipe_refresh);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                viewModel.refresh(new CommentModel.CompListener() {
//                    @Override
//                    public void onComplete() {
//                        swipeRefresh.setRefreshing(false);
//                    }
//                });
//            }
//        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostDetailsViewModel.class);
    }

    private void update_Post_display() {
        postTile.setText(post.getTitle());
        authorPostName.setText(post.getUsername());
        postContent.setText(post.getDescription());
        if (post.getUserImage() != null && !post.getUserImage().equals(""))
            Picasso.get().load(post.getUserImage()).placeholder(R.drawable.f).into(authorPostImg);
         else authorPostImg.setImageResource(R.drawable.f);
        if (post.getImage() != null && !post.getImage().equals(""))
            Picasso.get().load(post.getImage()).placeholder(R.drawable.f).into(postImg);
         else postImg.setImageResource(R.drawable.f);
        if (user.getImgUrl() != null && !user.getImgUrl().equals(""))
            Picasso.get().load(user.getImgUrl()).placeholder(R.drawable.f).into(authorCommentImg);
        else authorCommentImg.setImageResource(R.drawable.f);

        liveData = viewModel.getData(post.getId().toString());
        // when tha values in liveData changes this function observes
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> comments) {
                data = comments;
                adapter.notifyDataSetChanged(); //refresh
            }
        });


    }

    public void addComment(){
        String theComment=comment.getText().toString();
        Comment newComment=new Comment();
        newComment.setPostId(post.getId());
        newComment.setAuthorImg(user.getImgUrl());
        newComment.setAuthorName(user.getOwnerName());
        newComment.setCommentContent(theComment);
        newComment.setCommentId(user.getOwnerName()+theComment);
        CommentModel.instance.addComment(newComment, new CommentModel.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
//                NavController navController = Navigation.findNavController(view);
//                navController.navigateUp();
            }
        });
    }



    interface OnItemClickListener {
        void onClick (int position);
    }

    static class CommentRowViewHolder extends RecyclerView.ViewHolder {
        TextView authorName;
        TextView content;
        ImageView authorImage;


        public CommentRowViewHolder(@NonNull View itemView, final PostDetailsFragment.OnItemClickListener listener) {
            super(itemView);
            authorName = itemView.findViewById(R.id.comment_list_authorName_txt);
            content = itemView.findViewById(R.id.comment_list_content_txt);
            authorImage = itemView.findViewById(R.id.comment_list_user_img);

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

        public void bind(Comment c) {
            authorName.setText(c.getAuthorName());
            content.setText(c.getCommentContent());

            if (c.getAuthorImg() != null && !c.getAuthorImg().equals("")) {
                Picasso.get().load(c.getAuthorImg()).placeholder(R.drawable.f).into(authorImage);
            } else {
                authorImage.setImageResource(R.drawable.f);
            }

        }
    }

    class CommentListAdapter extends RecyclerView.Adapter<PostDetailsFragment.CommentRowViewHolder>{
        private PostDetailsFragment.OnItemClickListener listener;

        void setOnItemClickListener(PostDetailsFragment.OnItemClickListener listener) {
            this.listener = listener;
        }


        @NonNull
        @Override
        public PostDetailsFragment.CommentRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.post_list_row, viewGroup,false );
            PostDetailsFragment.CommentRowViewHolder vh = new PostDetailsFragment.CommentRowViewHolder(v, listener);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull PostDetailsFragment.CommentRowViewHolder commentRowViewHolder, int position) {
            Comment c = data.get(position);
            commentRowViewHolder.bind(c);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }



}

