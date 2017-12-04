package com.kunyang.android.headnews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunyang.android.headnews.object.MeListItem;

import java.util.ArrayList;
import java.util.List;

public class MeActivity extends AppCompatActivity {

    private ImageButton mImageButton;

    private MeAdapter mAdapter;

    private List<MeListItem>mListItems=new ArrayList<MeListItem>();
    private String[] meTitle={"我的文章","我的收藏","通用设置"};
    private int[] imgId={R.drawable.my_book,R.drawable.mycollect,R.drawable.commandsetting};

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        initData();

        mImageButton=(ImageButton)this.findViewById(R.id.news_Button);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MeActivity.this,NewsActivity.class);
                startActivity(i);
                finish();
            }
        });

        mRecyclerView=(RecyclerView)this.findViewById(R.id.me_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter=new MeAdapter(mListItems);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.me_activity_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.write_my_data:
                Intent i=new Intent(MeActivity.this,MyDataActivity.class);
                startActivity(i);
                break;
            default:
        }
        return true;
    }

    private void initData(){
        for (int i=0;i<3;i++){
            MeListItem meListItem=new MeListItem();
            meListItem.setTitle(meTitle[i]);
            meListItem.setImgId(imgId[i]);
            mListItems.add(meListItem);
        }
    }

    private class MeAdapter extends RecyclerView.Adapter<MeAdapter.ViewHolder>{

        private List<MeListItem> mListItems;

        class ViewHolder extends RecyclerView.ViewHolder{
            View contentView;
            ImageView picImg,turnImg;
            TextView title;

            public ViewHolder(View view) {
                super(view);
                contentView=view;
                picImg=(ImageView)view.findViewById(R.id.pic);
                title=(TextView)view.findViewById(R.id.explain);
                turnImg=(ImageView) view.findViewById(R.id.turn);
            }
        }

        public MeAdapter(List<MeListItem>meListItems){
            mListItems=meListItems;
        }

        @Override
        public MeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.me_item,parent,false);
            final ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MeAdapter.ViewHolder holder, int position) {
            MeListItem meListItem=mListItems.get(position);

            holder.turnImg.setImageResource(R.drawable.enter);
            holder.title.setText(meListItem.getTitle());
            holder.picImg.setImageResource(meListItem.getImgId());
        }

        @Override
        public int getItemCount() {
            return mListItems.size();
        }
    }
}
