package com.example.workapp.data.database.fragments;
/*
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workapp.R;
import com.example.workapp.database.entity.Comment;
import com.example.workapp.database.room_dao.CommentDao;

import java.util.List;

import static com.example.workapp.presentation.screen.main.MainActivity.db;

public class ShowTimerCommentsFragment extends Fragment {
    private CommentDao commentDao = db.commentDao();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comments_show_fragment, container, false);
        List<Comment> listOfComments = commentDao.getall();
        if (listOfComments.size() == 0) {
            String noComments = "There are no any comments";
            ((TextView) view.findViewById(R.id.showFirstComment)).setText(noComments);
        } else if (listOfComments.size() == 1) {
            displayTimerComments(1, view, R.id.showFirstComment, listOfComments);
        } else if (listOfComments.size() == 2) {
            displayTimerComments(1, view, R.id.showFirstComment, listOfComments);
            displayTimerComments(2, view, R.id.showSecondComment, listOfComments);
        } else if (listOfComments.size() == 3) {
            displayTimerComments(1, view, R.id.showFirstComment, listOfComments);
            displayTimerComments(2, view, R.id.showSecondComment, listOfComments);
            displayTimerComments(3, view, R.id.showThirdComment, listOfComments);
        } else if (listOfComments.size() == 4) {
            displayTimerComments(1, view, R.id.showFirstComment, listOfComments);
            displayTimerComments(2, view, R.id.showSecondComment, listOfComments);
            displayTimerComments(3, view, R.id.showThirdComment, listOfComments);
            displayTimerComments(4, view, R.id.showFourthComment, listOfComments);
        } else {
            displayTimerComments(1, view, R.id.showFirstComment, listOfComments);
            displayTimerComments(2, view, R.id.showSecondComment, listOfComments);
            displayTimerComments(3, view, R.id.showThirdComment, listOfComments);
            displayTimerComments(4, view, R.id.showFourthComment, listOfComments);
            displayTimerComments(5, view, R.id.showFifthComment, listOfComments);
        }
        return view;
    }

    private void displayTimerComments(Integer index, View view, Integer id, List<Comment> listOfComments) {
        int lastCommentPosition = listOfComments.size() - index;
        String timeOfComment = "At time: ";
        String comment = "Comment: ";
        ((TextView) view.findViewById(id)).setText((comment + listOfComments.get(lastCommentPosition).
                getText() + "\n" + timeOfComment + listOfComments.get(lastCommentPosition).getTime()));
    }
}*/