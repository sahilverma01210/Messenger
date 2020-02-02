package com.example.sahil.messanger;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.sahil.messanger.Fragments.ChatFragment;
import com.example.sahil.messanger.Fragments.UserFragment;
import com.example.sahil.messanger.Model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_window);
        TabLayout tabLayout=findViewById(R.id.tab_layout);
        ViewPager viewPager=findViewById(R.id.view_pager);
        Toolbar toolbar=findViewById(R.id.toolbar);

        //setting up the 'bar' layout on top
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        username=findViewById(R.id.username);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //initializing Fragmentation in a Activity
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        //adding 2 Fragments in single Pager View
        viewPagerAdapter.addFragment(new ChatFragment(),"Chat");
        viewPagerAdapter.addFragment(new UserFragment(),"User");
        //setting up the Fragments
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    //method to create upper right corner menu view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);//inflate (LayoutInflator) is used to show temporary view created over main view
        return true;
    }

    //method to select options from  upper right corner menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ChatWindow.this,Start.class));
                finish();
                return true;
        }
        return false;
    }

    //common class for arranging objects of each fragment in created Pager View
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        //method for adding objects of chat or user fragment
        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}