package com.lauren.simplenews.images.view;

import com.lauren.simplenews.beans.ImageBean;

import java.util.List;

/**
 * Description :
 */
public interface ImageView {
    void addImages(List<ImageBean> list);
    void showProgress();
    void hideProgress();
    void showLoadFailMsg();
}
