package com.orango.electronic.orangetxusb.tool;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import com.orango.electronic.orangetxusb.R;

/**
 * The <code>CustomTextWatcher</code> class is used to add a TextWatcher to the
 * list of those whose methods are called whenever this TextView's text changes.
 *
 * @author IndexCqq
 * @version 1.00.00, 11 May 2015
 */
public class CustomTextWatcherForpad implements TextWatcher {

    private static final String TAG = "CustomTextWatcher";

    private boolean mFormat;

    private boolean mInvalid;

    private int mSelection;
    private int count;

    private String mLastText;

    /**
     * The editText to edit text.
     */
    private EditText mEditText;

    /**
     * Creates an instance of <code>CustomTextWatcher</code>.
     *
     * @param editText
     *        the editText to edit text.
     */
    public CustomTextWatcherForpad(EditText editText,int count) {

        super();
        mFormat = false;
        mInvalid = false;
        this.count=count;
        mLastText = "";
        this.mEditText = editText;
        this.mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start,
                                  int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before,
                              int count) {

        try {

            String temp = charSequence.toString();

            // Set selection.
            if (mLastText.equals(temp)) {
                if (mInvalid) {
                    mSelection -= 1;
                } else {
                    if ((mSelection >= 1) && (temp.length() > mSelection - 1)
                            && (temp.charAt(mSelection - 1)) == ' ') {
                        mSelection += 1;
                    }
                }
                int length = mLastText.length();
                if (mSelection > length) {

                    mEditText.setSelection(length);
                } else {

                    mEditText.setSelection(mSelection);
                }
                mFormat = false;
                mInvalid = false;
                return;
            }

            mFormat = true;
            mSelection = start;

            // Delete operation.
            if (count == 0) {
                if ((mSelection >= 1) && (temp.length() > mSelection - 1)
                        && (temp.charAt(mSelection - 1)) == ' ') {
                    mSelection -= 1;
                }

                return;
            }

            // Input operation.
            mSelection += count;
            char[] lastChar = (temp.substring(start, start + count))
                    .toCharArray();
            int mid = lastChar[0];
            if (mid >= 48 && mid <= 57) {
                /* 1-9. */
            } else if (mid >= 65 && mid <= 70) {
                /* A-F. */
            } else if (mid >= 97 && mid <= 102) {
                /* a-f. */
            } else {
                /* Invalid input. */
                mInvalid = true;
                temp = temp.substring(0, start)
                        + temp.substring(start + count);
                mEditText.setText(temp);
                return;
            }

        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

        try {

            /* Format input. */
            if (mFormat) {
                StringBuilder text = new StringBuilder();
                text.append(editable.toString().replace(" ", ""));
                mLastText = text.toString();
                mEditText.setText(text);
            }
            if(mEditText.getText().toString().length()==count){mEditText.setBackgroundResource(R.mipmap.icon_input_box_write);}else {
                mEditText.setBackgroundResource(R.mipmap.icon_input_box_locked);
            }
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }

}