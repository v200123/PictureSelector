package com.luck.picture.lib.interfaces;

import android.content.Context;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;

public interface OnPhotoSelectListener {
    void onPhotoSelect(boolean isAdd, ArrayList<LocalMedia> selectList, int mediaNumber);
}
