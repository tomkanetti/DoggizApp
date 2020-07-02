package com.example.myapplication.Drawer.friendList;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.model.Model;
import com.example.myapplication.R;

import com.example.myapplication.model.User;

import java.util.LinkedList;
import java.util.List;


public class FriendsListFragment extends Fragment {
    RecyclerView list;
    List<User> data = new LinkedList<User>();
    FriendsListAdapter adapter;
    //StudentListViewModel viewModel;
    //LiveData<List<Student>> liveData;

//    interface Delegate{
//        void onItemSelected(User user);
//    }
//
//    Delegate parent;


    public FriendsListFragment() {
        Model.instance.getAllUsers(new Model.getAllUsersListener() {
            @Override
            public void onComplete(List<User> _data) {
                data = _data;
                // update the friends list with the new data
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
//        StudentModel.instance.getAllStudents(new StudentModel.Listener<List<Student>>() {
//            @Override
//            public void onComplete(List<Student> _data) {
//                data = _data;
//                if (adapter != null){
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof Delegate) {
//            parent = (Delegate) getActivity();
//        } else {
//            throw new RuntimeException(context.toString()
//                    + "student list parent activity must implement dtudent ;list fragment Delegate");
//        }
//        setHasOptionsMenu(true);
//
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);

        list = view.findViewById(R.id.friends_list_recycleView);
        list.setHasFixedSize(true);

        // for displaying the rows and contents of them
        //data = Model.instance.getUserLst();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        adapter = new FriendsListAdapter();
        list.setAdapter(adapter);

        // click on specific friend
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG","row was clicked" + position);
//                Student student = data.get(position);
//                parent.onItemSelected(student);
            }
        });

//        liveData = viewModel.getData();
//        liveData.observe(getViewLifecycleOwner(), new Observer<List<Student>>() {
//            @Override
//            public void onChanged(List<Student> students) {
//                data = students;
//                adapter.notifyDataSetChanged();
//            }
//        });

//        final SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.students_list_swipe_refresh);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                viewModel.refresh(new StudentModel.CompListener() {
//                    @Override
//                    public void onComplete() {
//                        swipeRefresh.setRefreshing(false);
//                    }
//                });
//            }
//        });

        return view;
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        parent = null;
//    }

    static class FriendsRowViewHolder extends RecyclerView.ViewHolder {
        TextView dogName;
        TextView ownerName;
        ImageView userImage;
        User userFriend;

        public FriendsRowViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            dogName = itemView.findViewById(R.id.user_list_dogName);
            ownerName = itemView.findViewById(R.id.user_list_ownerName);
            userImage = itemView.findViewById(R.id.user_list_userImg);

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

        public void bind(User u) {
            dogName.setText(u.dogName);
            ownerName.setText(u.ownerName);
//            student = st;
//            if (st.imgUrl != null && st.imgUrl != "") {
//                Picasso.get().load(st.imgUrl).placeholder(R.drawable.avatar).into(image);
//            } else {
//                image.setImageResource(R.drawable.avatar);
//            }
        }
    }

    interface OnItemClickListener {
        void onClick (int position);
    }

    class FriendsListAdapter extends RecyclerView.Adapter<FriendsRowViewHolder>{
        private OnItemClickListener listener;

        void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }


        @NonNull
        @Override
        public FriendsRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.user_list_row, viewGroup,false );
            FriendsRowViewHolder vh = new FriendsRowViewHolder(v, listener);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull FriendsRowViewHolder friendRowViewHolder, int position) {
            User u = data.get(position);
            friendRowViewHolder.bind(u);

        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.,menu);
//    }


//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.menu_student_list_add:
//                Log.d("TAG","fragment handle add menu");
//                NavController navCtrl = Navigation.findNavController(list);
//                NavDirections directions = NewStudentFragmentDirections.actionGlobalNewStudentFragment();
//                navCtrl.navigate(directions);
//                return true;
//
//            case R.id.menu_student_list_info:
//                Log.d("TAG","fragment handle add menu");
//                AlertDialogFragment dialog = AlertDialogFragment.newInstance("Student App Info","Welcom to the student app info page...");
//                dialog.show(getParentFragmentManager(),"TAG");
//
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}