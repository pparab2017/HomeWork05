package com.amad.homework05;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pushparajparab on 9/11/17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductAdapterHolder> {

    private ArrayList<Message> mQuestions;
    private LayoutInflater inflater;
    private int mRecourseID;
    private Context mContext;
    private int lastPosition = -1;



    private ItemClickCallBack itemClickCallBack;

    public interface ItemClickCallBack
    {
        void OnSubmitClick(int p,String response);
    }





    public void SetProducts(ArrayList<Message> questions){
        this.mQuestions = questions;
    }

    public ProductAdapter(Context context, int recourseID)
    {
        this.inflater = LayoutInflater.from(context);
        //this.mQuestions = products;
        this.mRecourseID = recourseID;
        this.mContext = context;
        this.itemClickCallBack = (ItemClickCallBack) context;

    }

    @Override
    public ProductAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(this.mRecourseID,parent,false);
        return new ProductAdapterHolder(view);
    }

    private void setFadeAnimation(View view, int position) {
        if (position > lastPosition) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(500);
            view.startAnimation(anim);
            lastPosition = position;
        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void setScaleAnimation(View view, int position) {
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(500);
            view.startAnimation(anim);
            lastPosition = position;
        }
    }

    @Override
    public void onBindViewHolder(ProductAdapterHolder holder, int position) {


        Message question = mQuestions.get(position);
        holder.question.setText(question.getText());
        //holder.submit.setVisibility(View.GONE);
        //holder.submit.setEnabled(false);

        String choises[] = question.getChoises().split(",");



            holder.rbChoices.setOrientation(LinearLayout.VERTICAL);
            if(question.getResponse() != null)
            if(!question.getResponse().equals("null")){
                holder.status_layout.setVisibility(View.VISIBLE);
                holder.img_status.setImageResource(R.mipmap.ic_check_circle_black_24dp);
                holder.submitted.setText( "You submitted: " + question.getResponse());
                holder.submit.setVisibility(View.GONE);


            }else{

                holder.status_layout.setVisibility(View.GONE);
                holder.submit.setEnabled(false);
                holder.submit.setVisibility(View.VISIBLE);
            }

        holder.rbChoices.removeAllViews();

        //if(holder.rbChoices.getChildCount()  == 0)
            for (int i = 0; i < choises.length; i++) {
                //String uniqueID = UUID.randomUUID().toString();
                RadioButton rdbtn = new RadioButton(mContext);
                //rdbtn.setId(uniqueID);
                rdbtn.setText(choises[i]);
                if(question.getResponse() != null){
                    if(!question.getResponse().equals("null")){
                    if(question.getResponse().equals(choises[i])){
                        rdbtn.setChecked(true);
                    }
                        rdbtn.setEnabled(false);
                    }

                }

                //rdbtn.setChecked(true);
                holder.rbChoices.addView(rdbtn);
            }
        setFadeAnimation(holder.itemView, position);
    }





    @Override
    public int getItemCount() {
        return mQuestions.size();
    }


    class ProductAdapterHolder extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
        private TextView question,submitted;
        private View container;
        private RadioGroup rbChoices;
        private Button submit;
        private LinearLayout status_layout;
        private ImageView img_status;

        public ProductAdapterHolder(View itemView) {
            super(itemView);
            status_layout = (LinearLayout) itemView.findViewById(R.id.ly_status);
            question = (TextView) itemView.findViewById(R.id.lbl_question);
            submitted = (TextView) itemView.findViewById(R.id.lbl_resSub);
            rbChoices = (RadioGroup) itemView.findViewById(R.id.rb_answerGroup);
            submit = (Button) itemView.findViewById(R.id.btn_submit);
            container = (LinearLayout)itemView.findViewById(R.id.item_Layout);
            img_status = (ImageView) itemView.findViewById(R.id.img_status);

            rbChoices.setOnCheckedChangeListener(this);
            submit.setOnClickListener(this);

        }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            submit.setEnabled(true);
        }

        @Override
        public void onClick(View view) {
            this.submit.setVisibility(View.GONE);
            this.rbChoices.setEnabled(false);

            int selectedId = this.rbChoices.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton = (RadioButton) this.rbChoices.findViewById(selectedId);

            String response =  radioButton.getText().toString();

            this.status_layout.setVisibility(View.VISIBLE);
            this.submitted.setText( "Posting your response...");


            for(int i = 0; i < this.rbChoices.getChildCount(); i++){
                ((RadioButton)this.rbChoices.getChildAt(i)).setEnabled(false);
            }


           itemClickCallBack.OnSubmitClick(getAdapterPosition(),response);


        }
    }
}
