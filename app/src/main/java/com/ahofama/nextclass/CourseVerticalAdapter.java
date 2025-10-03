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

public class CourseVerticalAdapter extends RecyclerView.Adapter<CourseVerticalAdapter.CourseViewHolder> {

    private List<ExploreFragment2.Course> courses = new ArrayList<>();
    private OnCourseClickListener courseListener;
    private OnBookmarkClickListener bookmarkListener;

    public interface OnCourseClickListener {
        void onCourseClick(ExploreFragment2.Course course);
    }

    public interface OnBookmarkClickListener {
        void onBookmarkClick(ExploreFragment2.Course course);
    }

    public CourseVerticalAdapter(OnCourseClickListener courseListener, OnBookmarkClickListener bookmarkListener) {
        this.courseListener = courseListener;
        this.bookmarkListener = bookmarkListener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_vertical, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        ExploreFragment2.Course course = courses.get(position);
        holder.bind(course, courseListener, bookmarkListener);
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
        private TextView courseDescription;
        private TextView courseInstructor;
        private TextView courseRating;
        private TextView courseDuration;
        private ImageView bookmarkIcon;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.courseImage);
            courseTitle = itemView.findViewById(R.id.courseTitle);
            courseDescription = itemView.findViewById(R.id.courseDescription);
            courseInstructor = itemView.findViewById(R.id.courseInstructor);
            courseRating = itemView.findViewById(R.id.courseRating);
            courseDuration = itemView.findViewById(R.id.courseDuration);
            bookmarkIcon = itemView.findViewById(R.id.bookmarkIcon);
        }

        public void bind(ExploreFragment2.Course course, OnCourseClickListener courseListener, OnBookmarkClickListener bookmarkListener) {
            courseImage.setImageResource(course.getImageRes());
            courseTitle.setText(course.getTitle());
            courseDescription.setText(course.getDescription());
            courseInstructor.setText(course.getInstructor());
            courseRating.setText(String.valueOf(course.getRating()));
            courseDuration.setText(course.getDuration());

            // Update bookmark icon based on state
            bookmarkIcon.setImageResource(course.isBookmarked() ?
                    R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline);

            itemView.setOnClickListener(v -> {
                if (courseListener != null) {
                    courseListener.onCourseClick(course);
                }
            });

            bookmarkIcon.setOnClickListener(v -> {
                course.setBookmarked(!course.isBookmarked());
                bookmarkIcon.setImageResource(course.isBookmarked() ?
                        R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline);
                if (bookmarkListener != null) {
                    bookmarkListener.onBookmarkClick(course);
                }
            });
        }
    }
}