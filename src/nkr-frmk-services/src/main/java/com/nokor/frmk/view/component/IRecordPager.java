package com.nokor.frmk.view.component;

import java.io.Serializable;


/**
 * 
 * @author prasnar
 *
 */
public interface IRecordPager  {

    int getCurrentPageIndex();

    void setMaxRowPerPage(final int rowNumPerPage);

    void setCurrentPage(final int pageIndex);

    Serializable[] getCurrentRowKeys();

    void moveFirst();

    void moveLast();

    void movePrevious();

    void moveNext();

    void addPageChangedHandler(final PageChangedHandler handler);

}
