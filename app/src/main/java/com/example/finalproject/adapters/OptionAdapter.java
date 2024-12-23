package com.example.finalproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.example.finalproject.R;
import com.example.finalproject.models.Question;

import java.util.List;
import java.util.Objects;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder>{
    public List<String> options;
    public Question question;
    Context context;
    @RequiresApi(api = Build.VERSION_CODES.R)
    public OptionAdapter(Context context, Question question){
        this.context = context;
        this.question = question;
        options = List.of(question.option1,question.option2,question.option3,question.option4);

    }

    @Override
    public OptionViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater =(LayoutInflater) LayoutInflater.from(parent.getContext());
        View view = (View) inflater.inflate(R.layout.option_layout,parent,false);

        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OptionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String data = options.get(position);
        holder.option.setText(data);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
//            Toast.makeText(context,""+options.get(position),Toast.LENGTH_SHORT).show();
                question.userAnswer = options.get(position);
                Log.d("TESTTAG",question.userAnswer + " " + options.get(position));
                notifyDataSetChanged();
            }

        });
        if((!Objects.equals(question.userAnswer, "")) && (Objects.equals(question.userAnswer, options.get(position)))){
            holder.itemView.setBackgroundResource(R.drawable.option_item_selected_bg);
        }
        else{
            holder.itemView.setBackgroundResource(R.drawable.option_item_bg);
        }
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public static class OptionViewHolder extends RecyclerView.ViewHolder{
        LinearLayout mainLayout;
        TextView option;
        public OptionViewHolder( View itemView) {
            super(itemView);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.quiz_option_list);
            option =(TextView) itemView.findViewById(R.id.option_quiz);

        }
    }
}