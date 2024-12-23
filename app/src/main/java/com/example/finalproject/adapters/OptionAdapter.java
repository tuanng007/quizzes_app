package com.example.finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.Question;

import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder> {

    private Context context;
    private Question question;
    private List<String> options;

    // Constructor
    public OptionAdapter(Context context, Question question) {
        this.context = context;
        this.question = question;
        this.options = List.of(question.getOption1(), question.getOption2(), question.getOption3(), question.getOption4());
    }

    // ViewHolder class
    public static class OptionViewHolder extends RecyclerView.ViewHolder {
        public TextView optionView;

        public OptionViewHolder(View itemView) {
            super(itemView);
            optionView = itemView.findViewById(R.id.quiz_option);
        }
    }

    @Override
    public OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.option_item, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OptionViewHolder holder, int position) {
        holder.optionView.setText(options.get(position));

        holder.itemView.setOnClickListener(v -> {
            question.setUserAnswer(options.get(position));
            notifyDataSetChanged();
        });

        // Change background based on userAnswer
        if (question.getUserAnswer() != null && question.getUserAnswer().equals(options.get(position))) {
            holder.itemView.setBackgroundResource(R.drawable.option_item_selected_bg);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.option_item_bg);
        }
    }

    @Override
    public int getItemCount() {
        return options.size();
    }
}
