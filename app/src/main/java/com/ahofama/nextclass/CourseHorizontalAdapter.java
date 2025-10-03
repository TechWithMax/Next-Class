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

public class CourseHorizontalAdapter extends RecyclerView.Adapter<CourseHorizontalAdapter.CourseViewHolder> {

    private List<ExploreFragment2.Course> courses = new ArrayList<>();
    private OnCourseClickListener listener;

    public interface OnCourseClickListener {
        void onCourseClick(ExploreFragment2.Course course);
    }

    public CourseHorizontalAdapter(OnCourseClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_horizontal, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        ExploreFragment2.Course course = courses.get(position);
        holder.bind(course, listener);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void updateData(List<ExploreFragment2.Course> newCourses) {
        this.courses = new ArrayList<>(newCourses);
        notifyDataSetChanged();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        private ImageView courseImage;
        private TextView courseTitle;
        private TextView courseInstructor;
        private TextView courseRating;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.courseImage);
            courseTitle = itemView.findViewById(R.id.courseTitle);
            courseInstructor = itemView.findViewById(R.id.courseInstructor);
            courseRating = itemView.findViewById(R.id.courseRating);
        }

        public void bind(ExploreFragment2.Course course, OnCourseClickListener listener) {
            courseImage.setImageResource(course.getImageRes());
            courseTitle.setText(course.getTitle());
            courseInstructor.setText(course.getInstructor());
            courseRating.setText(String.valueOf(course.getRating()));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCourseClick(course);
                }
            });
        }
    }
}