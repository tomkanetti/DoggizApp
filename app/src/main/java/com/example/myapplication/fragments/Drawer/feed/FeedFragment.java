package com.example.myapplication.fragments.Drawer.feed;
//
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.myapplication.R;
import com.example.myapplication.activities.HomeActivity;
import com.example.myapplication.fragments.Drawer.usersList.UsersListFragment;
import com.example.myapplication.model.Post;
import com.example.myapplication.model.PostModel;
import com.example.myapplication.model.StoreModel;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FeedFragment extends DialogFragment {
    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private FeedViewModel feedViewModel;
    View view;
    LiveData<User> postLiveData;
    Dialog popAddPost ;
    ImageView popupUserImage, popupAddImageBtn, popupPostImage;
    Button popUpShareBtn;
    TextView popupTitle,popupDescription;
    User user;

    RecyclerView list;
    List<Post> data = new LinkedList<Post>();
    PostListAdapter adapter;
    FeedViewModel viewModel;
    LiveData<List<Post>> liveData;

    static Boolean nav;


    public interface Delegate{
        void onItemSelected(Post post);
    }

    Delegate parent;

    private Bitmap pickedImgBit = null;

    public FeedFragment(){}
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_feed,container,false);

        iniPopup();

        FloatingActionButton writePost = (FloatingActionButton) view.findViewById(R.id.feed_writePost_btn);
        writePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPostData();
                popAddPost.show();
            }
        });

        //------------------- FEED -------------------------
        list = view.findViewById(R.id.feed_recycleView);
        list.setHasFixedSize(true);

        // for displaying the rows and contents of them
        //data = Model.instance.getUserLst();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        adapter = new FeedFragment.PostListAdapter();
        list.setAdapter(adapter);

        // click on specific friend
        adapter.setOnItemClickListener(new FeedFragment.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG","row was clicked" + position);
                Post post = data.get(position);

                parent.onItemSelected(post);
            }
        });


        liveData = viewModel.getData();
        // when tha values in liveData changes this function observes
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                //Log.d("TAG", "3");
                data = posts;
                adapter.notifyDataSetChanged(); //refresh
            }
        });

        final SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.feed_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh(new PostModel.CompListener() {
                    @Override
                    public void onComplete() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Delegate) {
            parent = (Delegate) getActivity();
        } else {
            throw new RuntimeException(context.toString()
                    + "student list parent activity must implement dtudent ;list fragment Delegate");
        }

        setHasOptionsMenu(true);
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);
    }

    @Override
    public void onDetach() {
        //Log.d("TAG", "4");
        super.onDetach();
        parent = null;
    }

    private void iniPopup() {

        popAddPost = new Dialog(getActivity());
        popAddPost.setContentView(R.layout.popup_share_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        // ini popup widgets
        popupUserImage = popAddPost.findViewById(R.id.sharePost_user_image);
        popupPostImage = popAddPost.findViewById(R.id.sharePost_img_imgV);
        popupTitle = popAddPost.findViewById(R.id.sharePost_title_txt);
        popupDescription = popAddPost.findViewById(R.id.sharePost_description_txt);
        popupAddImageBtn = popAddPost.findViewById(R.id.sharePost_addImg_B);
        popUpShareBtn = popAddPost.findViewById(R.id.sharePost_share_btn);

        UserModel.instance.getCurrentUserDetails(new UserModel.Listener<User>() {
            @Override
            public void onComplete(User u) {
                user=u;
                if (user != null){
                    if (user.imgUrl != null && !user.imgUrl.equals("")) {
                        Picasso.get().load(user.imgUrl).placeholder(R.drawable.f).into(popupUserImage);
                    } else {
                        popupUserImage.setImageResource(R.drawable.f);
                    }
                }
            }
        });

        popupAddImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        popUpShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePost();
                //Navigation.findNavController(view).navigate(FeedFragmentDirections.actionGlobalFeedFragment());
                popAddPost.dismiss();
            }
        });
    }

    public void sharePost() {
        //Log.d("TAG", "5");
        Date d = new Date();
        if (popupPostImage != null) {
            StoreModel.uploadImage(pickedImgBit, "post_image" + d.getTime(), new StoreModel.Listener() {
                @Override
                public void onSuccess(String url) {
                    savePost(url);
                }
                @Override
                public void onFail() {
                }
            });
        } else {savePost("");}
    }

    public void savePost(final String imageUrl) {
        //Log.d("TAG", "6");
        final String title = popupTitle.getText().toString();
        final String description = popupDescription.getText().toString();

        UserModel.instance.getCurrentUserDetails(new UserModel.Listener<User>() {
            @Override
            public void onComplete(User data) {
                Post post = new Post();

                post.setUserEmail(data.getEmail());
                post.setUserImage(data.getImgUrl());
                post.setTitle(title);
                post.setUsername(data.ownerName);
                post.setDescription(description);
                post.setImage(imageUrl);
                post.setDelete(false);
                post.setId(post.getTitle()+post.getDescription());
                PostModel.instance.addPost(post, new PostModel.Listener<Post>() {
                    @Override
                    public void onComplete(Post data) {
//                        NavController navController = Navigation.findNavController(view);
//                        navController.navigateUp();
                    }
                });
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //Log.d("TAG", "7");
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }




    private void openGallery() {
        //Log.d("TAG", "8");
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d("TAG", "INSIDE onActivityResult - UPLOAD PIC");
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d("TAG", "1");
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri uri=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                pickedImgBit =rotateImage((Bitmap) bitmap);
                popupPostImage.setImageBitmap(pickedImgBit);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        nav=true;
        //Log.d("TAG", "2");
    }


    public static Bitmap rotateImage(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate(0);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    //-----------------------------FEED-------------------------------------------

    interface OnItemClickListener {
        void onClick (int position);
    }

    static class PostsRowViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView authorName;
        ImageView userImage;
        ImageView postImage;


        public PostsRowViewHolder(@NonNull View itemView, final FeedFragment.OnItemClickListener listener) {
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


    class PostListAdapter extends RecyclerView.Adapter<FeedFragment.PostsRowViewHolder>{
        private FeedFragment.OnItemClickListener listener;

        void setOnItemClickListener(FeedFragment.OnItemClickListener listener) {
            this.listener = listener;
        }


        @NonNull
        @Override
        public FeedFragment.PostsRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.post_list_row, viewGroup,false );
            FeedFragment.PostsRowViewHolder vh = new FeedFragment.PostsRowViewHolder(v, listener);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull FeedFragment.PostsRowViewHolder postRowViewHolder, int position) {
            Post p = data.get(position);
            postRowViewHolder.bind(p);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    public void clearPostData() {
        popupTitle.setText("");
        popupDescription.setText("");
        popupPostImage.setImageBitmap(null);

    }

}


