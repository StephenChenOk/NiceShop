package com.chen.fy.niceshop.main.home.comment;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.XWApplication;
import com.lxj.xpopup.core.BottomPopupView;

/**
 * 评论弹窗
 */
public class CommentView extends BottomPopupView {
    private ICommentClickListener listener;

    public CommentView(@NonNull Context context) {
        super(context);
    }

    public CommentView(@NonNull Context context
            , ICommentClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.comment_bottom_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onShow() {
        super.onShow();
        findViewById(R.id.btn_publish_comment).setOnClickListener(v -> {
            String content = getContent();
            if (!content.isEmpty()) {
                listener.clickComment(content);
                dismiss();
            }else{
                Toast.makeText(XWApplication.getContext(), "请正确输入", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    private String getContent() {
        EditText et = findViewById(R.id.et_comment);
        return et.getText().toString();
    }

    public interface ICommentClickListener {
        /**
         * 评论/回复
         *
         * @param content 内容
         */
        void clickComment(String content);
    }
}
