package com.example.priscillaclient.viewmodels.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.browse.GetTasks;
import com.example.priscillaclient.api.tasks.GetAnswer;
import com.example.priscillaclient.viewmodels.app.models.Answer;
import com.example.priscillaclient.viewmodels.app.models.Task;
import com.example.priscillaclient.viewmodels.ViewModelBase;

import java.util.ArrayList;

public class TasksViewModel extends ViewModelBase {

    private final MutableLiveData<ArrayList<Task>> state = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Answer> helpState = new MutableLiveData<>(null);
    private final MutableLiveData<Answer> answerState = new MutableLiveData<>(null);

    private int lastLessonId = -1;

    public LiveData<ArrayList<Task>> getData() {
        return state;
    }

    public LiveData<Answer> getHelpState() {
        return helpState;
    }

    public LiveData<Answer> getAnswerState() {
        return answerState;
    }

    public void fetchData(int courseId, int chapterId, int lessonId, boolean forceFetch) {
        if (forceFetch || lastLessonId != lessonId) {
            apiTask.executeAsync(new GetTasks(courseId, chapterId, lessonId), (data, error) -> {
                setError(error);
                state.setValue(data);
            });
            lastLessonId = lessonId;
        }
    }

    public void getHelp(int taskId) {
        apiTask.executeAsync(new GetAnswer(taskId, Answer.AnswerType.HELP), (data, error) -> {
            setError(error);
            helpState.setValue(data);
            helpState.setValue(null); // clear help, no longer needed
        });
    }

    public void getAnswer(int taskId) {
        apiTask.executeAsync(new GetAnswer(taskId, Answer.AnswerType.ANSWER), (data, error) -> {
            setError(error);
            answerState.setValue(data);
            answerState.setValue(null); // clear answer, no longer needed
        });
    }

    public void clear() {
        state.setValue(new ArrayList<>());
    }
}
