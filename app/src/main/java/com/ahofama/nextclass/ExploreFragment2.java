package com.ahofama.nextclass;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class ExploreFragment2 extends Fragment {

    private RecyclerView categoriesRecyclerView;
    private RecyclerView popularCoursesRecyclerView;
    private RecyclerView recommendedCoursesRecyclerView;
    private RecyclerView learningPathsRecyclerView;
    private EditText searchEditText;

    // Adapters
    private CategoryAdapter categoryAdapter;
    private CourseHorizontalAdapter popularCoursesAdapter;
    private CourseVerticalAdapter recommendedCoursesAdapter;
    private LearningPathAdapter learningPathAdapter;

    public ExploreFragment2() {
        // Required empty public constructor
    }

    public static ExploreFragment2 newInstance() {
        return new ExploreFragment2();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerViews();
        setupSearchFunctionality();
        loadSampleData();
    }

    private void initViews(View view) {
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        popularCoursesRecyclerView = view.findViewById(R.id.popularCoursesRecyclerView);
        recommendedCoursesRecyclerView = view.findViewById(R.id.recommendedCoursesRecyclerView);
        learningPathsRecyclerView = view.findViewById(R.id.learningPathsRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
    }

    private void setupRecyclerViews() {
        // Categories RecyclerView (Horizontal)
        LinearLayoutManager categoriesLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoriesRecyclerView.setLayoutManager(categoriesLayoutManager);
        categoryAdapter = new CategoryAdapter(this::onCategoryClick);
        categoriesRecyclerView.setAdapter(categoryAdapter);

        // Popular Courses RecyclerView (Horizontal)
        LinearLayoutManager popularLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        popularCoursesRecyclerView.setLayoutManager(popularLayoutManager);
        popularCoursesAdapter = new CourseHorizontalAdapter(this::onCourseClick);
        popularCoursesRecyclerView.setAdapter(popularCoursesAdapter);

        // Recommended Courses RecyclerView (Vertical)
        LinearLayoutManager recommendedLayoutManager = new LinearLayoutManager(getContext());
        recommendedCoursesRecyclerView.setLayoutManager(recommendedLayoutManager);
        recommendedCoursesAdapter = new CourseVerticalAdapter(this::onCourseClick, this::onBookmarkClick);
        recommendedCoursesRecyclerView.setAdapter(recommendedCoursesAdapter);

        // Learning Paths RecyclerView (Horizontal)
        LinearLayoutManager pathsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        learningPathsRecyclerView.setLayoutManager(pathsLayoutManager);
        learningPathAdapter = new LearningPathAdapter(this::onLearningPathClick);
        learningPathsRecyclerView.setAdapter(learningPathAdapter);
    }

    private void setupSearchFunctionality() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    // Perform search when user types more than 2 characters
                    performSearch(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadSampleData() {
        // Load Categories
        List<Category> categories = Arrays.asList(
                new Category("Programming", R.drawable.ic_code),
                new Category("Design", R.drawable.ic_code),
                new Category("Business", R.drawable.ic_code),
                new Category("Marketing", R.drawable.ic_code),
                new Category("Photography", R.drawable.ic_code)
        );
        categoryAdapter.updateData(categories);

        // Load Popular Courses
        List<Course> popularCourses = Arrays.asList(
                new Course("React Native Development", "John Smith", 4.8f, "Build mobile apps", R.drawable.course_placeholder),
                new Course("UI/UX Design Masterclass", "Sarah Johnson", 4.9f, "Learn design principles", R.drawable.course_placeholder),
                new Course("Python for Beginners", "Mike Chen", 4.7f, "Programming basics", R.drawable.course_placeholder)
        );
        popularCoursesAdapter.updateData(popularCourses);

        // Load Recommended Courses
        List<Course> recommendedCourses = Arrays.asList(
                new Course("Complete Web Development Bootcamp", "Dr. Angela Yu", 4.9f, "Learn HTML, CSS, JavaScript, Node.js, and more", R.drawable.course_placeholder, "45h"),
                new Course("Android Development with Kotlin", "Google Developers", 4.8f, "Build modern Android apps with Kotlin", R.drawable.course_placeholder, "32h"),
                new Course("Data Science and Machine Learning", "Jose Portilla", 4.7f, "Python, Pandas, NumPy, Matplotlib, Seaborn, Scikit-Learn", R.drawable.course_placeholder, "40h"),
                new Course("Digital Marketing Complete Course", "Phil Ebiner", 4.6f, "SEO, Social Media Marketing, Email Marketing", R.drawable.course_placeholder, "28h"),
                new Course("Graphic Design Masterclass", "Lindsay Marsh", 4.8f, "Photoshop, Illustrator, InDesign", R.drawable.course_placeholder, "25h")
        );
        recommendedCoursesAdapter.updateData(recommendedCourses);

        // Load Learning Paths
        List<LearningPath> learningPaths = Arrays.asList(
                new LearningPath("Android Developer", "Master Android app development from basics to advanced", 8, R.drawable.ic_path),
                new LearningPath("Web Developer", "Full-stack web development journey", 12, R.drawable.ic_path),
                new LearningPath("Data Scientist", "Complete data science learning path", 10, R.drawable.ic_path)
        );
        learningPathAdapter.updateData(learningPaths);
    }

    // Click Handlers
    private void onCategoryClick(Category category) {
        Toast.makeText(getContext(), "Category: " + category.getName(), Toast.LENGTH_SHORT).show();
        // Navigate to category courses or filter courses
    }

    private void onCourseClick(Course course) {
        Toast.makeText(getContext(), "Course: " + course.getTitle(), Toast.LENGTH_SHORT).show();
        // Navigate to course details
    }

    private void onBookmarkClick(Course course) {
        Toast.makeText(getContext(), "Bookmarked: " + course.getTitle(), Toast.LENGTH_SHORT).show();
        // Add to bookmarks
    }

    private void onLearningPathClick(LearningPath path) {
        Toast.makeText(getContext(), "Learning Path: " + path.getTitle(), Toast.LENGTH_SHORT).show();
        // Navigate to learning path details
    }

    private void performSearch(String query) {
        Toast.makeText(getContext(), "Searching: " + query, Toast.LENGTH_SHORT).show();
        // Implement search functionality
    }

    // Data Models
    public static class Category {
        private String name;
        private int iconRes;

        public Category(String name, int iconRes) {
            this.name = name;
            this.iconRes = iconRes;
        }

        public String getName() { return name; }
        public int getIconRes() { return iconRes; }
    }

    public static class Course {
        private String title;
        private String instructor;
        private float rating;
        private String description;
        private int imageRes;
        private String duration;
        private boolean isBookmarked;

        public Course(String title, String instructor, float rating, String description, int imageRes) {
            this(title, instructor, rating, description, imageRes, "");
        }

        public Course(String title, String instructor, float rating, String description, int imageRes, String duration) {
            this.title = title;
            this.instructor = instructor;
            this.rating = rating;
            this.description = description;
            this.imageRes = imageRes;
            this.duration = duration;
            this.isBookmarked = false;
        }

        // Getters
        public String getTitle() { return title; }
        public String getInstructor() { return instructor; }
        public float getRating() { return rating; }
        public String getDescription() { return description; }
        public int getImageRes() { return imageRes; }
        public String getDuration() { return duration; }
        public boolean isBookmarked() { return isBookmarked; }
        public void setBookmarked(boolean bookmarked) { isBookmarked = bookmarked; }
    }

    public static class LearningPath {
        private String title;
        private String description;
        private int courseCount;
        private int iconRes;

        public LearningPath(String title, String description, int courseCount, int iconRes) {
            this.title = title;
            this.description = description;
            this.courseCount = courseCount;
            this.iconRes = iconRes;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public int getCourseCount() { return courseCount; }
        public int getIconRes() { return iconRes; }
    }
}