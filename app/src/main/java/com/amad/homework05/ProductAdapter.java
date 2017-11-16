package com.amad.homework05;

import android.content.Context;
import android.graphics.Color;
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



import java.util.ArrayList;
import java.util.UUID;

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



    public void clear() {
        int size = this.mQuestions.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {

                this.mQuestions.remove(0);

                this.notifyItemRemoved(i);
            }

            //this.notifyDataSetChanged();
            //this.notifyItemRemoved(0, size);
        }
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

    @Override
    public void onBindViewHolder(ProductAdapterHolder holder, int position) {


        Message question = mQuestions.get(position);
        holder.question.setText(question.getText());
        //holder.submit.setVisibility(View.GONE);
        holder.submit.setEnabled(false);

        String choises[] = question.getChoises().split(",");
        for (int row = 0; row < 1; row++) {


            holder.rbChoices.setOrientation(LinearLayout.VERTICAL);

            for (int i = 0; i < choises.length; i++) {
                //String uniqueID = UUID.randomUUID().toString();
                RadioButton rdbtn = new RadioButton(mContext);
                //rdbtn.setId(uniqueID);
                rdbtn.setText(choises[i]);
                //rdbtn.setChecked(true);
                holder.rbChoices.addView(rdbtn);
            }
            //((ViewGroup) findViewById(R.id.radiogroup)).addView(ll);
        }

       // stopButton.setVisibility(View.VISIBLE);
        //holder.r.setText("$"+  question.getPrice());
//        holder.discount.setText(question.getDiscount() + "% OFF");
//        holder.region.setText(question.getRegion());
//        String item_color = "#FFC107";
//        if(question.getRegion().equals("produce"))
//        {
//            item_color = "#8BC34A";
//        }else if(question.getRegion().equals("lifestyle")){
//            item_color = "#2196F3";
//        }


       // setFadeAnimation(holder.itemView, position);
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
    public int getItemCount() {
        return mQuestions.size();
    }


    class ProductAdapterHolder extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
        private TextView question;
        private View container;
        private RadioGroup rbChoices;
        private Button submit;

        public ProductAdapterHolder(View itemView) {
            super(itemView);
            question = (TextView) itemView.findViewById(R.id.lbl_question);
            rbChoices = (RadioGroup) itemView.findViewById(R.id.rb_answerGroup);
            submit = (Button) itemView.findViewById(R.id.btn_submit);
            container = itemView.findViewById(R.id.item_Layout);

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

            String respose =  radioButton.getText().toString();
//            Log.d("tag", radioButton.getText().toString());
//            Toast.makeText(MyAndroidAppActivity.this,
//                    radioButton.getText(), Toast.LENGTH_SHORT).show();


            for(int i = 0; i < this.rbChoices.getChildCount(); i++){
                ((RadioButton)this.rbChoices.getChildAt(i)).setEnabled(false);
            }





            itemClickCallBack.OnSubmitClick(getAdapterPosition(),respose);


        }
    }
}
