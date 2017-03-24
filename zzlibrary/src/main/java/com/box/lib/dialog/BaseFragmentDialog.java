package com.box.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by xxy on 2017/3/1 0001.
 */

public class BaseFragmentDialog extends DialogFragment {
    private Builder builder;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // Get the layout inflater
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.fragment_login_dialog, null);
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(view)
//                // Add action buttons
//                .setPositiveButton("Sign in",
//                        new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id)
//                            {
//                            }
//                        }).setNegativeButton("Cancel", null);
//        return builder.create();
        return super.onCreateDialog(savedInstanceState);
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉Title
        if (builder == null) throw new RuntimeException("Builder of BaseFragmentDialog is NULL !");
        getDialog().setCanceledOnTouchOutside(builder.canceledOnTouchOutside);

        getDialog().setOnKeyListener(builder.onKeyListener);
        getDialog().setOnCancelListener(builder.onCancelListener);
        getDialog().setOnDismissListener(builder.onDismissListener);

        return builder.getRootView();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (builder != null && builder.onCancelListener != null)
            builder.onCancelListener.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (builder != null && builder.onDismissListener != null)
            builder.onDismissListener.onDismiss(dialog);
    }

    /**
     * @param context
     * @param layoutId
     * @return
     */
    public static Builder Builder(Context context, int layoutId) {
        return new Builder(context, layoutId);
    }

    /**
     * @param context
     * @param view
     */
    public static Builder Builder(Context context, View view) {
        return new Builder(context, view);
    }

    /**
     * @param viewBuilder
     */
    public static Builder Builder(ViewBuilder viewBuilder) {
        return new Builder(viewBuilder);
    }


    public abstract static class ViewBuilder {
        public Builder builder;
        public View root;
        public Context context;

        public ViewBuilder(Context context, int layoutId) {
            this(context, LayoutInflater.from(context).inflate(layoutId, null));
        }

        public ViewBuilder(Context context, View view) {
            this.context = context;
            root = view;
            builder(root);
        }

        public void setBuilder(Builder builder) {
            this.builder = builder;
        }

        protected Builder getBuilder() {
            return builder;
        }

        /**
         * @param resId
         * @param <T>
         * @return
         */
        public <T extends View> T findViewById(int resId) {
            return (T) root.findViewById(resId);
        }

        protected void dismiss() {
            if (getBuilder() != null && getBuilder().getDialog() != null)
                getBuilder().getDialog().dismiss();
        }

        protected abstract void builder(View root);
    }

    public interface DialogClickListener {
        /**
         * @param v
         * @return 是否关闭
         */
        boolean onClick(View v);
    }

    public static class Builder {
        private Bundle bundle;
        private ViewBuilder viewBuilder;
        private BaseFragmentDialog dialog;

        private boolean cancelable = true;
        private boolean canceledOnTouchOutside = true;

        private DialogInterface.OnKeyListener onKeyListener;
        private DialogInterface.OnCancelListener onCancelListener;
        private DialogInterface.OnDismissListener onDismissListener;

        /**
         * @param context
         * @param layoutId
         */
        public Builder(Context context, int layoutId) {
            this(context, LayoutInflater.from(context).inflate(layoutId, null));
        }

        /**
         * @param context
         * @param view
         */
        public Builder(Context context, View view) {
            viewBuilder = new ViewBuilder(context, view) {
                @Override
                public void builder(View root) {

                }
            };
            viewBuilder.setBuilder(this);
        }

        /**
         * @param viewBuilder
         */
        public Builder(ViewBuilder viewBuilder) {
            this.viewBuilder = viewBuilder;
            viewBuilder.setBuilder(this);
        }

        public View getRootView() {
            return viewBuilder.root;
        }

        public BaseFragmentDialog getDialog() {
            return dialog;
        }

        public void setBundle(Bundle bundle) {
            this.bundle = bundle;
        }

        /**
         * @param viewId
         * @param text
         * @return
         */
        public Builder setText(int viewId, CharSequence text) {
            View v = viewBuilder.findViewById(viewId);
            if (TextView.class.isInstance(v))
                ((TextView) v).setText(text);
            return this;
        }

        /**
         * @param viewId
         * @param listener
         * @return
         */
        public Builder setOnClickListener(int viewId, final DialogClickListener listener) {
            viewBuilder.findViewById(viewId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener == null || listener.onClick(v)) dialog.dismiss();
                }
            });
            return this;
        }

        /**
         * @param viewId
         * @param listener
         * @return
         */
        public Builder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
            viewBuilder.findViewById(viewId).setOnLongClickListener(listener);
            return this;
        }


        /**
         * @param canceledOnTouchOutside
         * @return
         */
        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        /**
         * @param cancelable
         * @return
         */
        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }


        /**
         * @param onCancelListener
         * @return
         */
        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.onCancelListener = onCancelListener;
            return this;
        }

        /**
         * @param onDismissListener
         * @return
         */
        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        /**
         * @param onKeyListener
         * @return
         */
        public Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            this.onKeyListener = onKeyListener;
            return this;
        }

        /**
         * @return
         */
        public BaseFragmentDialog create() {
            // Context has already been wrapped with the appropriate theme.
            dialog = new BaseFragmentDialog();
            if (bundle != null)
                dialog.setArguments(bundle);
            dialog.setCancelable(cancelable);//因为是DialogFragment写在这里才生效
            dialog.setBuilder(this);
            return dialog;
        }

        /**
         * @param fragmentManager
         * @param tag
         * @return
         */
        public BaseFragmentDialog show(FragmentManager fragmentManager, String tag) {
            final BaseFragmentDialog dialog = create();
            dialog.show(fragmentManager, tag);
            return dialog;
        }

        /**
         * @param transaction
         * @param tag
         * @return
         */
        public BaseFragmentDialog show(FragmentTransaction transaction, String tag) {
            final BaseFragmentDialog dialog = create();
            dialog.show(transaction, tag);
            return dialog;
        }
    }
}
