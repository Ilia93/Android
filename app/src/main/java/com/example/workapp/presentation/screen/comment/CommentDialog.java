package com.example.workapp.presentation.screen.comment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.workapp.R;

import org.jetbrains.annotations.NotNull;

public class CommentDialog extends DialogFragment implements TextView.OnEditorActionListener {

    //private DialogOnClickListener dialogOnClickListener;

    private EditText userInput;

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    @NotNull
    public static CommentDialog newInstance() {
        CommentDialog dialogFragment = new CommentDialog();
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.timer_comment_dialog, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userInput = view.findViewById(R.id.inputDialogComment);
        userInput.setOnEditorActionListener(this);
    }

   /* @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.timer_comment_dialog, null);
        builder.setView(view)
                .setPositiveButton(R.string.timer_dialog_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogOnClickListener.onDialogPositiveClick(CommentDialog.this);
                    }
                })
                .setNegativeButton(R.string.timer_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogOnClickListener.onDialogNegativeClick(CommentDialog.this);
                    }
                });
        return builder.create();
    }*/

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            EditNameDialogListener listener = (EditNameDialogListener) getActivity();
            assert listener != null;
            listener.onFinishEditDialog(userInput.getText().toString());
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }


    /* @Override
     public void onAttach(Context context) {
         super.onAttach(context);
         try {
             dialogOnClickListener = (DialogOnClickListener) context;
         } catch (ClassCastException e) {
             throw new ClassCastException(context.toString()
                     + " must implement NoticeDialogListener");
         }
     }
 */


   /* public interface DialogOnClickListener {

        void onDialogPositiveClick(String userInput);

        void onDialogNegativeClick(DialogFragment dialog);
    }*/
}
