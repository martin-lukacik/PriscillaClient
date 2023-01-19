package com.example.priscillaclient.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.priscillaclient.CourseListAdapter;
import com.example.priscillaclient.R;
import com.example.priscillaclient.api.GetUserCourses;
import com.example.priscillaclient.client.Client;
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

        if (this.courses == null)
            new GetUserCourses(this).execute();

        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.courses != null)
            onUpdate(this.courses);
    }

    CourseListAdapter adapter;
    ArrayList<Course> courses;
    final String PREF_SET = "settings";

    @Override
    public void onUpdate(Object response) {

        courses = Client.getInstance().courses;

        if (courses == null)
            return;

        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(PREF_SET, 0);
        int pinnedCourseId = settings.getInt("pinnedCourseId", -1);

        if (pinnedCourseId != -1) {
            int index = -1;

            for (int i = 0; i < courses.size(); ++i) {
                if (pinnedCourseId == courses.get(i).course_id) {
                    index = i;
                    break;
                }
            }

            Course course = courses.remove(index);
            course.isPinned = true;
            courses.add(0, course);
        }
        Toast.makeText(getActivity(), pinnedCourseId + " pinned update", Toast.LENGTH_SHORT).show();

        adapter = new CourseListAdapter(getActivity(), courses);
        GridView courseListView = getActivity().findViewById(R.id.courseListView);
        courseListView.setAdapter(adapter);
        courseListView.setOnItemClickListener(this::courseSelected);
        courseListView.setOnItemLongClickListener(this::coursePinned);
    }

    private boolean coursePinned(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO clear this up
        Course course = courses.get(i);

        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(PREF_SET, 0);
        int pinnedCourseId = settings.getInt("pinnedCourseId", -1);

        if (pinnedCourseId == -1) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("pinnedCourseId", courses.get(i).course_id);
            editor.apply();
            courses.remove(i);
            courses.add(0, course);
            course.isPinned = true;
            adapter.notifyDataSetChanged();
        } else if (course.course_id == pinnedCourseId) {
            course.isPinned = false;
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("pinnedCourseId", -1);
            editor.apply();
            courses = new ArrayList<>(Client.getInstance().courses);
            adapter = new CourseListAdapter(getActivity(), courses);
            GridView courseListView = getActivity().findViewById(R.id.courseListView);
            courseListView.setAdapter(adapter);
        }

        return true;
    }

    private void courseSelected(AdapterView<?> adapterView, View view, int i, long l){
        Bundle bundle = new Bundle();
        int courseId = courses.get(i).course_id;
        bundle.putInt("courseId", courseId);

        swapFragment(ChapterFragment.newInstance(courseId));
    }
}