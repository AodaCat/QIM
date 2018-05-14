package cc.adcat.qim.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;

/**
 * 基类支持dialog代理及接口类
 * Created by coco on 2017/9/6.
 */

public class DialogProxy {

    private SparseArray<ManagedDialog> mManagedDialogs;

    private DialogSupport mSupport;

    public DialogProxy(DialogSupport support) {
        mSupport = support;
    }

    public boolean showSupportDialog(int id, Bundle args) {
        if (mManagedDialogs == null) {
            mManagedDialogs = new SparseArray<>();
        }
        ManagedDialog md = mManagedDialogs.get(id);
        if (md == null) {
            md = new ManagedDialog();
            md.mDialog = createDialog(id, null, args);
            if (md.mDialog == null) {
                return false;
            } else {
                mManagedDialogs.put(id, md);
            }

        }

        md.mArgs = args;
        mSupport.onPrepareSupportDialog(id, md.mDialog, args);
        if (md.mDialog != null && !md.mDialog.isShowing()
                && mSupport.getActivity() != null
                && !mSupport.getActivity().isFinishing()) {
            try {
                md.mDialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public void dismissSupportDialog(int id) {
        if (mManagedDialogs == null) {
            return;
        }
        final ManagedDialog md = mManagedDialogs.get(id);
        if (md != null
                && md.mDialog!=null) {
            md.mDialog.dismiss();
        }
    }

    public void removeSupportDialog(int id) {
        if (mManagedDialogs != null) {
            final ManagedDialog md = mManagedDialogs.get(id);
            if (md != null
                    && md.mDialog!=null) {
                md.mDialog.dismiss();
                mManagedDialogs.remove(id);
            }
        }
    }

    private AlertDialog createDialog(Integer dialogId, Bundle state, Bundle args) {
        final AlertDialog dialog = mSupport.onCreateSupportDialog(dialogId, args);

        if (dialog == null) {
            return null;
        }

        mSupport.onPrepareSupportDialog(dialogId, dialog, args);
        return dialog;
    }

    public interface DialogSupport {

        Activity getActivity();

        FragmentManager getSupportFragmentManager();

        AlertDialog onCreateSupportDialog(int id, Bundle args);

        void onPrepareSupportDialog(int id, AlertDialog dialog, Bundle args);

        boolean showSupportDialog(int id, Bundle args);

        void dismissSupportDialog(int id);

        void removeSupportDialog(int id);
    }

    public class ManagedDialog {
        AlertDialog mDialog;
        Bundle mArgs;
    }
}
