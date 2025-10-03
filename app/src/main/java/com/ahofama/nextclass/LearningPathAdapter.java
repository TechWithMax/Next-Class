package com.ahofama.nextclass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class LearningPathAdapter extends RecyclerView.Adapter<LearningPathAdapter.LearningPathViewHolder> {

    private List<ExploreFragment2.LearningPath> learningPaths = new ArrayList<>();
    private OnLearningPathClickListener listener;

    public interface OnLearningPathClickListener {
        void onLearningPathClick(ExploreFragment2.LearningPath path);
    }

    public LearningPathAdapter(OnLearningPathClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LearningPathViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_learning_path, parent, false);
        return new LearningPathViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LearningPathViewHolder holder, int position) {
        ExploreFragment2.LearningPath path = learningPaths.get(position);
        holder.bind(path, listener);
    }

    @Override
    public int getItemCount() {
        return learningPaths.size();
    }

    public void updateData(List<ExploreFragment2.LearningPath> newPaths) {
        this.learningPaths = new ArrayList<>(newPaths);
        notifyDataSetChanged();
    }

    static class LearningPathViewHolder extends RecyclerView.ViewHolder {
        private ImageView pathIcon;
        private TextView pathTitle;
        private TextView pathDescription;
        private TextView courseCount;

        public LearningPathViewHolder(@NonNull View itemView) {
            super(itemView);
            pathIcon = itemView.findViewById(R.id.pathIcon);
            pathTitle = itemView.findViewById(R.id.pathTitle);
            pathDescription = itemView.findViewById(R.id.pathDescription);
            courseCount = itemView.findViewById(R.id.courseCount);
        }

        public void bind(ExploreFragment2.LearningPath path, OnLearningPathClickListener listener) {
            pathIcon.setImageResource(path.getIconRes());
            pathTitle.setText(path.getTitle());
            pathDescription.setText(path.getDescription());
            courseCount.setText(path.getCourseCount() + " courses");

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLearningPathClick(path);
                }
            });
        }
    }
}