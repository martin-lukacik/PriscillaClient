package com.example.priscillaclient.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.priscillaclient.views.adapters.CourseListAdapter;
import com.example.priscillaclient.R;
import com.example.priscillaclient.api.GetCourses;
import com.example.priscillaclient.api.client.Client;
import com.example.priscillaclient.models.Course;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursesFragment extends FragmentBase {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CoursesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoursesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursesFragment newInstance(String param1, String param2) {
        CoursesFragment fragment = new CoursesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new GetCourses(this).execute();

        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    CourseListAdapter adapter;
    ArrayList<Course> courses;

    final String PREF_SET = "settings";

    void pinCourse(int courseId) {
        for (int i = 0; i < courses.size(); ++i) {
            courses.get(i).isPinned = false;

            if (courseId == courses.get(i).course_id) {
                courses.get(i).isPinned = true;
                Course c = courses.remove(i);
                courses.add(0, c);
            }
        }
    }

    @Override
    public void onUpdate(Object response) {

        courses = new ArrayList<>(Client.getInstance().courses);

        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(PREF_SET, 0);
        int pinnedCourseId = settings.getInt("pinnedCourseId", -1);
        pinCourse(pinnedCourseId);

        adapter = new CourseListAdapter(getActivity(), courses);
        GridView courseListView = getActivity().findViewById(R.id.courseListView);
        courseListView.setAdapter(adapter);
        courseListView.setOnItemClickListener(this::courseSelected);
        courseListView.setOnItemLongClickListener(this::coursePinned);
    }

    private boolean coursePinned(AdapterView<?> adapterView, View view, int i, long l) {

        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(PREF_SET, 0);
        int pinnedCourseId = settings.getInt("pinnedCourseId", -1);

        Course course = courses.get(i);
        if (pinnedCourseId == -1) {
            togglePin(course.course_id);
            adapter.notifyDataSetChanged();
        } else if (course.course_id == pinnedCourseId) {
            togglePin(-1);
            courses = new ArrayList<>(Client.getInstance().courses);
            adapter = new CourseListAdapter(getActivity(), courses);
            GridView courseListView = getActivity().findViewById(R.id.courseListView);
            courseListView.setAdapter(adapter);
        }

        return true;
    }

    private void togglePin(int courseId) {
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(PREF_SET, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("pinnedCourseId", courseId);
        editor.apply();
        pinCourse(courseId);
    }

    private void courseSelected(AdapterView<?> adapterView, View view, int i, long l){
        int courseId = courses.get(i).course_id;
        swapFragment(ChaptersFragment.newInstance(courseId));
    }
}