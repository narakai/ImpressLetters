package com.jeycorp.impressletters.result;


import com.jeycorp.impressletters.type.Notice;

import java.util.List;

public class GetNoticeResult extends BaseResult{
    private List<Notice> noticeList;
    private Notice notice;

    public List<Notice> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<Notice> noticeList) {
        this.noticeList = noticeList;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
